package dk.magenta.databroker.cprvejregister.model.postnummer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

/**
 * Created by lars on 11-11-14.
 */
public interface PostnummerRepository extends JpaRepository<PostnummerEntity, Long> {
    public PostnummerEntity findByNummer(int nummer);

    @Query("select postnr from PostnummerEntity postnr " +
            "where cast(postnr.nummer as string) like :nummerpart")
    public Collection<PostnummerEntity> getByNummer(@Param("nummerpart") String nummerPart);

    @Query("select postnr from PostnummerEntity postnr " +
            "join postnr.latestVersion version " +
            "where version.navn like :navn")
    public Collection<PostnummerEntity> getByNavn(@Param("navn") String navn);
}