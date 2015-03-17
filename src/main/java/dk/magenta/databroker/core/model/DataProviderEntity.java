package dk.magenta.databroker.core.model;

import dk.magenta.databroker.core.DataProvider;
import dk.magenta.databroker.core.DataProviderConfiguration;
import dk.magenta.databroker.core.DataProviderRegistry;
import dk.magenta.databroker.core.RegistreringInfo;
import dk.magenta.databroker.core.model.oio.RegistreringEntity;
import dk.magenta.databroker.core.model.oio.RegistreringRepository;
import dk.magenta.databroker.correction.CorrectionCollectionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpRequest;

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
    private String name;
    private String type;
    private Boolean active;
    private Integer priority;
    private String configuration;
    private Collection<UpdateLogEntryEntity> updateLogEntries;


    private CorrectionCollectionEntity corrections;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "DataProviderId", nullable = false, insertable = true, updatable = true)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "UUID", nullable = false, insertable = true, updatable = true, length = 36, unique = true)
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Basic
    @Column(name = "name", nullable = false, insertable = true, updatable = true, length = 36, unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "type", nullable = false, insertable = true, updatable = true, length = 255)
    public String getType() {
        return type;
    }

    private void setType(String type) {
        this.type = type;
    }

    public void setType(Class<? extends DataProvider> cls) {
        this.type = cls.getCanonicalName();
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
    @Column(name = "configuration", nullable = true, insertable = true, updatable = true, length = 10000)
    private String getConfiguration() {
        return configuration;
    }

    private void setConfiguration(String configuration) {
        this.configuration = configuration;
    }


    @Transient
    public DataProviderConfiguration getConfig() {
        return new DataProviderConfiguration(this.getConfiguration());
    }

    @Transient
    public void setConfig(DataProviderConfiguration config) {
        this.setConfiguration(config.toString());
    }


    @OneToOne
    public CorrectionCollectionEntity getCorrections() {
        return corrections;
    }

    public void setCorrections(CorrectionCollectionEntity corrections) {
        this.corrections = corrections;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataProviderEntity that = (DataProviderEntity) o;

        if (active != null ? !active.equals(that.active) : that.active != null) return false;
        if (configuration != null ? !configuration.equals(that.configuration) : that.configuration != null) return false;
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
        result = 31 * result + (configuration != null ? configuration.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "dataProvider")
    public Collection<UpdateLogEntryEntity> getUpdateLogEntries() {
        return updateLogEntries;
    }

    public void setUpdateLogEntries(Collection<UpdateLogEntryEntity> updateLogEntries) {
        this.updateLogEntries = updateLogEntries;
    }

    /**************************************************************************************
     * Non-column fields                                                                  *
     **************************************************************************************/

    @Transient
    private DataProvider dataProvider;

    @Transient
    public DataProvider getDataProvider() {
        if(dataProvider == null) {
            dataProvider = DataProviderRegistry.getDataProviderForEntity(this);
        }
        return dataProvider;
    }

    @Transient
    private void setDataProvider(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }


    @Transient
    private RegistreringInfo registreringInfo;

    @Transient
    public RegistreringInfo getRegistreringInfo() {
        if(registreringInfo == null) {
            registreringInfo = RegistreringInfo.getRegistreringInfo(this);
        }
        return registreringInfo;
    }

    @Transient
    public void setRegistreringInfo(RegistreringInfo registreringInfo) {
        System.out.println("Setting registreringInfo to " + registreringInfo);
        this.registreringInfo = registreringInfo;
    }

    /**************************************************************************************
     * Proxy-methods DataProvider implementations                                         *
     **************************************************************************************/

    public void pull() {
        this.getDataProvider().pull(this);
    }

    public boolean canPull() {
        return this.getDataProvider().canPull(this.getConfig());
    }

    /**************************************************************************************
     * Shorthand methods for display                                                      *
     **************************************************************************************/

    public String fetchShortType() {
        return this.type.substring(this.type.lastIndexOf('.')+1);
    }

}
