package dk.magenta.databroker.cprvejregister.model.adresse;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikBase;
import dk.magenta.databroker.core.model.oio.RegistreringEntity;
import dk.magenta.databroker.core.model.oio.VirkningEntity;
import dk.magenta.databroker.cprvejregister.model.RepositoryCollection;
import dk.magenta.databroker.cprvejregister.model.doerpunkt.DoerpunktEntity;
import dk.magenta.databroker.cprvejregister.model.husnummer.HusnummerEntity;
import dk.magenta.databroker.cprvejregister.model.husnummer.HusnummerRegistreringEntity;
import dk.magenta.databroker.cprvejregister.model.kommune.KommuneRegistreringEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by jubk on 11/10/14.
 */
@Entity
@Table(name = "adresse", indexes = { @Index(name="husnummer", columnList="husnummer_id") })
public class AdresseEntity
        extends DobbeltHistorikBase<AdresseEntity, AdresseRegistreringEntity, AdresseRegistreringsVirkningEntity>
        implements Serializable {


    public static AdresseEntity create() {
        AdresseEntity entity = new AdresseEntity();
        entity.generateNewUUID();
        return entity;
    }

    public JpaRepository getRepository(RepositoryCollection repositoryCollection) {
        return repositoryCollection.adresseRepository;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        AdresseEntity that = (AdresseEntity) other;

        if (this.getId() != that.getId()) {
            return false;
        }
        if (this.getUuid() != null ? !this.getUuid().equals(that.getUuid()) : that.getUuid() != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        long result = this.getId();
        result = 31 * result + (this.getUuid() != null ? this.getUuid().hashCode() : 0);
        return (int) result;
    }

    @Override
    protected AdresseRegistreringEntity createRegistreringEntity() {
        return new AdresseRegistreringEntity(this);
    }


    public AdresseRegistreringEntity addRegistrering(HusnummerEntity husnummer, DoerpunktEntity doerpunkt, String etage, String doerbetegnelse, String status, RegistreringEntity fromOIORegistrering, List<VirkningEntity> virkninger) {
        AdresseRegistreringEntity reg = super.addRegistrering(fromOIORegistrering, virkninger);
        reg.setHusnummer(husnummer);
        reg.setDoerpunkt(doerpunkt);
        reg.setEtagebetegnelse(etage);
        reg.setDoerbetegnelse(doerbetegnelse);
        reg.setStatus(status);
        return reg;
    }
}
