package dk.magenta.databroker.cprvejregister.model.kommune;

import dk.magenta.databroker.register.conditions.GlobalCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

/**
 * Created by lars on 11-11-14.
 */
public interface KommuneRepository extends JpaRepository<KommuneEntity, Long> {
    @Query("select kom from KommuneEntity kom " +
            "where kom.kommunekode = :komkode")
    public KommuneEntity getByKommunekode(@Param("komkode") int kommunekode);

    @Query("select kom from KommuneEntity kom " +
            "join kom.latestVersion version " +
            "where version.navn like :navn "+
            "order by kom.kommunekode")
    public Collection<KommuneEntity> findByName(@Param("navn") String navn);

    public Collection<KommuneEntity> search(String land, String[] kommune, GlobalCondition globalCondition);

    public KommuneEntity findByUuid(String uuid);
}