package dk.magenta.databroker.dawa.model.temaer;

import dk.magenta.databroker.dawa.model.SearchParameters;
import dk.magenta.databroker.register.conditions.GlobalCondition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

/**
 * Created by lars on 19-12-14.
 */
public interface KommuneRepository extends JpaRepository<KommuneEntity, Long> {

    public KommuneEntity getByKode(int kommunekode);

    public Collection<KommuneEntity> getByNavn(String navn);

    /*
    * To be implemented in interface implementation
    * */
    public Collection<KommuneEntity> search(SearchParameters parameters);
    public void clear();

}

