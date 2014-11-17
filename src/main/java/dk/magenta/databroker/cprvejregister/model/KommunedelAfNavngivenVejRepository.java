package dk.magenta.databroker.cprvejregister.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by lars on 11-11-14.
 */
public interface KommunedelAfNavngivenVejRepository extends JpaRepository<KommunedelAfNavngivenVejEntity, Long> {

    @Query("select del from KommunedelAfNavngivenVejEntity del where del.kommune.kommunekode = :kommune and del.vejkode = :vej")
    public KommunedelAfNavngivenVejEntity findByKommunekodeAndVejkode(@Param("kommune") int kommune, @Param("vej") int vej);

}
