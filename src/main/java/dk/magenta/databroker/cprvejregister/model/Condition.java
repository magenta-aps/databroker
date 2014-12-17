package dk.magenta.databroker.cprvejregister.model;

import java.util.List;
import java.util.Map;

/**
 * Created by lars on 16-12-14.
 */
public interface Condition {
    public String getWhere();
    public Map<String, Object> getParameters();
    public boolean hasRequiredJoin();
    public List<String> getRequiredJoin();
    public boolean hasCondition(Condition condition);
    public int size();
}
