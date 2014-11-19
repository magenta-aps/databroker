package dk.magenta.databroker.core.model.oio;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RegistreringLivscyklusRepository
        extends CrudRepository<RegistreringLivscyklusEntity, Long>,
        RegistreringLivscyklusRepositoryCustom
{
    public RegistreringLivscyklusEntity getByNavn(String navn);
}
