package dk.magenta.databroker.core;

import org.springframework.http.HttpRequest;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import dk.magenta.databroker.core.model.DataProviderEntity;

import java.util.Properties;


/**
 * Created by jubk on 05-11-2014.
 */
public abstract class DataProvider {
    private DataProviderEntity dataProviderEntity;

    public DataProvider(DataProviderEntity dataProviderEntity) {
        this.dataProviderEntity = dataProviderEntity;
    }

    public DataProviderEntity getDataProviderEntity() {
        return dataProviderEntity;
    }

    public void pull() {
        throw new NotImplementedException();
    }

    public void handlePush(HttpRequest request) {
        throw new NotImplementedException();
    }

    public Properties getConfigSpecification() {
        throw new NotImplementedException();
    }

}
