package dk.magenta.databroker.core;

import dk.magenta.databroker.core.model.oio.RegistreringEntity;

/**
 * Created by lars on 25-02-15.
 */
public class RegistreringInfo {
    private RegistreringEntity createRegistrering;
    private RegistreringEntity updateRegisterering;

    public RegistreringInfo(RegistreringEntity createRegistrering, RegistreringEntity updateRegistrering) {

        if (createRegistrering == null && updateRegistrering == null) {
            System.err.println("Both registrations are null; cannot create RegistreringInfo object");
        } else if (createRegistrering == null) {
            createRegistrering = updateRegistrering;
        } else if (updateRegistrering == null) {
            updateRegistrering = createRegistrering;
        }

        this.createRegistrering = createRegistrering;
        this.updateRegisterering = updateRegistrering;
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
