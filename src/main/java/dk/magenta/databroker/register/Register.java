package dk.magenta.databroker.register;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;
import dk.magenta.databroker.core.DataProvider;
import dk.magenta.databroker.core.model.DataProviderEntity;
import dk.magenta.databroker.core.model.DataProviderStorageEntity;
import dk.magenta.databroker.core.model.DataProviderStorageRepository;
import dk.magenta.databroker.core.model.oio.RegistreringEntity;
import dk.magenta.databroker.core.model.oio.RegistreringRepository;
import dk.magenta.databroker.register.records.Record;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lars on 15-12-14.
 */
public abstract class Register extends DataProvider {


    protected static final File cacheDir = new File("cache/");

    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private DataProviderStorageRepository dataProviderStorageRepository;

    protected DataProviderStorageRepository getDataProviderStorageRepository() {
        return this.dataProviderStorageRepository;
    }


    protected DataProviderStorageEntity storageEntity;

    public Register() {
    }

    @PostConstruct
    public void PostConstructRegister() {
        if (this.dataProviderStorageRepository != null) {
            DataProviderStorageEntity storageEntity = this.dataProviderStorageRepository.getByOwningClass(this.getClass().getName());
            if (storageEntity == null) {
                storageEntity = new DataProviderStorageEntity();
                storageEntity.setOwningClass(this.getClass().getName());
                this.dataProviderStorageRepository.save(storageEntity);
            }
            this.storageEntity = storageEntity;
        }
    }

    public URL getRecordUrl() throws MalformedURLException {
        return null;
    }

    public Resource getRecordResource() {
        return null;
    }

    protected String getEncoding() {
        return null;
    }

    /*
    * Registration
    * */
    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    protected RegistreringRepository registreringRepository;

    protected RegistreringEntity createRegistrering;
    protected RegistreringEntity updateRegistrering;

    protected void clearRegistreringEntities() {
        createRegistrering = null;
        updateRegistrering = null;
    }

    protected RegistreringEntity getCreateRegistrering(DataProviderEntity entity) {
        if(this.createRegistrering == null) {
            this.createRegistrering = registreringRepository.createNew(entity);
        }
        return this.createRegistrering;
    }
    protected RegistreringEntity getUpdateRegistrering(DataProviderEntity entity) {
        if(this.updateRegistrering == null) {
            this.updateRegistrering = registreringRepository.createUpdate(entity);
        }
        return this.updateRegistrering;
    }

    protected abstract void saveRunToDatabase(RegisterRun run, DataProviderEntity dataProviderEntity);

    public void pull(DataProviderEntity dataProviderEntity) {
        this.pull(false, false, dataProviderEntity);
    }

    public void pull(boolean forceFetch, boolean forceParse, DataProviderEntity dataProviderEntity) {
        this.clearRegistreringEntities();
        System.out.println("-----------------------------");
        System.out.println(this.getClass().getSimpleName() + " pulling...");
        try {
            InputStream input = null;
            boolean fromCache = false;
            String checksum = null;

            if (!forceFetch) {
                File cacheFile = this.getCacheFile(false);
                if (cacheFile != null && cacheFile.canRead()) {
                    input = this.readFile(cacheFile);
                    checksum = DigestUtils.md5Hex(input);
                    input.close();
                    System.out.println("Loading data from " + cacheFile.getAbsolutePath());
                    input = this.readFile(cacheFile);
                    fromCache = true;
                }
            }

            if (input == null && this.getRecordUrl() != null) {
                System.out.println("Loading data from " + this.getRecordUrl().toString());
                input = this.readUrl(this.getRecordUrl());
            }

            if (input == null && this.getRecordResource() != null) {
                System.out.println("Loading data from " + this.getRecordResource().toString());
                input = this.readResource(this.getRecordResource());
            }

            if (input != null) {
                if (!fromCache) {
                    File cacheFile = this.getCacheFile(true);
                    if (cacheFile != null && cacheFile.canWrite() && cacheFile.canRead()) {
                        Files.copy(input, cacheFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        input.close();
                        checksum = DigestUtils.md5Hex(Files.newInputStream(cacheFile.toPath()));
                        input = Files.newInputStream(cacheFile.toPath());
                    }
                }

                boolean checksumMatch = compareChecksum(checksum);

                if (forceParse || checksum == null || !checksumMatch) {
                    System.out.println("Checksum mismatch; parsing new data into database");
                    this.importData(input, dataProviderEntity);
                } else {
                    System.out.println("Checksum match; no need to update database");
                }

            } else {
                System.out.println("No input data");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(this.getClass().getSimpleName() + " done!");
        System.gc();
    }

    protected void importData(InputStream input, DataProviderEntity dataProviderEntity) {
        RegisterRun run = this.parse(input);
        this.saveRunToDatabase(run, dataProviderEntity);
    }


    private boolean compareChecksum(String checksum) {
        boolean checksumMatch = false;
        if (checksum != null) {
            String dataString = this.storageEntity.getData();
            if (dataString != null) {
                try {
                    JSONObject storageData = new JSONObject(dataString);
                    if (checksum.equals(storageData.optString("checksum"))) {
                        checksumMatch = true;
                    } else {
                        storageData.put("checksum", checksum);
                        this.storageEntity.setData(storageData.toString());
                        this.dataProviderStorageRepository.saveAndFlush(this.storageEntity);
                    }
                } catch (JSONException e) {
                }
            }
        }
        return checksumMatch;
    }


    public void handlePush(boolean forceParse, DataProviderEntity dataProviderEntity, InputStream input) {
        this.clearRegistreringEntities();
        System.out.println("-----------------------------");
        System.out.println(this.getClass().getSimpleName() + " receiving push...");
        try {
            String checksum = null;

            if (input != null) {
                File cacheFile = this.getCacheFile(true);
                if (cacheFile != null && cacheFile.canWrite() && cacheFile.canRead()) {
                    Files.copy(input, cacheFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    input.close();
                    checksum = DigestUtils.md5Hex(Files.newInputStream(cacheFile.toPath()));
                    input = Files.newInputStream(cacheFile.toPath());
                }

                boolean checksumMatch = compareChecksum(checksum);

                if (forceParse || checksum == null || !checksumMatch) {
                    System.out.println("Checksum mismatch; parsing new data into database");
                    RegisterRun run = this.parse(input);
                    this.saveRunToDatabase(run, dataProviderEntity);
                } else {
                    System.out.println("Checksum match; no need to update database");
                }



            } else {
                System.out.println("No input data");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(this.getClass().getSimpleName() + " done!");
        System.gc();
    }



    public InputStream getUploadStream(HttpServletRequest request) {
        InputStream input = null;
        try {

            Part uploadPart = request.getPart(this.getUploadPartName());
            if (uploadPart != null && uploadPart.getSize() > 0) {
                System.out.println("receiving "+uploadPart.getSize()+" byte upload from "+this.getUploadPartName());
                input = uploadPart.getInputStream();
            }
        } catch (IOException e) {
        } catch (ServletException e) {
        }
        return input;
    }


    @Transactional
    @Override
    public void handlePush(DataProviderEntity dataProviderEntity, HttpServletRequest request) {
        InputStream input = this.getUploadStream(request);
        if (input != null) {
            this.handlePush(true, dataProviderEntity, input);
        }
    }

    public String getUploadPartName() {
        return "sourceUpload";
    }

    protected abstract RegisterRun parse(InputStream input);

    protected void processRecord(Record record) {
        // Override me
    }

    // obtain a file to cache input data to/from
    protected File getCacheFile(boolean forceCreateNew) throws IOException {
        File dir = new File(cacheDir, this.getClass().getSimpleName());
        if (!dir.exists()) {
            dir.mkdirs();
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
        if (forceCreateNew) {
            String filename = dateFormat.format(new Date()) + ".txt";
            File file = new File(dir, filename);
            file.createNewFile();
            return file;
        } else {
            File[] files = dir.listFiles();
            File latest = null;
            Date latestDate = null;
            for (File file : files) {
                try {
                    Date fileDate = dateFormat.parse(file.getName().replaceAll("\\.txt$",""));
                    if (latestDate == null || fileDate.compareTo(latestDate) > 0) {
                        latestDate = fileDate;
                        latest = file;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            return latest;

        }
    }

    //------------------------------------------------------------------------------------------------------------------


    private long ticTime = 0;
    protected long tic() {
        this.ticTime = this.indepTic();
        return this.ticTime;
    }
    protected long indepTic() {
        return new Date().getTime();
    }
    protected long toc(long ticTime) {
        return new Date().getTime() - ticTime;
    }
    protected long toc() {
        return new Date().getTime() - this.ticTime;
    }


}
