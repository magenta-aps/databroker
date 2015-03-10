package dk.magenta.databroker.correction;

import dk.magenta.databroker.core.model.DataProviderEntity;
import dk.magenta.databroker.core.model.RepositoryImplementation;
import dk.magenta.databroker.register.RepositoryUtil;
import dk.magenta.databroker.register.conditions.ConditionList;
import dk.magenta.databroker.register.conditions.GlobalCondition;
import dk.magenta.databroker.register.conditions.SingleCondition;
import dk.magenta.databroker.util.objectcontainers.StringList;

import java.util.Collection;

/**
 * Created by lars on 09-03-15.
 */

interface CorrectionCollectionRepositoryCustom {
    public Collection<CorrectionCollectionEntity> getByFoobar(DataProviderEntity dataProviderEntity, String subregister);
}


public class CorrectionCollectionRepositoryImpl extends RepositoryImplementation<CorrectionCollectionEntity> implements CorrectionCollectionRepositoryCustom {

    public Collection<CorrectionCollectionEntity> getByFoobar(DataProviderEntity dataProviderEntity, String subregister) {
        StringList hql = new StringList();
        hql.append("select collection from CorrectionCollectionEntity collection where ");
        ConditionList conditions = new ConditionList();
        conditions.addCondition(RepositoryUtil.whereField(subregister, null, "collection.subregister"));
        conditions.addCondition(new SingleCondition("collection.dataProviderEntity.id", "=", dataProviderEntity.getId()));
        hql.append(conditions.getWhere());
        return this.query(hql, conditions, GlobalCondition.singleCondition);
    }

}
