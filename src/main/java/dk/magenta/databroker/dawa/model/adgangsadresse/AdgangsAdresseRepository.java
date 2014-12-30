package dk.magenta.databroker.dawa.model.adgangsadresse;

import dk.magenta.databroker.dawa.model.vejstykker.VejstykkeEntity;
import dk.magenta.databroker.register.conditions.GlobalCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

/**
 * Created by lars on 29-12-14.
 */
public interface AdgangsAdresseRepository extends JpaRepository<AdgangsAdresseEntity, Long> {

    public AdgangsAdresseEntity getByUuid(String uuid);

    @Query("select adgangsadresse from " +
            "VejstykkeEntity vej " +
            "join vej.adgangsAdresseVersioner adresseversion " +
            "join adresseversion.entity adgangsadresse " +
            "where vej.kommune.kode = :kommunekode " +
            "and vej.kode = :vejkode")
    public Collection<AdgangsAdresseEntity> getByKommunekodeAndVejkode(@Param("kommunekode") int kommuneKode, @Param("vejkode") int vejKode);

    @Query("select adgangsadresse from " +
            "VejstykkeEntity vej " +
            "join vej.adgangsAdresseVersioner adresseversion " +
            "join adresseversion.entity adgangsadresse " +
            "where vej = :vejstykke " +
            "and adresseversion.husnr = :husnr")
    public Collection<AdgangsAdresseEntity> getByVejstykkeAndHusnr(@Param("vejstykke") VejstykkeEntity vejstykke, @Param("husnr") String husnr);


    @Query("select adgangsadresse from " +
            "VejstykkeEntity vej " +
            "join vej.adgangsAdresseVersioner adresseversion " +
            "join adresseversion.entity adgangsadresse " +
            "where vej.kommune.kode = :kommunekode " +
            "and vej.kode = :vejkode " +
            "and adresseversion.husnr = :husnr")
    public Collection<AdgangsAdresseEntity> getByKommunekodeAndVejkodeAndHusnr(@Param("kommunekode") int kommuneKode, @Param("vejkode") int vejKode, @Param("husnr") String husnr);

    /*
    * To be implemented in interface implementation
    * */
    public Collection<AdgangsAdresseEntity> search(String land, String[] kommune, String[] post, String[] vej, String[] husnr, GlobalCondition globalCondition);
}
