package dk.magenta.databroker.core.model.oio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

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

    public RegistreringEntity() {
    }

    public RegistreringEntity(Timestamp registreringFra, String aktoerUUID, String note) {
        this.registreringFra = registreringFra;
        this.aktoerUUID = aktoerUUID;
        this.note = note;
    }

    public RegistreringEntity(
            Timestamp registreringFra, String aktoerUUID, String note, RegistreringLivscyklusEntity livscyklus
    ) {
        this.registreringFra = registreringFra;
        this.aktoerUUID = aktoerUUID;
        this.note = note;
        this.livscyklus = livscyklus;
    }

    public Timestamp getRegistreringFra() {
        return registreringFra;
    }

    public void setRegistreringFra(Timestamp registreringFra) {
        this.registreringFra = registreringFra;
    }

    public String getAktoerUUID() {
        return aktoerUUID;
    }

    public void setAktoerUUID(String aktoerUUID) {
        this.aktoerUUID = aktoerUUID;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public RegistreringLivscyklusEntity getLivscyklus() {
        return livscyklus;
    }

    public void setLivscyklus(RegistreringLivscyklusEntity livscyklus) {
        this.livscyklus = livscyklus;
    }
}
