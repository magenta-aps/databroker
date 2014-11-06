package dk.magenta.databroker.test;

import dk.magenta.databroker.Application;
import dk.magenta.databroker.core.testmodel.AddressRepository;
import dk.magenta.databroker.core.testmodel.Address;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.junit.Assert.assertEquals;


@RunWith(SpringJUnit4ClassRunner.class)
@IntegrationTest
@SpringApplicationConfiguration(classes = Application.class)
public class SimpleTest {

    @Autowired
    private AddressRepository addressRepository;


    @Test
    public void testOle() {

        Address ad = new Address();
        ad.setStreetName("My street");
        ad.setNumber(10);
        ad.setZipCode(8000);

        addressRepository.save(ad);

        Address ads = addressRepository.findAll().get(0);

        assertEquals( ads.getZipCode(), ad.getZipCode() );

    }


}