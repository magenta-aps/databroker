package dk.magenta.databroker.dawa.model.adgangsadresse;

import dk.magenta.databroker.core.Session;
import dk.magenta.databroker.core.model.EntityRepositoryCustom;
import dk.magenta.databroker.core.model.EntityRepositoryImplementation;
import dk.magenta.databroker.dawa.model.SearchParameters;
import dk.magenta.databroker.dawa.model.SearchParameters.Key;
import dk.magenta.databroker.dawa.model.lokalitet.LokalitetEntity;
import dk.magenta.databroker.dawa.model.postnummer.PostNummerEntity;
import dk.magenta.databroker.dawa.model.temaer.KommuneEntity;
import dk.magenta.databroker.dawa.model.vejstykker.VejstykkeEntity;
import dk.magenta.databroker.register.conditions.ConditionList;
import dk.magenta.databroker.register.conditions.GlobalCondition;
import dk.magenta.databroker.util.TransactionCallback;
import dk.magenta.databroker.util.Util;
import dk.magenta.databroker.util.objectcontainers.StringList;
import org.apache.log4j.Logger;
import org.hibernate.StatelessSession;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Created by lars on 09-12-14.
 */

interface AdgangsAdresseRepositoryCustom extends EntityRepositoryCustom<AdgangsAdresseEntity, Long> {
}

public class AdgangsAdresseRepositoryImpl extends EntityRepositoryImplementation<AdgangsAdresseEntity, Long> implements AdgangsAdresseRepositoryCustom {

    private Logger log = Logger.getLogger(AdgangsAdresseRepositoryImpl.class);

    @Override
    public AdgangsAdresseEntity getByDescriptor(Long descriptor) {
        return this.getByDescriptor(descriptor, null);
    }

    @Override
    public AdgangsAdresseEntity getByDescriptor(Long descriptor, Session session) {
        return this.getByDescriptor(descriptor.longValue(), session);
    }



    public AdgangsAdresseEntity getByDescriptor(long descriptor, Session session) {
        // TODO: Kan vi på nogen måde gøre denne metode hurtigere? p.t. kører den på 1-3 ms
        if (!this.hasKnownDescriptor(descriptor, true)) {
            return null;
        }
        ConditionList conditions = new ConditionList();
        conditions.addCondition(AdgangsAdresseEntity.descriptorCondition(descriptor));
        final String key = this.getRandomKey();
        final String hql = "select distinct " + AdgangsAdresseEntity.databaseKey + " from AdgangsAdresseEntity as " + AdgangsAdresseEntity.databaseKey + " where " + conditions.getWhere(key);
        Collection<AdgangsAdresseEntity> adgangsAdresseEntities = this.query(hql, conditions.getParameters(key), GlobalCondition.singleCondition, session);
        //System.out.println("Found "+adgangsAdresseEntities.size()+" candidates for descriptor "+descriptor);
        if (adgangsAdresseEntities != null && adgangsAdresseEntities.size() > 0) {
            return adgangsAdresseEntities.iterator().next();
        }
        return null;
    }


    @Override
    public Collection<AdgangsAdresseEntity> search(SearchParameters parameters) {

        StringList hql = new StringList();
        StringList join = new StringList();
        ConditionList conditions = new ConditionList();

        hql.append("select distinct "+AdgangsAdresseEntity.databaseKey+" from AdgangsAdresseEntity as "+AdgangsAdresseEntity.databaseKey);
        join.setPrefix("join ");

        if (parameters.hasAny(Key.LAND, Key.KOMMUNE, Key.POST, Key.VEJ, Key.LOKALITET)) {

            join.append(AdgangsAdresseEntity.joinVej());

            conditions.addCondition(VejstykkeEntity.vejCondition(parameters));
            if (parameters.hasAny(Key.LAND, Key.KOMMUNE)) {
                join.append(VejstykkeEntity.joinKommune());
                conditions.addCondition(KommuneEntity.landCondition(parameters));
                conditions.addCondition(KommuneEntity.kommuneCondition(parameters));

            }
            if (parameters.has(Key.POST)) {
                join.append(VejstykkeEntity.joinPost());
                conditions.addCondition(PostNummerEntity.postCondition(parameters));
            }
            if (parameters.has(Key.LOKALITET)) {
                join.append(VejstykkeEntity.joinLokalitet());
                conditions.addCondition(LokalitetEntity.lokalitetCondition(parameters));
            }
        }

        conditions.addCondition(AdgangsAdresseEntity.husnrCondition(parameters));
        conditions.addCondition(AdgangsAdresseEntity.bnrCondition(parameters));
        
        if (parameters.hasGlobalCondition()) {
            conditions.addCondition(parameters.getGlobalCondition().whereField("adresse"));
        }

        // our conditions list should now be complete

        if (conditions.hasRequiredJoin()) {
            join.append(conditions.getRequiredJoin());
        }

        // our join list should now be complete

        if (join.size()>0) {
            hql.append(join.join(" "));
        }
        if (conditions.size() > 0) {
            hql.append("where");
            hql.append(conditions.getWhere());
        }

        hql.append("order by "+AdgangsAdresseEntity.databaseKey+".husnr");

        return this.query(hql, conditions, parameters.getGlobalCondition());
    }

    public List<TransactionCallback> getBulkwireCallbacks() {
        ArrayList<TransactionCallback> transactionCallbacks = new ArrayList<TransactionCallback>();
        transactionCallbacks.add(new TransactionCallback() {
            @Override
            public void run(Session session) {
                AdgangsAdresseRepositoryImpl repositoryImplementation = AdgangsAdresseRepositoryImpl.this;
                repositoryImplementation.log.info("Updating references between addresses and roads");
                double time = Util.getTime();
                repositoryImplementation.entityManager.createNativeQuery("update dawa_adgangsadresse adresse join dawa_vejstykke vej on adresse.vejstykke_descriptor=vej.descriptor set adresse.vejstykke_id=vej.id where adresse.vejstykke_id is NULL").executeUpdate();
                repositoryImplementation.log.info("References updated in "+(Util.getTime()-time)+" ms");
            }
        });
        return transactionCallbacks;
    }

    @Override
    public HashSet<Long> getKnownDescriptors() {
        Query q = this.entityManager.createQuery("select " + AdgangsAdresseEntity.databaseKey + ".descriptor from AdgangsAdresseEntity as " + AdgangsAdresseEntity.databaseKey);
        return new HashSet<Long>(q.getResultList());
    }
}
