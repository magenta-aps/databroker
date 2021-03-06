package dk.magenta.databroker.dawa.model.temaer;

import dk.magenta.databroker.dawa.model.SearchParameters;
import dk.magenta.databroker.dawa.model.SearchParameters.Key;
import dk.magenta.databroker.dawa.model.lokalitet.LokalitetEntity;
import dk.magenta.databroker.dawa.model.postnummer.PostNummerEntity;
import dk.magenta.databroker.dawa.model.postnummer.PostNummerVersionEntity;
import dk.magenta.databroker.dawa.model.vejstykker.VejstykkeEntity;
import dk.magenta.databroker.register.RepositoryUtil;
import dk.magenta.databroker.register.conditions.Condition;
import dk.magenta.databroker.service.rest.SearchService;
import dk.magenta.databroker.util.Util;
import dk.magenta.databroker.util.cache.Cacheable;
import dk.magenta.databroker.util.objectcontainers.Pair;
import org.hibernate.annotations.Index;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;

/**
 * Created by jubk on 18-12-2014.
 */
@Entity
@Table(name = "dawa_kommune")
public class KommuneEntity extends TemaBase implements Cacheable {

    public KommuneEntity() {
        this.postnumre = new HashSet<PostNummerVersionEntity>();
        this.vejstykker = new HashSet<VejstykkeEntity>();
    }

    @Override
    public TemaType getTemaType() {
        return TemaType.KOMMUNE;
    }

    //------------------------------------------------------------------------------------------------------------------

    @Column(nullable = false)
    @Index(name = "kodeIndex")
    private int kode;

    public int getKode() {
        return kode;
    }

    public void setKode(int kode) {
        this.kode = kode;
    }

    //----------------------------------------------------

    // Not sure all data sources can supply this
    @ManyToOne(optional = true)
    private RegionEntity region;

    public RegionEntity getRegion() {
        return region;
    }

    public void setRegion(RegionEntity region) {
        this.region = region;
    }

    //----------------------------------------------------

    @OneToMany(mappedBy = "kommune")
    private Collection<LokalitetEntity> lokaliteter;

    public Collection<LokalitetEntity> getLokaliteter() {
        return lokaliteter;
    }

    public void setLokaliteter(Collection<LokalitetEntity> lokaliteter) {
        this.lokaliteter = lokaliteter;
    }

    //----------------------------------------------------

    @OneToMany(mappedBy = "kommune")
    private Collection<VejstykkeEntity> vejstykker;

    public Collection<VejstykkeEntity> getVejstykkeVersioner() {
        return vejstykker;
    }

    public void setVejstykker(Collection<VejstykkeEntity> vejstykker) {
        this.vejstykker = vejstykker;
    }

    //----------------------------------------------------

    @ManyToMany(mappedBy = "kommuner")
    private Collection<PostNummerVersionEntity> postnumre;

    public Collection<PostNummerVersionEntity> getPostnumre() {
        return postnumre;
    }

    public void setPostnumre(Collection<PostNummerVersionEntity> postnumre) {
        this.postnumre = postnumre;
    }

    public void addPostnummer(PostNummerVersionEntity postnummer) {
        this.postnumre.add(postnummer);
    }

    //------------------------------------------------------------------------------------------------------------------

    public String getTypeName() {
        return "kommune";
    }

    public JSONObject toJSON() {
        JSONObject obj = super.toJSON();
        obj.put("navn", this.getNavn());
        obj.put("kode", this.getKode());
        obj.put("href", SearchService.getKommuneBaseUrl() + "/" + this.getKode());
        return obj;
    }

    public JSONObject toFullJSON() {
        JSONObject obj = this.toJSON();

        JSONArray delveje = new JSONArray();
        for (VejstykkeEntity vejstykkeEntity : this.vejstykker) {
            JSONObject delvej = vejstykkeEntity.toJSON();
            delveje.put(delvej);
        }
        obj.put("veje", delveje);

        JSONArray postnumre = new JSONArray();
        for (PostNummerVersionEntity postNummerVersionEntity : this.postnumre) {
            if (postNummerVersionEntity.getEntity().getLatestVersion() == postNummerVersionEntity) {
                postnumre.put(postNummerVersionEntity.getEntity().toJSON());
            }
        }
        obj.put("postnumre", postnumre);

        JSONArray lokaliteter = new JSONArray();
        for (LokalitetEntity lokalitetEntity : this.lokaliteter) {
            lokaliteter.put(lokalitetEntity.toJSON());
        }
        obj.put("lokaliteter", lokaliteter);


        return obj;
    }

    //------------------------------------------------------------------------------------------------------------------

    public static final String databaseKey = "kommune";

    public static Condition kommuneCondition(SearchParameters parameters) {
        if (parameters.has(Key.KOMMUNE)) {
            return RepositoryUtil.whereField(parameters.get(Key.KOMMUNE), databaseKey+".kode", databaseKey+".navn");
        }
        return null;
    }

    public static Condition kommuneCondition(int kommuneKode) {
        return RepositoryUtil.whereField(kommuneKode, databaseKey+".kode", null);
    }

    public static Condition landCondition(SearchParameters parameters) {
        if (parameters.has(Key.LAND)) {
            return RepositoryUtil.whereFieldLand(parameters.get(Key.LAND));
        }
        return null;
    }

    public static String joinVej() {
        return databaseKey+".vejstykker as "+VejstykkeEntity.databaseKey;
    }
    public static String joinLokalitet() {
        return databaseKey+".lokaliteter as "+LokalitetEntity.databaseKey;
    }
    public static Pair<String[], Condition> joinPost() {
        String version = PostNummerEntity.databaseKey+"Version";
        return new Pair<String[], Condition>(
                new String[] {
                        databaseKey+".postnumre "+version,
                        version+".entity as "+PostNummerEntity.databaseKey
                },
                RepositoryUtil.whereVersionLatest(version)
        );
    }

    @Override
    public String[] getIdentifiers() {
        return new String[] { ""+this.getKode() };
    }

    //------------------------------------------------------------------------------------------------------------------

    public boolean matches(int kode, String navn) {
        return this.kode == kode && Util.compare(this.getNavn(), navn);
    }
}
