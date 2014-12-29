package dk.magenta.databroker.dawa.model.postnummer;

import dk.magenta.databroker.register.conditions.GlobalCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

/**
 * Created by lars on 19-12-14.
 */
public interface PostNummerRepository extends JpaRepository<PostNummerEntity, Long> {

    public PostNummerEntity getByUuid(String uuid);

    @Query("select post from PostNummerEntity post " +
            "join post.versioner version " +
            "where version.nr = :nummer")
    public PostNummerEntity getByNr(@Param("nummer") int nummer);

    /*
    * To be implemented in interface implementation
    * */
    public Collection<PostNummerEntity> search(String land, String[] post, String[] kommune, GlobalCondition globalCondition);
}
