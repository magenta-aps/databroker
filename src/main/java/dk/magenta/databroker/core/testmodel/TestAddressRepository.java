package dk.magenta.databroker.core.testmodel;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface TestAddressRepository extends JpaRepository<TestAddressEntity, Long> {
}