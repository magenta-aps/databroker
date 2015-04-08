package dk.magenta.databroker.cvr.model.companyunit;

import dk.magenta.databroker.core.model.RepositoryImplementation;
import dk.magenta.databroker.cvr.model.company.CompanyEntity;
import dk.magenta.databroker.dawa.model.SearchParameters;
import dk.magenta.databroker.register.conditions.ConditionList;
import dk.magenta.databroker.register.conditions.GlobalCondition;
import dk.magenta.databroker.util.Util;
import dk.magenta.databroker.util.objectcontainers.StringList;
import org.apache.log4j.Logger;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Created by lars on 02-02-15.
 */


interface CompanyUnitRepositoryCustom {
    public List<TransactionCallback> getBulkwireCallbacks();
    public Collection<CompanyUnitEntity> search(SearchParameters parameters);
    public List<Long> getIdentifiers();
    public CompanyUnitEntity getByIdentifier(long pno);
    public void clear();
    public void detach(CompanyUnitEntity companyUnitEntity);
    public void detach(CompanyUnitVersionEntity companyUnitVersionEntity);
}

public class CompanyUnitRepositoryImpl extends RepositoryImplementation<CompanyUnitEntity> implements CompanyUnitRepositoryCustom {

    private Logger log = Logger.getLogger(CompanyUnitRepositoryImpl.class);

    public List<TransactionCallback> getBulkwireCallbacks() {
        ArrayList<TransactionCallback> transactionCallbacks = new ArrayList<TransactionCallback>();

        transactionCallbacks.add(new TransactionCallback() {
            @Override
            public Object doInTransaction(TransactionStatus transactionStatus) {
                CompanyUnitRepositoryImpl repositoryImplementation = CompanyUnitRepositoryImpl.this;
                repositoryImplementation.log.info("Updating references between units and companies");
                double time = Util.getTime();
                repositoryImplementation.runNativeQuery("update cvr_companyunit_version unitversion " +
                        "join cvr_company company on unitversion.cvr_nummer=company.cvr_nummer " +
                        "set unitversion.company_version_id=company.latest_version_id " +
                        "where unitversion.company_version_id is NULL");
                repositoryImplementation.log.info("References updated in " + (Util.getTime() - time) + " ms");
                return null;
            }
        });


        transactionCallbacks.add(new TransactionCallback() {
            @Override
            public Object doInTransaction(TransactionStatus transactionStatus) {
                CompanyUnitRepositoryImpl repositoryImplementation = CompanyUnitRepositoryImpl.this;
                repositoryImplementation.log.info("Updating references between units and addresses");
                double time = Util.getTime();
                repositoryImplementation.runNativeQuery("update cvr_companyunit_version unitversion " +
                        "join dawa_enhedsadresse address on unitversion.postal_address_descriptor=address.descriptor " +
                        "set unitversion.postal_address_enheds_adresse=address.id " +
                        "where unitversion.postal_address_enheds_adresse is NULL");
                repositoryImplementation.runNativeQuery("update cvr_companyunit_version unitversion " +
                        "join dawa_enhedsadresse address on unitversion.location_address_descriptor=address.descriptor " +
                        "set unitversion.location_address_enheds_adresse=address.id " +
                        "where unitversion.location_address_enheds_adresse is NULL");
                repositoryImplementation.log.info("References updated in " + (Util.getTime() - time) + " ms");
                return null;
            }
        });


        //System.out.println("getEntityCount: "+this.getSession().getStatistics().getEntityCount());
        //System.out.println("getCollectionCount: "+this.getSession().getStatistics().getCollectionCount());


        return transactionCallbacks;
    }


    @Override
    public Collection<CompanyUnitEntity> search(SearchParameters parameters) {

        StringList hql = new StringList();
        StringList join = new StringList();
        ConditionList conditions = new ConditionList(ConditionList.Operator.AND);

        hql.append("select distinct "+CompanyUnitEntity.databaseKey+" from CompanyUnitEntity as "+CompanyUnitEntity.databaseKey);
        join.setPrefix("join ");

        conditions.addCondition(CompanyUnitEntity.nameCondition(parameters));
        conditions.addCondition(CompanyUnitEntity.cvrCondition(parameters));
        conditions.addCondition(CompanyUnitEntity.pnoCondition(parameters));

        if (parameters.hasAny(SearchParameters.Key.LAND, SearchParameters.Key.KOMMUNE, SearchParameters.Key.VEJ, SearchParameters.Key.POST, SearchParameters.Key.HUSNR, SearchParameters.Key.ETAGE, SearchParameters.Key.DOER, SearchParameters.Key.LOKALITET)) {
            conditions.addCondition(CompanyUnitEntity.landCondition(parameters));
            conditions.addCondition(CompanyUnitEntity.kommuneCondition(parameters));
            conditions.addCondition(CompanyUnitEntity.vejCondition(parameters));
            conditions.addCondition(CompanyUnitEntity.postCondition(parameters));
            conditions.addCondition(CompanyUnitEntity.lokalitetCondition(parameters));
            conditions.addCondition(CompanyUnitEntity.husnrCondition(parameters));
            conditions.addCondition(CompanyUnitEntity.etageCondition(parameters));
            conditions.addCondition(CompanyUnitEntity.doerCondition(parameters));


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

        conditions.addCondition(CompanyUnitEntity.emailCondition(parameters));
        conditions.addCondition(CompanyUnitEntity.phoneCondition(parameters));
        conditions.addCondition(CompanyUnitEntity.faxCondition(parameters));

        conditions.addCondition(CompanyUnitEntity.primaryIndustryCondition(parameters));
        conditions.addCondition(CompanyUnitEntity.secondaryIndustryCondition(parameters));
        conditions.addCondition(CompanyUnitEntity.anyIndustryCondition(parameters));

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


    public List<Long> getIdentifiers() {
        Query q = this.entityManager.createQuery("select " + CompanyUnitEntity.databaseKey + ".pno from CompanyUnitEntity as " + CompanyUnitEntity.databaseKey);
        return q.getResultList();
    }


    public CompanyUnitEntity getByIdentifier(long pno) {
        final String key = "id_"+ UUID.randomUUID().toString().replace("-","");
        ConditionList conditions = new ConditionList();
        conditions.addCondition(CompanyUnitEntity.pnoCondition(pno));
        final String hql = "select distinct " + CompanyUnitEntity.databaseKey + " from CompanyUnitEntity as " + CompanyUnitEntity.databaseKey + "where" + conditions.getWhere(key);
        Collection<CompanyUnitEntity> companyUnitEntities = this.query(hql, conditions.getParameters(key), GlobalCondition.singleCondition);
        CompanyUnitEntity entity = companyUnitEntities.size() > 0 ? companyUnitEntities.iterator().next() : null;
        companyUnitEntities.clear();
        companyUnitEntities = null;
        return entity;
    }


    public void detach(CompanyUnitEntity companyUnitEntity) {
        this.entityManager.detach(companyUnitEntity);
    }
    public void detach(CompanyUnitVersionEntity companyUnitVersionEntity) {
        this.entityManager.detach(companyUnitVersionEntity);
    }

}
