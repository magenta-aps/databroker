package dk.magenta.databroker.correction;

import dk.magenta.databroker.core.model.DataProviderEntity;
import dk.magenta.databroker.register.records.Record;
import dk.magenta.databroker.util.Util;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.springframework.core.io.Resource;

import javax.persistence.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by lars on 06-03-15.
 */
@Entity
@Table(name = "corrections")
public class CorrectionCollectionEntity {

    public CorrectionCollectionEntity() {
        this.correctionEntities = new ArrayList<CorrectionEntity>();
    }


    @Transient
    private Logger log = Logger.getLogger(CorrectionCollectionEntity.class);


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, insertable = true, updatable = true)
    private Long id;


    public Long getId() {
        return id;
    }
/*
    @Column(unique = true)
    private String registerClassName;

    public String getRegisterClassName() {
        return registerClassName;
    }

    public void setRegisterClassName(String registerClassName) {
        this.registerClassName = registerClassName;
    }
*/


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "correctionCollectionEntity", fetch = FetchType.EAGER)
    private Collection<CorrectionEntity> correctionEntities;

    public Collection<CorrectionEntity> getCorrectionEntities() {
        return correctionEntities;
    }

    public void setCorrectionEntities(Collection<CorrectionEntity> correctionEntities) {
        this.correctionEntities = correctionEntities;
    }



    @OneToOne(mappedBy = "corrections")
    private DataProviderEntity dataProviderEntity;

    public DataProviderEntity getDataProviderEntity() {
        return dataProviderEntity;
    }

    public void setDataProviderEntity(DataProviderEntity dataProviderEntity) {
        this.dataProviderEntity = dataProviderEntity;
    }

    @Column(nullable = true)
    private String subregister;

    public String getSubregister() {
        return subregister;
    }

    public void setSubregister(String subregister) {
        this.subregister = subregister;
    }





    private List<CorrectionEntity> getApplicableCorrections(Record record) {
        ArrayList<CorrectionEntity> list = new ArrayList<CorrectionEntity>();
        for (CorrectionEntity correction : this.correctionEntities) {
            if (correction.matches(record)) {
                System.out.println(correction.getPrerequisites()+" <=> "+record.toJSON());
                list.add(correction);
            }
        }
        return list;
    }

    public boolean canCorrectRecord(Record record) {
        List<CorrectionEntity> corrections = this.getApplicableCorrections(record);
        return corrections != null && corrections.size() > 0;
    }

    public boolean correctRecord(Record record) {
        List<CorrectionEntity> corrections = this.getApplicableCorrections(record);
        if (corrections != null && corrections.size() > 0) {
            this.log.trace("There are " + corrections.size() + " corrections to record " + record.toJSON().toString());
            for (CorrectionEntity correction : corrections) {
                correction.apply(record);
            }
            this.log.trace("Record changed to " + record.toJSON().toString());
            return true;
        }
        return false;
    }

    public int correctRecords(Collection<Record> records) {
        int updated = 0;
        for (Record record : records) {
            if (this.correctRecord(record)) {
                updated++;
            }
        }
        return updated;
    }





    public void loadFromJSON(Resource jsonResource) throws IOException,IllegalArgumentException {
        this.loadFromJSON(jsonResource.getInputStream());
    }

    public void loadFromJSON(InputStream jsonInput) throws IOException,IllegalArgumentException {
        this.loadFromJSON(new JSONArray(Util.readInputStream(jsonInput)));
    }

    public void loadFromJSON(JSONArray jsonArray) throws IllegalArgumentException {
        for (int i=0; i<jsonArray.length(); i++) {
            CorrectionEntity correctionEntity = new CorrectionEntity(jsonArray.getJSONObject(i));
            correctionEntity.setCorrectionCollectionEntity(this);
            this.correctionEntities.add(correctionEntity);
        }
    }
}
