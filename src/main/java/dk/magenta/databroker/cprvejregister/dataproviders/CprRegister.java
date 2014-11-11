package dk.magenta.databroker.cprvejregister.dataproviders;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;
import dk.magenta.databroker.core.DataProvider;
import dk.magenta.databroker.core.model.DataProviderEntity;
import dk.magenta.databroker.cprvejregister.dataproviders.records.*;
import org.json.JSONArray;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by lars on 04-11-14.
 */
public abstract class CprRegister extends DataProvider {

    public CprRegister(DataProviderEntity dbObject) {
        super(dbObject);
    }

    public URL getRecordUrl() throws MalformedURLException {
        return null;
    }

    protected RegisterRun createRun() {
        return new RegisterRun();
    }

    public void pull() {
        System.out.println("Pulling...");
        try {
            RegisterRun run = this.createRun();
            URL url = this.getRecordUrl();
            if (url != null) {
                System.out.println("Loading data from "+url.toString());
                InputStream input = url.openStream();

                if (url.getFile().endsWith(".zip")) {
                    System.out.println("Passing data through ZIP filter");
                    ZipInputStream zinput = new ZipInputStream(input);
                    ZipEntry entry = zinput.getNextEntry();
                    input = zinput;
                }

                BufferedInputStream inputstream = new BufferedInputStream(input);

                String encoding;
                // Try to guess the encoding based on the stream contents
                CharsetDetector detector = new CharsetDetector();
                detector.setText(inputstream);
                CharsetMatch match = detector.detect();
                if (match != null) {
                    encoding = match.getName();
                    System.out.println("Interpreting data as "+encoding);
                } else {
                    encoding = "UTF-8";
                    System.out.println("Falling back to default encoding "+encoding);
                }

                BufferedReader reader = new BufferedReader(new InputStreamReader(inputstream, encoding.toUpperCase()));

                System.out.println("Reading data");
                int i=0, j=0;
                for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                    if (line != null) {
                        line = line.trim();
                        if (line.length()>3) {
                            Record record = this.parseTrimmedLine(line.substring(0, 3), line);
                            if (record != null) {
                                this.processRecord(record);
                                run.saveRecord(record);
                            }
                        }
                    }
                    i++;
                    if (i>=100000) {
                        j++;
                        System.out.println("    parsed " + (j * i) + " entries");
                        i=0;
                    }
                }
                System.out.println("    parsed "+(j*100000 + i)+" entries");
                System.out.println("Input parsed, making sense of it...");

                //System.out.println(run.toJSON().toString(2));
                this.saveRunToDatabase(run);
            }
            else {
                System.out.println("No url");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    protected void processRecord(Record record) {
        // Override me
    }

}
