package dk.magenta.databroker.cprvejregister.model.navngivenvej;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

/**
 * Created by lars on 11-11-14.
 */
public interface NavngivenVejRepository extends JpaRepository<NavngivenVejEntity, Long> {
    @Query("select vej from " +
            "KommunedelAfNavngivenVejEntity as del " +
            "inner join del.kommune as kom " +
            "inner join del.navngivenVejVersion as vejversion " +
            "inner join vejversion.entity vej "+
            "where del.vejkode = :vej and kom.kommunekode = :kommune")
    public NavngivenVejEntity findByKommunekodeAndVejkode(@Param("kommune") int kommunekode, @Param("vej") int vejkode);


    @Query("select vej from " +
            "KommunedelAfNavngivenVejEntity as del " +
            "inner join del.kommune as kom " +
            "inner join del.navngivenVejVersion as vejversion " +
            "inner join vejversion.entity vej "+
            "where del.vejkode = :vejkode and kom.kommunekode = :kommune")
    public Collection<NavngivenVejEntity> getByKommunekodeAndVejkode(@Param("kommune") int kommunekode, @Param("vejkode") int vejkode);

    @Query("select vej from " +
            "KommunedelAfNavngivenVejEntity as del " +
            "inner join del.kommune as kom " +
            "inner join del.navngivenVejVersion as vejversion " +
            "inner join vejversion.entity vej "+
            "where vejversion.vejnavn like :vejnavn and kom.kommunekode = :kommune")
    public Collection<NavngivenVejEntity> getByKommunekodeAndVejnavn(@Param("kommune") int kommunekode, @Param("vejnavn") String vejnavn);

    @Query("select vej from " +
            "KommunedelAfNavngivenVejEntity as del " +
            "inner join del.kommune as kom " +
            "inner join del.navngivenVejVersion as vejversion " +
            "inner join vejversion.entity vej "+
            "where kom.kommunekode = :kommune")
    public Collection<NavngivenVejEntity> getByKommunekode(@Param("kommune") int kommunekode);
}