package dk.magenta.databroker.core.controller;

import dk.magenta.databroker.core.DataProvider;
import dk.magenta.databroker.core.DataProviderConfiguration;
import dk.magenta.databroker.core.DataProviderRegistry;
import dk.magenta.databroker.core.model.DataProviderEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

/**
 * Created by jubk on 15-12-2014.
 */

@Controller
public class DataProviderController {

    private HashMap<String, Thread> threads;

    public DataProviderController() {
        this.threads = new HashMap<String, Thread>();
    }

    /*@PostConstruct
    public void postConstruct() {
        for (DataProviderEntity dataProviderEntity : this.dataProviderRegistry.getDataProviderEntities()) {
            this.updateCronScheduling(dataProviderEntity);
        }
    }*/

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private DataProviderRegistry dataProviderRegistry;

    @RequestMapping("/dataproviders/")
    public ModelAndView index() {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("dataproviderEntities", dataProviderRegistry.getDataProviderEntities());
        return new ModelAndView("dataproviders/index", model);
    }

    private ModelAndView redirectToIndex() {
        return new ModelAndView(new RedirectView("/dataproviders/"));
    }


    @RequestMapping("/dataproviders/new")
    public ModelAndView newEntity(HttpServletRequest request) {
        return edit(request);
    }


    @RequestMapping("/dataproviders/edit")
    public ModelAndView editEntity(HttpServletRequest request) {
        return edit(request);
    }

    private ModelAndView edit(HttpServletRequest request) {

        Map<String, String[]> params = request.getParameterMap();
        String uuid = request.getParameter("uuid");
        String submit = request.getParameter("submit");
        String action;

        if (submit != null && !submit.equals("ok")) {
            // User canceled
            return this.redirectToIndex();
        }
        boolean processSubmit = submit != null && submit.equals("ok");

        Map<String, String[]> values = new HashMap<String, String[]>();

        DataProviderEntity dataProviderEntity = null;

        HashMap<String, String[]> valueMap = new HashMap<String, String[]>();
        String[] reservedFields = new String[] {
                "submit",
                "name",
                "uuid",
                "active"
        };
        for (String key : params.keySet()) {
            if (key != null) {
                boolean found = false;
                for (String reserved : reservedFields) {
                    if (key.equals(reserved)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    valueMap.put(key, params.get(key));
                }
            }
        }


        if (uuid != null) { // We are editing an existing entity
            dataProviderEntity = this.dataProviderRegistry.getDataProviderEntity(uuid);
            if (processSubmit) {
                boolean active = "active".equals(request.getParameter("active"));
                String name = request.getParameter("name");

                DataProvider dataProvider = dataProviderEntity.getDataProvider();
                String oldConfiguration = dataProviderEntity.getConfiguration();
                boolean wasActive = dataProviderEntity.getActive();
                boolean updated = this.dataProviderRegistry.updateDataProviderEntity(dataProviderEntity, valueMap, name, active);
                if (updated) {
                    if (wasActive != active || (active && dataProvider.wantCronUpdate(oldConfiguration, dataProviderEntity.getConfiguration()))) {
                        this.updateCronScheduling(dataProviderEntity);
                    }
                }

                if (dataProvider.wantUpload(dataProviderEntity.getConfiguration()) && this.requestHasDataInFields(request, dataProvider.getUploadFields())) {
                    Thread thread = dataProvider.asyncPush(dataProviderEntity, request, this.transactionManager);
                    this.threads.put(uuid, thread);
                    return this.processingEntity(request, uuid);
                }
                return this.redirectToIndex();
            }
            values.put("uuid", new String[]{uuid});
            action = "edit";
        } else {
            if (processSubmit) {
                // Processing a "new" submit, create a new DataProviderEntity
                String providerType = request.getParameter("dataprovider");
                values.put("dataprovider", new String[]{providerType});
                boolean active = "active".equals(request.getParameter("active"));
                dataProviderEntity = this.dataProviderRegistry.createDataProviderEntity(providerType, valueMap, active);
                DataProvider dataProvider = dataProviderEntity.getDataProvider();
                if (active && dataProvider.wantCronUpdate(null, dataProviderEntity.getConfiguration())) {
                    this.updateCronScheduling(dataProviderEntity);
                }
                if (dataProvider.wantUpload(dataProviderEntity.getConfiguration()) && this.requestHasDataInFields(request, dataProvider.getUploadFields())) {
                    Thread thread = dataProvider.asyncPush(dataProviderEntity, request, this.transactionManager);
                    this.threads.put(uuid, thread);
                    return this.processingEntity(request, uuid);
                }
                return this.redirectToIndex();
            }
            action = "new";
        }

        values.putAll(params);

        if (dataProviderEntity != null) {
            values.putAll(this.dataProviderRegistry.getDataProviderEntityValues(dataProviderEntity));
            values.put("dataprovider", new String[]{dataProviderEntity.getType()});
            values.put("name", new String[]{dataProviderEntity.getName()});
            values.put("active", dataProviderEntity.getActive() ? new String[]{"active"} : null);
        } else {
            values.put("active", new String[]{"active"});
        }

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("values", values);
        model.put("action", action);

        model.put("dataproviders", this.getDataProviderData());
        return new ModelAndView("dataproviders/edit", model);
    }

    private boolean requestHasDataInFields(HttpServletRequest request, List<String> fields) {
        for (String uploadField : fields) {
            try {
                if (request.getPart(uploadField).getSize() > 0) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    private Map<String, Object> getDataProviderData() {
        HashMap<String, Object> providerData = new HashMap<String, Object>();
        Collection<DataProvider> dataProviders = DataProviderRegistry.getRegisteredDataProviders();
        for (DataProvider dataProvider : dataProviders) {
            HashMap<String, Object> data = new HashMap<String, Object>();
            data.put("template", dataProvider.getTemplatePath());
            DataProviderConfiguration configuration = dataProvider.getDefaultConfiguration();
            data.put("values", configuration != null ? configuration.toArrayMap() : new DataProviderConfiguration());
            providerData.put(dataProvider.getClass().getCanonicalName(), data);
        }
        return providerData;
    }


    @RequestMapping("/dataproviders/delete")
    public ModelAndView deleteEntity(HttpServletRequest request) {
        String uuid = request.getParameter("uuid");
        if (uuid != null) {
            DataProviderEntity dataProviderEntity = this.dataProviderRegistry.getDataProviderEntity(uuid);
            if (dataProviderEntity != null) {
                String submit = request.getParameter("submit");
                if (submit != null) {
                    if (submit.equals("ok")) {
                        this.dataProviderRegistry.deleteDataProviderEntity(dataProviderEntity);
                    }
                    return this.redirectToIndex();
                }
                Map<String, Object> model = new HashMap<String, Object>();
                model.put("name", dataProviderEntity.getName());
                return new ModelAndView("dataproviders/delete", model);
            }
        }
        return this.redirectToIndex();
    }

    @RequestMapping("/dataproviders/pull")
    public ModelAndView pullEntity(HttpServletRequest request) {
        // Do something
        String uuid = request.getParameter("uuid");
        String submit = request.getParameter("submit");

        if (uuid != null) {
            DataProviderEntity dataProviderEntity = this.dataProviderRegistry.getDataProviderEntity(uuid);
            if (dataProviderEntity != null) {
                Thread t = this.threads.get(uuid);
                if (t != null && t.isAlive()) {
                    // Thread is already running
                    return this.processingEntity(request, uuid);
                } else {
                    if (submit != null) {
                        if (submit.equals("ok")) {
                            DataProvider dataProvider = dataProviderEntity.getDataProvider();
                            Thread thread = dataProvider.asyncPull(dataProviderEntity, this.transactionManager);
                            this.threads.put(uuid, thread);
                            return this.processingEntity(request, uuid);
                        } else {
                            return this.redirectToIndex();
                        }
                    } else {
                        Map<String, Object> model = new HashMap<String, Object>();
                        model.put("name", dataProviderEntity.getName());
                        model.put("uuid", uuid);
                        return new ModelAndView("dataproviders/preprocessing", model);
                    }
                }
            }
        }
        return this.redirectToIndex();
    }


    @RequestMapping("/dataproviders/processing")
    public ModelAndView processingEntity(HttpServletRequest request) {
        String uuid = request.getParameter("uuid");
        return this.processingEntity(request, uuid);
    }

    public ModelAndView processingEntity(HttpServletRequest request, String uuid) {
        HashMap<String, Object> model = new HashMap<String, Object>();
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("uuid", uuid);
        data.put("name", this.dataProviderRegistry.getDataProviderEntity(uuid).getName());
        data.put("onDone", "/dataproviders/");
        model.put("data", data);
        return new ModelAndView("dataproviders/processing", model);
    }

    @RequestMapping("/dataproviders/processingStatus")
    public ModelAndView processStatus(HttpServletRequest request) {
        HashMap<String, Object> model = new HashMap<String, Object>();
        String uuid = request.getParameter("uuid");
        Thread thread = this.threads.get(uuid);
        if (thread != null) {
            Thread.State state = thread.getState();
            model.put("state", state.toString());
        }
        return new ModelAndView(new MappingJackson2JsonView(), model);
    }

    //------------------------------------------------------------------------------------------------------------------

    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    TaskScheduler scheduler;

    private HashMap<DataProviderEntity, ScheduledFuture> sheduledTasks;

    public void updateCronScheduling(DataProviderEntity entity) {
        if (this.sheduledTasks == null) {
            this.sheduledTasks = new HashMap<DataProviderEntity, ScheduledFuture>();
        } else {
            ScheduledFuture scheduledTask = this.sheduledTasks.get(entity);
            if (scheduledTask != null && !scheduledTask.isCancelled()) {
                scheduledTask.cancel(false); // Cancel the scheduled task, but don't interrupt if it's currently running
            }
        }
        if (entity.getActive()) {
            DataProvider dataProvider = entity.getDataProvider();
            DataProviderConfiguration dataProviderConfiguration = new DataProviderConfiguration(entity.getConfiguration());
            String cronExpression = dataProvider.getCronExpression(dataProviderConfiguration);
            if (cronExpression != null) {
                Thread task = entity.getDataProvider().asyncPull(entity, this.transactionManager, false);
                ScheduledFuture scheduledTask = this.scheduler.schedule(task, new CronTrigger(cronExpression));
                this.sheduledTasks.put(entity, scheduledTask);
            }
        }
    }

    public void updateCronScheduling(DataProvider dataProvider) {
        for (DataProviderEntity dataProviderEntity : this.dataProviderRegistry.getDataProviderEntities(dataProvider)) {
            this.updateCronScheduling(dataProviderEntity);
        }
    }
}
