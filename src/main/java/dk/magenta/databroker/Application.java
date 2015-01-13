package dk.magenta.databroker;

import dk.magenta.databroker.core.DataProviderRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@ImportResource("/*-context.xml")
@Configuration
@EnableScheduling
@EnableAutoConfiguration
@ComponentScan
@EnableJpaRepositories
@EnableAsync
@EnableTransactionManagement
public class Application {

    @Autowired
    private DataProviderRegistry dataProviderRegistry;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        // If called with command line arguments, assume we do not want the web environment
        if(args.length > 0) {
            app.setWebEnvironment(false);
        }
        app.run(args);
    }

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(10);
        return threadPoolTaskScheduler;
    }

}