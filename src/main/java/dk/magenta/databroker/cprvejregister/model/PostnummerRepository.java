package dk.magenta.databroker.cprvejregister.model;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by lars on 11-11-14.
 */
public interface PostnummerRepository extends JpaRepository<PostnummerEntity, Long> {
    public PostnummerEntity findByNummer(int nummer);
}