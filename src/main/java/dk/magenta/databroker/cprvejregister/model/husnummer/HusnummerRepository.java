package dk.magenta.databroker.cprvejregister.model.husnummer;

import dk.magenta.databroker.cprvejregister.model.GlobalCondition;
import dk.magenta.databroker.cprvejregister.model.navngivenvej.NavngivenVejEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

/**
 * Created by lars on 11-11-14.
 */
public interface HusnummerRepository extends JpaRepository<HusnummerEntity, Long> {
    @Query("select husnummer from HusnummerEntity husnummer " +
            "inner join husnummer.navngivenVej vej " +
            "inner join vej.latestVersion.kommunedeleAfNavngivenVej del " +
            "inner join del.kommune kommune " +
            "where del.vejkode = :vej " +
            "and kommune.kommunekode = :kommunekode " +
            "and husnummer.husnummerbetegnelse = :husnr"
    )
    public HusnummerEntity getByKommunekodeAndVejkodeAndHusnr(@Param("kommunekode") int kommunekode, @Param("vej") int vejkode, @Param("husnr") String husNr);

    public Collection<HusnummerEntity> search(String kommune, String vej, String post, String husnr);
}