package dk.magenta.databroker.core.model;

import javax.persistence.*;

/**
 * Created by jubk on 07-11-2014.
 */
@Entity
@Table(name = "IpAuthentication")
public class IpAuthenticationEntity {
    private Integer id;
    private Integer consumerId;
    private Integer ipRangeMin;
    private Integer ipRangeMax;
    private ConsumerEntity consumer;

    @Id
    @Column(name = "IpAuthenticationId", nullable = false, insertable = true, updatable = true)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "IpRangeMin", nullable = false, insertable = true, updatable = true)
    public Integer getIpRangeMin() {
        return ipRangeMin;
    }

    public void setIpRangeMin(Integer ipRangeMin) {
        this.ipRangeMin = ipRangeMin;
    }

    @Basic
    @Column(name = "IpRangeMax", nullable = false, insertable = true, updatable = true)
    public Integer getIpRangeMax() {
        return ipRangeMax;
    }

    public void setIpRangeMax(Integer ipRangeMax) {
        this.ipRangeMax = ipRangeMax;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IpAuthenticationEntity that = (IpAuthenticationEntity) o;

        if (consumerId != null ? !consumerId.equals(that.consumerId) : that.consumerId != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (ipRangeMax != null ? !ipRangeMax.equals(that.ipRangeMax) : that.ipRangeMax != null) return false;
        if (ipRangeMin != null ? !ipRangeMin.equals(that.ipRangeMin) : that.ipRangeMin != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (consumerId != null ? consumerId.hashCode() : 0);
        result = 31 * result + (ipRangeMin != null ? ipRangeMin.hashCode() : 0);
        result = 31 * result + (ipRangeMax != null ? ipRangeMax.hashCode() : 0);
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
}
