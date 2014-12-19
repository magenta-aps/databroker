package dk.magenta.databroker.dawa.model.vejstykker;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikVersion;
import dk.magenta.databroker.dawa.model.postnummer.PostNummerEntity;
import dk.magenta.databroker.dawa.model.temaer.KommuneEntity;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by jubk on 18-12-2014.
 */
@Entity
@Table(name = "dawa_vejstykke_version")
public class VejstykkeVersionEntity extends DobbeltHistorikVersion<VejstykkeEntity, VejstykkeVersionEntity> {
    @ManyToOne(optional = false)
    private VejstykkeEntity entity;

    @Override
    public VejstykkeEntity getEntity() {
        return entity;
    }

    @Override
    public void setEntity(VejstykkeEntity entity) {
        this.entity = entity;
    }

    public VejstykkeVersionEntity() {
    }

    public VejstykkeVersionEntity(VejstykkeEntity entitet) {
        super(entitet);
    }

    /* Domain specific fields */
    @Column
    private int kode;

    @ManyToOne
    private KommuneEntity kommune;

    @ManyToOne
    private PostNummerEntity postnummer;

    @Column
    private String vejnavn;

    @Column
    private String vejadresseringsnavn;

    public int getKode() {
        return kode;
    }

    public void setKode(int kode) {
        this.kode = kode;
    }

    public KommuneEntity getKommune() {
        return kommune;
    }

    public void setKommune(KommuneEntity kommune) {
        this.kommune = kommune;
    }

    public PostNummerEntity getPostnummer() {
        return postnummer;
    }

    public void setPostnummer(PostNummerEntity postnummer) {
        this.postnummer = postnummer;
    }

    public String getVejnavn() {
        return vejnavn;
    }

    public void setVejnavn(String vejnavn) {
        this.vejnavn = vejnavn;
    }

    public String getVejadresseringsnavn() {
        return vejadresseringsnavn;
    }

    public void setVejadresseringsnavn(String vejadresseringsnavn) {
        this.vejadresseringsnavn = vejadresseringsnavn;
    }
}
