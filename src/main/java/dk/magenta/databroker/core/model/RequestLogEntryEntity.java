package dk.magenta.databroker.core.model;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by jubk on 07-11-2014.
 */
@Entity
@Table(name = "core_request_log_entry")
public class RequestLogEntryEntity {
    private Long id;
    private Integer consumerId;
    private Timestamp timestamp;
    private String type;
    private String subType;
    private Integer resultCount;
    private Integer remoteIp;
    private ConsumerEntity consumer;
    private RequestLogEntryErrorEntity error;

    @Id
    @Column(name = "RequestLogId", nullable = false, insertable = true, updatable = true)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "Timestamp", nullable = false, insertable = true, updatable = true)
    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Basic
    @Column(name = "Type", nullable = false, insertable = true, updatable = true, length = 255)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Basic
    @Column(name = "SubType", nullable = false, insertable = true, updatable = true, length = 255)
    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    @Basic
    @Column(name = "ResultCount", nullable = true, insertable = true, updatable = true)
    public Integer getResultCount() {
        return resultCount;
    }

    public void setResultCount(Integer resultCount) {
        this.resultCount = resultCount;
    }

    @Basic
    @Column(name = "RemoteIp", nullable = false, insertable = true, updatable = true)
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

        RequestLogEntryEntity that = (RequestLogEntryEntity) o;

        if (consumerId != null ? !consumerId.equals(that.consumerId) : that.consumerId != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (remoteIp != null ? !remoteIp.equals(that.remoteIp) : that.remoteIp != null) return false;
        if (resultCount != null ? !resultCount.equals(that.resultCount) : that.resultCount != null) return false;
        if (subType != null ? !subType.equals(that.subType) : that.subType != null) return false;
        if (timestamp != null ? !timestamp.equals(that.timestamp) : that.timestamp != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (consumerId != null ? consumerId.hashCode() : 0);
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (subType != null ? subType.hashCode() : 0);
        result = 31 * result + (resultCount != null ? resultCount.hashCode() : 0);
        result = 31 * result + (remoteIp != null ? remoteIp.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "ConsumerId", referencedColumnName = "ConsumerId", nullable = false)
    public ConsumerEntity getConsumer() {
        return consumer;
    }

    public void setConsumer(ConsumerEntity consumer) {
        this.consumer = consumer;
    }

    @OneToOne(mappedBy = "requestLogEntry")
    public RequestLogEntryErrorEntity getError() {
        return error;
    }

    public void setError(RequestLogEntryErrorEntity error) {
        this.error = error;
    }
}
