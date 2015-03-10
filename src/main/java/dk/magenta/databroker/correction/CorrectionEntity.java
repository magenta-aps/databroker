package dk.magenta.databroker.correction;

import dk.magenta.databroker.register.records.Record;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.*;

/**
 * Created by lars on 06-03-15.
 */
@Entity
@Table(name = "correction_item")
public class CorrectionEntity {

    private static final String CATEGORY_PREREQUISITES = "prereq";
    private static final String CATEGORY_CORRECTION = "correction";

    public CorrectionEntity(){
        this.items = new HashSet<CorrectionKvPEntity>();
    }

    public CorrectionEntity(HashMap<String, String> prerequisites, HashMap<String, String> changes) {
        this();
        for (String key : prerequisites.keySet()) {
            this.addPrerequisite(key, prerequisites.get(key));
        }
        for (String key : changes.keySet()) {
            this.addChange(key, changes.get(key));
        }
    }

    public CorrectionEntity(JSONObject jsonObject) throws IllegalArgumentException {
        this();
        if (jsonObject.has(CATEGORY_PREREQUISITES)) {
            JSONObject prereqObject = jsonObject.getJSONObject(CATEGORY_PREREQUISITES);
            for (Object key : prereqObject.keySet()) {
                String sKey = (String) key;
                this.addPrerequisite(sKey, String.valueOf(prereqObject.get(sKey)));
            }
        } else {
            throw new IllegalArgumentException("Input JSON must contain a "+CATEGORY_PREREQUISITES+" key");
        }
        if (jsonObject.has(CATEGORY_CORRECTION)) {
            JSONObject changeObject = jsonObject.getJSONObject(CATEGORY_CORRECTION);
            for (Object key : changeObject.keySet()) {
                String sKey = (String) key;
                this.addChange(sKey, String.valueOf(changeObject.get(sKey)));
            }
        } else {
            throw new IllegalArgumentException("Input JSON must contain a "+CATEGORY_CORRECTION+" key");
        }
    }

    private HashMap<String, String> loadJSON(JSONObject obj) {
        HashMap<String, String> map = new HashMap<String, String>();
        for (Object key : obj.keySet()) {
            String sKey = (String) key;
            map.put(sKey, obj.get(sKey).toString());
        }
        return map;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, insertable = true, updatable = true)
    private Long id;



    @OneToMany(cascade = CascadeType.ALL, mappedBy = "correction", fetch = FetchType.EAGER)
    private Set<CorrectionKvPEntity> items;

    public Collection<CorrectionKvPEntity> getItems() {
        return items;
    }


    @ManyToOne
    private CorrectionCollectionEntity correctionCollectionEntity;

    public CorrectionCollectionEntity getCorrectionCollectionEntity() {
        return correctionCollectionEntity;
    }

    public void setCorrectionCollectionEntity(CorrectionCollectionEntity correctionCollectionEntity) {
        this.correctionCollectionEntity = correctionCollectionEntity;
    }





    public Collection<CorrectionKvPEntity> getPrerequisites() {
        ArrayList<CorrectionKvPEntity> prerequisites = new ArrayList<CorrectionKvPEntity>();
        for (CorrectionKvPEntity item : this.items) {
            if (item.isPrerequisite()) {
                prerequisites.add(item);
            }
        }
        return prerequisites;
    }

    private CorrectionKvPEntity addPrerequisite(String key, String value) {
        CorrectionKvPEntity entity = new CorrectionKvPEntity(this, key, value, true);
        this.items.add(entity);
        return entity;
    }
    private CorrectionKvPEntity addChange(String key, String value) {
        CorrectionKvPEntity entity = new CorrectionKvPEntity(this, key, value, false);
        this.items.add(entity);
        return entity;
    }

    public Collection<CorrectionKvPEntity> getChanges() {
        ArrayList<CorrectionKvPEntity> changes = new ArrayList<CorrectionKvPEntity>();
        for (CorrectionKvPEntity item : this.items) {
            if (!item.isPrerequisite()) {
                changes.add(item);
            }
        }
        return changes;
    }

    public boolean matches(Record record) {
        for (CorrectionKvPEntity prereq : this.getPrerequisites()) {
            if (!prereq.matches(record)) {
                return false;
            }
        }
        return true;
    }

    public void apply(Record record) {
        for (CorrectionKvPEntity change : this.getChanges()) {
            change.apply(record);
        }
    }

}
