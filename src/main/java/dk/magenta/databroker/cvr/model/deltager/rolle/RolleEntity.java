package dk.magenta.databroker.cvr.model.deltager.rolle;

import dk.magenta.databroker.core.model.OutputFormattable;
import dk.magenta.databroker.core.model.oio.UniqueBase;
import dk.magenta.databroker.util.cache.Cacheable;
import org.hibernate.annotations.Index;
import org.json.JSONObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by lars on 26-01-15.
 */
@Entity
@Table(name = "cvr_deltager_rolle")
public class RolleEntity extends UniqueBase implements OutputFormattable, Cacheable {

    public RolleEntity() {
        this.generateNewUUID();
    }

    //----------------------------------------------------

    @Column(nullable = true, unique = true)
    @Index(name = "nameIndex")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //----------------------------------------------------

    public boolean equals(Object o) {
        if (o != null) {
            if (o.getClass() == RolleEntity.class) {
                RolleEntity r = (RolleEntity) o;
                if (this.getName().equals(r.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    public String getTypeName() {
        return "industry";
    }

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("name", this.getName());
        return obj;
    }

    @Override
    public String[] getIdentifiers() {
        return new String[] { this.name };
    }
}
