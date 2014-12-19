package dk.magenta.databroker.dawa.model.stormodtagere;

import dk.magenta.databroker.core.model.oio.UniqueBase;
import dk.magenta.databroker.dawa.model.adgangsadresse.AdgangsAdresseEntity;

import javax.persistence.*;

/**
 * Created by jubk on 18-12-2014.
 */
@Entity
@Table(name = "dawa_stormodtager")
public class StormodtagerEntity extends UniqueBase {
    @Column
    private int nr;

    @Column
    private String navn;

    @OneToOne(cascade = CascadeType.ALL)
    private AdgangsAdresseEntity adgangsadresse;

    public int getNr() {
        return nr;
    }

    public void setNr(int nr) {
        this.nr = nr;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public AdgangsAdresseEntity getAdgangsadresse() {
        return adgangsadresse;
    }

    public void setAdgangsadresse(AdgangsAdresseEntity adgangsAdresse) {
        this.adgangsadresse = adgangsAdresse;
    }
}