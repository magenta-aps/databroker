package dk.magenta.databroker.cprvejregister.model.navngivenvej;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikVersion;
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

        @OneToMany(mappedBy = "navngivenVejRegistrering")
        private Collection<KommunedelAfNavngivenVejEntity> kommunedeleAfNavngivenVej;


        @ManyToOne
        private NavngivenVejEntity entitet;

        protected NavngivenVejVersionEntity() {
                super();
        }

        public NavngivenVejVersionEntity(NavngivenVejEntity entitet) {
                super(entitet);
        }

        @Override
        public NavngivenVejEntity getEntitet() {
                return entitet;
        }

        @Override
        public void setEntitet(NavngivenVejEntity entitet) {
                this.entitet = entitet;
        }


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

        @Override
        public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                NavngivenVejVersionEntity that = (NavngivenVejVersionEntity) o;

                if (beskrivelse != null ? !beskrivelse.equals(that.beskrivelse) : that.beskrivelse != null)
                        return false;
                if (kommunedeleAfNavngivenVej != null ? !kommunedeleAfNavngivenVej.equals(that.kommunedeleAfNavngivenVej) : that.kommunedeleAfNavngivenVej != null)
                        return false;
                if (retskrivningskontrol != null ? !retskrivningskontrol.equals(that.retskrivningskontrol) : that.retskrivningskontrol != null)
                        return false;
                if (status != null ? !status.equals(that.status) : that.status != null) return false;
                if (vejaddresseringsnavn != null ? !vejaddresseringsnavn.equals(that.vejaddresseringsnavn) : that.vejaddresseringsnavn != null)
                        return false;
                if (vejnavn != null ? !vejnavn.equals(that.vejnavn) : that.vejnavn != null) return false;

                return true;
        }

        @Override
        public int hashCode() {
                int result = vejnavn != null ? vejnavn.hashCode() : 0;
                result = 31 * result + (status != null ? status.hashCode() : 0);
                result = 31 * result + (vejaddresseringsnavn != null ? vejaddresseringsnavn.hashCode() : 0);
                result = 31 * result + (beskrivelse != null ? beskrivelse.hashCode() : 0);
                result = 31 * result + (retskrivningskontrol != null ? retskrivningskontrol.hashCode() : 0);
                result = 31 * result + (kommunedeleAfNavngivenVej != null ? kommunedeleAfNavngivenVej.hashCode() : 0);
                return result;
        }
}
