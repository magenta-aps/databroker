package dk.magenta.databroker.core;

import dk.magenta.databroker.core.model.DataProviderRepository;
import org.springframework.http.HttpRequest;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Properties;


/**
 * Created by jubk on 05-11-2014.
 */
public abstract class DataProvider {
    private dk.magenta.databroker.core.model.DataProvider dbObject;

    public DataProvider(dk.magenta.databroker.core.model.DataProvider dbObject) {
        this.dbObject = dbObject;
    }

    public dk.magenta.databroker.core.model.DataProvider getDbObject() {
        return dbObject;
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
