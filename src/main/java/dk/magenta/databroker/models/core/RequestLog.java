package dk.magenta.databroker.models.core;
// Generated Nov 5, 2014 3:21:03 PM by Hibernate Tools 3.2.2.GA


import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * RequestLog generated by hbm2java
 */
@Entity
@Table(name="RequestLog"
)
public class RequestLog  implements java.io.Serializable {


     private Long requestLogId;
     private Date timestamp;
     private Consumer consumer;
     private String type;
     private String subType;
     private Integer resultCount;
     private int remoteIp;
     private Set<RequestLogError> requestLogErrors = new HashSet<RequestLogError>(0);

    public RequestLog() {
    }

	
    public RequestLog(Consumer consumer, String type, String subType, int remoteIp) {
        this.consumer = consumer;
        this.type = type;
        this.subType = subType;
        this.remoteIp = remoteIp;
    }
    public RequestLog(Consumer consumer, String type, String subType, Integer resultCount, int remoteIp, Set<RequestLogError> requestLogErrors) {
       this.consumer = consumer;
       this.type = type;
       this.subType = subType;
       this.resultCount = resultCount;
       this.remoteIp = remoteIp;
       this.requestLogErrors = requestLogErrors;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)
    
    @Column(name="RequestLogId", unique=true, nullable=false)
    public Long getRequestLogId() {
        return this.requestLogId;
    }
    
    public void setRequestLogId(Long requestLogId) {
        this.requestLogId = requestLogId;
    }
    @Version@Temporal(TemporalType.TIMESTAMP)
    @Column(name="Timestamp", nullable=false, length=19)
    public Date getTimestamp() {
        return this.timestamp;
    }
    
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ConsumerId", nullable=false)
    public Consumer getConsumer() {
        return this.consumer;
    }
    
    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }
    
    @Column(name="Type", nullable=false)
    public String getType() {
        return this.type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    @Column(name="SubType", nullable=false)
    public String getSubType() {
        return this.subType;
    }
    
    public void setSubType(String subType) {
        this.subType = subType;
    }
    
    @Column(name="ResultCount")
    public Integer getResultCount() {
        return this.resultCount;
    }
    
    public void setResultCount(Integer resultCount) {
        this.resultCount = resultCount;
    }
    
    @Column(name="RemoteIp", nullable=false)
    public int getRemoteIp() {
        return this.remoteIp;
    }
    
    public void setRemoteIp(int remoteIp) {
        this.remoteIp = remoteIp;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="requestLog")
    public Set<RequestLogError> getRequestLogErrors() {
        return this.requestLogErrors;
    }
    
    public void setRequestLogErrors(Set<RequestLogError> requestLogErrors) {
        this.requestLogErrors = requestLogErrors;
    }




}


