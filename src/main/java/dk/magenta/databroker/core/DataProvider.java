package dk.magenta.databroker.core;

import dk.magenta.databroker.core.controller.DataProviderController;
import dk.magenta.databroker.correction.CorrectionCollectionEntity;
import dk.magenta.databroker.correction.CorrectionCollectionRepository;
import dk.magenta.databroker.util.TransactionCallback;
import dk.magenta.databroker.util.Util;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.odftoolkit.simple.SpreadsheetDocument;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.odftoolkit.simple.table.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.transaction.TransactionException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import dk.magenta.databroker.core.model.DataProviderEntity;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;


/**
 * Created by jubk on 05-11-2014.
 */
public abstract class DataProvider {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    private SessionFactory sessionFactory;

    @PostConstruct
    private void unwrapSessionFactory() {
        this.sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
    }

    protected void runTransacationCallback(TransactionCallback transactionCallback) {
        runTransacationCallback(transactionCallback, true);
    }
    protected void runTransacationCallback(TransactionCallback transactionCallback, boolean stateless) {
        log.info("Running "+(stateless ? "staleless":"stateful")+" transaction");
        org.hibernate.Transaction transaction;
        StatelessSession statelessSession = null;
        Session session = null;
        if (stateless) {
            statelessSession = sessionFactory.openStatelessSession();
            transaction = statelessSession.getTransaction();
        } else {
            session = sessionFactory.openSession();
            transaction = session.getTransaction();
        }
        transaction.begin();
        try {
            transactionCallback.run();
            log.info("Ending transaction");
            transaction.commit();
        } catch (Exception e) {
            log.info("Rolling back transaction");
            transaction.rollback();
        }
        if (session != null) {
            session.close();
        }
        if (statelessSession != null) {
            statelessSession.close();
        }
        log.info("Transaction complete, session closed");
    }

    private class DataProviderThread extends Thread {
        private DataProvider dataProvider;
        private DataProviderEntity dataProviderEntity;
        public DataProviderThread(DataProvider dataProvider, DataProviderEntity dataProviderEntity) {
            this.dataProvider = dataProvider;
            this.dataProviderEntity = dataProviderEntity;
        }

        public DataProvider getDataProvider() {
            return dataProvider;
        }

        public DataProviderEntity getDataProviderEntity() {
            return dataProviderEntity;
        }

        protected List<TransactionCallback> getTransactionCallbacks() {
            return null;
        }

        @Override
        public void run() {
            try {
                List<TransactionCallback> transactionCallbacks = this.getTransactionCallbacks();
                if (transactionCallbacks != null) {
                    for (TransactionCallback transactionCallback : transactionCallbacks) {
                        runTransacationCallback(transactionCallback);
                    }
                }
            } catch (Exception e) {
                log.error("Exception in transaction: ", e);
                e.printStackTrace();
            }
        }

    }


    private class DataProviderPusher extends DataProviderThread {
        private File uploadData;

        public DataProviderPusher(DataProviderEntity dataProviderEntity, HttpServletRequest request) {
            super(DataProvider.this, dataProviderEntity);
            try {
                this.uploadData = File.createTempFile(dataProviderEntity.getUuid(), ".tmp");
                this.uploadData.deleteOnExit();
                InputStream data = DataProvider.this.getUploadStream(request);
                Files.copy(data, this.uploadData.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        protected List<TransactionCallback> getTransactionCallbacks() {
            ArrayList<TransactionCallback> transactionCallbacks = new ArrayList<TransactionCallback>();

            final DataProviderPusher pusher = this;
            transactionCallbacks.add(new TransactionCallback(){
                @Override
                public void run() throws Exception {
                    FileInputStream inputStream = null;
                    try {
                        inputStream = new FileInputStream(pusher.uploadData);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    DataProvider.this.handlePush(true, pusher.getDataProviderEntity(), inputStream);
                }
            });

            transactionCallbacks.addAll(this.getDataProvider().getBulkwireCallbacks(this.getDataProviderEntity()));

            return transactionCallbacks;
        }

        @Override
        public void run() {
            super.run();
            this.uploadData.delete();
        }
    }

    private class DataProviderPuller extends DataProviderThread {

        public DataProviderPuller(DataProviderEntity dataProviderEntity) {
            super(DataProvider.this, dataProviderEntity);
        }

        protected List<TransactionCallback> getTransactionCallbacks() {
            ArrayList<TransactionCallback> transactionCallbacks = new ArrayList<TransactionCallback>();

            final DataProviderPuller puller = this;
            transactionCallbacks.add(new TransactionCallback(){
                @Override
                public void run() throws Exception {
                    DataProvider.this.pull(puller.getDataProviderEntity());
                }
            });

            transactionCallbacks.addAll(this.getDataProvider().getBulkwireCallbacks(this.getDataProviderEntity()));

            return transactionCallbacks;
        }
    }

    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private DataProviderController dataProviderController;

    private Logger log = Logger.getLogger(DataProvider.class);

    public DataProvider() {
        this.uuid = UUID.randomUUID().toString();
    }

    @PostConstruct
    protected void postConstruct() {
        DataProviderRegistry.registerDataProvider(this);
        this.dataProviderController.updateCronScheduling(this); // This MUST be called after registerDataProvider, or we get a NPE
    }


    private String uuid;

    public String getUuid() {
        return uuid;
    }

    public abstract String getTemplatePath();

    public abstract DataProviderConfiguration getDefaultConfiguration();

    public abstract void pull(DataProviderEntity dataProviderEntity);

    public abstract InputStream getUploadStream(HttpServletRequest request);


    public void handlePush(DataProviderEntity dataProviderEntity, HttpServletRequest request) {
        throw new NotImplementedException();
    }

    public void handlePush(boolean forceParse, DataProviderEntity dataProviderEntity, InputStream input) {
        throw new NotImplementedException();
    }

    public Thread asyncPush(DataProviderEntity dataProviderEntity, HttpServletRequest request) {
        Thread thread = new DataProviderPusher(dataProviderEntity, request);
        thread.start();
        return thread;
    }

    public Thread asyncPull(DataProviderEntity dataProviderEntity) {
        return this.asyncPull(dataProviderEntity, true);
    }
    public Thread asyncPull(DataProviderEntity dataProviderEntity, boolean runNow) {
        Thread thread = new DataProviderPuller(dataProviderEntity);
        if (runNow) {
            thread.start();
        }
        return thread;
    }

    protected List<TransactionCallback> getBulkwireCallbacks(DataProviderEntity dataProviderEntity) {
        // Override me
        return null;
    }

    //public abstract Properties getConfigSpecification(DataProviderEntity dataProviderEntity);


    private static String getExtension(String filename) {
        return filename != null ? filename.substring(filename.lastIndexOf('.')+1).toLowerCase() : null;
    }

    protected InputStream readUrl(URL url) {
        if (url != null) {
            try {
                return this.read(url.getFile(), url.openStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    protected NamedInputStream readFile(File file) {
        if (file.canRead()) {
            try {
                if (getExtension(file.getAbsolutePath()).equals("zip")) {
                    return this.unzip(file);
                } else {
                    return this.read(file.getAbsolutePath(), new FileInputStream(file));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    protected InputStream readResource(Resource resource) {
        try {
            return this.read(resource.getFilename(), resource.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isZipStream(InputStream input) {
        try {
            return (new ZipInputStream(input).getNextEntry() != null);
        } catch (IOException e) {
            return false;
        }
    }

    private NamedInputStream read(String filename, InputStream input) throws IOException {
        return this.read(new NamedInputStream(input, filename));
    }
    private NamedInputStream read(NamedInputStream input) throws IOException {
        if (input.extensionEquals("zip")) {
            this.log.info("Passing data through ZIP filter");
            input = this.unzip(input);
        }
        if (input != null && (input.extensionEquals("xls") || input.extensionEquals("xlsx"))) {
            this.log.info("Passing data through XLS filter");
            input = this.convertExcelSpreadsheet(input);
        }
        if (input != null && input.extensionEquals("ods")) {
            this.log.info("Passing data through ODS filter");
            input = this.convertOdfSpreadsheet(input);
        }
        return input;
    }

    private static final String[] acceptedExtensions = {"xml","csv","ods","xls","xlsx","txt"};


    protected NamedInputStream unzip(NamedInputStream input) throws IOException {
        ZipInputStream zinput = new ZipInputStream(input);
        for (ZipEntry entry = zinput.getNextEntry(); entry != null; entry = zinput.getNextEntry()) {
            String extension = getExtension(entry.getName());
            if (extension != null && Util.inArray(acceptedExtensions, extension)) {
                this.log.info("Unzipping entry " + entry.getName() + " (" + entry.getSize() + " bytes)");
                NamedInputStream output = new NamedInputStream(zinput, entry.getName());
                output.setKnownSize(entry.getSize());
                return output;
            }
        }
        return null;
    }

    protected NamedInputStream unzip(File input) throws IOException {
        ZipFile zipfile = new ZipFile(input);
        Enumeration<? extends ZipEntry> entries = zipfile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            String extension = getExtension(entry.getName());
            if (extension != null && Util.inArray(acceptedExtensions, extension)) {
                this.log.info("Unzipping entry " + entry.getName() + " (" + entry.getSize() + " bytes)");
                NamedInputStream output = new NamedInputStream(zipfile.getInputStream(entry), entry.getName());
                output.setKnownSize(entry.getSize());
                return output;
            }
        }
        return null;
    }

    private NamedInputStream convertExcelSpreadsheet(NamedInputStream input) throws IOException {
        final Workbook wb = input.extensionEquals("xlsx") ? new XSSFWorkbook(input) : new HSSFWorkbook(input);
        PipedInputStream returnStream = new PipedInputStream();
        final OutputStream outputStream = new PipedOutputStream(returnStream);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=0; i<wb.getNumberOfSheets(); i++) {
                    Sheet sheet = wb.getSheetAt(i);
                    for (Row row : sheet) {
                        try {
                            boolean first = true;
                            for (Cell cell : row) {
                                String cellText;
                                if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                    double d = cell.getNumericCellValue();
                                    if (Math.abs(Math.rint(d) - d) < 0.001) {
                                        cellText = "" + (int) Math.rint(d);
                                    } else {
                                        cellText = "" + d;
                                    }
                                } else {
                                    cellText = cell.getStringCellValue();
                                }
                                if (first) {
                                    outputStream.write(",".getBytes());
                                    first = false;
                                }
                                outputStream.write(cellText.getBytes());
                            }
                            outputStream.write("\n".getBytes());
                            outputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return new NamedInputStream(returnStream, null, "csv");
    }
    private NamedInputStream convertOdfSpreadsheet(NamedInputStream input) throws IOException {
        try {
            final SpreadsheetDocument document = SpreadsheetDocument.loadDocument(input);
            PipedInputStream returnStream = new PipedInputStream();
            final OutputStream outputStream = new PipedOutputStream(returnStream);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (Table table : document.getTableList()) {
                        int columns = table.getColumnCount();
                        for (Iterator<org.odftoolkit.simple.table.Row> rIter = table.getRowIterator(); rIter.hasNext(); ) {
                            org.odftoolkit.simple.table.Row row = rIter.next();
                            try {
                                for (int i = 0; i < columns; i++) {
                                    org.odftoolkit.simple.table.Cell cell = row.getCellByIndex(i);
                                    String cellText = cell.getStringValue();
                                    if (i > 0) {
                                        outputStream.write(",".getBytes());
                                    }
                                    outputStream.write(cellText.getBytes());
                                }
                                outputStream.write("\n".getBytes());
                                outputStream.flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            return new NamedInputStream(returnStream, null, "csv");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return input;
    }

    public List<String> getUploadFields() {
        return null;
    }

    public boolean wantUpload(DataProviderConfiguration configuration) {
        return false;
    }

    public boolean wantCronUpdate(DataProviderConfiguration oldConfiguration, DataProviderConfiguration newConfiguration) {
        return false;
    }

    public String getCronExpression(DataProviderConfiguration configuration) {
        return null;
    }

    public boolean canPull(DataProviderConfiguration configuration) {
        return false;
    }

    public Resource getCorrectionSeed() {
        return null;
    }


    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    CorrectionCollectionRepository correctionCollectionRepository;

    public void loadCorrectionSeed(DataProviderEntity dataProviderEntity) {
        Resource resource = this.getCorrectionSeed();
        if (resource != null) {

            CorrectionCollectionEntity correctionCollectionEntity = null;
            String subregister = this.getClass().getSimpleName();

            Collection<CorrectionCollectionEntity> matches = this.correctionCollectionRepository.getByFoobar(dataProviderEntity, subregister);
            if (matches != null && !matches.isEmpty()) {
                correctionCollectionEntity = matches.iterator().next();
            }

            if (correctionCollectionEntity == null) {
                correctionCollectionEntity = new CorrectionCollectionEntity();
                correctionCollectionEntity.setDataProviderEntity(dataProviderEntity);
                correctionCollectionEntity.setSubregister(subregister);
            }
            try {
                correctionCollectionEntity.loadFromJSON(resource);
                correctionCollectionRepository.save(correctionCollectionEntity);
                dataProviderEntity.setCorrections(correctionCollectionEntity);
                this.dataProviderController.saveDataProviderEntity(dataProviderEntity);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
