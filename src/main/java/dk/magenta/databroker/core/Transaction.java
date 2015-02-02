package dk.magenta.databroker.core;

import dk.magenta.databroker.core.DataProviderRegistry;
import dk.magenta.databroker.core.model.DataProviderEntity;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.hibernate.SessionFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;



/**
 * Created by lars on 15-01-15.
 */

@Component
public class Transaction extends Thread {

    @Autowired
    private ConfigurableApplicationContext configurableApplicationContext;

    @Autowired
    private PlatformTransactionManager txManager;

    public void run() {

        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("CommandLineTransactionDefinition");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        System.out.println(this.getTxManager());

        TransactionStatus status = this.getTxManager().getTransaction(def);
        try {
            this.transaction();
            this.getTxManager().commit(status);
        }
        catch (Exception ex) {
            this.getTxManager().rollback(status);
        }

        //SpringApplication.exit(configurableApplicationContext);
    }

    public PlatformTransactionManager getTxManager() {
        return txManager;
    }

    public void transaction(){

    }
}
