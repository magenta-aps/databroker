package dk.magenta.databroker.cprvejregister.model.postnummer;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikVersion;

import javax.persistence.*;

/**
 * Created by jubk on 11/12/14.
 */
@Entity
@Table(name = "postnummer_registrering")
public class PostnummerVersionEntity
        extends DobbeltHistorikVersion<PostnummerEntity, PostnummerVersionEntity> {

        @ManyToOne(fetch = FetchType.LAZY)
        private PostnummerEntity entity;

        protected PostnummerVersionEntity() {
                super();
        }

        public PostnummerVersionEntity(PostnummerEntity entity) {
                super(entity);
        }

        @Override
        public PostnummerEntity getEntity() {
                return entity;
        }

        @Override
        public void setEntity(PostnummerEntity entitet) {
                this.entity = entitet;
        }




        /*
         * Version-specific data
         */

        @Basic
        @Column(name = "nummer", nullable = false, insertable = true, updatable = true)
        private int nummer;

        @Basic
        @Column(name = "navn", nullable = true, insertable = true, updatable = true, length = 36)
        private String navn;

        public int getNummer() {
                return this.nummer;
        }

        public void setNummer(int nummer) {
                this.nummer = nummer;
        }

        public String getNavn() {
                return navn;
        }

        public void setNavn(String navn) {
                this.navn = navn;
        }


        @Override
        public boolean equals(Object other) {
                if (this == other) return true;
                if (!super.equals(other)) {
                        return false;
                }
                return true;
        }

        @Override
        public int hashCode() {
                long result = this.getId();

                result = 31 * result + (this.nummer);
                result = 31 * result + (this.navn != null ? this.navn.hashCode() : 0);

                return (int) result;
        }
}
