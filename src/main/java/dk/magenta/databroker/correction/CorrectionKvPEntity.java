package dk.magenta.databroker.correction;

import dk.magenta.databroker.register.records.Record;
import dk.magenta.databroker.util.Util;

import javax.persistence.*;

/**
 * Created by lars on 06-03-15.
 */
@Entity
@Table(name = "correction_fragment")
public class CorrectionKvPEntity {

    public CorrectionKvPEntity(){
    }

    public CorrectionKvPEntity(CorrectionEntity correction, String name, String value, boolean prerequisite) {
        this.correction = correction;
        this.name = name;
        this.value = value;
        this.prerequisite = prerequisite;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, insertable = true, updatable = true)
    private Long id;


    @Column(nullable = false)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String key) {
        this.name = key;
    }


    @Column
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean matches(Record record) {
        return Util.compare(record.get(this.name), this.value);
    }

    public void apply(Record record) {
        record.put(this.name, this.value);
    }


    @Column
    private boolean prerequisite;

    public boolean isPrerequisite() {
        return prerequisite;
    }

    public void setPrerequisite(boolean prerequisite) {
        this.prerequisite = prerequisite;
    }


    @ManyToOne
    private CorrectionEntity correction;

    public CorrectionEntity getCorrection() {
        return correction;
    }

    public void setCorrection(CorrectionEntity correction) {
        this.correction = correction;
    }
}
