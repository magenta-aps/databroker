package dk.magenta.databroker.dawa.model.adgangsadresse;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikVersion;
import dk.magenta.databroker.dawa.model.ejerlav.EjerLavEntity;
import dk.magenta.databroker.dawa.model.postnummer.PostNummerEntity;
import dk.magenta.databroker.dawa.model.vejstykker.VejstykkeEntity;

import javax.persistence.*;
import java.util.UUID;

/**
 * Created by jubk on 18-12-2014.
 */
@Entity
@Table(name = "dawa_adgangsadresse_version")
public class AdgangsAdresseVersionEntity
        extends DobbeltHistorikVersion<AdgangsAdresseEntity, AdgangsAdresseVersionEntity> {
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private AdgangsAdresseEntity entity;

    @Override
    public AdgangsAdresseEntity getEntity() {
        return entity;
    }

    @Override
    public void setEntity(AdgangsAdresseEntity entity) {
        this.entity = entity;
    }

    public AdgangsAdresseVersionEntity() {
    }

    public AdgangsAdresseVersionEntity(AdgangsAdresseEntity entitet) {
        super(entitet);
    }

    @Column
    private String supplerendeByNavn;

    @ManyToOne
    private EjerLavEntity ejerlav;

    @Column
    private String matrikelnr;

    @Column
    private int esrejendomsnr;

    @Column
    // This field seems to be dawa-specific. Can probably be ignored by other data providers.
    private int objekttype;

    @Column
    private UUID adgangspunktid;

    @Column
    private double etrs89oest;
    @Column
    private double etrs89nord;

    @Column
    private String noejagtighed;

    @Column
    // This field seems to be dawa-specific. Can probably be ignored by other data providers.
    private int dawaKilde;

    @Column
    private String placering;
    @Column
    private String tekniskstandard;

    @Column
    private double tekstretning;

    @Column
    private String kn100mdk;

    @Column
    private String kn1kmdk;

    @Column
    private String kn10kmdk;

    @ManyToOne(optional = true)
    private PostNummerEntity postnummer;

    public String getSupplerendeByNavn() {
        return supplerendeByNavn;
    }

    public void setSupplerendeByNavn(String supplerendeByNavn) {
        this.supplerendeByNavn = supplerendeByNavn;
    }

    public EjerLavEntity getEjerlav() {
        return ejerlav;
    }

    public void setEjerlav(EjerLavEntity ejerlav) {
        this.ejerlav = ejerlav;
    }

    public String getMatrikelnr() {
        return matrikelnr;
    }

    public void setMatrikelnr(String matrikelnr) {
        this.matrikelnr = matrikelnr;
    }

    public int getEsrejendomsnr() {
        return esrejendomsnr;
    }

    public void setEsrejendomsnr(int esrejendomsnr) {
        this.esrejendomsnr = esrejendomsnr;
    }

    public int getObjekttype() {
        return objekttype;
    }

    public void setObjekttype(int objekttype) {
        this.objekttype = objekttype;
    }

    public UUID getAdgangspunktid() {
        return adgangspunktid;
    }

    public void setAdgangspunktid(UUID adgangspunktid) {
        this.adgangspunktid = adgangspunktid;
    }

    public double getEtrs89oest() {
        return etrs89oest;
    }

    public void setEtrs89oest(double etrs89oest) {
        this.etrs89oest = etrs89oest;
    }

    public double getEtrs89nord() {
        return etrs89nord;
    }

    public void setEtrs89nord(double etrs89nord) {
        this.etrs89nord = etrs89nord;
    }

    public String getNoejagtighed() {
        return noejagtighed;
    }

    public void setNoejagtighed(String noejagtighed) {
        this.noejagtighed = noejagtighed;
    }

    public int getDawaKilde() {
        return dawaKilde;
    }

    public void setDawaKilde(int dawaKilde) {
        this.dawaKilde = dawaKilde;
    }

    public String getPlacering() {
        return placering;
    }

    public void setPlacering(String placering) {
        this.placering = placering;
    }

    public String getTekniskstandard() {
        return tekniskstandard;
    }

    public void setTekniskstandard(String tekniskstandard) {
        this.tekniskstandard = tekniskstandard;
    }

    public double getTekstretning() {
        return tekstretning;
    }

    public void setTekstretning(double tekstretning) {
        this.tekstretning = tekstretning;
    }

    public String getKn100mdk() {
        return kn100mdk;
    }

    public void setKn100mdk(String kn100mdk) {
        this.kn100mdk = kn100mdk;
    }

    public String getKn1kmdk() {
        return kn1kmdk;
    }

    public void setKn1kmdk(String kn1kmdk) {
        this.kn1kmdk = kn1kmdk;
    }

    public String getKn10kmdk() {
        return kn10kmdk;
    }

    public void setKn10kmdk(String kn10kmdk) {
        this.kn10kmdk = kn10kmdk;
    }

    public PostNummerEntity getPostnummer() {
        return postnummer;
    }

    public void setPostnummer(PostNummerEntity postnummer) {
        this.postnummer = postnummer;
    }
}
