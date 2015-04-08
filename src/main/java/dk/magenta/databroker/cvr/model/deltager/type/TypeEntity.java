package dk.magenta.databroker.cvr.model.deltager.type;

import dk.magenta.databroker.core.model.OutputFormattable;
import dk.magenta.databroker.core.model.oio.UniqueBase;
import dk.magenta.databroker.register.RepositoryUtil;
import dk.magenta.databroker.register.conditions.Condition;
import dk.magenta.databroker.util.cache.CacheableEntity;
import org.json.JSONObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by lars on 26-01-15.
 */
@Entity
@Table(name = "cvr_deltager_type", indexes = {@javax.persistence.Index(columnList = "name")})
public class TypeEntity extends UniqueBase implements OutputFormattable, CacheableEntity {

    public static final String databaseKey = "deltagertype";

    public TypeEntity() {
        this.generateNewUUID();
    }

    //----------------------------------------------------

    @Column(nullable = true, unique = true)
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
            if (o.getClass() == TypeEntity.class) {
                TypeEntity t = (TypeEntity) o;
                if (this.getName().equals(t.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    public String getTypeName() {
        return "deltagertype";
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


    public static Condition uuidCondition(String uuid) {
        return RepositoryUtil.whereField(uuid, null, databaseKey + ".uuid");
    }
    public static Condition nameCondition(String name) {
        return RepositoryUtil.whereField(name, null, databaseKey + ".name");
    }
}
