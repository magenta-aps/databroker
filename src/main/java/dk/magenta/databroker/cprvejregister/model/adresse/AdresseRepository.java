package dk.magenta.databroker.cprvejregister.model.adresse;

import dk.magenta.databroker.register.conditions.GlobalCondition;
import dk.magenta.databroker.cprvejregister.model.husnummer.HusnummerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

/**
 * Created by lars on 11-11-14.
 */
public interface AdresseRepository extends JpaRepository<AdresseEntity, Long> {
    @Query("select adresse from AdresseEntity adresse " +
            "join adresse.latestVersion version " +
            "where adresse.husnummer = :husnummer " +
            "and version.doerBetegnelse = :doer " +
            "and version.etageBetegnelse = :etage")
    public AdresseEntity findByHusnummerAndDoerbetegnelseAndEtagebetegnelse(@Param("husnummer") HusnummerEntity husnummer, @Param("doer") String doerbetegnelse, @Param("etage") String etagebetegnelse);

    @Query("select adresse from AdresseEntity adresse " +
            "join adresse.husnummer husnummer " +
            "join husnummer.navngivenVej vej " +
            "join vej.latestVersion vejversion " +
            "join vejversion.kommunedeleAfNavngivenVej delvej " +
            "join delvej.kommune kommune " +
            "join kommune.latestVersion kommuneversion " +
            "where kommuneversion.navn = :kommuneNavn")
    public Collection<AdresseEntity> findByKommuneNavn(@Param("kommuneNavn") String kommuneNavn);


    public Collection<AdresseEntity> search(String land, String[] kommune, String[] vej, String[] postnr, String[] husnr, String[] etage, String[] doer, GlobalCondition globalCondition);

    public AdresseEntity findByUuid(String UUID);
}