package dk.magenta.databroker.cprvejregister.model.husnummer;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikRegistrering;
import dk.magenta.databroker.core.model.oio.RegistreringEntity;
import dk.magenta.databroker.core.model.oio.VirkningEntity;
import dk.magenta.databroker.cprvejregister.model.adgangspunkt.AdgangspunktEntity;
import dk.magenta.databroker.cprvejregister.model.adresse.AdresseEntity;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by jubk on 11/12/14.
 */
@Entity
@Table(name = "husnummer_registrering")
public class HusnummerRegistreringEntity
        extends DobbeltHistorikRegistrering<HusnummerEntity, HusnummerRegistreringEntity, HusnummerRegistreringsVirkningEntity> {

        public HusnummerRegistreringEntity(HusnummerEntity entitet) {
                super(entitet);
        }


        @Basic
        @Column(name = "husnummerbetegnelse", nullable = true, insertable = true, updatable = true, length = 255)
        private String husnummerbetegnelse;

        @OneToMany(mappedBy = "husnummer")
        private Collection<AdresseEntity> adresser;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "tilknyttet_adgangspunkt_id", nullable = true)
        private AdgangspunktEntity tilknyttetAdgangspunkt;



        public String getHusnummerbetegnelse() {
                return this.husnummerbetegnelse;
        }

        public void setHusnummerbetegnelse(String husnummerbetegnelse) {
                this.husnummerbetegnelse = husnummerbetegnelse;
        }

        public Collection<AdresseEntity> getAdresser() {
                return this.adresser;
        }

        public void setAdresser(Collection<AdresseEntity> adresser) {
                this.adresser = adresser;
        }

        public AdgangspunktEntity getTilknyttetAdgangspunkt() {
                return this.tilknyttetAdgangspunkt;
        }

        public void setTilknyttetAdgangspunkt(AdgangspunktEntity tilknyttetAdgangspunkt) {
                this.tilknyttetAdgangspunkt = tilknyttetAdgangspunkt;
        }

        @Override
        protected HusnummerRegistreringsVirkningEntity createVirkningEntity() {
                return new HusnummerRegistreringsVirkningEntity(this);
        }
}
