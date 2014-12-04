package dk.magenta.databroker.cprvejregister.model.kommunedelafnavngivenvej;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

/**
 * Created by lars on 11-11-14.
 */
public interface KommunedelAfNavngivenVejRepository extends JpaRepository<KommunedelAfNavngivenVejEntity, Long> {

    @Query("select del from KommunedelAfNavngivenVejEntity del " +
            "join del.kommune kommune " +
            "join del.navngivenVejVersion version " +
            "join version.entity vej " +
            "where kommune.kommunekode = :kommune " +
            "and del.vejkode = :vej " +
            "and vej.latestVersion = version")
    public KommunedelAfNavngivenVejEntity getByKommunekodeAndVejkode(@Param("kommune") int kommune, @Param("vej") int vej);

    @Query("select del from KommunedelAfNavngivenVejEntity del " +
            "join del.navngivenVejVersion version " +
            "join version.entity vej " +
            "where vej.latestVersion = version")
    public List<KommunedelAfNavngivenVejEntity> getAllLatest();
}
