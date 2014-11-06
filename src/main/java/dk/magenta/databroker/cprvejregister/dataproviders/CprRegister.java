package dk.magenta.databroker.cprvejregister.dataproviders;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;
import dk.magenta.databroker.core.DataProvider;
import dk.magenta.databroker.cprvejregister.dataproviders.records.*;
import org.json.JSONArray;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by lars on 04-11-14.
 */
public abstract class CprRegister extends DataProvider {

    public CprRegister(dk.magenta.databroker.core.model.DataProvider dbObject) {
        super(dbObject);
    }

    public URL getRecordUrl() throws MalformedURLException {
        return null;
    }

    public void pull() {
        System.out.println("Pulling...");
        try {
            ArrayList<Record> records = new ArrayList<Record>();
            URL url = this.getRecordUrl();
            if (url != null) {
                System.out.println("We have an URL");
                InputStream input = url.openStream();

                if (url.getFile().endsWith(".zip")) {
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
                } else {
                    encoding = "UTF-8";
                }

                BufferedReader reader = new BufferedReader(new InputStreamReader(inputstream, encoding.toUpperCase()));

                for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                    this.addLine(line, records);
                }

                System.out.println(this.toJSON(records).toString(2));
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

    public JSONArray toJSON(ArrayList<Record> records) {
        JSONArray array = new JSONArray();
        for (Record record : records) {
            array.put(record.toJSON());
        }
        return array;
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

    private void addLine(String line, ArrayList<Record> records) {
        Record record = this.parseLine(line);
        if (record != null) {
            records.add(record);
        }
    }

    private Record parseLine(String line) {
        if (line != null) {
            line = line.trim();
            if (line.length()>3) {
                String recordType = line.substring(0, 3);
                return this.parseTrimmedLine(recordType, line);
            }
        }
        return null;
    }

}
