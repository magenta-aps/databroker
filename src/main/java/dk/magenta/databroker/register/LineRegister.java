package dk.magenta.databroker.register;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;
import dk.magenta.databroker.register.records.Record;
import java.io.*;
import java.net.MalformedURLException;
import java.util.Date;

/**
 * Created by lars on 15-12-14.
 */
public abstract class LineRegister extends Register {

    public LineRegister() {
    }

    protected RegisterRun parse(InputStream input) {
        try {
            BufferedInputStream inputstream = new BufferedInputStream(input);

            String encoding = this.getEncoding();
            if (encoding != null) {
                System.out.println("Using explicit encoding " + encoding);
            } else {
                // Try to guess the encoding based on the stream contents
                CharsetDetector detector = new CharsetDetector();
                detector.setText(inputstream);
                CharsetMatch match = detector.detect();
                if (match != null) {
                    encoding = match.getName();
                    System.out.println("Interpreting data as " + encoding);
                } else {
                    encoding = "UTF-8";
                    System.out.println("Falling back to default encoding " + encoding);
                }
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputstream, encoding.toUpperCase()));

            System.out.println("Reading data");
            long time = this.tic();
            int i = 0, j = 0;

            RegisterRun run = this.createRun();

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                if (line != null) {
                    line = line.trim();
                    if (line.length() > 3) {
                        try {
                            Record record = this.parseTrimmedLine(line);
                            if (record != null) {
                                //this.processRecord(record);
                                run.add(record);
                            }
                        } catch (OutOfMemoryError e) {
                            System.out.println(line);
                        }
                    }
                }
                i++;
                if (i >= 100000) {
                    j++;
                    System.gc();
                    System.out.println("    parsed " + (j * i) + " lines");
                    i = 0;
                }
            }
            System.out.println("    parsed " + (j * 100000 + i) + " lines");
            System.out.println("Parse complete ("+run.size()+" usable entries found) in "+ this.toc(time) + " ms");
            return run;


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Parse failed");
        return null;
    }

    protected Record parseTrimmedLine(String line) {
        return null;
    }

    protected abstract RegisterRun createRun();

}
