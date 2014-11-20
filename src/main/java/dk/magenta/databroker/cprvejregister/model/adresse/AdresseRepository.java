package dk.magenta.databroker.cprvejregister.model.adresse;

import dk.magenta.databroker.cprvejregister.model.husnummer.HusnummerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by lars on 11-11-14.
 */
public interface AdresseRepository extends JpaRepository<AdresseEntity, Long> {
    public AdresseEntity findByHusnummerAndDoerbetegnelseAndEtagebetegnelse(HusnummerEntity husnummer, String doerbetegnelse, String etagebetegnelse);
}