package dk.magenta.databroker.cprvejregister.model.vejnavneomraade;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikVersion;
import dk.magenta.databroker.cprvejregister.model.navngivenvej.NavngivenVejEntity;

import javax.persistence.*;

/**
 * Created by jubk on 11/12/14.
 */
@Entity
@Table(name = "vejnavneomraade_registrering")
public class VejnavneomraadeVersionEntity
        extends DobbeltHistorikVersion<VejnavneomraadeEntity, VejnavneomraadeVersionEntity> {

        @ManyToOne
        private VejnavneomraadeEntity entity;

        protected VejnavneomraadeVersionEntity() {
                super();
        }

        public VejnavneomraadeVersionEntity(VejnavneomraadeEntity entity) {
                super(entity);
        }

        @Override
        public VejnavneomraadeEntity getEntity() {
                return entity;
        }

        @Override
        public void setEntity(VejnavneomraadeEntity entity) {
                this.entity = entity;
        }






        /*
         * Version-specific data
         */

        @Basic
        @Column(name = "vejnavneomraade", nullable = false, insertable = true, updatable = true, columnDefinition="Text")
        private String vejnavneomraade;

        @Basic
        @Column(name = "vejnavnelinje", nullable = true, insertable = true, updatable = true, length = 255)
        private String vejnavnelinje;

        @Basic
        @Column(name = "noejagtighedsklasse", nullable = true, insertable = true, updatable = true, length = 255)
        private String noejagtighedsklasse;

        @Basic
        @Column(name = "kilde", nullable = true, insertable = true, updatable = true, length = 255)
        private String kilde;

        @Basic
        @Column(name = "teknisk_standard", nullable = true, insertable = true, updatable = true, length = 255)
        private String tekniskStandard;

        @OneToOne(mappedBy = "vejnavneomraade")
        private NavngivenVejEntity navngivenVej;



        public String getVejnavneomraade() {
                return this.vejnavneomraade;
        }

        public void setVejnavneomraade(String vejnavneomraade) {
                this.vejnavneomraade = vejnavneomraade;
        }

        public String getVejnavnelinje() {
                return this.vejnavnelinje;
        }

        public void setVejnavnelinje(String vejnavnelinje) {
                this.vejnavnelinje = vejnavnelinje;
        }

        public String getNoejagtighedsklasse() {
                return this.noejagtighedsklasse;
        }

        public void setNoejagtighedsklasse(String noejagtighedsklasse) {
                this.noejagtighedsklasse = noejagtighedsklasse;
        }

        public String getKilde() {
                return this.kilde;
        }

        public void setKilde(String kilde) {
                this.kilde = kilde;
        }

        public String getTekniskStandard() {
                return this.tekniskStandard;
        }

        public void setTekniskStandard(String tekniskStandard) {
                this.tekniskStandard = tekniskStandard;
        }

        public NavngivenVejEntity getNavngivenVeje() {
                return this.navngivenVej;
        }

        public void setNavngivenVeje(NavngivenVejEntity navngivneVeje) {
                this.navngivenVej = navngivenVej;
        }


        @Override
        public boolean equals(Object o) {
                if (this == o) return true;
                if (!super.equals(o)) {
                        return false;
                }
                VejnavneomraadeVersionEntity that = (VejnavneomraadeVersionEntity) o;
                if (this.kilde != null ? !this.kilde.equals(that.kilde) : that.kilde != null) {
                        return false;
                }
                if (this.noejagtighedsklasse != null ? !this.noejagtighedsklasse.equals(that.noejagtighedsklasse) : that.noejagtighedsklasse != null) {
                        return false;
                }
                if (this.tekniskStandard != null ? !this.tekniskStandard.equals(that.tekniskStandard) : that.tekniskStandard != null) {
                        return false;
                }
                if (this.vejnavnelinje != null ? !this.vejnavnelinje.equals(that.vejnavnelinje) : that.vejnavnelinje != null) {
                        return false;
                }
                if (this.vejnavneomraade != null ? !this.vejnavneomraade.equals(that.vejnavneomraade) : that.vejnavneomraade != null) {
                        return false;
                }
                return true;
        }

        @Override
        public int hashCode() {
                long result = this.getId();
                result = 31 * result + (this.vejnavneomraade != null ? this.vejnavneomraade.hashCode() : 0);
                result = 31 * result + (this.vejnavnelinje != null ? this.vejnavnelinje.hashCode() : 0);
                result = 31 * result + (this.noejagtighedsklasse != null ? this.noejagtighedsklasse.hashCode() : 0);
                result = 31 * result + (this.kilde != null ? this.kilde.hashCode() : 0);
                result = 31 * result + (this.tekniskStandard != null ? this.tekniskStandard.hashCode() : 0);
                return (int) result;
        }
}
