package dk.magenta.databroker.core;

import dk.magenta.databroker.core.model.DataProviderEntity;
import dk.magenta.databroker.core.model.RegistreringManager;
import dk.magenta.databroker.core.model.oio.RegistreringEntity;
import dk.magenta.databroker.core.model.oio.RegistreringLivscyklusRepository;
import dk.magenta.databroker.core.model.oio.RegistreringRepository;
import org.apache.commons.io.input.CountingInputStream;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by lars on 25-02-15.
 */
public class RegistreringInfo {
    private RegistreringEntity createRegistrering;
    private RegistreringEntity updateRegisterering;
    private DataProviderEntity dataProviderEntity;
    private CountingInputStream inputStream;
    private long inputSize;
    private double progress = 0;

    private static HashMap<String, RegistreringInfo> registreringInfoHashMap = new HashMap<String, RegistreringInfo>();

    public RegistreringInfo(RegistreringRepository regRepo, RegistreringLivscyklusRepository lsRepo, RegistreringManager registreringManager, DataProviderEntity dataProviderEntity, NamedInputStream inputStream) {
        this.dataProviderEntity = dataProviderEntity;
        RegistreringEntity createRegistrering = registreringManager.createNewRegistrering(regRepo, lsRepo, dataProviderEntity);
        RegistreringEntity updateRegistrering = registreringManager.createUpdateRegistrering(regRepo, lsRepo, dataProviderEntity);

        if (createRegistrering == null && updateRegistrering == null) {
            System.err.println("Both registrations are null; cannot create RegistreringInfo object");
        } else if (createRegistrering == null) {
            createRegistrering = updateRegistrering;
        } else if (updateRegistrering == null) {
            updateRegistrering = createRegistrering;
        }

        this.createRegistrering = createRegistrering;
        this.updateRegisterering = updateRegistrering;
        this.inputStream = new CountingInputStream(inputStream);
        this.inputSize = inputStream.getKnownSize();
        dataProviderEntity.setRegistreringInfo(this);
        registreringInfoHashMap.put(dataProviderEntity.getUuid(), this);
    }

    public DataProviderEntity getDataProviderEntity() {
        return dataProviderEntity;
    }

    public RegistreringEntity getCreateRegistrering() {
        return createRegistrering;
    }

    public RegistreringEntity getUpdateRegisterering() {
        return updateRegisterering;
    }

    public boolean has(RegistreringEntity registreringEntity) {
        return registreringEntity == this.createRegistrering || registreringEntity == this.updateRegisterering;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public long getInputSize() {
        return this.inputSize;
    }

    public long getBytesRead() {
        return this.inputStream.getByteCount();
    }

    public void logProcess() {
        this.logProcess(Logger.getLogger(RegistreringInfo.class));
    }
    public void logProcess(Logger log) {
        long bytesRead = this.getBytesRead();
        this.logProcess(log, Level.INFO);
    }
    public void logProcess(Logger log, Level level) {
        long bytesRead = this.getBytesRead();
        String pct = "";
        if (this.getInputSize() > 0) {
            this.progress = (double) bytesRead / (double) this.getInputSize();
            pct = " (" + String.format("%.2f", 100.0 * this.progress) + "%)";
        }
        log.log(level, "Processed " + bytesRead + " of " + this.getInputSize() + " bytes" + pct);
    }

    public void clear() {
        if (this.dataProviderEntity.getRegistreringInfo() == this) {
            this.dataProviderEntity.setRegistreringInfo(null);
        }
    }

    public double getProgress() {
        return this.progress;
    }

    public static RegistreringInfo getRegistreringInfo(String uuid) {
        return registreringInfoHashMap.get(uuid);
    }

    public static RegistreringInfo getRegistreringInfo(DataProviderEntity dataProviderEntity) {
        return getRegistreringInfo(dataProviderEntity.getUuid());
    }
}
