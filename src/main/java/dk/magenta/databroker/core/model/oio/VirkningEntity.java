package dk.magenta.databroker.core.model.oio;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by jubk on 11/12/14.
 */
@Entity
@Table(name = "core_oio_virkning")
public class VirkningEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, insertable = true, updatable = true)
    private Long id;

    @Column(nullable = false, insertable = true, updatable = true)
    private Timestamp fra;

    @Column(nullable = false, insertable = true, updatable = true)
    private Timestamp til;

    @Column(nullable = false, insertable = true, updatable = true)
    private String aktoerUUID;

    @Column(nullable = true, insertable = true, updatable = true)
    private String note;

    public VirkningEntity(Timestamp fra, Timestamp til, String aktoerUUID, String note) {
        this.fra = fra;
        this.til = til;
        this.aktoerUUID = aktoerUUID;
        this.note = note;
    }
}
