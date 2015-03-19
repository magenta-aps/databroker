package dk.magenta.databroker.register.conditions;

import dk.magenta.databroker.util.objectcontainers.StringList;

import java.util.*;

/**
 * Created by lars on 10-12-14.
 */
// A Condition represents one part of the WHERE clause in a database query, keeping track of the queried key, the comparison operator, and the value parameter
// the static concatWhere method will take a list of these objects and make a string out of them
public class MultiCondition implements Condition {
    private String fieldspec;
    private boolean negated;
    private HashMap<String, Object> value;
    private String requiredJoin;

    public MultiCondition(String fieldspec) {
        this(fieldspec, null, false, null);
    }
    public MultiCondition(String fieldspec, Object value, boolean negated) {
        this(fieldspec, value, negated, null);
    }
    public MultiCondition(String fieldspec, Object value, boolean negated, String requiredJoin) {
        this(fieldspec, null, negated, requiredJoin);
        this.addValue(value);
    }
    public MultiCondition(String fieldspec, List<Object> value, String requiredJoin) {
        this(fieldspec, value, false, requiredJoin);
    }


    public MultiCondition(String fieldspec, List<Object> value, boolean negated, String requiredJoin) {
        this.fieldspec = fieldspec;
        this.negated = negated;
        this.value = new HashMap<String, Object>();
        if (value != null) {
            for (Object v : value) {
                this.addValue(v);
            }
        }
        this.requiredJoin = requiredJoin;
    }
    public void addValue(Object value) {
        if (value != null) {
            this.value.put("id_" + UUID.randomUUID().toString().replace("-", ""), value);
        }
    }

    public List<String> getRequiredJoin() {
        ArrayList<String> list = new ArrayList<String>();
        list.add(this.requiredJoin);
        return list;
    }
    public boolean hasRequiredJoin() {return this.requiredJoin!=null; }

    @Override
    public void addRequiredJoin(String requiredJoin) {
        this.requiredJoin = requiredJoin;
    }

    public String getWhere() {
        StringList keyList = new StringList(this.value.keySet());
        keyList.setPrefix(":");
        return this.fieldspec + " " + (this.negated ? "not in":"in") + "(" + keyList.join(", ") + ")";

    }
    public Map<String, Object> getParameters() {
        return this.value;
    }
    public boolean hasCondition(Condition condition) {
        return (condition == this);
    }
    public int size() {
        return 1;
    }
}
