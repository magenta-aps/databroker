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

    @Query("select vej from VejstykkeEntity vej " +
            "join vej.kommune kommune " +
            "where kommune.kode = :kommunekode " +
            "and vej.kode = :vejkode")
    public VejstykkeEntity getByKommunekodeAndVejkode(@Param("kommunekode") int kommunekode, @Param("vejkode") int vejkode);

    public VejstykkeEntity getByUuid(String uuid);


    /*
    * To be implemented in interface implementation
    * */
    public Collection<VejstykkeEntity> search(SearchParameters parameters, boolean printQuery);

}
