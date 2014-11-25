package dk.magenta.databroker.core.model.oio;

import javax.persistence.*;
import java.util.UUID;

/**
 * Created by jubk on 11/25/14.
 */
@MappedSuperclass
public abstract class UniqueBase {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, insertable = true, updatable = true)
    private Long id;

    @Column(nullable = false, insertable = true, updatable = true, unique = true)
    private String uuid;

    protected UniqueBase() {
    }

    public UniqueBase(String uuid) {
        this.uuid = uuid;
    }

    public Long getId() {
        return id;
    }

    protected void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void generateNewUUID() {
        this.uuid = UUID.randomUUID().toString();
    }

}
