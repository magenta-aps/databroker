package dk.magenta.databroker.register.conditions;

import java.util.*;

/**
 * Created by lars on 10-12-14.
 */
// A Condition represents one part of the WHERE clause in a database query, keeping track of the queried key, the comparison operator, and the value parameter
// the static concatWhere method will take a list of these objects and make a string out of them
public class SingleCondition implements Condition {
    private String fieldspec;
    private String operator;
    private Object value;
    private String key;
    private String requiredJoin;

    public SingleCondition(String fieldspec, String operator, Object value) {
        this(fieldspec, operator, value, null);
    }
    public SingleCondition(String fieldspec, String operator, Object value, String requiredJoin) {
        this.fieldspec = fieldspec;
        this.operator = operator;
        this.value = value;
        this.key = "id_"+UUID.randomUUID().toString().replace("-","");
        this.requiredJoin = requiredJoin;
    }

    public List<String> getRequiredJoin() {
        ArrayList<String> list = new ArrayList<String>();
        if (this.requiredJoin != null) {
            list.add(this.requiredJoin);
        }
        return list;
    }
    public boolean hasRequiredJoin() {return this.requiredJoin!=null; }

    @Override
    public void addRequiredJoin(String requiredJoin) {
        this.requiredJoin = requiredJoin;
    }

    public String getWhere() {
        return this.getWhere(this.key);
    }
    public String getWhere(String key) {
        return this.fieldspec + " " + this.operator + " :" + key;
    }
    public Map<String, Object> getParameters() {
        return this.getParameters(this.key);
    }
    public Map<String, Object> getParameters(String key) {
        HashMap<String, Object> list = new HashMap<String, Object>();
        list.put(key, this.value);
        return list;
    }
    public boolean hasCondition(Condition condition) {
        return (condition == this);
    }
    public int size() {
        return 1;
    }
}
