package dk.magenta.databroker.core.model.oio;

import org.springframework.data.repository.CrudRepository;

public interface RegistreringLivscyklusRepository
        extends CrudRepository<RegistreringLivscyklusEntity, Long>,
        RegistreringLivscyklusRepositoryCustom
{
    public RegistreringLivscyklusEntity getByNavn(String navn);
    public void clear();
}
