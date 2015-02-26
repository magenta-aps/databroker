package dk.magenta.databroker.core;

import dk.magenta.databroker.core.model.DataProviderEntity;
import dk.magenta.databroker.core.model.oio.RegistreringEntity;
import dk.magenta.databroker.core.model.oio.RegistreringRepository;

/**
 * Created by lars on 25-02-15.
 */
public class RegistreringInfo {
    private RegistreringEntity createRegistrering;
    private RegistreringEntity updateRegisterering;
    private DataProviderEntity dataProviderEntity;

    public RegistreringInfo(RegistreringRepository registreringRepository, DataProviderEntity dataProviderEntity) {
        this.dataProviderEntity = dataProviderEntity;
        RegistreringEntity createRegistrering = registreringRepository.createNew(dataProviderEntity);
        RegistreringEntity updateRegistrering = registreringRepository.createUpdate(dataProviderEntity);

        if (createRegistrering == null && updateRegistrering == null) {
            System.err.println("Both registrations are null; cannot create RegistreringInfo object");
        } else if (createRegistrering == null) {
            createRegistrering = updateRegistrering;
        } else if (updateRegistrering == null) {
            updateRegistrering = createRegistrering;
        }

        System.out.println(createRegistrering+" "+updateRegistrering);

        this.createRegistrering = createRegistrering;
        this.updateRegisterering = updateRegistrering;
    }

    public DataProviderEntity getDataProviderEntity() {
        return dataProviderEntity;
    }

    public RegistreringEntity getCreateRegistrering() {
        return createRegistrering;
    }

    public RegistreringEntity getUpdateRegisterering() {
        return updateRegisterering;
    }

    public boolean has(RegistreringEntity registreringEntity) {
        return registreringEntity == this.createRegistrering || registreringEntity == this.updateRegisterering;
    }

}
