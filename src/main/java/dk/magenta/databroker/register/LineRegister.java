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
            Date startTime = new Date();
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
                    tic();
                    System.gc();

                    System.out.println("    parsed " + (j * i) + " entries (cleanup took "+toc()+" ms)");

                    i = 0;

                    //Runtime runtime = Runtime.getRuntime();
                    //NumberFormat format = NumberFormat.getInstance();
                    //System.out.println("free memory: " + format.format(runtime.freeMemory() / 1024) + " of " + format.format(runtime.maxMemory() / 1024));

                }
            }

            System.out.println("    parsed " + (j * 100000 + i) + " entries in " + String.format("%.3f", 0.001 * (new Date().getTime() - startTime.getTime())) + " seconds");

            //System.out.println(run.toFullJSON().toString(2));
            System.out.println("Parse complete ("+run.size()+" entries)");
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

}
