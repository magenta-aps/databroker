package dk.magenta.databroker.core.controller;

import dk.magenta.databroker.core.DataProvider;
import dk.magenta.databroker.core.DataProviderConfiguration;
import dk.magenta.databroker.core.DataProviderRegistry;
import dk.magenta.databroker.core.model.DataProviderEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jubk on 15-12-2014.
 */

@Controller
public class DataProviderController {

    @Autowired
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

        if (uuid != null) { // We are editing an existing entity
            dataProviderEntity = this.dataProviderRegistry.getDataProviderEntity(uuid);
            if (processSubmit) {
                this.dataProviderRegistry.updateDataProviderEntity(dataProviderEntity, params);
                DataProvider dataProvider = dataProviderEntity.getDataProvider();
                if (dataProvider.wantUpload(dataProviderEntity.getConfiguration())) {
                    System.out.println("DO upload");
                    dataProvider.handlePush(dataProviderEntity, request);
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
                dataProviderEntity = this.dataProviderRegistry.createDataProviderEntity(providerType, params);
                DataProvider dataProvider = dataProviderEntity.getDataProvider();
                if (dataProvider.wantUpload(dataProviderEntity.getConfiguration())) {
                    System.out.println("DO upload");

                }
                return this.redirectToIndex();
            }
            action = "new";
        }

        values.putAll(params);

        if (dataProviderEntity != null) {
            values.putAll(this.dataProviderRegistry.getDataProviderEntityValues(dataProviderEntity));
            values.put("dataprovider", new String[]{dataProviderEntity.getType()});
        }

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("values", values);
        model.put("action", action);

        model.put("dataproviders", this.getDataProviderData());
        return new ModelAndView("dataproviders/edit", model);
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
                model.put("uuid", uuid);
                return new ModelAndView("dataproviders/delete", model);
            }
        }
        return this.redirectToIndex();
    }



}
