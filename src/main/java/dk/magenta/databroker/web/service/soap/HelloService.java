package dk.magenta.databroker.web.service.soap;


import org.apache.cxf.annotations.EndpointProperties;
import org.apache.cxf.annotations.EndpointProperty;
import org.springframework.stereotype.Component;

import javax.jws.WebService;

@WebService(endpointInterface = "dk.magenta.databroker.web.service.soap.HelloInterface")
public class HelloService implements HelloInterface {
    public String sayHi(String t) {
        return "Hello " + t;
    }
}
