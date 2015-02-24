package dk.magenta.databroker.cvr.model.company;

import dk.magenta.databroker.cvr.model.companyunit.CompanyUnitEntity;
import dk.magenta.databroker.dawa.model.SearchParameters;
import dk.magenta.databroker.dawa.model.SearchParameters.Key;
import dk.magenta.databroker.dawa.model.adgangsadresse.AdgangsAdresseEntity;
import dk.magenta.databroker.dawa.model.enhedsadresser.EnhedsAdresseEntity;
import dk.magenta.databroker.dawa.model.postnummer.PostNummerEntity;
import dk.magenta.databroker.dawa.model.temaer.KommuneEntity;
import dk.magenta.databroker.dawa.model.vejstykker.VejstykkeEntity;
import dk.magenta.databroker.register.conditions.ConditionList;
import dk.magenta.databroker.util.objectcontainers.StringList;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import java.util.Collection;
import java.util.Map;

/**
 * Created by lars on 27-01-15.
 */
interface CompanyRepositoryCustom {
    public Collection<CompanyEntity> search(SearchParameters parameters, boolean printQuery);
    public void clear();
}


public class CompanyRepositoryImpl implements CompanyRepositoryCustom {

    private EntityManager entityManager;

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    public void setEntityManager(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public Collection<CompanyEntity> search(SearchParameters parameters, boolean printQuery) {

        StringList hql = new StringList();
        StringList join = new StringList();
        ConditionList conditions = new ConditionList(ConditionList.Operator.AND);

        hql.append("select distinct "+CompanyEntity.databaseKey+" from CompanyEntity as "+CompanyEntity.databaseKey);
        join.setPrefix("join ");

        conditions.addCondition(CompanyEntity.virksomhedCondition(parameters));
        conditions.addCondition(CompanyEntity.cvrCondition(parameters));

        if (parameters.hasAny(Key.HUSNR, Key.ETAGE, Key.LAND, Key.KOMMUNE, Key.VEJ, Key.POST)) {
            join.append(CompanyEntity.joinCompanyUnit());
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
            }
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

        if (printQuery) {
            System.out.println(hql.join(" \n"));
        }
        Query q = this.entityManager.createQuery(hql.join(" "));
        q.setMaxResults(1000);
        Map<String, Object> queryParameters = conditions.getParameters();
        for (String key : queryParameters.keySet()) {
            if (printQuery) {
                System.out.println(key + " = " + queryParameters.get(key));
            }
            q.setParameter(key, queryParameters.get(key));
        }
        return q.getResultList();
    }

    public void clear() {
        if (this.entityManager != null) {
            this.entityManager.flush();
            this.entityManager.clear();
        }
    }
}



