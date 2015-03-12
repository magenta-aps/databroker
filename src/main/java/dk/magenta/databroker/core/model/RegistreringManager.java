package dk.magenta.databroker.core.model;

import dk.magenta.databroker.core.model.oio.*;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * Created by lars on 12-03-15.
 */
@Component
public class RegistreringManager {

    private RegistreringEntity createRegistrering(RegistreringRepository regRepo, RegistreringLivscyklusRepository lsRepo, DataProviderEntity sourceDataProvider, String note, RegistreringLivscyklusStatus status) {
        RegistreringEntity result = new RegistreringEntity(
                new Timestamp(System.currentTimeMillis()),
                sourceDataProvider.getUuid(),
                note
        );
        result.setLivscyklus(this.findOrCreateLivscyklusByStatus(lsRepo, status));
        regRepo.saveAndFlush(result);
        return result;
    }

    public RegistreringEntity createNewRegistrering(RegistreringRepository regRepo, RegistreringLivscyklusRepository lsRepo, DataProviderEntity sourceDataProvider, String note) {
        return this.createRegistrering(regRepo, lsRepo, sourceDataProvider, note, RegistreringLivscyklusStatus.OPRETTET);
    }

    public RegistreringEntity createUpdateRegistrering(RegistreringRepository regRepo, RegistreringLivscyklusRepository lsRepo, DataProviderEntity sourceDataProvider, String note) {
        return this.createRegistrering(regRepo, lsRepo, sourceDataProvider, note, RegistreringLivscyklusStatus.RETTET);
    }

    public RegistreringEntity createNewRegistrering(RegistreringRepository regRepo, RegistreringLivscyklusRepository lsRepo, DataProviderEntity sourceDataProvider) {
        String note = "Created by dataprovider "+sourceDataProvider.getClass().getName();
        return this.createNewRegistrering(regRepo, lsRepo, sourceDataProvider, note);
    }
    public RegistreringEntity createUpdateRegistrering(RegistreringRepository regRepo, RegistreringLivscyklusRepository lsRepo, DataProviderEntity sourceDataProvider) {
        String note = "Updated by dataprovider "+sourceDataProvider.getClass().getName();
        return this.createUpdateRegistrering(regRepo, lsRepo, sourceDataProvider, note);
    }
/*
    public RegistreringEntity importNewRegistrering(RegistreringRepository regRepo, RegistreringLivscyklusRepository lsRepo,
                                                    Timestamp registreringstidspunk, String aktoerUUID, String note, RegistreringLivscyklusStatus status
    ) {
        RegistreringEntity result = new RegistreringEntity(registreringstidspunk, aktoerUUID, note);
        result.setLivscyklus(this.findOrCreateLivscyklusByStatus(lsRepo, status));
        return result;
    }
*/

    private RegistreringLivscyklusEntity findOrCreateLivscyklusByNavn(RegistreringLivscyklusRepository lsRepo, String navn) {
        RegistreringLivscyklusEntity result = lsRepo.getByNavn(navn);
        if(result == null) {
            result = new RegistreringLivscyklusEntity(navn);
            lsRepo.save(result);
        }
        return result;
    }

    private RegistreringLivscyklusEntity findOrCreateLivscyklusByStatus(RegistreringLivscyklusRepository lsRepo, RegistreringLivscyklusStatus status) {
        return this.findOrCreateLivscyklusByNavn(lsRepo, status.toString());
    }

}
