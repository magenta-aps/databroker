package dk.magenta.databroker.test;

import dk.magenta.databroker.Application;
import dk.magenta.databroker.core.model.oio.*;
import dk.magenta.databroker.jubk.model.JubkEntity;
import dk.magenta.databroker.jubk.model.JubkRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by jubk on 11/17/14.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@IntegrationTest
@SpringApplicationConfiguration(classes = Application.class)
public class DoubleHistoryTest {
    @Autowired
    JubkRepository jubkRepo;
    @Autowired
    VirkningRepository virkRepo;
    @Autowired
    RegistreringRepository regRepo;

    @Test
    // Creates a JubkEntity and adds a registration with two "virkninger" to it
    public void testJubk() {

        Timestamp now = new Timestamp(System.currentTimeMillis());

        RegistreringEntity oioReg = regRepo.createNew(
                now, UUID.randomUUID().toString(), null, RegistreringLivscyklusStatus.OPRETTET
        );
        regRepo.save(oioReg);

        List<VirkningEntity> virkninger = new ArrayList<VirkningEntity>();
        UUID virkningSourceUUID = UUID.randomUUID();
        virkninger.add(new VirkningEntity(
                        new Timestamp(System.currentTimeMillis() - 2000),
                        new Timestamp(System.currentTimeMillis() - 1000),
                        virkningSourceUUID.toString()
                )
        );
        virkninger.add(new VirkningEntity(
                        new Timestamp(System.currentTimeMillis() - 500),
                        new Timestamp(System.currentTimeMillis() - 200),
                        virkningSourceUUID.toString()
                )
        );
        virkRepo.save(virkninger);

        JubkEntity entity = new JubkEntity(UUID.randomUUID().toString(), "asdf");

        entity.addToRegistreringer(oioReg, virkninger);

        jubkRepo.save(entity);
    }

}