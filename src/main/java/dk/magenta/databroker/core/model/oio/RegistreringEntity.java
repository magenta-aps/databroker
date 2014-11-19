package dk.magenta.databroker.core.model.oio;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by jubk on 11/12/14.
 */
@Entity
@Table(name = "core_oio_registration")
public class RegistreringEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, insertable = true, updatable = true)
    private Long id;

    @Column(nullable = false, insertable = true, updatable = true)
    private Timestamp registreringFra;

    @Column(nullable = false, insertable = true, updatable = true)
    private String aktoerUUID;

    @Column(nullable = true, insertable = true, updatable = true)
    private String note;


    @ManyToOne
    private RegistreringLivscyklusEntity livscyklus;

    public RegistreringEntity(
            Timestamp registreringFra, String aktoerUUID, String note, RegistreringLivscyklusEntity livscyklus
    ) {
        this.registreringFra = registreringFra;
        this.aktoerUUID = aktoerUUID;
        this.note = note;
        this.livscyklus = livscyklus;
    }
}
