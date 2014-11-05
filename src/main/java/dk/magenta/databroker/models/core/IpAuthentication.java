package dk.magenta.databroker.models.core;
// Generated Nov 5, 2014 3:21:03 PM by Hibernate Tools 3.2.2.GA


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * IpAuthentication generated by hbm2java
 */
@Entity
@Table(name="IpAuthentication"
)
public class IpAuthentication  implements java.io.Serializable {


     private Integer ipAuthenticationId;
     private Consumer consumer;
     private int ipRangeMin;
     private int ipRangeMax;

    public IpAuthentication() {
    }

    public IpAuthentication(Consumer consumer, int ipRangeMin, int ipRangeMax) {
       this.consumer = consumer;
       this.ipRangeMin = ipRangeMin;
       this.ipRangeMax = ipRangeMax;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)
    
    @Column(name="IpAuthenticationId", unique=true, nullable=false)
    public Integer getIpAuthenticationId() {
        return this.ipAuthenticationId;
    }
    
    public void setIpAuthenticationId(Integer ipAuthenticationId) {
        this.ipAuthenticationId = ipAuthenticationId;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ConsumerId", nullable=false)
    public Consumer getConsumer() {
        return this.consumer;
    }
    
    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }
    
    @Column(name="IpRangeMin", nullable=false)
    public int getIpRangeMin() {
        return this.ipRangeMin;
    }
    
    public void setIpRangeMin(int ipRangeMin) {
        this.ipRangeMin = ipRangeMin;
    }
    
    @Column(name="IpRangeMax", nullable=false)
    public int getIpRangeMax() {
        return this.ipRangeMax;
    }
    
    public void setIpRangeMax(int ipRangeMax) {
        this.ipRangeMax = ipRangeMax;
    }




}


