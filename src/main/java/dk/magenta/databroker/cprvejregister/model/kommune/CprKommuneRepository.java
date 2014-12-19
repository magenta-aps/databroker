package dk.magenta.databroker.cprvejregister.model.kommune;

import dk.magenta.databroker.register.conditions.GlobalCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

/**
 * Created by lars on 11-11-14.
 */
public interface CprKommuneRepository extends JpaRepository<CprKommuneEntity, Long> {
    @Query("select kom from CprKommuneEntity kom " +
            "where kom.kommunekode = :komkode")
    public CprKommuneEntity getByKommunekode(@Param("komkode") int kommunekode);

    @Query("select kom from CprKommuneEntity kom " +
            "join kom.latestVersion version " +
            "where version.navn like :navn "+
            "order by kom.kommunekode")
    public Collection<CprKommuneEntity> findByName(@Param("navn") String navn);

    public Collection<CprKommuneEntity> search(String kommune, GlobalCondition globalCondition);
    public Collection<CprKommuneEntity> search(String land, String[] kommune, GlobalCondition globalCondition);

    public CprKommuneEntity findByUuid(String uuid);
}