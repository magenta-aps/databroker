package dk.magenta.databroker.core.model.oio;

import javax.persistence.*;
import java.util.*;

/**
 * Created by jubk on 11/12/14.
 */
@MappedSuperclass
public abstract class DobbeltHistorikVersion<
        E extends DobbeltHistorikBase<E, R>,
        R extends DobbeltHistorikVersion<E, R>
        > {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(nullable = false, insertable = true, updatable = true)
        private Long id;

        @OneToOne(optional = false)
        private RegistreringEntity registrering;

        @ManyToMany
        @JoinTable(
                joinColumns = @JoinColumn( name="reg_id"),
                inverseJoinColumns = @JoinColumn( name="virk_id")
        )
        private Collection<VirkningEntity> virkninger;


        protected DobbeltHistorikVersion() {
                this(null);
        }

        public DobbeltHistorikVersion(E entitet) {
                this.setEntity(entitet);
                this.virkninger = new ArrayList<VirkningEntity>();
        }

        public Long getId() {
                return id;
        }

        public abstract E getEntity();

        public abstract void setEntity(E entitet);

        public RegistreringEntity getRegistrering() {
                return registrering;
        }

        public void setRegistrering(RegistreringEntity registrering) {
                this.registrering = registrering;
        }

        private void setId(Long id) {
                this.id = id;
        }

        public Collection<VirkningEntity> getVirkninger() {
                return virkninger;
        }

        public void setVirkninger(Collection<VirkningEntity> virkninger) {
                this.virkninger = virkninger;
        }
}
