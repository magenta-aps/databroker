package dk.magenta.databroker.core.controller;

import dk.magenta.databroker.core.DataProviderRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jubk on 15-12-2014.
 */

@Controller
public class DataProviderController {
    @RequestMapping("/dataproviders/")
    public ModelAndView index() {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("test", "hephey!");
        model.put("dataprovidertypes", DataProviderRegistry.getRegisteredDataProviderTypes());
        return new ModelAndView("dataproviders/index", model);
    }

}
