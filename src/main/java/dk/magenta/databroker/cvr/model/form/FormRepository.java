package dk.magenta.databroker.cvr.model.form;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by lars on 19-12-14.
 */
public interface FormRepository extends JpaRepository<FormEntity, Long> {
    public FormEntity getByCode(int code);
    public FormEntity getByName(String name);
}
