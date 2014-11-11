package dk.magenta.databroker.core.model;

import javax.persistence.*;

/**
 * Created by jubk on 07-11-2014.
 */
@Entity
@Table(name = "UpdateLogEntry")
public class UpdateLogEntryEntity {
    private Long id;
    private Integer sourceDataProviderId;
    private Integer updateTypeId;
    private Integer count;
    private Boolean isPush;
    private Integer remoteIp;
    private DataProviderEntity dataProvider;
    private UpdateTypeEntity updateType;

    @Id
    @Column(name = "UpdateLogId", nullable = false, insertable = true, updatable = true)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "Count", nullable = true, insertable = true, updatable = true)
    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Basic
    @Column(name = "IsPush", nullable = false, insertable = true, updatable = true)
    public Boolean getIsPush() {
        return isPush;
    }

    public void setIsPush(Boolean isPush) {
        this.isPush = isPush;
    }

    @Basic
    @Column(name = "RemoteIp", nullable = true, insertable = true, updatable = true)
    public Integer getRemoteIp() {
        return remoteIp;
    }

    public void setRemoteIp(Integer remoteIp) {
        this.remoteIp = remoteIp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UpdateLogEntryEntity that = (UpdateLogEntryEntity) o;

        if (count != null ? !count.equals(that.count) : that.count != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (isPush != null ? !isPush.equals(that.isPush) : that.isPush != null) return false;
        if (remoteIp != null ? !remoteIp.equals(that.remoteIp) : that.remoteIp != null) return false;
        if (sourceDataProviderId != null ? !sourceDataProviderId.equals(that.sourceDataProviderId) : that.sourceDataProviderId != null)
            return false;
        if (updateTypeId != null ? !updateTypeId.equals(that.updateTypeId) : that.updateTypeId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (sourceDataProviderId != null ? sourceDataProviderId.hashCode() : 0);
        result = 31 * result + (updateTypeId != null ? updateTypeId.hashCode() : 0);
        result = 31 * result + (count != null ? count.hashCode() : 0);
        result = 31 * result + (isPush != null ? isPush.hashCode() : 0);
        result = 31 * result + (remoteIp != null ? remoteIp.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "SourceDataProviderId", referencedColumnName = "DataProviderId", nullable = false)
    public DataProviderEntity getDataProvider() {
        return dataProvider;
    }

    public void setDataProvider(DataProviderEntity dataProvider) {
        this.dataProvider = dataProvider;
    }

    @ManyToOne
    @JoinColumn(name = "UpdateTypeId", referencedColumnName = "UpdateTypeId", nullable = false)
    public UpdateTypeEntity getUpdateType() {
        return updateType;
    }

    public void setUpdateType(UpdateTypeEntity updateType) {
        this.updateType = updateType;
    }
}
