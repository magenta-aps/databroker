package dk.magenta.databroker.core;

import dk.magenta.databroker.core.model.DataProviderEntity;
import dk.magenta.databroker.core.model.DataProviderRepository;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by jubk on 15-12-2014.
 */

@Component
public class DataProviderRegistry {
    private static HashMap<String, DataProvider> dataProviders = new HashMap<String, DataProvider>();

    @Autowired
    private DataProviderRepository dataProviderRepository;

    public static void registerDataProvider(DataProvider dataProvider) {
        String className = dataProvider.getClass().getCanonicalName();
        if (dataProviders.containsKey(className)) {
            // TODO: Add some kind of warning? Throw an error?
            return;
        }
        dataProviders.put(className, dataProvider);
    }

    public static Collection<String> getRegisteredDataProviderTypes() {
        ArrayList<String> result = new ArrayList<String>();
        result.addAll(dataProviders.keySet());
        Collections.sort(result);
        return result;
    }

    public static DataProvider getDataProvider(String type) {
        return dataProviders.get(type);
    }


    public static Collection<DataProvider> getRegisteredDataProviders() {
        // TODO: return list of dataprovider dataProviders
        return dataProviders.values();
    }

    public static DataProvider getDataProviderForEntity(DataProviderEntity dataproviderEntity) {
        return dataProviders.get(dataproviderEntity.getType());
    }

    public Collection<DataProviderEntity> getDataProviderEntities() {
        return dataProviderRepository.findAll();
    }

    public DataProviderEntity createDataProviderEntity(String type, Map<String, String[]> parameters) {
        DataProviderEntity entity = null;
        if (type != null && dataProviders.keySet().contains(type)) {
            entity = new DataProviderEntity();
            entity.setType(dataProviders.get(type).getClass());
            entity.setUuid(UUID.randomUUID().toString());

            String name = this.getIdleNumberedName(type);
            if (name == null) {
                name = entity.getUuid();
            }
            entity.setName(name);

            entity.setActive(true);
            entity.setPriority(1);

            DataProviderConfiguration configuration = new DataProviderConfiguration(parameters);
            entity.setConfiguration(configuration.toString());

            this.dataProviderRepository.save(entity);
        }
        return entity;
    }

    public String getIdleNumberedName(String type) {
        String className = dataProviders.get(type).getClass().getSimpleName();
        for (int i=0; i<10000; i++) {
            String nameCandidate = className+" #"+i;
            if (dataProviderRepository.getByName(nameCandidate) == null) {
                return nameCandidate;
            }
        }
        return null;
    }

    public void updateDataProviderEntity(DataProviderEntity entity, Map<String, String[]> parameters, String name) {
        if (entity != null) {
            try {
                DataProviderConfiguration configuration = new DataProviderConfiguration(entity.getConfiguration());
                boolean updated = false;
                for (String key : parameters.keySet()) {
                    if (configuration.update(key, parameters.get(key))) {
                        updated = true;
                    }
                }
                if (name != null && !name.equals(entity.getName())) {
                    entity.setName(name);
                    updated = true;
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

    public void deleteDataProviderEntity(DataProviderEntity entity) {
        this.dataProviderRepository.delete(entity);
    }
}
