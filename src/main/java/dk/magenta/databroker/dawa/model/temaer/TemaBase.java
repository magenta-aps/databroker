package dk.magenta.databroker.dawa.model.temaer;

import dk.magenta.databroker.core.model.oio.UniqueBase;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.persistence.*;

/**
 * Created by jubk on 18-12-2014.
 */

@MappedSuperclass
public abstract class TemaBase {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, insertable = true, updatable = true)
    private Long id;

    @Column
    private String navn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

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
