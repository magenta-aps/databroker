package dk.magenta.databroker.core.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;


/**
 * Created by ole on 17/10/14.
 */
@Controller
public class SampleController {

    @RequestMapping("/sample")
    @ResponseBody
    public String sample() {
        return "Sample!";
    }

}
