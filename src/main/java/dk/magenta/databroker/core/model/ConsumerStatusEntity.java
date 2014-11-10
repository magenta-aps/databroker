package dk.magenta.databroker.core.model;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by jubk on 07-11-2014.
 */
@Entity
@Table(name = "ConsumerStatus", schema = "", catalog = "DataBrokerCore")
public class ConsumerStatusEntity {
    private Integer id;
    private String name;
    private String description;
    private Collection<ConsumerEntity> consumers;

    @Id
    @Column(name = "ConsumerStatusId", nullable = false, insertable = true, updatable = true)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "Name", nullable = false, insertable = true, updatable = true, length = 255)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "Description", nullable = false, insertable = true, updatable = true, columnDefinition="Text")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConsumerStatusEntity that = (ConsumerStatusEntity) o;

        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "status")
    public Collection<ConsumerEntity> getConsumers() {
        return consumers;
    }

    public void setConsumers(Collection<ConsumerEntity> consumers) {
        this.consumers = consumers;
    }
}
