package dk.magenta.databroker.core;

import dk.magenta.databroker.core.controller.DataProviderController;
import dk.magenta.databroker.correction.CorrectionCollectionEntity;
import dk.magenta.databroker.correction.CorrectionCollectionRepository;
import dk.magenta.databroker.util.Util;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.odftoolkit.simple.SpreadsheetDocument;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.odftoolkit.simple.table.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import dk.magenta.databroker.core.model.DataProviderEntity;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


/**
 * Created by jubk on 05-11-2014.
 */
public abstract class DataProvider {


    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    protected PlatformTransactionManager txManager;


    private class DataProviderPusher extends Thread {
        private DataProviderEntity dataProviderEntity;
        private File uploadData;
        private TransactionTemplate transactionTemplate;

        public DataProviderPusher(DataProviderEntity dataProviderEntity, HttpServletRequest request, PlatformTransactionManager transactionManager) {
            this.dataProviderEntity = dataProviderEntity;
            try {
                this.uploadData = File.createTempFile(dataProviderEntity.getUuid(), "tmp");
                this.uploadData.deleteOnExit();
                InputStream data = DataProvider.this.getUploadStream(request);
                Files.copy(data, this.uploadData.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.transactionTemplate = new TransactionTemplate(transactionManager);
        }
        @Override
        public void run() {
            final DataProviderEntity dataProviderEntity = this.dataProviderEntity;

            //System.out.println("Starting push in transaction");
            //this.transactionTemplate.execute(new TransactionCallback() {
                // the code in this method executes in a transactional context
            //    public Object doInTransaction(TransactionStatus status) {
                    try {
            DataProvider.this.handlePush(true, dataProviderEntity, new FileInputStream(DataProviderPusher.this.uploadData));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
            //        return null;
            //    }
            //});
            this.uploadData.delete();
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
        Thread thread = new DataProviderPusher(dataProviderEntity, request, this.txManager);
        thread.start();
        return thread;
    }

    public Thread asyncPull(DataProviderEntity dataProviderEntity) {
        return this.asyncPull(dataProviderEntity, this.txManager, true);
    }
    public Thread asyncPull(DataProviderEntity dataProviderEntity, PlatformTransactionManager transactionManager, boolean runNow) {
        Thread thread = new DataProviderPuller(dataProviderEntity, transactionManager);
        if (runNow) {
            thread.start();
        }
        return thread;
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

    protected InputStream readFile(File file) {
        if (file.canRead()) {
            try {
                return this.read(file.getAbsolutePath(), new FileInputStream(file));
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
