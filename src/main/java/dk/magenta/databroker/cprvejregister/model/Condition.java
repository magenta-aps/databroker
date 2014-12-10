package dk.magenta.databroker.cprvejregister.model;

import dk.magenta.databroker.cprvejregister.dataproviders.objectcontainers.StringList;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

/**
 * Created by lars on 10-12-14.
 */
public class Condition {
    private String fieldspec;
    private String operator;
    private Object value;
    private String key;
    private String requiredJoin;

    private static int counter = 0;

    public Condition(String fieldspec, String operator, Object value) {
        this(fieldspec, operator, value, null);
    }
    public Condition(String fieldspec, String operator, Object value, String requiredJoin) {
        this.fieldspec = fieldspec;
        this.operator = operator;
        this.value = value;
        this.key = "value" + counter;
        this.requiredJoin = requiredJoin;
        counter++;
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

    public static void resetCounter() {
        counter = 0; // TODO: find another way to manage the keys
    }
}
