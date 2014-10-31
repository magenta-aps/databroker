package dk.magenta.databroker.service.rest;

import dk.magenta.databroker.component.DataBean;
import dk.magenta.databroker.model.Address;
import dk.magenta.databroker.model.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;


@Path("search")
@Produces({ "application/json", "application/xml" })
public class RestService {

    @Autowired
    private DataBean db;

    @Autowired
    private AddressRepository addressRepository;

    @GET
    @Path("by-street")
    public Address custom() {

        Address ad = new Address();
        ad.setStreetName("Solbærhaven");
        ad.setNumber(13);
        ad.setZipCode(8520);

        addressRepository.save(ad);

        Address ads = addressRepository.findAll().get(0);
        return ads;


        //return db.getSomeData();
    }




}
