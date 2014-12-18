package dk.magenta.databroker.register.conditions;

import dk.magenta.databroker.register.objectcontainers.StringList;

import java.util.*;

/**
 * Created by lars on 16-12-14.
 */
public class ConditionList extends ArrayList<Condition> implements Condition {

    public enum Operator {
        AND,
        OR
    }

    private Operator operator;

    public ConditionList(Operator operator) {
        this.operator = operator;
    }

    public void addCondition(SingleCondition condition) {
        if (condition != null) {
            this.addCondition((Condition) condition);
        }
    }
    public void addCondition(ConditionList condition) {
        if (condition != null) {
            this.addCondition((Condition) condition);
        }
    }
    public void addCondition(Condition condition) {
        if (condition != null && condition != this) {
            if (!this.hasCondition(condition)) {
                this.add(condition);
            }
        }
    }

    public boolean hasCondition(Condition condition) {
        if (condition == this) {
            return true;
        }
        for (Condition condition1 : this) {
            if (condition1.hasCondition(condition)) {
                return true;
            }
        }
        return false;
    }

    public int size() {
        int size = 0;
        for (Condition c : this) {
            size += c.size();
        }
        return size;
    }

    public List<String> getRequiredJoin() {
        ArrayList<String> list = new ArrayList<String>();
        for (Condition condition : this) {
            list.addAll(condition.getRequiredJoin());
        }
        return list;
    }
    public boolean hasRequiredJoin() {
        for (Condition condition : this) {
            if (condition.hasRequiredJoin()) {
                return true;
            }
        }
        return false;
    }


    public String getWhere() {
        StringList stringList = new StringList();
        for (Condition condition : this) {
            if (condition.size() > 0) {
                stringList.append(condition.getWhere());
            }
        }
        return "("+stringList.join(" "+this.operator+" ")+")";
    }

    public Map<String, Object> getParameters() {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        for (Condition condition : this) {
            parameters.putAll(condition.getParameters());
        }
        return parameters;
    }
}
