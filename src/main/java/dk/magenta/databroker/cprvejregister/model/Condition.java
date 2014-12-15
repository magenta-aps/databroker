package dk.magenta.databroker.cprvejregister.model;

import dk.magenta.databroker.cprvejregister.dataproviders.objectcontainers.StringList;

import javax.persistence.Query;
import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;

/**
 * Created by lars on 10-12-14.
 */
// A Condition represents one part of the WHERE clause in a database query, keeping track of the queried key, the comparison operator, and the value parameter
// the static concatWhere method will take a list of these objects and make a string out of them
public class Condition {
    private String fieldspec;
    private String operator;
    private Object value;
    private String key;
    private String requiredJoin;

    public Condition(String fieldspec, String operator, Object value) {
        this(fieldspec, operator, value, null);
    }
    public Condition(String fieldspec, String operator, Object value, String requiredJoin) {
        this.fieldspec = fieldspec;
        this.operator = operator;
        this.value = value;
        this.key = "id_"+UUID.randomUUID().toString().replace("-","");
        this.requiredJoin = requiredJoin;
    }

    public String getWhere() {
        return this.fieldspec + " " + this.operator + " :" + this.key;
    }
    public String getKey() {
        return this.key;
    }
    public Object getValue() {
        return this.value;
    }
    public String getRequiredJoin() {
        return this.requiredJoin;
    }
    public boolean hasRequiredJoin() {return this.requiredJoin!=null; }

    public static String concatWhere(Collection<Condition> conditions) {
        StringList stringList = new StringList();
        if (conditions.size() > 0) {
            stringList.append("where ");
            for (Iterator<Condition> cIter = conditions.iterator(); cIter.hasNext(); ) {
                stringList.append(cIter.next().getWhere());
                if (cIter.hasNext()) {
                    stringList.append(" and ");
                }
            }
        }
        return stringList.join();
    }

    public static Query addParameters(Collection<Condition> conditions, Query query) {
        for (Condition condition : conditions) {
            System.out.println(condition.getKey()+" = "+condition.getValue());
            query.setParameter(condition.getKey(), condition.getValue());
        }
        return query;
    }
}
