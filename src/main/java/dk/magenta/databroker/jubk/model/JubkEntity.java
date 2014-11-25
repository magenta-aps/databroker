package dk.magenta.databroker.jubk.model;

import dk.magenta.databroker.core.model.oio.OioEntityBase;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by jubk on 11/12/14.
 */
@Entity
@Table(name = "jubk_jubk")
public class JubkEntity extends OioEntityBase<JubkEntity, JubkRegistreringEntity> {
        @OneToMany(mappedBy = "entitet")
        private Collection<JubkRegistreringEntity> registreringer;

        @Override
        public Collection<JubkRegistreringEntity> getRegistreringer() {
                return registreringer;
        }

        private void setRegistreringer(Collection<JubkRegistreringEntity> registreringer) {
                this.registreringer = registreringer;
        }
/*
        @Override
        public Collection<JubkRegistreringEntity> getRegistreringer() {
                return this.registreringer;
        }
        */
}
