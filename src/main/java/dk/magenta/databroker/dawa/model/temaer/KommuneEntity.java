package dk.magenta.databroker.dawa.model.temaer;

import dk.magenta.databroker.cprvejregister.model.kommunedelafnavngivenvej.KommunedelAfNavngivenVejEntity;
import dk.magenta.databroker.dawa.model.postnummer.PostNummerEntity;
import dk.magenta.databroker.dawa.model.postnummer.PostNummerVersionEntity;
import dk.magenta.databroker.dawa.model.vejstykker.VejstykkeEntity;
import dk.magenta.databroker.dawa.model.vejstykker.VejstykkeVersionEntity;
import dk.magenta.databroker.register.objectcontainers.Level1Container;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by jubk on 18-12-2014.
 */
@Entity
@Table(name = "dawa_kommune")
public class KommuneEntity extends TemaBase {
    @Column(nullable = false)
    private int kode;

    // Not sure all data sources can supply this
    @ManyToOne(optional = true)
    private RegionEntity region;

    @OneToMany(mappedBy = "kommune")
    private Collection<VejstykkeEntity> vejstykker;

    @ManyToMany(mappedBy = "kommuner")
    private Collection<PostNummerVersionEntity> postnumre;

    @Override
    public TemaType getTemaType() {
        return TemaType.KOMMUNE;
    }

    public int getKode() {
        return kode;
    }

    public void setKode(int kode) {
        this.kode = kode;
    }

    public RegionEntity getRegion() {
        return region;
    }

    public void setRegion(RegionEntity region) {
        this.region = region;
    }

    public Collection<VejstykkeEntity> getVejstykkeVersioner() {
        return vejstykker;
    }

    public void setVejstykker(Collection<VejstykkeEntity> vejstykker) {
        this.vejstykker = vejstykker;
    }

    public Collection<PostNummerVersionEntity> getPostnumre() {
        return postnumre;
    }

    public void setPostnumre(Collection<PostNummerVersionEntity> postnumre) {
        this.postnumre = postnumre;
    }

    public void addPostnummer(PostNummerVersionEntity postnummer) {
        System.out.println("Adding postnummer to kommune");
        Set<PostNummerVersionEntity> postNummerVersionEntities = new HashSet<PostNummerVersionEntity>(this.postnumre);
        postNummerVersionEntities.add(postnummer);
        this.postnumre = postNummerVersionEntities;
    }

    public String getTypeName() {
        return "kommune";
    }

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("navn", this.getNavn());
        obj.put("kode", this.getKode());
        return obj;
    }

    public JSONObject toFullJSON() {
        JSONObject obj = this.toJSON();
        JSONArray delveje = new JSONArray();
        for (VejstykkeEntity vejstykkeEntity : this.vejstykker) {
            JSONObject delvej = vejstykkeEntity.toJSON();
            delveje.put(delvej);
        }
        obj.put("delveje", delveje);
        JSONArray postnumre = new JSONArray();
        for (PostNummerVersionEntity postNummerVersionEntity : this.postnumre) {
            if (postNummerVersionEntity.getEntity().getLatestVersion() == postNummerVersionEntity) {
                postnumre.put(postNummerVersionEntity.getEntity().toJSON());
            }
        }
        obj.put("postnumre", postnumre);
        return obj;
    }

}
