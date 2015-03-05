package dk.magenta.databroker.dawa.model.adgangsadresse;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikVersion;
import dk.magenta.databroker.dawa.model.ejerlav.EjerLavEntity;
import dk.magenta.databroker.dawa.model.postnummer.PostNummerEntity;
import dk.magenta.databroker.util.Util;

import javax.persistence.*;
import java.util.UUID;

/**
 * Created by jubk on 18-12-2014.
 */
@Entity
@Table(name = "dawa_adgangsadresse_version")
public class AdgangsAdresseVersionEntity
        extends DobbeltHistorikVersion<AdgangsAdresseEntity, AdgangsAdresseVersionEntity> {

    public AdgangsAdresseVersionEntity() {
    }

    public AdgangsAdresseVersionEntity(AdgangsAdresseEntity entitet) {
        super(entitet);
    }

    //------------------------------------------------------------------------------------------------------------------

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

    //------------------------------------------------------------------------------------------------------------------

    @Column
    private String supplerendeByNavn;

    public String getSupplerendeByNavn() {
        return supplerendeByNavn;
    }

    public void setSupplerendeByNavn(String supplerendeByNavn) {
        this.supplerendeByNavn = supplerendeByNavn;
    }

    //----------------------------------------------------

    @ManyToOne
    private EjerLavEntity ejerlav;

    public EjerLavEntity getEjerlav() {
        return ejerlav;
    }

    public void setEjerlav(EjerLavEntity ejerlav) {
        this.ejerlav = ejerlav;
    }

    //----------------------------------------------------

    @Column
    private String matrikelnr;

    public String getMatrikelnr() {
        return matrikelnr;
    }

    public void setMatrikelnr(String matrikelnr) {
        this.matrikelnr = matrikelnr;
    }

    //----------------------------------------------------

    @Column
    private int esrejendomsnr;

    public int getEsrejendomsnr() {
        return esrejendomsnr;
    }

    public void setEsrejendomsnr(int esrejendomsnr) {
        this.esrejendomsnr = esrejendomsnr;
    }

    //----------------------------------------------------

    @Column
    // This field seems to be dawa-specific. Can probably be ignored by other data providers.
    private int objekttype;

    public int getObjekttype() {
        return objekttype;
    }

    public void setObjekttype(int objekttype) {
        this.objekttype = objekttype;
    }

    //----------------------------------------------------

    @Column
    private UUID adgangspunktid;

    public UUID getAdgangspunktid() {
        return adgangspunktid;
    }

    public void setAdgangspunktid(UUID adgangspunktid) {
        this.adgangspunktid = adgangspunktid;
    }

    //----------------------------------------------------

    @Column
    private double etrs89oest;

    public double getEtrs89oest() {
        return etrs89oest;
    }

    public void setEtrs89oest(double etrs89oest) {
        this.etrs89oest = etrs89oest;
    }

    //----------------------------------------------------

    @Column
    private double etrs89nord;

    public double getEtrs89nord() {
        return etrs89nord;
    }

    public void setEtrs89nord(double etrs89nord) {
        this.etrs89nord = etrs89nord;
    }

    //----------------------------------------------------

    @Column
    private String noejagtighed;

    public String getNoejagtighed() {
        return noejagtighed;
    }

    public void setNoejagtighed(String noejagtighed) {
        this.noejagtighed = noejagtighed;
    }

    //----------------------------------------------------

    @Column
    // This field seems to be dawa-specific. Can probably be ignored by other data providers.
    private int dawaKilde;

    public int getDawaKilde() {
        return dawaKilde;
    }

    public void setDawaKilde(int dawaKilde) {
        this.dawaKilde = dawaKilde;
    }

    //----------------------------------------------------

    @Column
    private String placering;

    public String getPlacering() {
        return placering;
    }

    public void setPlacering(String placering) {
        this.placering = placering;
    }

    //----------------------------------------------------

    @Column
    private String tekniskstandard;

    public String getTekniskstandard() {
        return tekniskstandard;
    }

    public void setTekniskstandard(String tekniskstandard) {
        this.tekniskstandard = tekniskstandard;
    }

    //----------------------------------------------------

    @Column
    private double tekstretning;

    public double getTekstretning() {
        return tekstretning;
    }

    public void setTekstretning(double tekstretning) {
        this.tekstretning = tekstretning;
    }

    //----------------------------------------------------

    @Column
    private String kn100mdk;

    public String getKn100mdk() {
        return kn100mdk;
    }

    public void setKn100mdk(String kn100mdk) {
        this.kn100mdk = kn100mdk;
    }

    //----------------------------------------------------

    @Column
    private String kn1kmdk;

    public String getKn1kmdk() {
        return kn1kmdk;
    }

    public void setKn1kmdk(String kn1kmdk) {
        this.kn1kmdk = kn1kmdk;
    }

    //----------------------------------------------------

    @Column
    private String kn10kmdk;

    public String getKn10kmdk() {
        return kn10kmdk;
    }

    public void setKn10kmdk(String kn10kmdk) {
        this.kn10kmdk = kn10kmdk;
    }

    //----------------------------------------------------

    @Column
    private String kaldenavn;

    public String getKaldenavn() {
        return kaldenavn;
    }

    public void setKaldenavn(String kaldenavn) {
        this.kaldenavn = kaldenavn;
    }

    //----------------------------------------------------

    @ManyToOne(optional = true)
    private PostNummerEntity postnummer;

    public PostNummerEntity getPostnummer() {
        return postnummer;
    }

    public void setPostnummer(PostNummerEntity postnummer) {
        this.postnummer = postnummer;
    }



    public boolean matches(String kaldenavn) {
        return (kaldenavn == null || Util.compare(this.kaldenavn, kaldenavn));
    }
}
