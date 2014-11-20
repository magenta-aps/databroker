package dk.magenta.databroker.core.model.oio;

import javax.persistence.*;
import java.util.*;

/**
 * Created by jubk on 11/12/14.
 */
@MappedSuperclass
public class DobbeltHistorikRegistrering<
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

        public DobbeltHistorikRegistrering() {
        }

        public DobbeltHistorikRegistrering(
                E entitet, RegistreringEntity registrering, Collection<VirkningEntity> virkninger
        ) {
                this.entitet = entitet;
                this.registrering = registrering;
                this.registreringsVirkninger = new ArrayList<V>();

                for(VirkningEntity v: virkninger) {
                        V regVirkning = (V)new DobbeltHistorikVirkning<E, R, V>(
                                (R)this, v
                        );
                        this.registreringsVirkninger.add(regVirkning);
                }
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

        public void setRegistreringsVirkninger(Collection<V> registreringsVirkninger) {
                this.registreringsVirkninger = registreringsVirkninger;
        }

}
