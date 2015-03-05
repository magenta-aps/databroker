package dk.magenta.databroker.cprvejregister.dataproviders.registers;

import dk.magenta.databroker.core.DataProviderConfiguration;
import dk.magenta.databroker.core.RegistreringInfo;
import dk.magenta.databroker.core.model.DataProviderEntity;
import dk.magenta.databroker.dawa.model.DawaModel;
import dk.magenta.databroker.register.LineRegister;
import dk.magenta.databroker.register.RegisterRun;
import dk.magenta.databroker.util.objectcontainers.Pair;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
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
public class CprRegister extends LineRegister {


    private class CprPusher extends Thread {
        private DataProviderEntity dataProviderEntity;
        private List<Pair<CprSubRegister, File>> input;
        boolean deleteFiles;

        private TransactionTemplate transactionTemplate;

        public CprPusher(DataProviderEntity dataProviderEntity, List<Pair<CprSubRegister, File>> input, boolean deleteFiles, PlatformTransactionManager transactionManager) {
            this.dataProviderEntity = dataProviderEntity;
            this.input = input;
            this.deleteFiles = deleteFiles;
            this.transactionTemplate = new TransactionTemplate(transactionManager);
        }

        public void run() {

            final DataProviderEntity dataProviderEntity = this.dataProviderEntity;
            final List<Pair<CprSubRegister, File>> input = this.input;
            final boolean deleteFiles = this.deleteFiles;
            this.transactionTemplate.execute(new TransactionCallback() {
                // the code in this method executes in a transactional context
                public Object doInTransaction(TransactionStatus status) {
                    for (Pair<CprSubRegister, File> p : input) {
                        CprSubRegister cprSubRegister = p.getLeft();
                        File file = p.getRight();
                        if (cprSubRegister != null && file != null) {
                            try {
                                InputStream inputStream = new FileInputStream(file);
                                cprSubRegister.handlePush(true, dataProviderEntity, inputStream);
                                inputStream.close();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                if (deleteFiles) {
                                    file.delete();
                                }
                            }
                        }
                    }
                    CprRegister.this.dawaModel.flush();
                    return null;
                }
            });

            System.out.println("Push complete");
        }
    }


    private class CprPuller extends Thread {
        private DataProviderEntity dataProviderEntity;
        private List<CprSubRegister> registers;

        private TransactionTemplate transactionTemplate;

        public CprPuller(DataProviderEntity dataProviderEntity, List<CprSubRegister> registers, PlatformTransactionManager transactionManager) {
            this.dataProviderEntity = dataProviderEntity;
            this.registers = registers;
            this.transactionTemplate = new TransactionTemplate(transactionManager);
        }

        public void run() {

            final DataProviderEntity dataProviderEntity = this.dataProviderEntity;
            final List<CprSubRegister> registers = this.registers;

            this.transactionTemplate.execute(new TransactionCallback() {
                // the code in this method executes in a transactional context
                public Object doInTransaction(TransactionStatus status) {
                    for (CprSubRegister subRegister : registers) {
                        subRegister.pull(true, true, dataProviderEntity);
                    }
                    CprRegister.this.dawaModel.flush();
                    CprRegister.this.dawaModel.onTransactionEnd();
                    return null;
                }
            });
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


    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private DawaModel dawaModel;

    private List<CprSubRegister> subRegisters;

    public CprRegister() {
    }

    @PostConstruct
    protected void postConstruct() {
        this.subRegisters = new ArrayList<CprSubRegister>();
        this.subRegisters.add(this.myndighedsRegister);
        this.subRegisters.add(this.vejRegister);
        this.subRegisters.add(this.lokalitetsRegister);
        this.subRegisters.add(this.postnummerRegister);
        this.subRegisters.add(this.bynavnRegister);
        super.postConstruct();
    }

    @Override
    protected void saveRunToDatabase(RegisterRun run, RegistreringInfo registreringInfo) {
        // Do nothing
    }

    @Transactional
    public void pull(boolean forceFetch, boolean forceParse, DataProviderEntity dataProviderEntity) {
        for (CprSubRegister cprSubRegister : this.subRegisters) {
            cprSubRegister.pull(forceFetch, forceParse, dataProviderEntity);
        }
    }


    @Transactional
    @Override
    public void handlePush(DataProviderEntity dataProviderEntity, HttpServletRequest request) {
        for (CprSubRegister cprSubRegister : this.subRegisters) {
            cprSubRegister.handlePush(dataProviderEntity, request);
        }
    }


    public Thread asyncPush(DataProviderEntity dataProviderEntity, HttpServletRequest request, PlatformTransactionManager transactionManager) {
        List<Pair<CprSubRegister, File>> list = new ArrayList<Pair<CprSubRegister, File>>();
        for (CprSubRegister cprSubRegister : this.subRegisters) {
            list.add(new Pair<CprSubRegister, File>(cprSubRegister, this.getTempUploadFile(cprSubRegister, request)));
        }
        Thread thread = new CprPusher(dataProviderEntity, list, true, transactionManager);
        thread.start();
        return thread;
    }

    public Thread asyncPull(DataProviderEntity dataProviderEntity, PlatformTransactionManager transactionManager, boolean runNow) {
        Thread thread = new CprPuller(dataProviderEntity, this.subRegisters, transactionManager);
        if (runNow) {
            thread.start();
        }
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

    @Override
    public String getTemplatePath() {
        return "CprRegisterForm";
    }

    @Override
    public DataProviderConfiguration getDefaultConfiguration() {
        JSONObject config = new JSONObject();
        config.put("sourceType", "url");
        config.put("cron", "0 0 0 1 * *");
        for (CprSubRegister subRegister : this.subRegisters) {
            config = mergeJSONObjects(config, subRegister.getDefaultConfiguration().toJSON());
        }
        return new DataProviderConfiguration(config);
    }

    @Override
    public boolean wantUpload(DataProviderConfiguration configuration) {
        List<String> sourceType = configuration.get("sourceType");
        return sourceType != null && sourceType.contains("upload");
    }

    @Override
    public List<String> getUploadFields() {
        ArrayList<String> list = new ArrayList<String>();
        for (CprSubRegister subRegister : this.subRegisters) {
            list.add(subRegister.getUploadPartName());
        }
        return list;
    }

    @Override
    public boolean wantCronUpdate(DataProviderConfiguration oldConfiguration, DataProviderConfiguration newConfiguration) {
        String cronField = "cron";
        List<String> oldCronValue = oldConfiguration.get(cronField);
        List<String> newCronValue = newConfiguration.get(cronField);
        if (oldCronValue == null && newCronValue != null) {
            return true;
        }
        if (oldCronValue != null && !oldCronValue.equals(newCronValue)) {
            return true;
        }
        return false;
    }

    @Override
    public String getCronExpression(DataProviderConfiguration configuration) {
        List<String> value = configuration.get("cron");
        if (value != null && value.size() > 0) {
            return value.get(0);
        }
        return null;
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

    @Override
    public boolean canPull(DataProviderConfiguration configuration) {
        for (CprSubRegister subRegister : this.subRegisters) {
            if (subRegister.canPull(configuration)) {
                return true;
            }
        }
        return false;
    }

    protected RegisterRun createRun(){
        return new RegisterRun();
    }
}
