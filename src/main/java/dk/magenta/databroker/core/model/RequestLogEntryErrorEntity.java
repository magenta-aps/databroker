package dk.magenta.databroker.core.model;

import javax.persistence.*;

/**
 * Created by jubk on 07-11-2014.
 */
@Entity
@Table(name = "core_request_log_entry_error")
public class RequestLogEntryErrorEntity {
    private Integer id;
    private Long requestLogId;
    private String errorMessage;
    private RequestLogEntryEntity requestLogEntry;

    @Id
    @Column(name = "RequestLogErrorId", nullable = false, insertable = true, updatable = true)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "ErrorMessage", nullable = false, insertable = true, updatable = true, columnDefinition="Text")
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestLogEntryErrorEntity that = (RequestLogEntryErrorEntity) o;

        if (errorMessage != null ? !errorMessage.equals(that.errorMessage) : that.errorMessage != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (requestLogId != null ? !requestLogId.equals(that.requestLogId) : that.requestLogId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (requestLogId != null ? requestLogId.hashCode() : 0);
        result = 31 * result + (errorMessage != null ? errorMessage.hashCode() : 0);
        return result;
    }

    @OneToOne
    @JoinColumn(name = "RequestLogId", referencedColumnName = "RequestLogId", nullable = false)
    public RequestLogEntryEntity getRequestLogEntry() {
        return requestLogEntry;
    }

    public void setRequestLogEntry(RequestLogEntryEntity requestLogEntry) {
        this.requestLogEntry = requestLogEntry;
    }
}
