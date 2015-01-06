package dk.magenta.databroker.cron;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ole on 23/10/14.
 */
@Configuration
@EnableScheduling
//@ImportResource("/address-service-context.xml")
public class ExampleJob {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(cron = "0 * * * * ?")
    public void report() {
        System.err.println( "-> The time is now " + dateFormat.format(new Date()));
        Runtime runtime = Runtime.getRuntime();
        NumberFormat format = NumberFormat.getInstance();
        System.err.println("-> Memory: " + format.format(runtime.freeMemory() / 1024) + " kB available, of " + format.format(runtime.maxMemory() / 1024)+" kB total");
    }

}
