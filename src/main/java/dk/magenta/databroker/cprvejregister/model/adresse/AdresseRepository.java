package dk.magenta.databroker.cprvejregister.model.adresse;

import dk.magenta.databroker.cprvejregister.model.husnummer.HusnummerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
}