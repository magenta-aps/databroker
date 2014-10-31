package dk.magenta.databroker.cron;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
    public void reportTime() {

        System.err.println( "-> The time is now " + dateFormat.format(new Date()));

    }

}
