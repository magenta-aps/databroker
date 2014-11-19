package dk.magenta.databroker.core.model;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by jubk on 07-11-2014.
 */
@Entity
@Table(name = "core_consumer")
public class ConsumerEntity {
    private Integer id;
    private String uuid;
    private Integer consumerStatusId;
    private Boolean allowUuidOnlyAccess;
    private ConsumerStatusEntity status;
    private Collection<IpAuthenticationEntity> ipAuthentications;
    private Collection<PasswordLoginEntity> passwordLogins;
    private Collection<RequestLogEntryEntity> requestLogEntries;

    @Id
    @Column(name = "ConsumerId", nullable = false, insertable = true, updatable = true)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "UUID", nullable = true, insertable = true, updatable = true, length = 36)
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setConsumerStatusId(Integer consumerStatusId) {
        this.consumerStatusId = consumerStatusId;
    }

    @Basic
    @Column(name = "AllowUUIDOnlyAccess", nullable = false, insertable = true, updatable = true)
    public Boolean getAllowUuidOnlyAccess() {
        return allowUuidOnlyAccess;
    }

    public void setAllowUuidOnlyAccess(Boolean allowUuidOnlyAccess) {
        this.allowUuidOnlyAccess = allowUuidOnlyAccess;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConsumerEntity that = (ConsumerEntity) o;

        if (allowUuidOnlyAccess != null ? !allowUuidOnlyAccess.equals(that.allowUuidOnlyAccess) : that.allowUuidOnlyAccess != null)
            return false;
        if (consumerStatusId != null ? !consumerStatusId.equals(that.consumerStatusId) : that.consumerStatusId != null)
            return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (uuid != null ? !uuid.equals(that.uuid) : that.uuid != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (uuid != null ? uuid.hashCode() : 0);
        result = 31 * result + (consumerStatusId != null ? consumerStatusId.hashCode() : 0);
        result = 31 * result + (allowUuidOnlyAccess != null ? allowUuidOnlyAccess.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "ConsumerStatusId", referencedColumnName = "ConsumerStatusId", nullable = false)
    public ConsumerStatusEntity getStatus() {
        return status;
    }

    public void setStatus(ConsumerStatusEntity status) {
        this.status = status;
    }

    @OneToMany(mappedBy = "consumer")
    public Collection<IpAuthenticationEntity> getIpAuthentications() {
        return ipAuthentications;
    }

    public void setIpAuthentications(Collection<IpAuthenticationEntity> ipAuthentications) {
        this.ipAuthentications = ipAuthentications;
    }

    @OneToMany(mappedBy = "consumer")
    public Collection<PasswordLoginEntity> getPasswordLogins() {
        return passwordLogins;
    }

    public void setPasswordLogins(Collection<PasswordLoginEntity> passwordLogins) {
        this.passwordLogins = passwordLogins;
    }

    @OneToMany(mappedBy = "consumer")
    public Collection<RequestLogEntryEntity> getRequestLogEntries() {
        return requestLogEntries;
    }

    public void setRequestLogEntries(Collection<RequestLogEntryEntity> requestLogEntries) {
        this.requestLogEntries = requestLogEntries;
    }
}
