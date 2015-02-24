package dk.magenta.databroker.core;

import dk.magenta.databroker.core.controller.DataProviderController;
import dk.magenta.databroker.util.Util;
import dk.magenta.databroker.util.objectcontainers.Pair;
import org.apache.commons.io.input.CountingInputStream;
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
import java.util.*;
import java.util.zip.ZipEntry;
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
                    System.out.println("Running in transaction");
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

    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private DataProviderController dataProviderController;

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

    public void handlePush(DataProviderEntity dataProviderEntity, HttpServletRequest request) {
        System.err.println("aieee");
        throw new NotImplementedException();
    }

    public Thread asyncPush(DataProviderEntity dataProviderEntity, HttpServletRequest request, PlatformTransactionManager transactionManager) {
        Thread thread = new DataProviderPusher(dataProviderEntity, request, transactionManager);
        thread.start();
        System.out.println("Push thread started");
        return thread;
    }

    public Thread asyncPull(DataProviderEntity dataProviderEntity, PlatformTransactionManager transactionManager) {
        return this.asyncPull(dataProviderEntity, transactionManager, true);
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

    public class NamedInputStream extends FilterInputStream {
        long knownSize = 0;
        String basename;
        String extension;

        public NamedInputStream(InputStream input, String filename) {
            super(input);
            this.basename = this.extractBasename(filename);
            this.extension = "";
        }
        public NamedInputStream(InputStream input, String basename, String extension) {
            super(input);
            this.basename = basename;
            this.extension = extension;
        }

        public String getBasename() {
            return basename;
        }
        public String getExtension() {
            return this.extension;
        }
        public boolean extensionEquals(String extension) {
            return this.extension != null && this.extension.equalsIgnoreCase(extension);
        }
        public long getKnownSize() {
            return knownSize;
        }
        public void setKnownSize(long knownSize) {
            this.knownSize = knownSize;
        }
        private String extractBasename(String filename) {
            int index = filename.lastIndexOf(".");
            return index == -1 ? filename : filename.substring(0, index);
        }
        private String extractExtension(String filename) {
            int index = filename.lastIndexOf(".");
            return index == -1 ? "" : filename.substring(index);
        }
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
            System.out.println("Passing data through ZIP filter");
            input = this.unzip(input);
        }
        if (input != null && (input.extensionEquals("xls") || input.extensionEquals("xlsx"))) {
            System.out.println("Passing data through XLS filter");
            input = this.convertExcelSpreadsheet(input);
        }
        if (input != null && input.extensionEquals("ods")) {
            System.out.println("Passing data through ODS filter");
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
                System.out.println("Unzipping entry "+entry.getName());
                System.out.println("unzipped size: "+entry.getSize());
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
    public boolean wantUpload(String configuration) {
        return this.wantUpload(new DataProviderConfiguration(configuration));
    }

    public boolean wantCronUpdate(DataProviderConfiguration oldConfiguration, DataProviderConfiguration newConfiguration) {
        return false;
    }
    public boolean wantCronUpdate(String oldConfiguration, String newConfiguration) {
        return this.wantCronUpdate(new DataProviderConfiguration(oldConfiguration), new DataProviderConfiguration(newConfiguration));
    }

    public String getCronExpression(DataProviderConfiguration configuration) {
        return null;
    }

    public boolean canPull(DataProviderConfiguration configuration) {
        return false;
    }

}
