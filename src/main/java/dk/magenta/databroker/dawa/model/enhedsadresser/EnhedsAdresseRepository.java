package dk.magenta.databroker.dawa.model.enhedsadresser;

import dk.magenta.databroker.dawa.model.SearchParameters;
import dk.magenta.databroker.dawa.model.adgangsadresse.AdgangsAdresseEntity;
import dk.magenta.databroker.dawa.model.vejstykker.VejstykkeEntity;
import dk.magenta.databroker.register.conditions.GlobalCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

/**
 * Created by lars on 29-12-14.
 */
public interface EnhedsAdresseRepository extends JpaRepository<EnhedsAdresseEntity, Long> {

    public EnhedsAdresseEntity getByUuid(String uuid);

    //public EnhedsAdresseVersionEntity getByValues(int kommuneKode, int vejKode, String husnr, String etage, String doer);

    /*@Query("select distinct version from EnhedsAdresseEntity entity " +
            "join entity.latestVersion as version " +
            "where version.descriptor = :descriptor")*/
    public List<EnhedsAdresseEntity> getByLatestVersion_Descriptor(@Param("descriptor") String descriptor);


    public EnhedsAdresseEntity getFirstByLatestVersion_Descriptor(@Param("descriptor") String descriptor);

    //public EnhedsAdresseVersionEntity getFirstByEnhedsAdresseVersionEntity_Descriptor(String descriptor);

    /*
    * To be implemented in interface implementation
    * */
    public Collection<EnhedsAdresseEntity> search(SearchParameters parameters, boolean printQuery);
}
