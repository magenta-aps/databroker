package dk.magenta.databroker.cprvejregister.dataproviders;

import dk.magenta.databroker.core.model.DataProviderRepository;
import dk.magenta.databroker.cprvejregister.dataproviders.records.*;
import dk.magenta.databroker.core.DataProvider;
import dk.magenta.databroker.core.model.UpdateLog;
import org.json.JSONArray;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.text.ParseException;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;

/**
 * Created by lars on 04-11-14.
 */
public class Vejregister extends DataProvider {


    private ArrayList<Record> records;

    public Vejregister(dk.magenta.databroker.core.model.DataProvider dbObject) {
        super(dbObject);
    }

    public void pull() {
        try {
            //URL url = new URL("file:///home/lars/Projekt/databroker/src/test/resources/vejregister/a370727.txt");
            //URL url = new URL("https://cpr.dk/media/221860/a370727.txt");
            URL url = new URL("https://cpr.dk/media/152096/vejregister_hele_landet_pr_141101.zip");
            InputStream input = url.openStream();
            if (url.getFile().endsWith(".zip")) {
                ZipInputStream zinput = new ZipInputStream(input);
                ZipEntry entry = zinput.getNextEntry();
                input = zinput;
            }
            BufferedInputStream inputstream = new BufferedInputStream(input);

            String encoding = null;
            if (encoding == null) {
                // Try to guess the encoding based on the stream contents
                CharsetDetector detector = new CharsetDetector();
                detector.setText(inputstream);
                CharsetMatch match = detector.detect();
                if (match != null) {
                    encoding = match.getName();
                } else {
                    encoding = "UTF-8";
                }
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputstream, encoding.toUpperCase()));

            String line = null;
            ArrayList<Record> records = new ArrayList<Record>();
            do {
                line = reader.readLine();
                this.addLine(line, records);
            } while (line != null);
            this.toJSON(records);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*public Vejregister(String[] lines) {
        this.records = new ArrayList<Record>();
        for (String line : lines) {
            this.addLine(line);
        }
    }

    public Vejregister(String input) {
        this(input.split("\\n"));
    }*/

    /*public Vejregister(InputStream input, String encoding) throws IOException {
        this.records = new ArrayList<Record>();
        BufferedInputStream inputstream = new BufferedInputStream(input);

        if (encoding == null) {
            // Try to guess the encoding based on the stream contents
            CharsetDetector detector = new CharsetDetector();
            detector.setText(inputstream);
            CharsetMatch match = detector.detect();
            if (match != null) {
                encoding = match.getName();
            } else {
                encoding = "UTF-8";
            }
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputstream, encoding.toUpperCase()));

        String line = null;
        do {
            line = reader.readLine();
            this.addLine(line);
        } while (line != null);
    }*/
/*
    public Vejregister(File input) throws IOException {
        this(input, null);
    }
    public Vejregister(File input, String encoding) throws IOException {
        this(new FileInputStream(input), encoding);
    }*/

    public JSONArray toJSON() {
        return this.toJSON(this.records);
    }
    public JSONArray toJSON(ArrayList<Record> records) {
        JSONArray array = new JSONArray();
        for (Record record : records) {
            array.put(record.toJSON());
        }
        return array;
    }

    private void addLine(String line, ArrayList<Record> records) {
        System.out.println("Parse line "+line);
        Record record = this.parseLine(line);
        if (record != null) {
            records.add(record);
        }
    }
    private void addLine(String line) {
        this.addLine(line, this.records);
    }

    private Record parseLine(String line) {
        Record record = null;
        if (line != null) {
            line = line.trim();
            if (line.length()>3) {
                String recordType = line.substring(0, 3);
                try {
                    if (recordType.equals(Record.RECORDTYPE_START)) {
                        record = new Start(line);
                    }
                    if (recordType.equals(Record.RECORDTYPE_AKTVEJ)) {
                        record = new AktVej(line);
                    }
                    if (recordType.equals(Record.RECORDTYPE_BOLIG)) {
                        record = new Bolig(line);
                    }
                    if (recordType.equals(Record.RECORDTYPE_BYNAVN)) {
                        record = new Bynavn(line);
                    }
                    if (recordType.equals(Record.RECORDTYPE_POSTDIST)) {
                        record = new PostDistrikt(line);
                    }
                    if (recordType.equals(Record.RECORDTYPE_NOTATVEJ)) {
                        record = new NotatVej(line);
                    }
                    if (recordType.equals(Record.RECORDTYPE_BYFORNYDIST)) {
                        record = new ByfornyelsesDistrikt(line);
                    }
                    if (recordType.equals(Record.RECORDTYPE_DIVDIST)) {
                        record = new DiverseDistrikt(line);
                    }
                    if (recordType.equals(Record.RECORDTYPE_EVAKUERDIST)) {
                        record = new EvakueringsDistrikt(line);
                    }
                    if (recordType.equals(Record.RECORDTYPE_KIRKEDIST)) {
                        record = new KirkeDistrikt(line);
                    }
                    if (recordType.equals(Record.RECORDTYPE_SKOLEDIST)) {
                        record = new SkoleDistrikt(line);
                    }
                    if (recordType.equals(Record.RECORDTYPE_BEFOLKDIST)) {
                        record = new BefolkningsDistrikt(line);
                    }
                    if (recordType.equals(Record.RECORDTYPE_SOCIALDIST)) {
                        record = new SocialDistrikt(line);
                    }
                    if (recordType.equals(Record.RECORDTYPE_SOGNEDIST)) {
                        record = new SogneDistrikt(line);
                    }
                    if (recordType.equals(Record.RECORDTYPE_VALGDIST)) {
                        record = new ValgDistrikt(line);
                    }
                    if (recordType.equals(Record.RECORDTYPE_VARMEDIST)) {
                        record = new VarmeDistrikt(line);
                    }
                    if (recordType.equals(Record.RECORDTYPE_SLUT)) {
                        record = new Slut(line);
                    }
                    if (recordType.equals(Record.RECORDTYPE_HISTORISKVEJ)) {
                        record = new HistoriskVej(line);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return record;
    }
}
