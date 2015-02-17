package dk.magenta.databroker.web.service.soap;

import javax.jws.WebService;

@WebService(endpointInterface = "dk.magenta.databroker.web.service.soap.HelloInterface")
public class HelloService implements HelloInterface {
    public String sayHi(String t) {
        return "Hello " + t;
    }
}
