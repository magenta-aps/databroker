package dk.magenta.databroker.test;

import dk.magenta.databroker.Application;
import dk.magenta.databroker.core.testmodel.TestAddressRepository;
import dk.magenta.databroker.core.testmodel.TestAddressEntity;
import dk.magenta.databroker.dawa.model.adgangsadresse.AdgangsAdresseEntity;
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

    @Test
    public void test() {


        //printCalculation(860, 4631, "131");
        //printCalculation(860, 4631, "111");
        printCalculation(210, 702, "34");

    }

    //210:702:34 => 880983860

    private void printCalculation(int kommunekode, int vejkode, String husnr) {
        long husnrhash = AdgangsAdresseEntity.hashHusnr("111");
        printBinary((long)kommunekode << 54);
        printBinary((long)vejkode << 40);
        printBinary(husnrhash);
        long descriptor = ((long)kommunekode << 54) | ((long)vejkode << 40) | husnrhash;
        System.out.println("----------------------------------------------------------------");
        printBinary(descriptor);
        System.out.println(descriptor);
    }


    private String printBinary(long data) {
        String s = "";
        for (int i=63; i>=0; i--) {
            s += (data >> i) & 0x01;
        }
        System.out.println(s);
        return s;
    }


}