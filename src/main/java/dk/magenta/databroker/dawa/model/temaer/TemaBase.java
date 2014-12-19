package dk.magenta.databroker.dawa.model.temaer;

import dk.magenta.databroker.core.model.oio.UniqueBase;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.IdClass;
import javax.persistence.MappedSuperclass;

/**
 * Created by jubk on 18-12-2014.
 */

@MappedSuperclass
public abstract class TemaBase {

    @Id
    @Column(nullable = false)
    private Long id;

    @Column
    private String navn;

    public abstract TemaType getTemaType();

    // Static method for easy access to the different implementation classes
    public static final Class getClassForTemaType(TemaType type) {
        switch (type) {
            case KOMMUNE:
                break;
            case REGION:
                break;
            case SOGN:
            case OPSTILLINGSKREDS:
            case POLITIKREDS:
            case RETSKREDS:
            case AFSTEMNINGSOMRAADE:
            case POSTNUMMER:
            case DANMARK:
            case MENIGHEDSRAADSAFSTEMNINGSOMRAADE:
            case SAMLEPOSTNUMMER:
            case STORKREDS:
            case SUPPLERENDEBYNAVN:
            case VALGLANDSDEL:
            case ZONE:
            default:
                throw new NotImplementedException();
        }
        return null;
    }

}
