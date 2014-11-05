package dk.magenta.databroker.models.core;
// Generated Nov 5, 2014 3:21:03 PM by Hibernate Tools 3.2.2.GA


import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * UpdateType generated by hbm2java
 */
@Entity
@Table(name="UpdateType"
)
public class UpdateType  implements java.io.Serializable {


     private Integer updateTypeId;
     private String name;
     private String description;
     private Set<UpdateLog> updateLogs = new HashSet<UpdateLog>(0);

    public UpdateType() {
    }

    public UpdateType(String name, String description, Set<UpdateLog> updateLogs) {
       this.name = name;
       this.description = description;
       this.updateLogs = updateLogs;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)
    
    @Column(name="UpdateTypeId", unique=true, nullable=false)
    public Integer getUpdateTypeId() {
        return this.updateTypeId;
    }
    
    public void setUpdateTypeId(Integer updateTypeId) {
        this.updateTypeId = updateTypeId;
    }
    
    @Column(name="Name")
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @Column(name="Description", length=65535)
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="updateType")
    public Set<UpdateLog> getUpdateLogs() {
        return this.updateLogs;
    }
    
    public void setUpdateLogs(Set<UpdateLog> updateLogs) {
        this.updateLogs = updateLogs;
    }




}


