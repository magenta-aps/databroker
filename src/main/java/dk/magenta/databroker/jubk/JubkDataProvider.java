package dk.magenta.databroker.jubk;

import dk.magenta.databroker.core.DataProvider;
import dk.magenta.databroker.core.DataProviderConfiguration;
import dk.magenta.databroker.core.Session;
import dk.magenta.databroker.core.model.DataProviderEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;

/**
 * Created by jubk on 11/24/14.
 */
@Component
public class JubkDataProvider extends DataProvider {
    public JubkDataProvider() {
    }

    @Override
    public void pull(DataProviderEntity dataProviderEntity, Session session) {
        // Do nothing
    }

    @Override
    public InputStream getUploadStream(HttpServletRequest request) {
        return null;
    }

    public String getTemplatePath() {
        return null;
    }

    @Override
    public DataProviderConfiguration getDefaultConfiguration() {
        return new DataProviderConfiguration();
    }
}
