package dk.magenta.databroker.core.model.oio;

import org.hibernate.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface RegistreringRepository
        extends JpaRepository<RegistreringEntity, Long>, RegistreringRepositoryCustom
{
}
