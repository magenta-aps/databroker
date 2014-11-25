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
        @Column(nullable = false, insertable = true, updatable = true, length = 36)
        private String navn;

        public String getNavn() {
                return navn;
        }

        public void setNavn(String navn) {
                this.navn = navn;
        }

}
