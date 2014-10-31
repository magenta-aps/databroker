package dk.magenta.databroker.web.service.soap;

import javax.jws.WebService;


@WebService
public interface HelloInterface {
    public String sayHi(String text);
}
