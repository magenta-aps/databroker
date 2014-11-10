package dk.magenta.databroker.service.rest;

import dk.magenta.databroker.component.DataBean;
import dk.magenta.databroker.core.testmodel.TestAddressEntity;
import dk.magenta.databroker.core.testmodel.TestAddressRepository;
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
    private TestAddressRepository testAddressRepository;

    @GET
    @Path("by-street")
    public TestAddressEntity custom() {

        TestAddressEntity ad = new TestAddressEntity();
        ad.setStreetName("Solbærhaven");
        ad.setNumber(13);
        ad.setZipCode(8520);

        testAddressRepository.save(ad);

        TestAddressEntity ads = testAddressRepository.findAll().get(0);
        return ads;


        //return db.getSomeData();
    }




}
