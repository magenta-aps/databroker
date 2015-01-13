package dk.magenta.databroker.nukissiorfiit;

import dk.magenta.databroker.core.DataProviderConfiguration;
import dk.magenta.databroker.core.model.DataProviderEntity;
import dk.magenta.databroker.dawa.model.DawaModel;
import dk.magenta.databroker.dawa.model.SearchParameters;
import dk.magenta.databroker.dawa.model.SearchParameters.Key;
import dk.magenta.databroker.dawa.model.vejstykker.VejstykkeEntity;
import dk.magenta.databroker.register.Register;
import dk.magenta.databroker.register.RegisterRun;
import dk.magenta.databroker.register.records.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Collection;

/**
 * Created by lars on 13-01-15.
 */
@Component
public class NukissiorfiitRegister extends Register {

    private class NukissiorfiitRecord extends Record {
    }


    @Override
    public File getRecordFile() {
        return new File("src/test/resources/nukissiorfiit.csv");
    }

    @Override
    protected String getEncoding() {
        return "UTF-8";
    }


    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private DawaModel model;

    protected Record parseTrimmedLine(String line) {
        String[] parts = line.split(",");
        if (parts.length >= 9) {
            NukissiorfiitRecord record = new NukissiorfiitRecord();
            record.put("by", parts[0]);
            record.put("bolignr", parts[1]);
            record.put("bnr", parts[2]);
            record.put("gadenavn", parts[3]);
            record.put("gadenr", parts[4]);
            record.put("lejlighedsnr", parts[5]);
            record.put("boligbogstav", parts[6]);
            record.put("postnummer", parts[7]);
            record.put("bynavn", parts[8]);
            return record;
        }
        return null;
    }


    @Transactional
    public void pull(boolean forceFetch, boolean forceParse, DataProviderEntity dataProviderEntity) {
        super.pull(forceFetch, forceParse, dataProviderEntity);
    }


    @Override
    protected void saveRunToDatabase(RegisterRun run, DataProviderEntity dataProviderEntity) {
        this.model.resetAllCaches();
        for (Record r : run) {
            if (r instanceof NukissiorfiitRecord) {
                NukissiorfiitRecord record = (NukissiorfiitRecord) r;
                SearchParameters vejSearch = new SearchParameters();
                vejSearch.put(Key.LAND, "gl");
                vejSearch.put(Key.POST, record.get("postnummer"));
                vejSearch.put(Key.VEJ, record.get("gadenavn"));

                Collection<VejstykkeEntity> vejstykker = model.getVejstykke(vejSearch, false);
                int count = vejstykker.size();
                if (count == 0) {
                    System.err.println("No road found for record "+record.toJSON());
                } else if (count > 1) {
                    System.err.println("Multiple roads found for record "+record.toJSON());
                } else {
                    VejstykkeEntity vejstykkeEntity = vejstykker.iterator().next();
                    int kommuneKode = vejstykkeEntity.getKommune().getKode();
                    int vejKode = vejstykkeEntity.getKode();
                    String husnr = record.get("gadenr");
                    if (husnr == null || husnr.isEmpty()) {
                        husnr = "0";
                    }
                    String bnr = record.get("bnr");
                    String etage = null;
                    String doer = record.get("lejlighedsnr");
                    if (doer != null && doer.isEmpty()) {
                        doer = null;
                    }
                    this.model.setAdresse(kommuneKode, vejKode, husnr, bnr, etage, doer,
                            this.getCreateRegistrering(dataProviderEntity), this.getUpdateRegistrering(dataProviderEntity)
                    );
                }
            }
        }
    }

    @Override
    public String getTemplatePath() {
        return null;
    }

    @Override
    public DataProviderConfiguration getDefaultConfiguration() {
        return null;
    }
}
