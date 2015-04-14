package dk.magenta.databroker.dawa.model.enhedsadresser;

import dk.magenta.databroker.core.Session;
import dk.magenta.databroker.core.model.EntityRepositoryCustom;
import dk.magenta.databroker.core.model.EntityRepositoryImplementation;
import dk.magenta.databroker.dawa.model.SearchParameters;
import dk.magenta.databroker.dawa.model.SearchParameters.Key;
import dk.magenta.databroker.dawa.model.adgangsadresse.AdgangsAdresseEntity;
import dk.magenta.databroker.dawa.model.postnummer.PostNummerEntity;
import dk.magenta.databroker.dawa.model.temaer.KommuneEntity;
import dk.magenta.databroker.dawa.model.vejstykker.VejstykkeEntity;
import dk.magenta.databroker.register.RepositoryUtil;
import dk.magenta.databroker.register.conditions.ConditionList;
import dk.magenta.databroker.register.conditions.GlobalCondition;
import dk.magenta.databroker.util.TransactionCallback;
import dk.magenta.databroker.util.objectcontainers.StringList;

import javax.persistence.Query;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;


/**
 * Created by lars on 09-12-14.
 */

interface EnhedsAdresseRepositoryCustom extends EntityRepositoryCustom<EnhedsAdresseEntity, String> {

}

public class EnhedsAdresseRepositoryImpl extends EntityRepositoryImplementation<EnhedsAdresseEntity, String> implements EnhedsAdresseRepositoryCustom {


    @Override
    public List<TransactionCallback> getBulkwireCallbacks() {
        return null;
    }

    @Override
    public Collection<EnhedsAdresseEntity> search(SearchParameters parameters) {

        StringList hql = new StringList();
        StringList join = new StringList();
        ConditionList conditions = new ConditionList(ConditionList.Operator.AND);

        join.setPrefix("join ");
        hql.append("select " + EnhedsAdresseEntity.databaseKey + " from EnhedsAdresseEntity as " + EnhedsAdresseEntity.databaseKey);

        if (parameters.hasAny(Key.LAND, Key.KOMMUNE, Key.VEJ, Key.POST, Key.HUSNR, Key.BNR, Key.LOKALITET)) {

            join.append(EnhedsAdresseEntity.joinAdgangsAdresse());

            if (parameters.hasAny(Key.LAND, Key.KOMMUNE, Key.VEJ)) {
                join.append(AdgangsAdresseEntity.joinVej());
                conditions.addCondition(VejstykkeEntity.vejCondition(parameters));

                //conditions.addCondition(VejstykkeEntity.vejCondition(parameters));
                if (parameters.hasAny(Key.LAND, Key.KOMMUNE)) {
                    join.append(VejstykkeEntity.joinKommune());
                    conditions.addCondition(KommuneEntity.landCondition(parameters));
                    conditions.addCondition(KommuneEntity.kommuneCondition(parameters));
                }

                if (parameters.has(Key.POST)) {
                    //join.append(AdgangsAdresseEntity.databaseKey+".latestVersion.postnummer as post");
                    join.append(VejstykkeEntity.joinPost());
                    conditions.addCondition(PostNummerEntity.postCondition(parameters));
                }
                if (parameters.has(Key.LOKALITET)) {
                    join.append("vejstykke.latestVersion.lokaliteter as lokalitet");
                    conditions.addCondition(RepositoryUtil.whereField(parameters.get(Key.LOKALITET), null, "lokalitet.navn"));
                }

                if (parameters.has(Key.POST)) {
                    join.append("vejstykke.latestVersion.postnumre as post");
                    conditions.addCondition(RepositoryUtil.whereField(parameters.get(Key.POST), "post.latestVersion.nr", "post.latestVersion.navn"));
                }
            }
            conditions.addCondition(AdgangsAdresseEntity.husnrCondition(parameters));
            conditions.addCondition(AdgangsAdresseEntity.bnrCondition(parameters));
        }


        conditions.addCondition(EnhedsAdresseEntity.doerCondition(parameters));
        conditions.addCondition(EnhedsAdresseEntity.etageCondition(parameters));

        if (parameters.hasGlobalCondition()) {
            conditions.addCondition(parameters.getGlobalCondition().whereField(EnhedsAdresseEntity.databaseKey));
        }

        // our conditions list should now be complete

        if (conditions.hasRequiredJoin()) {
            join.append(conditions.getRequiredJoin());
        }

        // our join list should now be complete

        if (join.size()>0) {
            hql.append(join.join(" \n"));
        }
        if (conditions.size() > 0) {
            hql.append("where");
            hql.append(conditions.getWhere());
        }

        return this.query(hql, conditions, parameters.getGlobalCondition());
    }

    @Override
    public HashSet<String> getKnownDescriptors() {
        Query q = this.entityManager.createQuery("select " + EnhedsAdresseEntity.databaseKey + ".descriptor from EnhedsAdresseEntity as " + EnhedsAdresseEntity.databaseKey);
        return new HashSet<String>(q.getResultList());
    }


    @Override
    public EnhedsAdresseEntity getByDescriptor(String descriptor) {
        return this.getByDescriptor(descriptor, null);
    }

    @Override
    public EnhedsAdresseEntity getByDescriptor(String descriptor, Session session) {
        if (!this.hasKnownDescriptor(descriptor, true)) {
            return null;
        }
        ConditionList conditions = new ConditionList();
        conditions.addCondition(EnhedsAdresseEntity.descriptorCondition(descriptor));
        final String key = "id_"+ UUID.randomUUID().toString().replace("-","");
        final String hql = "select distinct " + EnhedsAdresseEntity.databaseKey + " from EnhedsAdresseEntity as " + EnhedsAdresseEntity.databaseKey + " where " + conditions.getWhere(key);
        Collection<EnhedsAdresseEntity> enhedsAdresseEntities = this.query(hql, conditions.getParameters(key), GlobalCondition.singleCondition, session);
        return enhedsAdresseEntities.size() > 0 ? enhedsAdresseEntities.iterator().next() : null;
    }
}
