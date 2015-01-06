package dk.magenta.databroker.cprvejregister.dataproviders.registers;

import dk.magenta.databroker.core.model.DataProviderEntity;
import dk.magenta.databroker.register.Register;
import dk.magenta.databroker.register.RegisterRun;
import dk.magenta.databroker.cprvejregister.dataproviders.records.*;
import org.springframework.stereotype.Component;

import java.text.ParseException;

/**
 * Created by lars on 04-11-14.
 */

@Component
public abstract class CprSubRegister extends Register {

    public CprSubRegister() {
    }

    protected RegisterRun createRun() {
        return new RegisterRun();
    }

    protected CprRecord parseTrimmedLine(String line) {
        return this.parseTrimmedLine(line.substring(0, 3), line);
    }
    protected CprRecord parseTrimmedLine(String recordType, String line) {
        try {
            if (recordType.equals(CprRecord.RECORDTYPE_START)) {
                return new CprStartRecord(line);
            }
            if (recordType.equals(CprRecord.RECORDTYPE_SLUT)) {
                return new CprSlutRecord(line);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
