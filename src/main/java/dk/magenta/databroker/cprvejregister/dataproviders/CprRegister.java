package dk.magenta.databroker.cprvejregister.dataproviders;




import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;
import dk.magenta.databroker.core.DataProvider;
import dk.magenta.databroker.core.model.DataProviderEntity;
import dk.magenta.databroker.core.model.oio.*;
import dk.magenta.databroker.cprvejregister.dataproviders.records.*;
import dk.magenta.databroker.cprvejregister.model.RepositoryCollection;
import org.hibernate.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.orm.hibernate4.SessionHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by lars on 04-11-14.
 */
public abstract class CprRegister extends DataProvider {

    protected class EntityModificationCounter {

        private int itemsCreated = 0;
        private int itemsUpdated = 0;

        public void countCreatedItem() {
            this.itemsCreated++;
        }

        public void countUpdatedItem() {
            this.itemsUpdated++;
        }

        public void printModifications() {
            if (this.itemsCreated > 0 || this.itemsUpdated > 0) {
                System.out.println("    " + this.itemsCreated + " new entries created\n    " + this.itemsUpdated + " existing entries updated");
            } else {
                System.out.println("    no changes necessary; old dataset matches new dataset");
            }
        }
    }

    public CprRegister(DataProviderEntity dbObject) {
        super(dbObject);
    }

    public CprRegister() {

    }

    public URL getRecordUrl() throws MalformedURLException {
        return null;
    }

    protected String getEncoding() {
        return null;
    }

    protected RegisterRun createRun() {

        return new RegisterRun();
    }


    @Transactional
    public void pull() {
        System.out.println("Pulling...");
        try {
            URL url = this.getRecordUrl();
            if (url != null) {
                System.out.println("Loading data from " + url.toString());
                InputStream input = url.openStream();
                if (url.getFile().endsWith(".zip")) {
                    System.out.println("Passing data through ZIP filter");
                    ZipInputStream zinput = new ZipInputStream(input);
                    ZipEntry entry = zinput.getNextEntry();
                    input = zinput;
                }
                RegisterRun run = this.parse(input);
                this.saveRunToDatabase(run);
            } else {
                System.out.println("No url");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void read(File inputfile, RepositoryCollection repositories) {
        try {
            if (inputfile.canRead()) {
                System.out.println("Loading data from " + inputfile.getAbsolutePath());
                InputStream input = new FileInputStream(inputfile);
                if (inputfile.getAbsolutePath().endsWith(".zip")) {
                    System.out.println("Passing data through ZIP filter");
                    ZipInputStream zinput = new ZipInputStream(input);
                    ZipEntry entry = zinput.getNextEntry();
                    input = zinput;
                }
                RegisterRun run = this.parse(input);
                this.saveRunToDatabase(run, repositories);
            } else {
                System.out.println("Can't read from file "+inputfile.getAbsolutePath());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private RegisterRun parse(InputStream input) {
        try {
            RegisterRun run = this.createRun();
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
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                if (line != null) {
                    line = line.trim();
                    if (line.length() > 3) {
                        Record record = this.parseTrimmedLine(line.substring(0, 3), line);
                        if (record != null) {
                            this.processRecord(record);
                            run.add(record);
                        }
                    }
                }
                i++;
                if (i >= 100000) {
                    j++;
                    System.out.println("    parsed " + (j * i) + " entries");
                    i = 0;
                }
            }
            System.out.println("    parsed " + (j * 100000 + i) + " entries in " + String.format("%.3f", 0.001 * (new Date().getTime() - startTime.getTime())) + " seconds");
            System.out.println("Input parsed, making sense of it");

            //System.out.println(run.toJSON().toString(2));
            System.out.println("Pull complete");
            return run;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Pull failed");
        return null;
    }

    protected Record parseTrimmedLine(String recordType, String line) {
        try {
            if (recordType.equals(Record.RECORDTYPE_START)) {
                return new Start(line);
            }
            if (recordType.equals(Record.RECORDTYPE_SLUT)) {
                return new Slut(line);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void saveRunToDatabase(RegisterRun run) {
        // Override me
    }

    protected void saveRunToDatabase(RegisterRun run, RepositoryCollection repositories) {
        // Override me
    }

    protected void processRecord(Record record) {
        // Override me
    }


    private int inputsProcessed = 0;
    private int inputChunks = 0;
    private long startTime = 0;

    protected void startInputProcessing() {
        this.startTime = new Date().getTime();
    }

    protected void printInputProcessed() {
        this.inputsProcessed++;
        if (this.inputsProcessed >= 1000) {
            this.inputsProcessed = 0;
            this.inputChunks++;
            System.out.println("    " + (this.inputChunks * 1000) + " inputs processed");
        }
    }

    protected void printFinalInputsProcessed() {
        String timeStr = this.startTime != 0 ?
                " in "+String.format("%.3f", 0.001 * (new Date().getTime() - this.startTime))+" seconds" :
                "";
        System.out.println("Processed " + (1000 * this.inputChunks + this.inputsProcessed) + " inputs" + timeStr + ".");
    }

    private long ticTime = 0;
    protected void tic() {
        this.ticTime = new Date().getTime();
    }
    protected long toc() {
        return new Date().getTime() - this.ticTime;
    }




}
