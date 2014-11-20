package dk.magenta.databroker.cprvejregister.model.kommune;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by lars on 11-11-14.
 */
public interface KommuneRepository extends JpaRepository<KommuneEntity, Long> {

    public KommuneEntity findByKommunekode(int kommunekode);
}