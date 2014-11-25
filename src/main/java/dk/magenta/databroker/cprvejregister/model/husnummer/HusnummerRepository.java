package dk.magenta.databroker.cprvejregister.model.husnummer;

import dk.magenta.databroker.cprvejregister.model.navngivenvej.NavngivenVejEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by lars on 11-11-14.
 */
public interface HusnummerRepository extends JpaRepository<HusnummerEntity, Long> {
    @Query("select hus from HusnummerEntity hus " +
            "inner join hus.registreringer " +
            "join reg. " +
            "inner join del.kommune kom " +
            "where del.vejkode = :vej and kom.kommunekode = :kommune " +
            "and reg = :husnr"
    )
    public List<HusnummerEntity> findByKommunekodeAndVejkodeAndHusnr(@Param("kommune") int kommunekode, @Param("vej") int vejkode, @Param("husnr") String husNr);


    @Query("select hus from HusnummerEntity hus " +
            "inner join hus.navngivenVej vej " +
            "inner join vej.kommunedeleAfNavngivenVej del " +
            "inner join del.kommune kom " +
            "where del.vejkode = :vej and kom.kommunekode = :kommune " +
            "and hus.husnummerbetegnelse = :husnr"
    )
    public HusnummerEntity findFirstByKommunekodeAndVejkodeAndHusnr(@Param("kommune") int kommunekode, @Param("vej") int vejkode, @Param("husnr") String husNr);


    @Query("select hus from HusnummerEntity hus " +
            "where hus.navngivenVej = :vej and hus.husnummerbetegnelse = :husnr")
    public HusnummerEntity findByNavngivenvejAndHusnr(@Param("vej") NavngivenVejEntity vej, @Param("husnr") String husNr);
}