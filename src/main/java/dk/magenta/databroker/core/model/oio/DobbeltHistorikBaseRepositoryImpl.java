package dk.magenta.databroker.core.model.oio;

import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

/**
 * Created by jubk on 11/20/14.
 */
interface DobbeltHistorikBaseRepositoryCustom<E, R, V> {
    public R createEntityRegistrering(R forRegistrering, RegistreringEntity oioRegistrering);
    public R createEntityRegistrering(R forRegistrering, RegistreringEntity oioRegistrering, Collection<V> virkninger);
}

public class DobbeltHistorikBaseRepositoryImpl<E, R, V> implements DobbeltHistorikBaseRepositoryCustom<E, R, V> {

    CrudRepository<E, Long> repo;

    @Override
    public R createEntityRegistrering(R forRegistrering, RegistreringEntity oioRegistrering, Collection<V> virkninger) {
        return null;
    }

    @Override
    public R createEntityRegistrering(R forRegistrering, RegistreringEntity oioRegistrering) {
        return null;
    }
}