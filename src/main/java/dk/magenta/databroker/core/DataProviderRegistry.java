package dk.magenta.databroker.core;

import dk.magenta.databroker.core.model.DataProviderEntity;
import java.util.*;

/**
 * Created by jubk on 15-12-2014.
 */

public class DataProviderRegistry {
    private static HashMap<String, DataProvider> instances = new HashMap<String, DataProvider>();

    public static void registerDataProvider(DataProvider dataProvider) {
        String className = dataProvider.getClass().getCanonicalName();
        if(instances.containsKey(className)) {
            // TODO: Add some kind of warning? Throw an error?
            return;
        }
        instances.put(className, dataProvider);
    }

    public static Collection<String> getRegisteredDataProviderTypes() {
        ArrayList<String> result = new ArrayList<String>();
        result.addAll(instances.keySet());
        Collections.sort(result);
        return result;
    }

    public static DataProvider getDataProviderForEntity(DataProviderEntity  dataproviderEntity) {
        return instances.get(dataproviderEntity.getType());
    }
}
