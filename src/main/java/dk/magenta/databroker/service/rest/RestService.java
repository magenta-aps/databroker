package dk.magenta.databroker.service.rest;

import dk.magenta.databroker.component.DataBean;
import dk.magenta.databroker.core.testmodel.Address;
import dk.magenta.databroker.core.testmodel.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;


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
