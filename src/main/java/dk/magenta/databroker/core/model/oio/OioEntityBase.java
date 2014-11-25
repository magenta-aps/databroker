package dk.magenta.databroker.core.model.oio;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by jubk on 11/24/14.
 */
@MappedSuperclass
public abstract class OioEntityBase<
        E extends OioEntityBase<E, R>,
        R extends OioRegistreringBase<E, R>
        > {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, insertable = true, updatable = true)
    protected Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public abstract Collection<R> getRegistreringer();
}
