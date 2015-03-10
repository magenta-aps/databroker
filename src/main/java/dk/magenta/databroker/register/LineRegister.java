package dk.magenta.databroker.register;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;
import dk.magenta.databroker.register.records.Record;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.MalformedURLException;
import java.util.Date;

/**
 * Created by lars on 15-12-14.
 */
public abstract class LineRegister extends Register {

    protected Logger log = Logger.getLogger(this.getClass());

    public LineRegister() {
    }

    protected RegisterRun parse(InputStream input) {
        try {
            BufferedInputStream inputstream = new BufferedInputStream(input);

            String encoding = this.getEncoding();
            if (encoding != null) {
                this.log.info("Using explicit encoding " + encoding);
            } else {
                // Try to guess the encoding based on the stream contents
                CharsetDetector detector = new CharsetDetector();
                detector.setText(inputstream);
                CharsetMatch match = detector.detect();
                if (match != null) {
                    encoding = match.getName();
                    this.log.info("Interpreting data as " + encoding);
                } else {
                    encoding = "UTF-8";
                    this.log.info("Falling back to default encoding " + encoding);
                }
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputstream, encoding.toUpperCase()));

            this.log.info("Reading data");
            double time = this.tic();
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
                    this.log.trace("    parsed " + (j * i) + " lines");
                    i = 0;
                }
            }
            this.log.trace("    parsed " + (j * 100000 + i) + " lines");
            time = this.toc(time);
            int count = run.size();
            this.log.info("Parse complete (" + count + " usable entries found) in " + time + " ms " + (count > 0 ? ("(avg " + (time / (double) count) + " ms)") : ""));
            return run;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.log.warn("Parse failed");
        return null;
    }

    protected Record parseTrimmedLine(String line) {
        return null;
    }

    protected abstract RegisterRun createRun();

}
