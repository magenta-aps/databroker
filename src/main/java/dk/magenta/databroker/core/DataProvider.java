package dk.magenta.databroker.core;

import dk.magenta.databroker.register.objectcontainers.Pair;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.odftoolkit.simple.SpreadsheetDocument;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.odftoolkit.odfdom.doc.OdfDocument;
import org.odftoolkit.odfdom.doc.table.OdfTable;
import org.odftoolkit.simple.table.Table;
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
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.concurrent.Future;
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


    public class NamedInputStream extends Pair<String, InputStream> {
        public NamedInputStream(String name, InputStream input) {
            super(name.substring(name.lastIndexOf('.')+1), input);
        }
        public String getFileExtension() {
            return this.getLeft();
        }
    }

    protected InputStream readUrl(URL url) {
        if (url != null) {
            try {
                return this.read(url.getFile(), url.openStream()).getRight();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    protected InputStream readFile(File file) {
        if (file.canRead()) {
            try {
                return this.read(file.getAbsolutePath(), new FileInputStream(file)).getRight();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    protected InputStream readResource(Resource resource) {
        try {
            return this.read(resource.getFilename(), resource.getInputStream()).getRight();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private NamedInputStream read(String filename, InputStream input) throws IOException {
        return this.read(new NamedInputStream(filename, input));
    }
    private NamedInputStream read(NamedInputStream input) throws IOException {
        System.out.println("input.getFileExtension(): "+input.getFileExtension());
        if (input.getFileExtension().equals("zip")) {
            System.out.println("Passing data through ZIP filter");
            input = this.unzip(input);
        }
        if (input.getFileExtension().equals("xls") || input.getFileExtension().equals("xlsx")) {
            System.out.println("Passing data through XLS filter");
            input = this.convertExcelSpreadsheet(input);
        }
        if (input.getFileExtension().equals("ods")) {
            System.out.println("Passing data through ODS filter");
            input = this.convertOdfSpreadsheet(input);
        }
        return input;
    }



    private NamedInputStream unzip(NamedInputStream input) throws IOException {
        ZipInputStream zinput = new ZipInputStream(input.getRight());
        ZipEntry entry = zinput.getNextEntry(); // Load the first entry in the zip archive
        return new NamedInputStream(entry.getName(), zinput);
    }

    private NamedInputStream convertExcelSpreadsheet(NamedInputStream input) throws IOException {
        final Workbook wb = input.getFileExtension().equals("xlsx") ? new XSSFWorkbook(input.getRight()) : new HSSFWorkbook(input.getRight());
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
        return new NamedInputStream("csv", returnStream);
    }
    private NamedInputStream convertOdfSpreadsheet(NamedInputStream input) throws IOException {
        try {
            final SpreadsheetDocument document = SpreadsheetDocument.loadDocument(input.getRight());
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
            return new NamedInputStream("csv", returnStream);
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

    public boolean canPull(DataProviderConfiguration configuration) {
        return false;
    }

}
