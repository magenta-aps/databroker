package dk.magenta.databroker.core.controller;

import dk.magenta.databroker.core.CustomJpaDialect;
import dk.magenta.databroker.core.DataProvider;
import dk.magenta.databroker.core.DataProviderConfiguration;
import dk.magenta.databroker.core.DataProviderRegistry;
import dk.magenta.databroker.core.model.DataProviderEntity;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ScheduledFuture;

/**
 * Created by jubk on 15-12-2014.
 */

@Controller
public class DataProviderController {

    private HashMap<String, Thread> userThreads;
    private HashMap<String, Thread> cronThreads;
    private Logger log;

    public DataProviderController() {
        this.userThreads = new HashMap<String, Thread>();
        this.cronThreads = new HashMap<String, Thread>();
        this.log = Logger.getLogger(DataProviderController.class);
    }

    /*@PostConstruct
    public void postConstruct() {
        for (DataProviderEntity dataProviderEntity : this.dataProviderRegistry.getDataProviderEntities()) {
            this.updateCronScheduling(dataProviderEntity);
        }
    }*/

    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private JpaTransactionManager transactionManager;

    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private DataProviderRegistry dataProviderRegistry;


    private void logRequest(HttpServletRequest request) {
        this.log.trace(request.getMethod()+" "+request.getServletPath());
        if (request.getMethod().equals("POST")) {
            Enumeration<String> headerNames = request.getHeaderNames();
            if (headerNames.hasMoreElements()) {
                this.log.trace("Headers:");
                while (headerNames.hasMoreElements()) {
                    String headerName = headerNames.nextElement();
                    Enumeration<String> headerValues = request.getHeaders(headerName);
                    while (headerValues.hasMoreElements()) {
                        this.log.trace(headerName + " = " + headerValues.nextElement());
                    }
                }
            }
            try {
                Collection<Part> parts = request.getParts();
                if (parts != null && parts.size() > 0) {
                    this.log.trace("Parts: ");
                    for (Part p : request.getParts()) {
                        this.log.trace(p.getName() + ": " + p.getContentType() + ", "+ p.getSize()+" bytes"); // Parten findes her, men ikke senere
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ServletException e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping("/dataproviders/")
    public ModelAndView index(HttpServletRequest request) {
        this.logRequest(request);
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("dataproviderEntities", dataProviderRegistry.getDataProviderEntities());
        Map<String, HashMap<String, String>> providerInfo = new HashMap<String, HashMap<String, String>>();
        for (DataProviderEntity dataProviderEntity : dataProviderRegistry.getDataProviderEntities()) {
            HashMap<String, String> info = new HashMap<String, String>();
            info.put("status", this.getProviderStatus(dataProviderEntity));
            providerInfo.put(dataProviderEntity.getUuid(), info);
        }
        model.put("dataproviderInfo", providerInfo);
        return new ModelAndView("dataproviders/index", model);
    }

    private ModelAndView redirectToIndex() {
        this.log.trace("Redirecting to index");
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
        this.logRequest(request);

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
                this.log.trace("Updating existing DataproviderEntity " + uuid + " (" + dataProviderEntity.getClass().getSimpleName() + ")");
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
                return processUpload(request, dataProviderEntity, dataProvider);
            }
            values.put("uuid", new String[]{uuid});
            action = "edit";
        } else {
            if (processSubmit) {
                // Processing a "new" submit, create a new DataProviderEntity
                String providerType = request.getParameter("dataprovider");
                this.log.trace("Creating new DataproviderEntity ("+providerType+")");
                values.put("dataprovider", new String[]{providerType});
                boolean active = "active".equals(request.getParameter("active"));
                dataProviderEntity = this.dataProviderRegistry.createDataProviderEntity(providerType, valueMap, active);
                DataProvider dataProvider = dataProviderEntity.getDataProvider();
                if (active && dataProvider.wantCronUpdate(null, dataProviderEntity.getConfiguration())) {
                    this.updateCronScheduling(dataProviderEntity);
                }
                return processUpload(request, dataProviderEntity, dataProvider);
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

    private ModelAndView processUpload(HttpServletRequest request, DataProviderEntity dataProviderEntity, DataProvider dataProvider) {
        String uuid = dataProviderEntity.getUuid();
        if (dataProvider.wantUpload(dataProviderEntity.getConfiguration()) && this.requestHasDataInFields(request, dataProvider.getUploadFields())) {
            this.log.info("Processing upload");
            //this.transactionManager.setJpaDialect(new CustomJpaDialect());
            Thread thread = dataProvider.asyncPush(dataProviderEntity, request, this.transactionManager);
            this.userThreads.put(uuid, thread);
            return this.processingEntity(request, uuid);
        }
        return this.redirectToIndex();
    }

    private boolean requestHasDataInFields(HttpServletRequest request, List<String> fields) {
        for (String uploadField : fields) {
            try {
                Part uploadPart = request.getPart(uploadField);
                if (uploadPart != null && uploadPart.getSize() > 0) {
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
        this.logRequest(request);
        String uuid = request.getParameter("uuid");
        if (uuid != null) {
            DataProviderEntity dataProviderEntity = this.dataProviderRegistry.getDataProviderEntity(uuid);
            if (dataProviderEntity != null) {
                String submit = request.getParameter("submit");
                if (submit != null) {
                    if (submit.equals("ok")) {
                        this.log.trace("Deleting DataproviderEntity "+dataProviderEntity.getUuid()+" ("+dataProviderEntity.getClass().getSimpleName()+")");
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
        this.logRequest(request);
        // Do something
        String uuid = request.getParameter("uuid");
        String submit = request.getParameter("submit");

        if (uuid != null) {
            DataProviderEntity dataProviderEntity = this.dataProviderRegistry.getDataProviderEntity(uuid);
            if (dataProviderEntity != null) {
                Thread t = this.userThreads.get(uuid);
                if (t != null && t.getState() != Thread.State.TERMINATED) {
                    // Thread is already running
                    return this.processingEntity(request, uuid);
                } else {
                    if (submit != null) {
                        if (submit.equals("ok")) {
                            this.log.trace("Pulling DataproviderEntity "+dataProviderEntity.getUuid()+" ("+dataProviderEntity.getClass().getSimpleName()+")");
                            DataProvider dataProvider = dataProviderEntity.getDataProvider();
                            Thread thread = dataProvider.asyncPull(dataProviderEntity, this.transactionManager);
                            this.userThreads.put(uuid, thread);
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
        this.logRequest(request);
        String uuid = request.getParameter("uuid");
        return this.processingEntity(request, uuid);
    }

    public ModelAndView processingEntity(HttpServletRequest request, String uuid) {
        /*HashMap<String, Object> model = new HashMap<String, Object>();
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("uuid", uuid);
        data.put("name", this.dataProviderRegistry.getDataProviderEntity(uuid).getName());
        data.put("onDone", "/dataproviders/");
        model.put("data", data);
        return new ModelAndView("dataproviders/processing", model);*/
        return this.redirectToIndex();
    }

    @RequestMapping("/dataproviders/processingStatus")
    public ModelAndView processStatus(HttpServletRequest request) {
        this.logRequest(request);
        HashMap<String, Object> model = new HashMap<String, Object>();
        String uuid = request.getParameter("uuid");
        DataProviderEntity dataProviderEntity = this.dataProviderRegistry.getDataProviderEntity(uuid);
        if (dataProviderEntity != null) {
            model.put("status", this.getProviderStatus(dataProviderEntity));
            model.put("uuid", uuid);
        }
        return new ModelAndView(new MappingJackson2JsonView(), model);
    }

    private String getProviderStatus(DataProviderEntity dataProviderEntity) {
        String providerStatus = dataProviderEntity.getActive() ? "ENABLED" : "DISABLED";
        Thread thread = this.userThreads.get(dataProviderEntity.getUuid());
        if (thread != null) {
            switch (thread.getState()) {
                case RUNNABLE:
                    providerStatus = "RUNNING";
                    break;
                case WAITING:
                case TIMED_WAITING:
                case BLOCKED:
                    providerStatus = "PAUSED";
                    break;
                case TERMINATED:
                    break;
            }
        }
        return providerStatus;
    }



    @RequestMapping("/dataproviders/fragment")
    public ModelAndView fragment(HttpServletRequest request) {
        this.logRequest(request);
        String fragment = request.getParameter("f");
        return new ModelAndView("dataproviders/fragments/" + fragment);
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
                this.cronThreads.put(entity.getUuid(), task);
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
