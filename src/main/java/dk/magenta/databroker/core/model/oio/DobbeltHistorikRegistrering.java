package dk.magenta.databroker.core.model.oio;

import javax.persistence.*;
import java.util.*;

/**
 * Created by jubk on 11/12/14.
 */
@MappedSuperclass
public abstract class DobbeltHistorikRegistrering<
        E extends DobbeltHistorikBase<E, R, V>,
        R extends DobbeltHistorikRegistrering<E, R, V>,
        V extends DobbeltHistorikVirkning<E, R, V>
        > {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(nullable = false, insertable = true, updatable = true)
        private Long id;

        @ManyToOne
        @JoinColumn(referencedColumnName = "id", nullable = false)
        private E entitet;

        @OneToOne(optional = false)
        private RegistreringEntity registrering;

        @OneToMany(mappedBy = "entitetsRegistrering", cascade = CascadeType.ALL)
        private Collection<V> registreringsVirkninger;

        private DobbeltHistorikRegistrering() {
                this.registreringsVirkninger = new ArrayList<V>();
        }

        public DobbeltHistorikRegistrering(
                E entitet
        ) {
                this.entitet = entitet;
                this.registreringsVirkninger = new ArrayList<V>();
        }

        public Long getId() {
                return id;
        }

        public E getEntitet() {
                return entitet;
        }

        public void setEntitet(E entitet) {
                this.entitet = entitet;
        }

        public RegistreringEntity getRegistrering() {
                return registrering;
        }

        public void setRegistrering(RegistreringEntity registrering) {
                this.registrering = registrering;
        }

        public Collection<V> getRegistreringsVirkninger() {
                return registreringsVirkninger;
        }

        protected abstract V createVirkningEntity();

        public V createVirkningEntity(VirkningEntity oioVirkning) {
                V newVirk = this.createVirkningEntity();
                newVirk.setVirkning(oioVirkning);
                return newVirk;
        }

        public void addToRegistreringsVirkninger(V registreringsVirkninger) {
                this.registreringsVirkninger.add(registreringsVirkninger);
        }

        public void addToRegistreringsVirkninger(VirkningEntity virk) {
                this.addToRegistreringsVirkninger(this.createVirkningEntity(virk));
        }

}
