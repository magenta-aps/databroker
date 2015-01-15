package dk.magenta.databroker.cprvejregister.dataproviders.registers;

import dk.magenta.databroker.core.DataProviderConfiguration;
import dk.magenta.databroker.core.model.DataProviderEntity;
import dk.magenta.databroker.register.Register;
import dk.magenta.databroker.register.RegisterRun;
import dk.magenta.databroker.register.objectcontainers.Pair;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lars on 18-12-14.
 */
@Component
public class CprRegister extends Register {


    private class CprPusher extends Thread {
        private DataProviderEntity dataProviderEntity;
        private List<Pair<CprSubRegister, File>> input;
        boolean deleteFiles;
        public CprPusher(DataProviderEntity dataProviderEntity, List<Pair<CprSubRegister, File>> input, boolean deleteFiles) {
            this.dataProviderEntity = dataProviderEntity;
            this.input = input;
            this.deleteFiles = deleteFiles;
        }

        public void run() {
            for (Pair<CprSubRegister, File> p : this.input) {
                CprSubRegister cprSubRegister = p.getLeft();
                File file = p.getRight();
                if (cprSubRegister != null && file != null) {
                    try {
                        InputStream inputStream = new FileInputStream(file);
                        cprSubRegister.handlePush(true, this.dataProviderEntity, inputStream);
                        inputStream.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (this.deleteFiles) {
                            file.delete();
                        }
                    }
                }
            }
        }
    }

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


    public Thread asyncPush(DataProviderEntity dataProviderEntity, HttpServletRequest request) {
        List<Pair<CprSubRegister, File>> list = new ArrayList<Pair<CprSubRegister, File>>();
        list.add(new Pair<CprSubRegister, File>(this.myndighedsRegister, this.getTempUploadFile(this.myndighedsRegister, request)));
        list.add(new Pair<CprSubRegister, File>(this.vejRegister, this.getTempUploadFile(this.vejRegister, request)));
        list.add(new Pair<CprSubRegister, File>(this.lokalitetsRegister, this.getTempUploadFile(this.lokalitetsRegister, request)));
        list.add(new Pair<CprSubRegister, File>(this.postnummerRegister, this.getTempUploadFile(this.postnummerRegister, request)));
        list.add(new Pair<CprSubRegister, File>(this.bynavnRegister, this.getTempUploadFile(this.bynavnRegister, request)));
        Thread thread = new CprPusher(dataProviderEntity, list, true);
        thread.start();
        return thread;
    }

    private File getTempUploadFile(CprSubRegister subRegister, HttpServletRequest request) {
        File file = null;
        if (subRegister != null && request != null) {
            try {
                InputStream inputStream = subRegister.getUploadStream(request);
                if (inputStream != null) {
                    file = File.createTempFile("CprRegister", null);
                    Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
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
