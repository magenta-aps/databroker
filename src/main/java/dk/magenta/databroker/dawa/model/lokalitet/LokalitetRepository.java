package dk.magenta.databroker.dawa.model.lokalitet;

import dk.magenta.databroker.register.conditions.GlobalCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

/**
 * Created by lars on 19-12-14.
 */
public interface LokalitetRepository extends JpaRepository<LokalitetEntity, Long> {

    public LokalitetEntity getByUuid(String uuid);


    @Query("select lokalitet from LokalitetEntity lokalitet " +
            "join lokalitet.kommune kommune " +
            "where kommune.kode = :kommunekode " +
            "and lokalitet.navn = :navn")
    public LokalitetEntity getByKommunekodeAndLokalitetsnavn(@Param("kommunekode") int kommuneKode, @Param("navn") String lokalitetsnavn);

    /*
    * To be implemented in interface implementation
    * */
    public Collection<LokalitetEntity> search(String land, String[] post, String[] kommune, String[] vej, String[] lokalitet, GlobalCondition globalCondition);
}
