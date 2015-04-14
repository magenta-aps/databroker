package dk.magenta.databroker.cvr.model.company;

import dk.magenta.databroker.core.Session;
import dk.magenta.databroker.core.model.EntityRepositoryCustom;
import dk.magenta.databroker.core.model.EntityRepositoryImplementation;
import dk.magenta.databroker.dawa.model.SearchParameters;
import dk.magenta.databroker.dawa.model.SearchParameters.Key;
import dk.magenta.databroker.dawa.model.adgangsadresse.AdgangsAdresseEntity;
import dk.magenta.databroker.register.conditions.ConditionList;
import dk.magenta.databroker.register.conditions.GlobalCondition;
import dk.magenta.databroker.util.TransactionCallback;
import dk.magenta.databroker.util.Util;
import dk.magenta.databroker.util.objectcontainers.StringList;
import org.apache.log4j.Logger;
import org.hibernate.StatelessSession;

import javax.persistence.*;
import java.util.*;

/**
 * Created by lars on 27-01-15.
 */
interface CompanyRepositoryCustom extends EntityRepositoryCustom<CompanyEntity, Long> {
    public void detach(CompanyEntity companyEntity);
    public void detach(CompanyVersionEntity companyVersionEntity);

}


public class CompanyRepositoryImpl extends EntityRepositoryImplementation<CompanyEntity, Long> implements CompanyRepositoryCustom {

    public List<TransactionCallback> getBulkwireCallbacks() {
        ArrayList<TransactionCallback> transactionCallbacks = new ArrayList<TransactionCallback>();
        transactionCallbacks.add(new TransactionCallback() {
            CompanyRepositoryImpl repositoryImplementation = CompanyRepositoryImpl.this;
            Logger log = repositoryImplementation.log;
            @Override
            public void run(Session session) {
                log.info("Updating references between companies and addresses");
                double time = Util.getTime();
                /*repositoryImplementation.query("update cvr_company_version companyversion " +
                        "set companyversion.postal_address_enheds_adresse= " +
                        "(select address.id from dawa_enhedsadresse address where address.descriptor = companyversion.postal_address_descriptor)" +
                        " where companyversion.postal_address_enheds_adresse = NULL", session);*/


                repositoryImplementation.runNativeQuery("update cvr_company_version companyversion " +
                        "join dawa_enhedsadresse address on companyversion.postal_address_descriptor=address.descriptor " +
                        "set companyversion.postal_address_enheds_adresse=address.id " +
                        "where companyversion.postal_address_enheds_adresse is NULL");

                repositoryImplementation.runNativeQuery("update cvr_companyunit_version companyversion " +
                        "join dawa_enhedsadresse address on companyversion.location_address_descriptor=address.descriptor " +
                        "set companyversion.location_address_enheds_adresse=address.id " +
                        "where companyversion.location_address_enheds_adresse is NULL");
                log.info("References updated in " + (Util.getTime() - time) + " ms");
            }
        });


        return transactionCallbacks;
    }


    @Override
    public Collection<CompanyEntity> search(SearchParameters parameters) {

        StringList hql = new StringList();
        StringList join = new StringList();
        ConditionList conditions = new ConditionList(ConditionList.Operator.AND);

        hql.append("select distinct "+CompanyEntity.databaseKey+" from CompanyEntity as "+CompanyEntity.databaseKey);
        join.setPrefix("join ");

        conditions.addCondition(CompanyEntity.virksomhedCondition(parameters));
        conditions.addCondition(CompanyEntity.cvrCondition(parameters));

        if (parameters.hasAny(Key.LAND, Key.KOMMUNE, Key.VEJ, Key.POST, Key.HUSNR, Key.ETAGE, Key.DOER, Key.LOKALITET)) {
            conditions.addCondition(CompanyEntity.landCondition(parameters));
            conditions.addCondition(CompanyEntity.kommuneCondition(parameters));
            conditions.addCondition(CompanyEntity.vejCondition(parameters));
            conditions.addCondition(CompanyEntity.postCondition(parameters));
            conditions.addCondition(CompanyEntity.lokalitetCondition(parameters));
            conditions.addCondition(CompanyEntity.husnrCondition(parameters));
            conditions.addCondition(CompanyEntity.etageCondition(parameters));
            conditions.addCondition(CompanyEntity.doerCondition(parameters));


            /*join.append(CompanyEntity.joinCompanyUnit());
            join.append(CompanyUnitEntity.joinEnhedsAdresse());
            join.append(EnhedsAdresseEntity.joinAdgangsAdresse());

            conditions.addCondition(EnhedsAdresseEntity.doerCondition(parameters));
            conditions.addCondition(EnhedsAdresseEntity.etageCondition(parameters));
            conditions.addCondition(AdgangsAdresseEntity.husnrCondition(parameters));
            conditions.addCondition(AdgangsAdresseEntity.bnrCondition(parameters));

            if (parameters.hasAny(Key.LAND, Key.KOMMUNE, Key.VEJ, Key.POST)) {
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
            }*/
        }

        conditions.addCondition(CompanyEntity.emailCondition(parameters));
        conditions.addCondition(CompanyEntity.phoneCondition(parameters));
        conditions.addCondition(CompanyEntity.faxCondition(parameters));

        conditions.addCondition(CompanyEntity.primaryIndustryCondition(parameters));
        conditions.addCondition(CompanyEntity.secondaryIndustryCondition(parameters));
        conditions.addCondition(CompanyEntity.anyIndustryCondition(parameters));

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

        return this.query(hql, conditions, parameters.getGlobalCondition());
    }

    public void detach(CompanyEntity companyEntity) {
        this.entityManager.detach(companyEntity);
    }
    public void detach(CompanyVersionEntity companyVersionEntity) {
        this.entityManager.detach(companyVersionEntity);
    }

    @Override
    public HashSet<Long> getKnownDescriptors() {
        Query q = this.entityManager.createQuery("select " + CompanyEntity.databaseKey + ".cvrNummer from CompanyEntity as " + CompanyEntity.databaseKey);
        return new HashSet<Long>(q.getResultList());
    }


    @Override
    public CompanyEntity getByDescriptor(Long descriptor) {
        return this.getByDescriptor(descriptor, null);
    }

    @Override
    public CompanyEntity getByDescriptor(Long descriptor, Session session) {
        if (!this.hasKnownDescriptor(descriptor, true)) {
            return null;
        }
        ConditionList conditions = new ConditionList();
        conditions.addCondition(CompanyEntity.descriptorCondition(descriptor));
        final String key = this.getRandomKey();
        final String hql = "select distinct "+CompanyEntity.databaseKey+" from CompanyEntity as "+CompanyEntity.databaseKey+" where "+conditions.getWhere(key);
        Collection<CompanyEntity> companyEntities = this.query(hql, conditions.getParameters(key), GlobalCondition.singleCondition, session);
        return companyEntities.size() > 0 ? companyEntities.iterator().next() : null;
    }
}