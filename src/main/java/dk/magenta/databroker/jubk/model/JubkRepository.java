package dk.magenta.databroker.jubk.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface JubkRepository extends JpaRepository<JubkEntity, Long> {
}