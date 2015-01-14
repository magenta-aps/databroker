package dk.magenta.databroker.cprvejregister.dataproviders.registers;

import dk.magenta.databroker.core.DataProviderConfiguration;
import dk.magenta.databroker.core.model.DataProviderEntity;
import dk.magenta.databroker.register.Register;
import dk.magenta.databroker.register.RegisterRun;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by lars on 18-12-14.
 */
@Component
public class CprRegister extends Register {

    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private VejRegister vejRegister;

    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private MyndighedsRegister myndighedsRegister;

    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private LokalitetsRegister lokalitetsRegister;

    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private PostnummerRegister postnummerRegister;

    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private BynavnRegister bynavnRegister;

    public CprRegister() {
    }

    @Override
    protected void saveRunToDatabase(RegisterRun run, DataProviderEntity dataProviderEntity) {
        // Do nothing
    }

    @Transactional
    public void pull(boolean forceFetch, boolean forceParse, DataProviderEntity dataProviderEntity) {
        this.myndighedsRegister.pull(forceFetch, forceParse, dataProviderEntity);
        this.vejRegister.pull(forceFetch, forceParse, dataProviderEntity);
        this.lokalitetsRegister.pull(forceFetch, forceParse, dataProviderEntity);
        this.postnummerRegister.pull(forceFetch, forceParse, dataProviderEntity);
        this.bynavnRegister.pull(forceFetch, forceParse, dataProviderEntity);
    }


    @Transactional
    @Override
    public void handlePush(DataProviderEntity dataProviderEntity, HttpServletRequest request) {
        this.myndighedsRegister.handlePush(dataProviderEntity, request);
        this.vejRegister.handlePush(dataProviderEntity, request);
        this.lokalitetsRegister.handlePush(dataProviderEntity, request);
        this.postnummerRegister.handlePush(dataProviderEntity, request);
        this.bynavnRegister.handlePush(dataProviderEntity, request);
    }

    //------------------------------------------------------------------------------------------------------------------

    public String getTemplatePath() {
        return "/fragments/CprRegisterForm.txt";
    }

    @Override
    public DataProviderConfiguration getDefaultConfiguration() {
        JSONObject config = new JSONObject();
        config.put("myndighedSourceType","url");
        config.put("myndighedSourceUrl","https://cpr.dk/media/219468/a370716.txt");
        config.put("lokalitetSourceType","url");
        config.put("lokalitetSourceUrl","https://cpr.dk/media/152108/a370714.txt");
        config.put("vejSourceType","url");
        config.put("vejSourceUrl","https://cpr.dk/media/152096/vejregister_hele_landet_pr_150101.zip");
        config.put("postnummerSourceType","url");
        config.put("postnummerSourceUrl","https://cpr.dk/media/152114/a370712.txt");
        config.put("bynavnSourceType","url");
        config.put("bynavnSourceUrl","https://cpr.dk/media/152120/a370713.txt");
        return new DataProviderConfiguration(config);
    }


    public boolean wantUpload(DataProviderConfiguration configuration) {
        String[] typeFields = new String[]{ "myndighedSourceType", "lokalitetSourceType", "vejSourceType", "postnummerSourceType", "bynavnSourceType" };
        for (String s : typeFields) {
            List<String> sourceType = configuration.get(s);
            if (sourceType != null && sourceType.contains("upload")) {
                return true;
            };
        }
        return false;
    }
}
