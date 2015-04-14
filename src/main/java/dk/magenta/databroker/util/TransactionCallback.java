package dk.magenta.databroker.util;

import dk.magenta.databroker.core.Session;

/**
 * Created by lars on 09-04-15.
 */
public class TransactionCallback {
    public void run(Session session) throws Exception {
        // Override me
        this.run();
    }
    public void run() throws Exception {
        // Override me
    }
}
