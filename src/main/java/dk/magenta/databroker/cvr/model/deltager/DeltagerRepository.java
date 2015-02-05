package dk.magenta.databroker.cvr.model.deltager;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by lars on 19-12-14.
 */
public interface DeltagerRepository extends JpaRepository<DeltagerEntity, Long> {

    public DeltagerEntity getByUuid(String uuid);
    public DeltagerEntity getByDeltagerNummer(long deltagernummer);


    /*
    * To be implemented in interface implementation
    * */

}
