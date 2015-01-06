package dk.magenta.databroker.dawa.model.vejstykker;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikVersion;
import dk.magenta.databroker.cprvejregister.dataproviders.registers.LokalitetsRegister;
import dk.magenta.databroker.dawa.model.lokalitet.LokalitetEntity;
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
        this.lokaliteter = new HashSet<LokalitetEntity>();
    }

    public VejstykkeVersionEntity(VejstykkeEntity entitet) {
        super(entitet);
        this.postnumre = new HashSet<PostNummerEntity>();
        this.lokaliteter = new HashSet<LokalitetEntity>();
    }

    /* Domain specific fields */

    @ManyToMany
    private Collection<PostNummerEntity> postnumre;

    @ManyToMany
    private Collection<LokalitetEntity> lokaliteter;

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


    public Collection<LokalitetEntity> getLokaliteter() {
        return lokaliteter;
    }

    public void addLokalitet(LokalitetEntity lokalitet) {
        if (!this.lokaliteter.contains(lokalitet)) {
            this.lokaliteter.add(lokalitet);
        }
    }
    public void removeLokalitet(LokalitetEntity lokalitet) {
        if (this.lokaliteter.contains(lokalitet)) {
            this.lokaliteter.remove(lokalitet);
        }
    }

    public boolean hasLokalitet(LokalitetEntity lokalitet) {
        return this.lokaliteter.contains(lokalitet);
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
            this.lokaliteter = new HashSet<LokalitetEntity>(otherVersion.getLokaliteter());
            this.vejnavn = otherVersion.getVejnavn();
            this.vejadresseringsnavn = otherVersion.getVejadresseringsnavn();
        }
    }
}
