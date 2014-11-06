package dk.magenta.databroker.component;

import dk.magenta.databroker.core.testmodel.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by ole on 20/10/14.
 */
@Component
public class DataBean {



    public String getSomeData() {
        return "Some data!!";
    }

}
