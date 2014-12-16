package dk.magenta.databroker.cprvejregister.model;

import dk.magenta.databroker.core.model.oio.RegistreringEntity;

import java.util.Date;

/**
 * Created by lars on 16-12-14.
 */
public abstract class Model {

    private RegistreringEntity createRegistrering;
    private RegistreringEntity updateRegistrering;

    public Model(RegistreringEntity createRegistrering, RegistreringEntity updateRegistrering) {
        this.createRegistrering = createRegistrering;
        this.updateRegistrering = updateRegistrering;
    }

    public RegistreringEntity getCreateRegistrering() {
        return createRegistrering;
    }

    public RegistreringEntity getUpdateRegistrering() {
        return updateRegistrering;
    }

    private long ticTime = 0;
    protected long tic() {
        this.ticTime = this.indepTic();
        return this.ticTime;
    }
    protected long indepTic() {
        return new Date().getTime();
    }
    protected long toc(long ticTime) {
        return new Date().getTime() - ticTime;
    }
    protected long toc() {
        return new Date().getTime() - this.ticTime;
    }

}
