package dk.magenta.databroker.dawa.model.vejstykker;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikVersion;
import dk.magenta.databroker.dawa.model.postnummer.PostNummerEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * Created by jubk on 18-12-2014.
 */
@Entity
@Table(name = "dawa_vejstykke_version")
public class VejstykkeVersionEntity extends DobbeltHistorikVersion<VejstykkeEntity, VejstykkeVersionEntity> {
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
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
        this.postnumre = new HashSet<PostNummerEntity>();
    }

    public VejstykkeVersionEntity(VejstykkeEntity entitet) {
        super(entitet);
        this.postnumre = new HashSet<PostNummerEntity>();
    }

    /* Domain specific fields */

    @ManyToMany
    private Collection<PostNummerEntity> postnumre;

    @Column
    private String vejnavn;

    @Column
    private String vejadresseringsnavn;

    public Collection<PostNummerEntity> getPostnumre() {
        return postnumre;
    }

    public void addPostnummer(PostNummerEntity postnummer) {
        if (!this.postnumre.contains(postnummer)) {
            this.postnumre.add(postnummer);
        }
    }
    public void removePostnummer(PostNummerEntity postnummer) {
        if (this.postnumre.contains(postnummer)) {
            this.postnumre.remove(postnummer);
        }
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

    public void copyFrom(VejstykkeVersionEntity otherVersion) {
        if (this.getEntity() == otherVersion.getEntity()) {
            this.postnumre = new HashSet<PostNummerEntity>(otherVersion.getPostnumre());
            this.vejnavn = otherVersion.getVejnavn();
            this.vejadresseringsnavn = otherVersion.getVejadresseringsnavn();
        }
    }
}
