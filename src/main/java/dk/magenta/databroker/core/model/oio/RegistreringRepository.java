package dk.magenta.databroker.core.model.oio;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistreringRepository
        extends JpaRepository<RegistreringEntity, Long>, RegistreringRepositoryCustom
{
}
