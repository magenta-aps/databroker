package dk.magenta.databroker.cprvejregister.model.adresse;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikRegistrering;
import dk.magenta.databroker.core.model.oio.RegistreringEntity;
import dk.magenta.databroker.core.model.oio.VirkningEntity;
import dk.magenta.databroker.cprvejregister.model.doerpunkt.DoerpunktEntity;
import dk.magenta.databroker.cprvejregister.model.husnummer.HusnummerEntity;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by jubk on 11/12/14.
 */
@Entity
@Table(name = "adresse_registrering")
public class AdresseRegistreringEntity
        extends DobbeltHistorikRegistrering<AdresseEntity, AdresseRegistreringEntity, AdresseRegistreringsVirkningEntity> {

        public AdresseRegistreringEntity(AdresseEntity entitet) {
                super(entitet);
        }
        @Basic
        @Column(name = "status", nullable = false, insertable = true, updatable = true, length = 255)
        private String status;

        @Basic
        @Column(name = "doerbetegnelse", nullable = true, insertable = true, updatable = true, length = 255)
        private String doerbetegnelse;

        @Basic
        @Column(name = "etagebetegnelse", nullable = true, insertable = true, updatable = true, length = 255)
        private String etagebetegnelse;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "husnummer_id", nullable = false)
        private HusnummerEntity husnummer;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "doerpunkt_id", nullable = true)
        private DoerpunktEntity doerpunkt;

        public String getStatus() {
                return this.status;
        }

        public void setStatus(String status) {
                this.status = status;
        }

        public String getDoerbetegnelse() {
                return this.doerbetegnelse;
        }

        public void setDoerbetegnelse(String doerbetegnelse) {
                this.doerbetegnelse = doerbetegnelse;
        }

        public String getEtagebetegnelse() {
                return this.etagebetegnelse;
        }

        public void setEtagebetegnelse(String etagebetegnelse) {
                this.etagebetegnelse = etagebetegnelse;
        }

        public HusnummerEntity getHusnummer() {
                return this.husnummer;
        }

        public void setHusnummer(HusnummerEntity husnummer) {
                this.husnummer = husnummer;
        }

        public DoerpunktEntity getDoerpunkt() {
                return this.doerpunkt;
        }

        public void setDoerpunkt(DoerpunktEntity doerpunkt) {
                this.doerpunkt = doerpunkt;
        }

        @Override
        protected AdresseRegistreringsVirkningEntity createVirkningEntity() {
                return new AdresseRegistreringsVirkningEntity(this);
        }
}
