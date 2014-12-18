package dk.magenta.databroker.register;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;
import dk.magenta.databroker.core.DataProvider;
import dk.magenta.databroker.core.model.DataProviderEntity;
import dk.magenta.databroker.core.model.DataProviderStorageEntity;
import dk.magenta.databroker.core.model.DataProviderStorageRepository;
import dk.magenta.databroker.core.model.oio.RegistreringEntity;
import dk.magenta.databroker.core.model.oio.RegistreringRepository;
import dk.magenta.databroker.register.RegisterRun;
import dk.magenta.databroker.register.records.Record;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by lars on 15-12-14.
 */
public abstract class Register extends DataProvider {


    private static final File cacheDir = new File("cache/");

    @Autowired
    private DataProviderStorageRepository dataProviderStorageRepository;



    private DataProviderStorageEntity storageEntity;

    public Register(DataProviderEntity dbObject) {
        super(dbObject);
    }

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

        DataProviderEntity provider = new DataProviderEntity();
        provider.setUuid(UUID.randomUUID().toString());
        this.setDataProviderEntity(provider);

        this.createRegistreringEntities();
    }

    public URL getRecordUrl() throws MalformedURLException {
        return null;
    }

    public File getRecordFile() {
        return null;
    }

    protected String getEncoding() {
        return null;
    }

    protected RegisterRun createRun() {
        return new RegisterRun();
    }




    /*
    * Registration
    * */
    @Autowired
    private RegistreringRepository registreringRepository;

    private RegistreringEntity createRegistrering;
    private RegistreringEntity updateRegistrering;

    protected void createRegistreringEntities() {
        this.createRegistrering = registreringRepository.createNew(this);
        this.updateRegistrering = registreringRepository.createUpdate(this);
    }
    protected RegistreringEntity getCreateRegistrering() {
        return this.createRegistrering;
    }
    protected RegistreringEntity getUpdateRegistrering() {
        return this.updateRegistrering;
    }




    public void pull() {
        this.pull(false, false);
    }

    public void pull(boolean forceFetch, boolean forceParse) {
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

            if (input == null && this.getRecordFile() != null) {
                System.out.println("Loading data from " + this.getRecordFile().toString());
                input = this.readFile(this.getRecordFile());
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
                if (checksum != null) {
                    JSONObject storageData = new JSONObject();
                    String dataString = this.storageEntity.getData();
                    if (dataString != null) {
                        try {
                            storageData = new JSONObject(dataString);
                        } catch (JSONException e) {
                        }
                    }

                    if (forceParse || !checksum.equals(storageData.optString("checksum"))) {
                        System.out.println("Checksum mismatch; parsing new data into database");
                        RegisterRun run = this.parse(input);
                        this.saveRunToDatabase(run);
                        storageData.put("checksum", checksum);
                        this.storageEntity.setData(storageData.toString());
                        this.dataProviderStorageRepository.saveAndFlush(this.storageEntity);
                    } else {
                        System.out.println("Checksum match; no need to update database");
                    }
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


    private RegisterRun parse(InputStream input) {
        try {
            BufferedInputStream inputstream = new BufferedInputStream(input);

            String encoding = this.getEncoding();
            if (encoding != null) {
                System.out.println("Using explicit encoding " + encoding);
            } else {
                // Try to guess the encoding based on the stream contents
                CharsetDetector detector = new CharsetDetector();
                detector.setText(inputstream);
                CharsetMatch match = detector.detect();
                if (match != null) {
                    encoding = match.getName();
                    System.out.println("Interpreting data as " + encoding);
                } else {
                    encoding = "UTF-8";
                    System.out.println("Falling back to default encoding " + encoding);
                }
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputstream, encoding.toUpperCase()));

            System.out.println("Reading data");
            Date startTime = new Date();
            int i = 0, j = 0;

            RegisterRun run = this.createRun();

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                if (line != null) {
                    line = line.trim();
                    if (line.length() > 3) {
                        try {
                            Record record = this.parseTrimmedLine(line);
                            if (record != null) {
                                //this.processRecord(record);
                                run.add(record);
                            }
                        } catch (OutOfMemoryError e) {
                            System.out.println(line);
                        }
                    }
                }
                i++;
                if (i >= 100000) {
                    j++;
                    tic();
                    System.gc();

                    System.out.println("    parsed " + (j * i) + " entries (cleanup took "+toc()+" ms)");

                    i = 0;

                    //Runtime runtime = Runtime.getRuntime();
                    //NumberFormat format = NumberFormat.getInstance();
                    //System.out.println("free memory: " + format.format(runtime.freeMemory() / 1024) + " of " + format.format(runtime.maxMemory() / 1024));

                }
            }

            System.out.println("    parsed " + (j * 100000 + i) + " entries in " + String.format("%.3f", 0.001 * (new Date().getTime() - startTime.getTime())) + " seconds");

            //System.out.println(run.toFullJSON().toString(2));
            System.out.println("Pull complete ("+run.size()+" entries)");
            return run;


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Pull failed");
        return null;
    }

    protected Record parseTrimmedLine(String line) {
        return null;
    }

    protected void saveRunToDatabase(RegisterRun run) {
        // Override me
    }

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
