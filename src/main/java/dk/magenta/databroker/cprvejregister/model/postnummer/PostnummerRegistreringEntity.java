package dk.magenta.databroker.cprvejregister.model.postnummer;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikRegistrering;
import dk.magenta.databroker.core.model.oio.RegistreringEntity;
import dk.magenta.databroker.core.model.oio.VirkningEntity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Collection;

/**
 * Created by jubk on 11/12/14.
 */
@Entity
@Table(name = "postnummer_registrering")
public class PostnummerRegistreringEntity
        extends DobbeltHistorikRegistrering<PostnummerEntity, PostnummerRegistreringEntity, PostnummerRegistreringsVirkningEntity> {

        @Basic
        @Column(name = "navn", nullable = false, insertable = true, updatable = true, length = 255)
        private String navn;

        public String getNavn() {
                return this.navn;
        }

        public void setNavn(String navn) {
                this.navn = navn;
        }

        public PostnummerRegistreringEntity(PostnummerEntity entitet) {
                super(entitet);
        }

        @Override
        protected PostnummerRegistreringsVirkningEntity createVirkningEntity() {
                return new PostnummerRegistreringsVirkningEntity(this);
        }
}
