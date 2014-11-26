package dk.magenta.databroker.cprvejregister.model.navngivenvej;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikVersion;
import dk.magenta.databroker.cprvejregister.model.kommune.KommuneEntity;
import dk.magenta.databroker.cprvejregister.model.kommunedelafnavngivenvej.KommunedelAfNavngivenVejEntity;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by jubk on 11/12/14.
 */
@Entity
@Table(name = "navngiven_vej_registrering", indexes = { @Index(columnList="vejnavn") })
public class NavngivenVejVersionEntity
        extends DobbeltHistorikVersion<NavngivenVejEntity, NavngivenVejVersionEntity> {


        @ManyToOne(fetch = FetchType.EAGER)
        private NavngivenVejEntity entity;

        protected NavngivenVejVersionEntity() {
                super();
        }

        public NavngivenVejVersionEntity(NavngivenVejEntity entity) {
                super(entity);
        }

        @Override
        public NavngivenVejEntity getEntity() {
                return entity;
        }

        @Override
        public void setEntity(NavngivenVejEntity entitet) {
                this.entity = entitet;
        }


        /*
         * Version-specific data
         */

        @Basic
        @Column(name = "vejnavn", nullable = true, insertable = true, updatable = true, length = 255)
        private String vejnavn;

        @Basic
        @Column(name = "status", nullable = true, insertable = true, updatable = true, length = 255)
        private String status;

        @Basic
        @Column(name = "vejaddresseringsnavn", nullable = true, insertable = true, updatable = true, length = 20)
        private String vejaddresseringsnavn;

        @Basic
        @Column(name = "beskrivelse", nullable = true, insertable = true, updatable = true, columnDefinition="Text")
        private String beskrivelse;

        @Basic
        @Column(name = "retskrivningskontrol", nullable = true, insertable = true, updatable = true, length = 255)
        private String retskrivningskontrol;

        @OneToMany(mappedBy = "navngivenVejVersion", fetch = FetchType.LAZY)
        private Collection<KommunedelAfNavngivenVejEntity> kommunedeleAfNavngivenVej;

        @ManyToOne(fetch = FetchType.LAZY, optional = true)
        private KommuneEntity ansvarligKommune;

        public String getVejnavn() {
                return this.vejnavn;
        }

        public void setVejnavn(String vejnavn) {
                this.vejnavn = vejnavn;
        }

        public String getStatus() {
                return this.status;
        }

        public void setStatus(String status) {
                this.status = status;
        }

        public String getVejaddresseringsnavn() {
                return this.vejaddresseringsnavn;
        }

        public void setVejaddresseringsnavn(String vejaddresseringsnavn) {
                this.vejaddresseringsnavn = vejaddresseringsnavn;
        }

        public String getBeskrivelse() {
                return this.beskrivelse;
        }

        public void setBeskrivelse(String beskrivelse) {
                this.beskrivelse = beskrivelse;
        }

        public String getRetskrivningskontrol() {
                return this.retskrivningskontrol;
        }

        public void setRetskrivningskontrol(String retskrivningskontrol) {
                this.retskrivningskontrol = retskrivningskontrol;
        }

        public Collection<KommunedelAfNavngivenVejEntity> getKommunedeleAfNavngivenVej() {
                return kommunedeleAfNavngivenVej;
        }

        public void setKommunedeleAfNavngivenVej(Collection<KommunedelAfNavngivenVejEntity> kommunedeleAfNavngivenVej) {
                this.kommunedeleAfNavngivenVej = kommunedeleAfNavngivenVej;
        }

        public KommuneEntity getAnsvarligKommune() {
                return this.ansvarligKommune;
        }

        public void setAnsvarligKommune(KommuneEntity ansvarligKommune) {
                this.ansvarligKommune = ansvarligKommune;
        }

}
