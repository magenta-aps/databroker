package dk.magenta.databroker.cprvejregister.dataproviders.registers;

import dk.magenta.databroker.core.model.DataProviderEntity;
import dk.magenta.databroker.register.Register;
import dk.magenta.databroker.register.RegisterRun;
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

    @Override
    protected void saveRunToDatabase(RegisterRun run, DataProviderEntity dataProviderEntity) {
        // Do nothing
    }

    @Transactional
    public void pull(boolean forceFetch, boolean forceParse, DataProviderEntity dataProviderEntity) {
        this.myndighedsRegister.pull(forceFetch, forceParse, dataProviderEntity);
        this.vejRegister.pull(forceFetch, forceParse, dataProviderEntity);
        this.lokalitetsRegister.pull(forceFetch, forceParse, dataProviderEntity);
        this.postnummerRegister.pull(forceFetch, forceParse, dataProviderEntity);
        this.bynavnRegister.pull(forceFetch, forceParse, dataProviderEntity);
    }
}
