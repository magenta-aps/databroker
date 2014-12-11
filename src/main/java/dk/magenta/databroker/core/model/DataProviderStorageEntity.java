package dk.magenta.databroker.core.model;

import javax.persistence.*;

/**
 * Created by lars on 11-12-14.
 */
@Entity
@Table(name = "core_data_provider_storage")
public class DataProviderStorageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, insertable = true, updatable = false)
    private Long id;

    @Basic
    @Column(name = "class", nullable = false, insertable = true, updatable = false, length = 255, unique = true)
    private String owningClass;


    @Basic
    @Column(name = "data", nullable = true, insertable = true, updatable = true, length = 10000)
    private String data;


    public String getOwningClass() {
        return this.owningClass;
    }

    public void setOwningClass(String owningClass) {
        this.owningClass = owningClass;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
