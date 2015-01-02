package dk.magenta.databroker.cprvejregister.dataproviders.registers;

import dk.magenta.databroker.register.Register;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by lars on 18-12-14.
 */
@Component
public class CprRegister extends Register {

    @Autowired
    private VejRegister vejRegister;

    @Autowired
    private MyndighedsRegister myndighedsRegister;

    @Autowired
    private LokalitetsRegister lokalitetsRegister;

    @Autowired
    private PostnummerRegister postnummerRegister;

    @Autowired
    private BynavnRegister bynavnRegister;


    public CprRegister() {
    }

    public void pull() {
        this.pull(false, false);
    }

    @Transactional
    public void pull(boolean forceFetch, boolean forceParse) {
        this.myndighedsRegister.pull(forceFetch, forceParse);
        this.vejRegister.pull(forceFetch, forceParse);
        this.lokalitetsRegister.pull(forceFetch, forceParse);
        this.postnummerRegister.pull(forceFetch, forceParse);
        this.bynavnRegister.pull(forceFetch, forceParse);
    }
}
