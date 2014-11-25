package dk.magenta.databroker.cprvejregister.model.kommune;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by lars on 11-11-14.
 */
public interface KommuneRepository extends JpaRepository<KommuneEntity, Long> {
    @Query("select kom from KommuneEntity kom where kom.kommunekode = :komkode")
    public KommuneEntity findByKommunekode(@Param("komkode") int kommunekode);
}