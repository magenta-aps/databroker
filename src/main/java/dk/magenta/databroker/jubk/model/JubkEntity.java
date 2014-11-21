package dk.magenta.databroker.jubk.model;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikBase;
import dk.magenta.databroker.core.model.oio.RegistreringEntity;
import dk.magenta.databroker.core.model.oio.VirkningEntity;
import dk.magenta.databroker.cprvejregister.model.RepositoryCollection;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

/**
 * Created by jubk on 11/12/14.
 */
@Entity
@Table(name = "jubk_jubk")
public class JubkEntity extends DobbeltHistorikBase<
        JubkEntity,
        JubkRegistreringEntity,
        JubkRegistreringsvirkningEntity
        > {
        public JubkEntity() {
                super();
        }

        public JubkEntity(String uuid, String brugervendtNoegle) {
                super(uuid, brugervendtNoegle);
        }


        protected JubkRegistreringEntity createRegistreringEntity(RegistreringEntity oioRegistrering, List<VirkningEntity> virkninger) {
                return new JubkRegistreringEntity(this, oioRegistrering, virkninger);
        }

        public JpaRepository getRepository(RepositoryCollection repositoryCollection) {
                return null;
        }
}
