package dk.magenta.databroker.cprvejregister.model.husnummer;

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

    @Query("select hus from HusnummerEntity hus " +
            "where hus.navngivenVej = :vej " +
            "and hus.husnummerbetegnelse = :husnr")
    public HusnummerEntity findByNavngivenvejAndHusnr(@Param("vej") NavngivenVejEntity vej, @Param("husnr") String husNr);

    @Query("select hus from HusnummerEntity hus " +
            "where hus.navngivenVej = :vej " +
            "and hus.husnummerbetegnelse like :husnr " +
            "order by hus.husnummerbetegnelse")
    public Collection<HusnummerEntity> getByNavngivenvejAndHusnr(@Param("vej") NavngivenVejEntity vej, @Param("husnr") String husNr);


    @Query("select hus from HusnummerEntity hus " +
            "join hus.adgangspunkt punkt " +
            "join punkt.latestVersion punktversion " +
            "join punktversion.liggerIPostnummer postnr " +
            "where hus.navngivenVej = :vej " +
            "and postnr.nummer = :husnr ")
    public Collection<HusnummerEntity> getByNavngivenvejAndPostnr(@Param("vej") NavngivenVejEntity vej, @Param("postnr") int postnr);

    @Query("select hus from HusnummerEntity hus " +
            "join hus.adgangspunkt punkt " +
            "join punkt.latestVersion punktversion " +
            "join punktversion.liggerIPostnummer postnr " +
            "where hus.navngivenVej = :vej " +
            "and postnr.nummer = :husnr " +
            "and hus.husnummerbetegnelse")
    public Collection<HusnummerEntity> getByNavngivenvejAndPostnrAndHusnr(@Param("vej") NavngivenVejEntity vej, @Param("postnr") int postnr, @Param("husnr") String husnr);
}