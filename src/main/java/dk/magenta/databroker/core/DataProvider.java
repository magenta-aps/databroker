package dk.magenta.databroker.core;

import org.springframework.http.HttpRequest;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import dk.magenta.databroker.core.model.DataProviderEntity;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.zip.ZipInputStream;


/**
 * Created by jubk on 05-11-2014.
 */
public abstract class DataProvider {

    public DataProvider() {

    }

    @PostConstruct
    protected void RegisterDataProviderInstance() {
        DataProviderRegistry.registerDataProvider(this);
    }

    public abstract void pull(DataProviderEntity dataProviderEntity);

    public void handlePush(DataProviderEntity dataProviderEntity, HttpRequest request) {
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

}
