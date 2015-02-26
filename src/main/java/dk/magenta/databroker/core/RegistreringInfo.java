package dk.magenta.databroker.core;

import dk.magenta.databroker.core.model.DataProviderEntity;
import dk.magenta.databroker.core.model.oio.RegistreringEntity;
import dk.magenta.databroker.core.model.oio.RegistreringRepository;
import org.apache.commons.io.input.CountingInputStream;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.InputStream;

/**
 * Created by lars on 25-02-15.
 */
public class RegistreringInfo {
    private RegistreringEntity createRegistrering;
    private RegistreringEntity updateRegisterering;
    private DataProviderEntity dataProviderEntity;
    private CountingInputStream inputStream;
    private long inputSize;

    public RegistreringInfo(RegistreringRepository registreringRepository, DataProviderEntity dataProviderEntity, NamedInputStream inputStream) {
        this.dataProviderEntity = dataProviderEntity;
        RegistreringEntity createRegistrering = registreringRepository.createNew(dataProviderEntity);
        RegistreringEntity updateRegistrering = registreringRepository.createUpdate(dataProviderEntity);

        if (createRegistrering == null && updateRegistrering == null) {
            System.err.println("Both registrations are null; cannot create RegistreringInfo object");
        } else if (createRegistrering == null) {
            createRegistrering = updateRegistrering;
        } else if (updateRegistrering == null) {
            updateRegistrering = createRegistrering;
        }

        System.out.println(createRegistrering+" "+updateRegistrering);

        this.createRegistrering = createRegistrering;
        this.updateRegisterering = updateRegistrering;
        this.inputStream = new CountingInputStream(inputStream);
        this.inputSize = inputStream.getKnownSize();
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
        log.log(level, "Processed " + bytesRead + " bytes (" + String.format("%.2f", 100.0 * (double) bytesRead / (double) this.getInputSize()) + "%)");
    }

}
