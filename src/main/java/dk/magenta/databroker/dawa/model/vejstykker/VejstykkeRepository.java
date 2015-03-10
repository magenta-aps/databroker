package dk.magenta.databroker.dawa.model.vejstykker;

import dk.magenta.databroker.dawa.model.SearchParameters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

/**
 * Created by lars on 19-12-14.
 */
public interface VejstykkeRepository extends JpaRepository<VejstykkeEntity, Long> {

    public VejstykkeEntity getByUuid(String uuid);

    /*
    * To be implemented in interface implementation
    * */
    public Collection<VejstykkeEntity> search(SearchParameters parameters);
    public VejstykkeEntity getByDesc(int descriptor);
    public void clear();

}
