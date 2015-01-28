package dk.magenta.databroker.cvr.model.company;

import dk.magenta.databroker.dawa.model.SearchParameters;
import dk.magenta.databroker.dawa.model.SearchParameters.Key;
import dk.magenta.databroker.register.RepositoryUtil;
import dk.magenta.databroker.register.conditions.ConditionList;
import dk.magenta.databroker.util.objectcontainers.StringList;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Collection;
import java.util.Map;

/**
 * Created by lars on 27-01-15.
 */
interface CompanyRepositoryCustom {
    public Collection<CompanyEntity> search(SearchParameters parameters, boolean printQuery);
}


public class CompanyRepositoryImpl implements CompanyRepositoryCustom {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public Collection<CompanyEntity> search(SearchParameters parameters, boolean printQuery) {

        StringList hql = new StringList();
        StringList join = new StringList();
        ConditionList conditions = new ConditionList(ConditionList.Operator.AND);

        hql.append("select distinct company from CompanyEntity as company");
        join.setPrefix("join ");

        if (parameters.has(Key.VIRKSOMHED)) {
            join.append("company.latestVersion as companyVersion");
            conditions.addCondition(RepositoryUtil.whereField(parameters.get(Key.VIRKSOMHED), null, "companyVersion.name"));
        }
        if (parameters.has(Key.CVR)) {
            conditions.addCondition(RepositoryUtil.whereField(parameters.get(Key.CVR), null, "company.cvrNummer"));
        }
        if (parameters.hasAny(Key.HUSNR, Key.ETAGE, Key.LAND, Key.KOMMUNE, Key.VEJ)) {
            join.append("company.units as unit");

            join.append("unit.latestVersion.address as adresse");
            join.append("adresse.latestVersion as adresseVersion");
            join.append("adresseVersion.adgangsadresse as adgang");
            if (parameters.has(Key.HUSNR)) {
                conditions.addCondition(RepositoryUtil.whereField(parameters.get(Key.HUSNR), null, "adgang.husnr"));
            }
            if (parameters.has(Key.ETAGE)) {
                conditions.addCondition(RepositoryUtil.whereField(parameters.get(Key.ETAGE), null, "adresseVersion.etage"));
            }
            if (parameters.has(Key.DOER)) {
                conditions.addCondition(RepositoryUtil.whereField(parameters.get(Key.DOER), null, "adresseVersion.doer"));
            }
            if (parameters.has(Key.BNR)) {
                conditions.addCondition(RepositoryUtil.whereField(parameters.get(Key.BNR), null, "adgang.bnr"));
            }


            if (parameters.hasAny(Key.LAND, Key.KOMMUNE, Key.VEJ)) {
                join.append("adgang.vejstykke as vejstykke");
                if (parameters.has(Key.VEJ)) {
                    conditions.addCondition(RepositoryUtil.whereField(parameters.get(Key.VEJ), "vejstykke.kode", "vejstykke.latestVersion.vejnavn"));
                }
                if (parameters.hasAny(Key.LAND, Key.KOMMUNE)) {
                    join.append("vejstykke.kommune as kommune");
                    if (parameters.has(Key.LAND)) {
                        conditions.addCondition(RepositoryUtil.whereFieldLand(parameters.get(Key.LAND)));
                    }
                    if (parameters.has(Key.KOMMUNE)) {
                        conditions.addCondition(RepositoryUtil.whereField(parameters.get(Key.KOMMUNE), "kommune.kode", "kommune.navn"));
                    }
                }

                if (parameters.has(Key.POST)) {
                    join.append("vejstykke.latestVersion.postnumre as post");
                    conditions.addCondition(RepositoryUtil.whereField(parameters.get(Key.POST), "post.latestVersion.nr", "post.latestVersion.navn"));
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
}



