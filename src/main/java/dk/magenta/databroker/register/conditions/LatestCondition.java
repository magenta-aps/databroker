package dk.magenta.databroker.register.conditions;

import java.util.List;
import java.util.Map;

/**
 * Created by lars on 12-01-15.
 */
public class LatestCondition implements Condition {

    private String versionField;
    public LatestCondition(String versionField) {
        this.versionField = versionField;
    }

    @Override
    public String getWhere() {
        return this.versionField + ".entity.latestVersion = "+this.versionField;
    }

    @Override
    public Map<String, Object> getParameters() {
        return null;
    }

    @Override
    public String getWhere(String key) {
        return null;
    }

    @Override
    public Map<String, Object> getParameters(String key) {
        return null;
    }

    @Override
    public boolean hasRequiredJoin() {
        return false;
    }

    @Override
    public List<String> getRequiredJoin() {
        return null;
    }

    @Override
    public void addRequiredJoin(String requiredJoin) {

    }

    @Override
    public boolean hasCondition(Condition condition) {
        return (condition == this);
    }

    @Override
    public int size() {
        return 1;
    }
}
