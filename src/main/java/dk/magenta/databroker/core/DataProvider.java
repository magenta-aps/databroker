package dk.magenta.databroker.core;

import org.springframework.http.HttpRequest;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import dk.magenta.databroker.core.model.DataProviderEntity;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.zip.ZipInputStream;


/**
 * Created by jubk on 05-11-2014.
 */
public abstract class DataProvider {

    public DataProvider() {
        this.uuid = UUID.randomUUID().toString();
    }

    @PostConstruct
    protected void RegisterDataProviderInstance() {
        DataProviderRegistry.registerDataProvider(this);
    }


    private String uuid;

    public String getUuid() {
        return uuid;
    }

    public abstract String getTemplatePath();

    public abstract DataProviderConfiguration getDefaultConfiguration();

    public abstract void pull(DataProviderEntity dataProviderEntity);

    public void handlePush(DataProviderEntity dataProviderEntity, HttpServletRequest request) {
        throw new NotImplementedException();
    }

    public Properties getConfigSpecification(DataProviderEntity dataProviderEntity) {
        throw new NotImplementedException();
    }

    protected InputStream readUrl(URL url) {
        if (url != null) {
            try {
                InputStream input = url.openStream();
                if (url.getFile().endsWith(".zip")) {
                    System.out.println("Passing data through ZIP filter");
                    input = this.unzip(input);
                }
                return input;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    protected InputStream readFile(File file) {
        if (file.canRead()) {
            try {
                InputStream input = new FileInputStream(file);
                if (file.getAbsolutePath().endsWith(".zip")) {
                    System.out.println("Passing data through ZIP filter");
                    input = this.unzip(input);
                }
                return input;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private InputStream unzip(InputStream input) throws IOException {
        ZipInputStream zinput = new ZipInputStream(input);
        zinput.getNextEntry(); // Load the first entry in the zip archive
        return zinput;
    }

    public List<String> getUploadFields() {
        return null;
    }

    public boolean wantUpload(DataProviderConfiguration configuration) {
        return false;
    }
    public boolean wantUpload(String configuration) {
        return this.wantUpload(new DataProviderConfiguration(configuration));
    }

}
