package dk.magenta.databroker.cvr.model.deltager;

import dk.magenta.databroker.core.model.oio.DobbeltHistorikVersion;
import dk.magenta.databroker.cvr.model.deltager.rolle.RolleEntity;
import dk.magenta.databroker.cvr.model.deltager.type.TypeEntity;
import dk.magenta.databroker.util.Util;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by jubk on 18-12-2014.
 */
@Entity
@Table(name = "cvr_deltager_version")
public class DeltagerVersionEntity extends DobbeltHistorikVersion<DeltagerEntity, DeltagerVersionEntity> {

    public DeltagerVersionEntity() {
    }

    public DeltagerVersionEntity(DeltagerEntity entity) {
        super(entity);
    }

    //------------------------------------------------------------------------------------------------------------------

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private DeltagerEntity entity;

    @Override
    public DeltagerEntity getEntity() {
        return entity;
    }

    @Override
    public void setEntity(DeltagerEntity entity) {
        this.entity = entity;
    }

    //------------------------------------------------------------------------------------------------------------------
    /* Domain specific fields */


    @Column(nullable = true)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //----------------------------------------------------

    @Column(nullable = true)
    private Date ajourDate;

    public Date getAjourDate() {
        return ajourDate;
    }

    public void setAjourDate(Date ajourDate) {
        this.ajourDate = ajourDate;
    }

    //----------------------------------------------------

    @Column(nullable = true)
    private Date gyldigDate;

    public Date getGyldigDate() {
        return gyldigDate;
    }

    public void setGyldigDate(Date gyldigDate) {
        this.gyldigDate = gyldigDate;
    }

    //----------------------------------------------------

    @ManyToOne(optional = true)
    private TypeEntity type;

    public TypeEntity getType() {
        return type;
    }

    public void setType(TypeEntity type) {
        this.type = type;
    }

    //----------------------------------------------------

    @ManyToOne(optional = true)
    private RolleEntity rolle;

    public RolleEntity getRolle() {
        return rolle;
    }

    public void setRolle(RolleEntity rolle) {
        this.rolle = rolle;
    }

    //----------------------------------------------------

    public boolean matches(String name, Date ajourDate, Date gyldigDate, TypeEntity type, RolleEntity rolle) {
        return Util.compare(this.name, name) &&
                Util.compare(this.ajourDate, ajourDate) &&
                Util.compare(this.gyldigDate, gyldigDate) &&
                Util.compare(this.type, type) &&
                Util.compare(this.rolle, rolle);
    }

}
