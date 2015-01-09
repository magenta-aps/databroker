package dk.magenta.databroker.core.controller;

import dk.magenta.databroker.core.DataProvider;
import dk.magenta.databroker.core.DataProviderRegistry;
import dk.magenta.databroker.core.model.DataProviderEntity;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import java.net.MalformedURLException;
import java.net.URL;
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

        Map<String, String[]> params = request.getParameterMap();
        String submit = request.getParameter("submit");

        if (submit == null || submit.equals("ok")) {

            Map<String, Object> model = new HashMap<String, Object>();
            Map<String, String[]> values = new HashMap<String, String[]>();
            Map<String, String> errors = new HashMap<String, String>();

            String providerType = request.getParameter("providerType");
            values.put("providerType", new String[]{providerType});

            if (providerType != null) {
                DataProviderEntity dataProviderEntity = null;

                try {
                    dataProviderEntity = this.dataProviderRegistry.createDataProviderEntity(providerType, params);

                //} catch (MalformedURLException e) {
                //    errors.put("sourceUrl", "Invalid url");
                } catch (Exception e) {
                    e.printStackTrace();
                    //errors.put("sourceUrl", "Invalid url");
                }


                if (dataProviderEntity != null) {
                    // item is created, show list

                    return this.redirectToIndex();
                }

                values.putAll(params);
            } else {
                //values.putAll();
            }

            HashMap<String, Object> providerData = new HashMap<String, Object>();


            model.put("errors", errors);
            model.put("values", values);
            model.put("action", "new");

            Collection<DataProvider> dataProviderTypes = DataProviderRegistry.getRegisteredDataProviders();
            for (DataProvider dataProviderType : dataProviderTypes) {
                HashMap<String, Object> data = new HashMap<String, Object>();
                data.put("template", dataProviderType.getTemplatePath());
                data.put("values", dataProviderType.getDefaultConfiguration().toArrayMap());
                providerData.put(dataProviderType.getClass().getCanonicalName(), data);
            }

            model.put("dataproviderTypes", providerData/*dataProviderRegistry.getRegisteredDataProviderTypes()*/);
            return new ModelAndView("dataproviders/edit", model);
        }
        return this.redirectToIndex();
    }


    @RequestMapping("/dataproviders/edit")
    public ModelAndView editEntity(HttpServletRequest request) {

        Map<String, String[]> params = request.getParameterMap();
        String uuid = request.getParameter("uuid");
        String submit = request.getParameter("submit");

        if (uuid != null) {
            Map<String, Object> model = new HashMap<String, Object>();
            Map<String, String[]> values = new HashMap<String, String[]>();
            Map<String, String> errors = new HashMap<String, String>();

            DataProviderEntity dataProviderEntity = this.dataProviderRegistry.getDataProviderEntity(uuid);
            if (dataProviderEntity != null) {

                if (submit != null) {
                   if (submit.equals("ok")) {
                       // User pressed "update"
                       try {
                           System.out.println("saving...");
                           this.dataProviderRegistry.updateDataProviderEntity(dataProviderEntity, params);
                           return this.redirectToIndex(); // Item was updated, show list
                       } catch (Exception e) {
                           e.printStackTrace();
                           // got an exception, re-show form
                           values.putAll(params);
                       }
                   } else {
                       // User pressed "cancel"
                       return this.redirectToIndex();
                   }
                } else {
                    // Displaying the page
                    values.putAll(this.dataProviderRegistry.getDataProviderEntityValues(dataProviderEntity));
                }


                HashMap<String, HashMap<String, String>> providerData = new HashMap<String, HashMap<String, String>>();

                values.put("uuid", new String[]{uuid});
                values.put("providerType", new String[]{dataProviderEntity.getType()});

                model.put("errors", errors);
                model.put("values", values);
                model.put("action", "edit");

                Collection<DataProvider> dataProviderTypes = DataProviderRegistry.getRegisteredDataProviders();
                for (DataProvider dataProviderType : dataProviderTypes) {
                    HashMap<String, String> data = new HashMap<String, String>();
                    data.put("template", dataProviderType.getTemplatePath());
                    providerData.put(dataProviderType.getClass().getCanonicalName(), data);
                }

                //model.put("dataproviderTypes", dataProviderRegistry.getRegisteredDataProviderTypes());
                model.put("dataproviderTypes", providerData/*dataProviderRegistry.getRegisteredDataProviderTypes()*/);
                return new ModelAndView("dataproviders/edit", model);
            }
        }
        return this.redirectToIndex();
    }

}
