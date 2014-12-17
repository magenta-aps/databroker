package dk.magenta.databroker.cprvejregister.model.postnummer;

import dk.magenta.databroker.cprvejregister.model.GlobalCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

/**
 * Created by lars on 11-11-14.
 */
public interface PostnummerRepository extends JpaRepository<PostnummerEntity, Long> {
    public PostnummerEntity findByNummer(int nummer);
    public Collection<PostnummerEntity> search(String[] post, GlobalCondition globalCondition);
}