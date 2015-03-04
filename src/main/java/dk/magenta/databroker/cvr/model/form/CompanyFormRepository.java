package dk.magenta.databroker.cvr.model.form;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by lars on 19-12-14.
 */
public interface CompanyFormRepository extends JpaRepository<CompanyFormEntity, Long> {
    public CompanyFormEntity getByCode(int code);
    public CompanyFormEntity getByName(String name);
}
