package dk.magenta.databroker.cvr.model.company;

import dk.magenta.databroker.core.model.RepositoryImplementation;
import dk.magenta.databroker.cvr.model.companyunit.CompanyUnitEntity;
import dk.magenta.databroker.dawa.model.SearchParameters;
import dk.magenta.databroker.dawa.model.SearchParameters.Key;
import dk.magenta.databroker.dawa.model.adgangsadresse.AdgangsAdresseEntity;
import dk.magenta.databroker.dawa.model.enhedsadresser.EnhedsAdresseEntity;
import dk.magenta.databroker.dawa.model.postnummer.PostNummerEntity;
import dk.magenta.databroker.dawa.model.temaer.KommuneEntity;
import dk.magenta.databroker.dawa.model.vejstykker.VejstykkeEntity;
import dk.magenta.databroker.register.conditions.ConditionList;
import dk.magenta.databroker.register.conditions.GlobalCondition;
import dk.magenta.databroker.util.objectcontainers.StringList;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

/**
 * Created by lars on 27-01-15.
 */
interface CompanyRepositoryCustom {
    public Collection<CompanyEntity> search(SearchParameters parameters);
    public void clear();
    public CompanyEntity getByCvr(String cvrNummer);
    public List<String> getCvrNumbers();
}


public class CompanyRepositoryImpl extends RepositoryImplementation<CompanyEntity> implements CompanyRepositoryCustom {

    @Override
    public Collection<CompanyEntity> search(SearchParameters parameters) {

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

        return this.query(hql, conditions, parameters.getGlobalCondition());
    }

    public void clear() {
        if (this.entityManager != null) {
            this.entityManager.flush();
            this.entityManager.clear();
        }
    }


    public CompanyEntity getByCvr(String cvrNummer) {
        StringList hql = new StringList();
        hql.append("select distinct "+CompanyEntity.databaseKey+" from CompanyEntity as "+CompanyEntity.databaseKey);
        ConditionList conditions = new ConditionList();
        conditions.addCondition(CompanyEntity.cvrCondition(cvrNummer));
        hql.append("where");
        hql.append(conditions.getWhere());
        Collection<CompanyEntity> companyEntities = this.query(hql, conditions, GlobalCondition.singleCondition);
        return companyEntities.size() > 0 ? companyEntities.iterator().next() : null;
    }



    public List<String> getCvrNumbers() {
        Query q = this.entityManager.createQuery("select " + CompanyEntity.databaseKey + ".cvrNummer from CompanyEntity as " + CompanyEntity.databaseKey);
                return q.getResultList();
    }
}