package dk.magenta.databroker.cprvejregister.model.kommunedelafnavngivenvej;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by lars on 11-11-14.
 */
public interface KommunedelAfNavngivenVejRepository extends JpaRepository<KommunedelAfNavngivenVejEntity, Long> {

    @Query("select del from KommunedelAfNavngivenVejEntity del " +
            "join del.kommune kommune " +
            "where kommune.kommunekode = :kommune " +
            "and del.vejkode = :vej")
    public KommunedelAfNavngivenVejEntity getByKommunekodeAndVejkode(@Param("kommune") int kommune, @Param("vej") int vej);

}
