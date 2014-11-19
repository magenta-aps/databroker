package dk.magenta.databroker.core.model.oio;

import org.springframework.beans.factory.annotation.Autowired;

interface RegistreringLivscyklusRepositoryCustom {
    public RegistreringLivscyklusEntity findOrCreateByStatus(RegistreringLivscyklusStatus status);

}

public class RegistreringLivscyklusRepositoryImpl implements RegistreringLivscyklusRepositoryCustom {
    @Autowired
    private RegistreringLivscyklusRepository baseRepo;

    private RegistreringLivscyklusEntity findOrCreateByNavn(String navn) {
        RegistreringLivscyklusEntity result = baseRepo.getByNavn(navn);
        if(result == null) {
            result = new RegistreringLivscyklusEntity(navn);
            baseRepo.save(result);
        }
        return result;
    }

    @Override
    public RegistreringLivscyklusEntity findOrCreateByStatus(RegistreringLivscyklusStatus status) {
        return this.findOrCreateByNavn(status.toString());
    }
}
