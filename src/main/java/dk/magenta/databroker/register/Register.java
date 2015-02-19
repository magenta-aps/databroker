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
import org.apache.commons.io.input.CountingInputStream;
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
            File cacheFile = this.getCacheFile(false);

            if (!forceFetch) {
                // See if we can obtain the data from cache
                if (cacheFile != null && cacheFile.canRead()) {
                    input = this.readFile(cacheFile);
                    checksum = DigestUtils.md5Hex(input);
                    input.close();
                    System.out.println("Loading data from cache file " + cacheFile.getAbsolutePath());
                    input = this.readFile(cacheFile);
                    fromCache = true;
                }
            }

            // Try fetching from URL
            if (input == null && this.getRecordUrl() != null) {
                System.out.println("Loading data from " + this.getRecordUrl().toString());
                input = this.readUrl(this.getRecordUrl());
            }

            // Try fetching from local resource
            if (input == null && this.getRecordResource() != null) {
                System.out.println("Loading data from " + this.getRecordResource().toString());
                input = this.readResource(this.getRecordResource());
            }
            if (input != null) {
                // Now that we have an input stream, process it
                this.handleInput(input, dataProviderEntity, fromCache, forceParse);
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


    public void handlePush(boolean forceParse, DataProviderEntity dataProviderEntity, InputStream input) {
        this.clearRegistreringEntities();
        System.out.println("-----------------------------");
        System.out.println(this.getClass().getSimpleName() + " receiving push...");
        if (input != null) {
            this.handleInput(input, dataProviderEntity, false, forceParse);
        } else {
            System.out.println("No input data");
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
                cacheFile = this.getCacheFile(true);

                if (cacheFile != null) {

                    if (!alreadyCached) {
                        // Pipe our data into the file
                        if (cacheFile.canWrite() && cacheFile.canRead()) {
                            Files.copy(input, cacheFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                            input.close();
                        } else {
                            System.err.println("Fatal: Unable to access cache file '" + cacheFile.getAbsolutePath() + "' for reading or writing");
                            return;
                        }
                    }

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
                    System.out.println("Data size: "+dataSize);

                    boolean checksumMatch = compareChecksum(checksum);

                    if (forceParse || checksum == null || !checksumMatch) {
                        if (forceParse) {
                            System.out.println("Parse forced; parsing new data into database");
                        } else {
                            System.out.println("Checksum mismatch; parsing new data into database");
                        }
                        final CountingInputStream countingInputStream = new CountingInputStream(data);
                        Thread t = new Thread(){
                            public void run() {
                                try {
                                    while (countingInputStream.available() > 0) {
                                        long bytesRead = countingInputStream.getByteCount();
                                        System.err.println("Processed "+bytesRead+" bytes ("+ String.format("%.2f", 100.0 * (double)bytesRead / (double) dataSize)+"%)");
                                        try {
                                            Thread.sleep(10000L);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        t.start();

                        this.importData(countingInputStream, dataProviderEntity);

                        //this.importData(input, dataProviderEntity);
                    } else {
                        System.out.println("Checksum match; no need to update database");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        } else {
            System.out.println("No input data");
        }
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
            System.out.println("Folder "+dir.getAbsolutePath()+" doesn't exist, creating it");
            dir.mkdirs();
            if (!dir.exists()) {
                throw new IOException("Failed to create folder '"+dir.getAbsolutePath()+"'. Possible permissions problem?");
            }
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
        return new Date().getTime();
    }
    protected long toc(long ticTime) {
        return new Date().getTime() - ticTime;
    }
    protected long toc() {
        return new Date().getTime() - this.ticTime;
    }


}
