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
        JSONObject myndighedConfig = this.myndighedsRegister.getDefaultConfiguration().toJSON();
        config = mergeJSONObjects(config, myndighedConfig);
        JSONObject lokalitetConfig = this.lokalitetsRegister.getDefaultConfiguration().toJSON();
        config = mergeJSONObjects(config, lokalitetConfig);
        JSONObject vejConfig = this.vejRegister.getDefaultConfiguration().toJSON();
        config = mergeJSONObjects(config, vejConfig);
        JSONObject postnummerConfig = this.postnummerRegister.getDefaultConfiguration().toJSON();
        config = mergeJSONObjects(config, postnummerConfig);
        JSONObject bynavnConfig = this.bynavnRegister.getDefaultConfiguration().toJSON();
        config = mergeJSONObjects(config, bynavnConfig);
        return new DataProviderConfiguration(config);
    }


    public boolean wantUpload(DataProviderConfiguration configuration) {
        String[] typeFields = new String[]{
                this.myndighedsRegister.getSourceTypeFieldName(),
                this.lokalitetsRegister.getSourceTypeFieldName(),
                this.vejRegister.getSourceTypeFieldName(),
                this.postnummerRegister.getSourceTypeFieldName(),
                this.bynavnRegister.getSourceTypeFieldName()
        };
        for (String s : typeFields) {
            List<String> sourceType = configuration.get(s);
            if (sourceType != null && sourceType.contains("upload")) {
                return true;
            };
        }
        return false;
    }


    private static JSONObject mergeJSONObjects(JSONObject obj1, JSONObject obj2) {
        JSONObject obj = new JSONObject();
        for (Object okey : obj1.keySet()) {
            String key = (String) okey;
            obj.put(key, obj1.get(key));
        }
        for (Object okey : obj2.keySet()) {
            String key = (String) okey;
            obj.put(key, obj2.get(key));
        }
        return obj;
    }


    public boolean canPull(DataProviderConfiguration configuration) {
        if (this.myndighedsRegister.canPull(configuration)) {
            return true;
        } else if (this.lokalitetsRegister.canPull(configuration)) {
            return true;
        } else if (this.vejRegister.canPull(configuration)) {
            return true;
        } else if (this.postnummerRegister.canPull(configuration)) {
            return true;
        } else if (this.bynavnRegister.canPull(configuration)) {
            return true;
        } else {
            return false;
        }
    }
}
