package dk.magenta.databroker.register;

import dk.magenta.databroker.core.DataProvider;
import dk.magenta.databroker.core.NamedInputStream;
import dk.magenta.databroker.core.RegistreringInfo;
import dk.magenta.databroker.core.model.DataProviderEntity;
import dk.magenta.databroker.core.model.DataProviderStorageEntity;
import dk.magenta.databroker.core.model.DataProviderStorageRepository;
import dk.magenta.databroker.core.model.oio.RegistreringRepository;
import dk.magenta.databroker.correction.CorrectionCollectionEntity;
import dk.magenta.databroker.register.records.Record;
import dk.magenta.databroker.util.Util;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
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
    protected Logger log = Logger.getLogger(Register.class);

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

    protected RegistreringInfo registreringInfo;

    protected void clearRegistreringEntities() {
        this.registreringInfo = null;
    }



    protected abstract void saveRunToDatabase(RegisterRun run, RegistreringInfo registreringInfo);

    public void pull(DataProviderEntity dataProviderEntity) {
        this.pull(false, false, dataProviderEntity);
    }

    public void pull(boolean forceFetch, boolean forceParse, DataProviderEntity dataProviderEntity) {
        this.clearRegistreringEntities();
        this.log.info(this.getClass().getSimpleName() + " pulling");
        try {
            InputStream input = null;
            boolean fromCache = false;
            String checksum = null;
            File cacheFile = this.getCacheFile(false);

            if (!forceFetch) {
                // See if we can obtain the data from cache
                if (cacheFile != null && cacheFile.canRead()) {
                    this.log.info("Loading data from cache file " + cacheFile.getAbsolutePath());
                    input = this.readFile(cacheFile);
                    fromCache = true;
                }
            }

            // Try fetching from local resource
            if (input == null && this.getRecordResource() != null) {
                this.log.info("Loading data from included resource " + this.getRecordResource().toString());
                input = this.readResource(this.getRecordResource());
            }
            if (input != null) {
                // Now that we have an input stream, process it
                this.handleInput(input, dataProviderEntity, fromCache, forceParse);
            } else {
                this.log.error("Register " + this.getClass().getSimpleName() + " cannot pull; no input data found");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        this.log.info(this.getClass().getSimpleName() + " pull complete");
        System.gc();
    }


    public void handlePush(DataProviderEntity dataProviderEntity, HttpServletRequest request) {
        InputStream input = this.getUploadStream(request);
        if (input != null) {
            this.handlePush(true, dataProviderEntity, input);
        } else {
            this.log.error("Register " + this.getClass().getSimpleName() + " cannot receive push; no input data found");
        }
    }

    @Transactional
    @Override
    public void handlePush(boolean forceParse, DataProviderEntity dataProviderEntity, InputStream input) {
        this.clearRegistreringEntities();
        this.log.info(this.getClass().getSimpleName() + " receiving push");
        if (input != null) {
            this.handleInput(input, dataProviderEntity, false, forceParse);
            this.log.info(this.getClass().getSimpleName() + " push complete");
        } else {
            this.log.error("Register " + this.getClass().getSimpleName() + " cannot receive push; no input data found");
        }
        System.gc();
    }




    protected void importData(RegistreringInfo registreringInfo) {
        RegisterRun run = this.parse(registreringInfo.getInputStream());


        CorrectionCollectionEntity correctionCollectionEntity = registreringInfo.getDataProviderEntity().getCorrections();
        if (correctionCollectionEntity != null) {
            correctionCollectionEntity.correctRecords(run);
        } else {
            System.out.println("correctionCollectionEntity is null for "+this);
        }

        this.saveRunToDatabase(run, registreringInfo);
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


    private NamedInputStream readCache(File cacheFile) throws IOException {
        NamedInputStream input = new NamedInputStream(Files.newInputStream(cacheFile.toPath()), cacheFile.getName());
        input.setKnownSize(cacheFile.length());
        return input;
    }

    private void handleInput(InputStream input, DataProviderEntity dataProviderEntity, boolean alreadyCached, boolean forceParse) {
        if (input != null) {
            // If cachefile doesn't exist, create it
            File cacheFile = null;
            String checksum;
            NamedInputStream data;
            try {
                if (alreadyCached) {
                    cacheFile = this.getCacheFile(false);
                } else {
                    // Pipe our data into the file
                    cacheFile = this.getCacheFile(true);
                    if (cacheFile.canWrite() && cacheFile.canRead()) {
                        this.log.info("Writing to cache " + cacheFile.getAbsolutePath());
                        Files.copy(input, cacheFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        input.close();
                    } else {
                        this.log.error("Fatal: Unable to access cache file '" + cacheFile.getAbsolutePath() + "' for reading or writing. Cannot continue import");
                        return;
                    }
                }

                if (cacheFile != null) {

                    checksum = DigestUtils.md5Hex(Files.newInputStream(cacheFile.toPath()));
                    data = this.readCache(cacheFile);

                    // Determining whether it's a zip stream, or in general just reading from the stream
                    // ruins it for further reading, so each time we need to reload it from the cache

                    if (this.isZipStream(data)) {
                        data.close();
                        // Re-read from cache
                        data = this.readCache(cacheFile);
                        // We have a zipped file, get the contained data
                        data = this.unzip(data);
                        if (data == null) {
                            data = this.readCache(cacheFile);
                        }
                    } else {
                        data.close();
                        data = this.readCache(cacheFile);
                    }

                    final long dataSize = data.getKnownSize();

                    boolean checksumMatch = compareChecksum(checksum);

                    if (forceParse || checksum == null || !checksumMatch) {
                        if (forceParse) {
                            this.log.info("Parse forced; parsing new data into database");
                        } else {
                            this.log.info("Checksum mismatch; parsing new data into database");
                        }
                        RegistreringInfo registreringInfo = new RegistreringInfo(this.registreringRepository, dataProviderEntity, data);
                        this.importData(registreringInfo);
                    } else {
                        this.log.info("Checksum match; no need to update database");
                    }
                } else {
                    this.log.error("No cached data exists. Cannot import");
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        } else {
            this.log.error("No input data received. Cannot import");
        }
    }





    public InputStream getUploadStream(HttpServletRequest request) {
        InputStream input = null;
        try {
            Part uploadPart = request.getPart(this.getUploadPartName());
            if (uploadPart != null && uploadPart.getSize() > 0) {
                this.log.info("Receiving " + uploadPart.getSize() + " byte upload from part " + this.getUploadPartName());
                input = uploadPart.getInputStream();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        }
        return input;
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
            this.log.info("Folder " + dir.getAbsolutePath() + " doesn't exist, creating it");
            dir.mkdirs();
            if (!dir.exists()) {
                throw new IOException("Failed to create folder '"+dir.getAbsolutePath()+"'. Possible permissions problem?");
            }
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
        if (forceCreateNew) {
            String filename = dateFormat.format(new Date()) + ".txt";
            File file = new File(dir, filename);
            this.log.info("Creating new cache file " + file.getAbsolutePath());
            file.createNewFile();
            return file;
        } else {
            File[] files = dir.listFiles();
            File latest = null;
            Date latestDate = null;
            if (files != null) {
                for (File file : files) {
                    try {
                        Date fileDate = dateFormat.parse(file.getName().replaceAll("\\.txt$", ""));
                        if (latestDate == null || fileDate.compareTo(latestDate) > 0) {
                            latestDate = fileDate;
                            latest = file;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
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
        return Util.getTime();
    }
    protected long toc(long ticTime) {
        return Util.getTime() - ticTime;
    }
    protected long toc() {
        return Util.getTime() - this.ticTime;
    }

}
