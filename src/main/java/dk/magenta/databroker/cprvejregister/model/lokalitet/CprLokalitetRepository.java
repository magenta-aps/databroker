package dk.magenta.databroker.cprvejregister.model.lokalitet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

/**
 * Created by lars on 11-11-14.
 */
public interface CprLokalitetRepository extends JpaRepository<CprLokalitetEntity, Long> {

    @Query("select lokalitet from KommunedelAfNavngivenVejEntity del " +
            "join del.lokalitet lokalitet " +
            "join del.kommune kommune " +
            "join del.navngivenVejVersion version " +
            "join version.entity vej " +
            "where kommune.kommunekode = :kommune " +
            "and del.vejkode = :vej " +
            "and vej.latestVersion = version")
    public Collection<CprLokalitetEntity> findByKommunekodeAndVejkode(@Param("kommune") int kommune, @Param("vej") int vej);

    public CprLokalitetEntity findByLokalitetsKode(int lokalitetsKode);
}
