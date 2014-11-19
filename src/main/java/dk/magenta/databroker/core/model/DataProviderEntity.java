package dk.magenta.databroker.core.model;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by jubk on 07-11-2014.
 */
@Entity
@Table(name = "core_data_provider")
public class DataProviderEntity {
    private Integer id;
    private String uuid;
    private String type;
    private Boolean active;
    private Integer priority;
    private String cronSchedule;
    private Collection<UpdateLogEntryEntity> updateLogEntries;

    @Id
    @Column(name = "DataProviderId", nullable = false, insertable = true, updatable = true)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "UUID", nullable = false, insertable = true, updatable = true, length = 36)
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Basic
    @Column(name = "type", nullable = false, insertable = true, updatable = true, length = 255)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Basic
    @Column(name = "active", nullable = false, insertable = true, updatable = true)
    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Basic
    @Column(name = "priority", nullable = false, insertable = true, updatable = true)
    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @Basic
    @Column(name = "cronSchedule", nullable = true, insertable = true, updatable = true, length = 255)
    public String getCronSchedule() {
        return cronSchedule;
    }

    public void setCronSchedule(String cronSchedule) {
        this.cronSchedule = cronSchedule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataProviderEntity that = (DataProviderEntity) o;

        if (active != null ? !active.equals(that.active) : that.active != null) return false;
        if (cronSchedule != null ? !cronSchedule.equals(that.cronSchedule) : that.cronSchedule != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (priority != null ? !priority.equals(that.priority) : that.priority != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (uuid != null ? !uuid.equals(that.uuid) : that.uuid != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (uuid != null ? uuid.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (active != null ? active.hashCode() : 0);
        result = 31 * result + (priority != null ? priority.hashCode() : 0);
        result = 31 * result + (cronSchedule != null ? cronSchedule.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "dataProvider")
    public Collection<UpdateLogEntryEntity> getUpdateLogEntries() {
        return updateLogEntries;
    }

    public void setUpdateLogEntries(Collection<UpdateLogEntryEntity> updateLogEntries) {
        this.updateLogEntries = updateLogEntries;
    }
}
