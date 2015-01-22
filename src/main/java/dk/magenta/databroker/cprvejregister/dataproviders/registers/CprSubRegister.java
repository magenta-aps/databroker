package dk.magenta.databroker.cprvejregister.dataproviders.registers;

import dk.magenta.databroker.core.DataProviderConfiguration;
import dk.magenta.databroker.core.DataProviderRegistry;
import dk.magenta.databroker.core.model.DataProviderEntity;
import dk.magenta.databroker.register.Register;
import dk.magenta.databroker.register.RegisterRun;
import dk.magenta.databroker.cprvejregister.dataproviders.records.*;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;

/**
 * Created by lars on 04-11-14.
 */

@Component
public abstract class CprSubRegister extends Register {

    public CprSubRegister() {
    }

    @PostConstruct
    @Override
    protected void RegisterDataProviderInstance() {
        // Do nothing
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

    public String getTemplatePath() {
        return null;
    }

    public DataProviderConfiguration getDefaultConfiguration() {
        return new DataProviderConfiguration();
    }



    public String getSourceTypeFieldName() {
        return "sourceType";
    }
    public String getSourceUrlFieldName() {
        return "sourceUrl";
    }
    public String getSourceCronFieldName() { return "sourceCron"; }

    public boolean canPull(DataProviderConfiguration configuration) {
        JSONObject obj = configuration.toJSON();
        String sourceType = obj.optString(this.getSourceTypeFieldName());
        String sourceUrl = obj.optString(this.getSourceUrlFieldName());
        if (sourceType != null && sourceType.equals("url") && sourceUrl != null && !sourceUrl.isEmpty()) {
            try {
                URL url = new URL(sourceUrl);
                return true;
            } catch (MalformedURLException e) {
            }
        }
        return false;
    }
}
