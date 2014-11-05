package dk.magenta.databroker.inputs.vejregister;

import dk.magenta.databroker.inputs.vejregister.records.*;
import org.json.JSONArray;

import java.io.*;
import java.util.ArrayList;
import java.text.ParseException;
import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;

/**
 * Created by lars on 04-11-14.
 */
public class Vejregister {

    public static void main(String[] args) {

        Vejregister register = null;
        try {
            register = new Vejregister(new File("src/test/resources/vejregister/a370727.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(register.toJSON().toString(4));
    }

    private ArrayList<Record> records;

    public Vejregister(String[] lines) {
        this.records = new ArrayList<Record>();
        for (String line : lines) {
            this.addLine(line);
        }
    }

    public Vejregister(String input) {
        this(input.split("\\n"));
    }

    public Vejregister(InputStream input, String encoding) throws IOException {
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
    }

    public Vejregister(File input) throws IOException {
        this(input, null);
    }
    public Vejregister(File input, String encoding) throws IOException {
        this(new FileInputStream(input), encoding);
    }

    private void addLine(String line) {
        Record record = this.parseLine(line);
        if (record != null) {
            this.records.add(record);
        }
    }


    public JSONArray toJSON() {
        JSONArray array = new JSONArray();
        for (Record record : this.records) {
            array.put(record.toJSON());
        }
        return array;
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
