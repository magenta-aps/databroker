package dk.magenta.databroker.core.model.oio;

import javax.persistence.*;
import java.util.*;

/**
 * Created by jubk on 11/12/14.
 */

@MappedSuperclass
public abstract class DobbeltHistorikBase<
        E extends DobbeltHistorikBase<E, R>,
        R extends DobbeltHistorikVersion<E, R>
        >  extends UniqueBase {

    protected DobbeltHistorikBase() {
    }

    public DobbeltHistorikBase(String uuid) {
        super(uuid);
    }

    public abstract Collection<R> getVersions();

    public abstract R getLatestVersion();
    public abstract void setLatestVersion(R latestVersion);

    public abstract R getPreferredVersion();
    public abstract void setPreferredVersion(R preferredVersion);

    protected abstract R createVersionEntity();

    private R createVersionEntity(RegistreringEntity forOIORegistrering) {
        return this.createVersionEntity(forOIORegistrering,new ArrayList<VirkningEntity>());
    }

    private R createVersionEntity(RegistreringEntity forOIORegistrering, Collection<VirkningEntity> virkninger) {
        R newReg = this.createVersionEntity();
        newReg.setRegistrering(forOIORegistrering);
        newReg.setVirkninger(virkninger);
        return newReg;
    }

    @SuppressWarnings("unchecked")
    private void addVersion(R version) throws InputMismatchException {
        version.setEntity((E) this);

        R latest = this.getLatestVersion();

        RegistreringEntity newReg = version.getRegistrering();
        if(newReg == null) {
            throw new InputMismatchException("Trying to add a version wihtout a registration");
        }

        if(
                latest == null ||
                        newReg.getRegistreringFra().after(latest.getRegistrering().getRegistreringFra())
                ) {
            this.setLatestVersion(version);
        }

        Collection<R> versioner = this.getVersions();
        versioner.add(version);
    }

    public R addVersion(RegistreringEntity fromOIORegistrering)
            throws InputMismatchException {
        return this.addVersion(fromOIORegistrering, new ArrayList<VirkningEntity>());
    }

    public R addVersion(RegistreringEntity fromOIORegistrering, List<VirkningEntity> virkninger)
            throws InputMismatchException {
        R newReg = this.createVersionEntity(fromOIORegistrering, virkninger);
        this.addVersion(newReg);
        return newReg;
    }

}
