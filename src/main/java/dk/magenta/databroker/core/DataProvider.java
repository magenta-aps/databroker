package dk.magenta.databroker.core;

import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
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
import java.util.Timer;
import java.util.UUID;
import java.util.concurrent.Future;
import java.util.zip.ZipInputStream;


/**
 * Created by jubk on 05-11-2014.
 */
public abstract class DataProvider {

    private class DataProviderPusher extends Thread {
        private DataProviderEntity dataProviderEntity;
        private HttpServletRequest request;

        private TransactionTemplate transactionTemplate;


        public DataProviderPusher(DataProviderEntity dataProviderEntity, HttpServletRequest request, PlatformTransactionManager transactionManager) {
            this.dataProviderEntity = dataProviderEntity;
            this.request = request;
            this.transactionTemplate = new TransactionTemplate(transactionManager);
        }
        @Override
        public void run() {
            final DataProviderEntity dataProviderEntity = this.dataProviderEntity;
            final HttpServletRequest request = this.request;

            this.transactionTemplate.execute(new TransactionCallback() {
                // the code in this method executes in a transactional context
                public Object doInTransaction(TransactionStatus status) {
                    DataProvider.this.handlePush(dataProviderEntity, request);
                    return null;
                }
            });
        }
    }

    private class DataProviderPuller extends Thread {
        private DataProviderEntity dataProviderEntity;

        private TransactionTemplate transactionTemplate;


        public DataProviderPuller(DataProviderEntity dataProviderEntity, PlatformTransactionManager transactionManager) {
            this.dataProviderEntity = dataProviderEntity;
            this.transactionTemplate = new TransactionTemplate(transactionManager);
        }
        @Override
        public void run() {
            final DataProviderEntity dataProviderEntity = this.dataProviderEntity;
            this.transactionTemplate.execute(new TransactionCallback() {
                // the code in this method executes in a transactional context
                public Object doInTransaction(TransactionStatus status) {
                    DataProvider.this.pull(dataProviderEntity);
                    return null;
                }
            });
        }
    }


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

    public Thread asyncPush(DataProviderEntity dataProviderEntity, HttpServletRequest request, PlatformTransactionManager transactionManager) {
        Thread thread = new DataProviderPusher(dataProviderEntity, request, transactionManager);
        thread.start();
        return thread;
    }

    public Thread asyncPull(DataProviderEntity dataProviderEntity, PlatformTransactionManager transactionManager) {
        Thread thread = new DataProviderPuller(dataProviderEntity, transactionManager);
        thread.start();
        return thread;
    }

    //public abstract Properties getConfigSpecification(DataProviderEntity dataProviderEntity);


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

    protected InputStream readResource(Resource resource) {
            try {
                InputStream input = resource.getInputStream();
                if (resource.getFilename().endsWith(".zip")) {
                    System.out.println("Passing data through ZIP filter");
                    input = this.unzip(input);
                }
                return input;
            } catch (IOException e) {
                e.printStackTrace();
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

    public boolean canPull(DataProviderConfiguration configuration) {
        return false;
    }

}
