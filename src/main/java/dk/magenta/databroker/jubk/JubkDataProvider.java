package dk.magenta.databroker.jubk;

import dk.magenta.databroker.core.DataProvider;
import dk.magenta.databroker.core.DataProviderConfiguration;
import dk.magenta.databroker.core.model.DataProviderEntity;
import org.springframework.stereotype.Component;

/**
 * Created by jubk on 11/24/14.
 */
@Component
public class JubkDataProvider extends DataProvider {
    public JubkDataProvider() {
    }

    @Override
    public void pull(DataProviderEntity dataProviderEntity) {
        // Do nothing
    }

    public String getTemplatePath() {
        return null;
    }

    @Override
    public DataProviderConfiguration getDefaultConfiguration() {
        return new DataProviderConfiguration();
    }
}
