package dk.magenta.databroker.cprvejregister.model.kommune;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikVersion;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by jubk on 11/12/14.
 */
@Entity
@Table(name = "kommune_registrering")
public class KommuneVersionEntity
        extends DobbeltHistorikVersion<KommuneEntity, KommuneVersionEntity, KommuneRegistreringsVirkningEntity> {

        @Basic
        @Column(name = "navn", nullable = false, insertable = true, updatable = true, length = 255)
        private String navn;

        public String getNavn() {
                return this.navn;
        }

        public void setNavn(String navn) {
                this.navn = navn;
        }

        public KommuneVersionEntity(KommuneEntity entitet) {
                super(entitet);
        }

        @Override
        protected KommuneRegistreringsVirkningEntity createVirkningEntity() {
                return new KommuneRegistreringsVirkningEntity(this);
        }
}
