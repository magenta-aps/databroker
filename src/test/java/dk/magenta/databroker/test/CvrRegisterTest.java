package dk.magenta.databroker.test;

import dk.magenta.databroker.Application;
import dk.magenta.databroker.core.model.DataProviderEntity;
import dk.magenta.databroker.cprvejregister.dataproviders.registers.CprRegister;
import dk.magenta.databroker.cvrregister.CvrRegister;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.UUID;

/**
 * Created by lars on 05-11-14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@IntegrationTest
@SpringApplicationConfiguration(classes = Application.class)
@EnableTransactionManagement
public class CvrRegisterTest {

    public CvrRegisterTest() {
    }

    @Autowired
    private ApplicationContext context;

    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private CvrRegister cvrRegister;

    @Test
    //@Transactional
    public void testCvrRegister() {
        DataProviderEntity tmpDataProviderEntity = new DataProviderEntity();
        tmpDataProviderEntity.setUuid(UUID.randomUUID().toString());
        tmpDataProviderEntity.setType(CvrRegister.class);
        cvrRegister.pull(false, true, tmpDataProviderEntity);
    }
}