package dk.magenta.databroker.dawa.model.enhedsadresser;

import dk.magenta.databroker.dawa.model.adgangsadresse.AdgangsAdresseEntity;
import dk.magenta.databroker.dawa.model.vejstykker.VejstykkeEntity;
import dk.magenta.databroker.register.conditions.GlobalCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

/**
 * Created by lars on 29-12-14.
 */
public interface EnhedsAdresseRepository extends JpaRepository<EnhedsAdresseEntity, Long> {

    public EnhedsAdresseEntity getByUuid(String uuid);

    /*
    * To be implemented in interface implementation
    * */
    public Collection<EnhedsAdresseEntity> search(String land, String[] post, String[] kommune, String[] vej, String[] husnr, String[] etage, String[] sidedoer, GlobalCondition globalCondition);
}
