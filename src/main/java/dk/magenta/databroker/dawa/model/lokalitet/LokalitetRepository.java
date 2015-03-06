package dk.magenta.databroker.dawa.model.lokalitet;

import dk.magenta.databroker.dawa.model.SearchParameters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

/**
 * Created by lars on 19-12-14.
 */
public interface LokalitetRepository extends JpaRepository<LokalitetEntity, Long> {

    public LokalitetEntity getByUuid(String uuid);


    /*
    * To be implemented in interface implementation
    * */
    public LokalitetEntity getByKommunekodeAndLokalitetsnavn(int kommuneKode, String lokalitetsnavn);
    public Collection<LokalitetEntity> search(SearchParameters parameters);
    public void clear();
}
