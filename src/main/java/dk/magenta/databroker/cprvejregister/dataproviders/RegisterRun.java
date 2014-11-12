package dk.magenta.databroker.cprvejregister.dataproviders;

import dk.magenta.databroker.cprvejregister.dataproviders.records.Record;
//import dk.magenta.databroker.models.adresser.Kommune;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lars on 10-11-14.
 */
public class RegisterRun {
    private ArrayList<Record> allRecords;
    /*private HashMap<String, HashMap<String, HashMap<String, ArrayList<Record>>>> lookup; // Construct to look up a record by type, key and value
    private HashMap<String, ArrayList<Record>> byType;
    private HashMap<String, ArrayList<Record>> byClass;*/

    public RegisterRun() {
        this.allRecords = new ArrayList<Record>();
        /*this.lookup = new HashMap<String, HashMap<String, HashMap<String, ArrayList<Record>>>>();
        this.byType = new HashMap<String, ArrayList<Record>>();
        this.byClass = new HashMap<String, ArrayList<Record>>();*/
    }

    public void saveRecord(Record record) {
        this.allRecords.add(record);
    }

/*
    public List<Record> findRecord(String recordClass, String key, String value) {
        HashMap<String, HashMap<String, ArrayList<Record>>> byKey = this.lookup.get(recordClass);
        if (byKey != null) {
            HashMap<String, ArrayList<Record>> byValue = byKey.get(key);
            if (byValue != null) {
                return byValue.get(value);
            }
        }
        return null;
    }*/

    public List<Record> getAll() {
        return this.allRecords;
    }


    public JSONArray toJSON() {
        JSONArray array = new JSONArray();
        for (Record record : this.allRecords) {
            array.put(record.toJSON());
        }
        return array;
    }
}
