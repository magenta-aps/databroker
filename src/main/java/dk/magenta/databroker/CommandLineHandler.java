package dk.magenta.databroker;

import dk.magenta.databroker.core.DataProviderRegistry;
import dk.magenta.databroker.core.model.DataProviderEntity;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;

/**
 * Created by jubk on 13-01-2015.
 */
@Component
public class CommandLineHandler implements CommandLineRunner {

    @Autowired
    private ConfigurableApplicationContext configurableApplicationContext;

    @Autowired
    private DataProviderRegistry dataProviderRegistry;

    @Override
    @Transactional
    public void run(String[] strings) throws Exception {
        if(strings.length == 0)
            return;

        System.out.println();
        System.out.println();
        System.out.println();

        if("listdataproviders".equals(strings[0].toLowerCase())) {
            Collection<DataProviderEntity> dataproviders = dataProviderRegistry.getDataProviderEntities();
            System.out.println("List of data providers:");
            if (dataproviders.isEmpty()) {
                System.out.println("  No data providers found");
            } else {
                for (DataProviderEntity dataProviderEntity : dataproviders) {
                    System.out.print("  " + dataProviderEntity.getUuid());
                    System.out.println(" (Type: " + dataProviderEntity.getType() + ")");
                    System.out.println();
                }
            }
        } else if("listdataprovidertypes".equals(strings[0].toLowerCase())) {
            System.out.println("List of data provider types:");
            for(String type : dataProviderRegistry.getRegisteredDataProviderTypes()) {
                System.out.println("  " + type);
            }
        } else if("createdataprovider".equals(strings[0].toLowerCase())) {
            if(strings.length < 2 || strings[1].equals("")) {
                System.out.println("You must specify the type of data provider to create");
            } else {
                DataProviderEntity dataProviderEntity = dataProviderRegistry.createDataProviderEntity(
                        strings[1],
                        new HashMap<String, String[]>()
                );
                if(dataProviderEntity == null) {
                    System.out.println("Failed to create data provider");
                } else {
                    System.out.println("Data provider created successfully");
                }
            }
        } else if("pulldataprovider".equals(strings[0].toLowerCase())) {
            if(strings.length < 2 || strings[1].equals("")) {
                System.out.println("You must specify the UUID for the dataprovider to pull with");
            } else {
                DataProviderEntity dataProviderEntity = dataProviderRegistry.getDataProviderEntity(strings[1]);
                if(dataProviderEntity == null) {
                    System.out.println("Could not find dataprovider with UUID " + strings[1]);
                } else {
                    DataProviderRegistry.pull(dataProviderEntity);
                }
            }
        } else {
            System.out.println("Could not parse command line arguments");
        }
        System.out.println();
        configurableApplicationContext.close();
    }
}
