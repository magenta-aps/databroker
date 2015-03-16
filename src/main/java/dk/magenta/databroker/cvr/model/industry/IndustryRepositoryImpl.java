package dk.magenta.databroker.cvr.model.industry;

import dk.magenta.databroker.core.model.RepositoryImplementation;


/**
 * Created by lars on 13-03-15.
 */

interface IndustryRepositoryCustom {
    public void clear();
}

public class IndustryRepositoryImpl extends RepositoryImplementation<IndustryEntity> implements IndustryRepositoryCustom {
}
