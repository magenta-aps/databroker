package dk.magenta.databroker.inputs.vejregister;

import dk.magenta.databroker.inputs.vejregister.records.*;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lars on 04-11-14.
 */
public class Vejregister {

    public static void main(String[] args) {
        String[] lines = {
                "00037071520141031",
                "001001000701991092312000000000000000000190001011200Norge               Norge",
                "00400100070    998 L2002090912009999Ukendt",
                "00400100070001 999 U2002090912009999Ukendt",
                "001001000711991092312000000000000000000190001011200Sverige             Sverige",
                "00400100071    998 L2002090912009999Ukendt",
                "00400100071001 999 U2002090912009999Ukendt",
                "001001000721991092312000000000000000000190001011200Finland             Finland",
                "00400100072    998 L2002090912009999Ukendt",
                "00400100072001 999 U2002090912009999Ukendt",
                "001001000731991092312000000000000000000190001011200Island              Island",
                "00400100073    998 L2002090912009999Ukendt",
                "00400100073001 999 U2002090912009999Ukendt",
                "001001000741991092312000000000000000000190001011200Grønland            Grønland",
                "00400100074    998 L2002090912009999Ukendt",
                "00400100074001 999 U2002090912009999Ukendt",
                "001001000751991092312000000000000000000190001011200Færøerne            Færøerne",
                "00400100075    998 L2002090912009999Ukendt"
        };
        Vejregister register = new Vejregister(lines);
        System.out.println(register.toJSON().toString(4));
    }

    private ArrayList<Record> records;

    public Vejregister(String[] lines) {
        this.records = new ArrayList<Record>();
        for (String line : lines) {
            Record record = this.parseLine(line);
            if (record != null) {
                this.records.add(record);
            }
        }
        System.out.println("There are "+this.records.size()+" records");
    }

    public Vejregister(String input) {
        this(input.split("\\n"));
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
            } catch (Exception e) {
e.printStackTrace();
            }
        }
        return record;
    }
}
