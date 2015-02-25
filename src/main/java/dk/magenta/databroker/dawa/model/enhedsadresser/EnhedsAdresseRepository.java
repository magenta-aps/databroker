package dk.magenta.databroker.dawa.model.enhedsadresser;

import dk.magenta.databroker.dawa.model.SearchParameters;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

/**
 * Created by lars on 29-12-14.
 */
public interface EnhedsAdresseRepository extends JpaRepository<EnhedsAdresseEntity, Long> {

    public EnhedsAdresseEntity getByUuid(String uuid);

    /*
    * To be implemented in interface implementation
    * */
    public Collection<EnhedsAdresseEntity> search(SearchParameters parameters);
    public void clear();
}
