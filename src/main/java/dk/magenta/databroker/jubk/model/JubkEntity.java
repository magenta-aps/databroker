package dk.magenta.databroker.jubk.model;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikBase;
import dk.magenta.databroker.core.model.oio.RegistreringEntity;
import dk.magenta.databroker.core.model.oio.VirkningEntity;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.InputMismatchException;
import java.util.List;

/**
 * Created by jubk on 11/12/14.
 */
@Entity
@Table(name = "jubk_jubk")
public class JubkEntity extends DobbeltHistorikBase<JubkEntity, JubkRegistreringEntity> {
        public JubkEntity() {
                this.versions = new ArrayList<JubkRegistreringEntity>();
        }

        /*
         * Versioning fields
         */

        @OneToMany(mappedBy = "entity", cascade = CascadeType.ALL)
        private Collection<JubkRegistreringEntity> versions;

        @OneToOne
        private JubkRegistreringEntity latestVersion;

        @OneToOne
        private JubkRegistreringEntity preferredVersion;

        @Override
        public Collection<JubkRegistreringEntity> getVersioner() {
                return versions;
        }

        @Override
        public JubkRegistreringEntity getLatestVersion() {
                return latestVersion;
        }

        @Override
        public void setLatestVersion(JubkRegistreringEntity latestVersion) {
                this.latestVersion = latestVersion;
        }

        @Override
        public JubkRegistreringEntity getPreferredVersion() {
                return preferredVersion;
        }

        @Override
        public void setPreferredVersion(JubkRegistreringEntity preferredVersion) {
                this.preferredVersion = preferredVersion;
        }

        @Override
        protected JubkRegistreringEntity createVersionEntity() {
                return new JubkRegistreringEntity(this);
        }

        /*
         * Fields belonging to the entity
         */


        /*
         * Custom methods
         */

        public JubkRegistreringEntity addVersion(
                String customData, RegistreringEntity fromOIORegistrering, List<VirkningEntity> virkninger
        ) throws InputMismatchException {
                JubkRegistreringEntity newVersion = super.addVersion(fromOIORegistrering, virkninger);
                newVersion.setCustomData(customData);
                return newVersion;
        }


        public String getTypeName() {
                return "jubk";
        }
        public JSONObject toJSON() {
                return new JSONObject();
        }
}
