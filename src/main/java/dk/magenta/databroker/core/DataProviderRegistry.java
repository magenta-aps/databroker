package dk.magenta.databroker.core;

import dk.magenta.databroker.core.model.DataProviderEntity;
import dk.magenta.databroker.core.model.DataProviderRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by jubk on 15-12-2014.
 */

@Component
public class DataProviderRegistry {
    private static HashMap<String, DataProvider> dataProviderTypes = new HashMap<String, DataProvider>();

    @Autowired
    private DataProviderRepository dataProviderRepository;

    public static void registerDataProvider(DataProvider dataProviderType) {
        String className = dataProviderType.getClass().getCanonicalName();
        System.out.println("Registered data provider type " + className);
        if (dataProviderTypes.containsKey(className)) {
            // TODO: Add some kind of warning? Throw an error?
            return;
        }
        dataProviderTypes.put(className, dataProviderType);
    }

    public static Collection<String> getRegisteredDataProviderTypes() {
        ArrayList<String> result = new ArrayList<String>();
        result.addAll(dataProviderTypes.keySet());
        Collections.sort(result);
        return result;
    }

    public static DataProvider getDataProviderType(String type) {
        return dataProviderTypes.get(type);
    }


    public static Collection<DataProvider> getRegisteredDataProviders() {
        // TODO: return list of dataprovider dataProviderTypes
        return dataProviderTypes.values();
    }

    public static DataProvider getDataProviderForEntity(DataProviderEntity dataproviderEntity) {
        return dataProviderTypes.get(dataproviderEntity.getType());
    }

    public Collection<DataProviderEntity> getDataProviderEntities() {
        System.out.println(dataProviderRepository);
        return dataProviderRepository.findAll();
    }

    public DataProviderEntity createDataProviderEntity(String type, Map<String, String[]> parameters) {
        DataProviderEntity entity = null;
        if (type != null && dataProviderTypes.keySet().contains(type)) {
            entity = new DataProviderEntity();
            entity.setType(dataProviderTypes.get(type).getClass());
            entity.setUuid(UUID.randomUUID().toString());
            entity.setActive(true);
            entity.setPriority(1);

            DataProviderConfiguration configuration = new DataProviderConfiguration(parameters);
            entity.setConfiguration(configuration.toString());

            this.dataProviderRepository.save(entity);
        }
        return entity;
    }

    public void updateDataProviderEntity(DataProviderEntity entity, Map<String, String[]> parameters) {
        if (entity != null) {
            try {
                DataProviderConfiguration configuration = new DataProviderConfiguration(entity.getConfiguration());
                boolean updated = false;
                for (String key : parameters.keySet()) {
                    if (configuration.update(key, parameters.get(key))) {
                        updated = true;
                    }
                }
                if (updated) {
                    entity.setConfiguration(configuration.toString());
                    this.dataProviderRepository.save(entity);
                }
            } catch (JSONException e) {
            }
        }
    }

    public Map<String, String[]> getDataProviderEntityValues(DataProviderEntity dataProviderEntity) {
        DataProviderConfiguration configuration = new DataProviderConfiguration(dataProviderEntity.getConfiguration());
        return configuration.toArrayMap();
    }

    public DataProviderEntity getDataProviderEntity(String uuid) {
        return this.dataProviderRepository.getByUuid(uuid);
    }
}
