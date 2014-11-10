package dk.magenta.databroker.test;

import dk.magenta.databroker.Application;
import dk.magenta.databroker.core.testmodel.TestAddressRepository;
import dk.magenta.databroker.core.testmodel.TestAddressEntity;
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
    private TestAddressRepository addressRepository;


    @Test
    public void testOle() {

        TestAddressEntity ad = new TestAddressEntity();
        ad.setStreetName("My street");
        ad.setNumber(10);
        ad.setZipCode(8000);

        addressRepository.save(ad);

        TestAddressEntity ads = addressRepository.findAll().get(0);

        assertEquals( ads.getZipCode(), ad.getZipCode() );

    }


}