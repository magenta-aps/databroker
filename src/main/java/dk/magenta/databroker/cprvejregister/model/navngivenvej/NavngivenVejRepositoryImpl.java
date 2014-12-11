package dk.magenta.databroker.cprvejregister.model.navngivenvej;

import dk.magenta.databroker.cprvejregister.dataproviders.objectcontainers.StringList;
import dk.magenta.databroker.cprvejregister.model.Condition;
import dk.magenta.databroker.cprvejregister.model.GlobalCondition;
import dk.magenta.databroker.cprvejregister.model.RepositoryUtil;
import dk.magenta.databroker.cprvejregister.model.husnummer.HusnummerEntity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Pattern;

/**
 * Created by lars on 09-12-14.
 */

interface NavngivenVejRepositoryCustom {
    public Collection<NavngivenVejEntity> search(String kommune, String vej, GlobalCondition globalCondition);
}

public class NavngivenVejRepositoryImpl implements NavngivenVejRepositoryCustom {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public Collection<NavngivenVejEntity> search(String kommune, String vej, GlobalCondition globalCondition) {

        StringList hql = new StringList();
        StringList join = new StringList();
        ArrayList<Condition> conditions = new ArrayList<Condition>();

        hql.append("select vej from NavngivenVejEntity as vej");

        join.setPrefix("join ");
        join.append("vej.latestVersion vejversion");
        join.append("vejversion.kommunedeleAfNavngivenVej delvej");
        if (kommune != null) {
            join.append("delvej.kommune kommune");
        }

        if (kommune != null) {
            conditions.add(RepositoryUtil.whereField(kommune, "kommune.kommunekode", "kommune.latestVersion.navn"));
        }
        if (vej != null) {
            conditions.add(RepositoryUtil.whereField(vej, "delvej.vejkode", "vejversion.vejnavn"));
        }
        if (globalCondition != null) {
            conditions.addAll(globalCondition.whereField("vej"));
        }

        // our conditions list should now be complete

        for (Condition c : conditions) {
            if (c.hasRequiredJoin()) {
                join.append(c.getRequiredJoin());
            }
        }

        // our join list should now be complete

        if (join.size()>0) {
            hql.append(join.join(" "));
        }
        if (conditions.size() > 0) {
            hql.append(Condition.concatWhere(conditions));
        }
        hql.append("order by vejversion.vejnavn");

        System.out.println(hql.join(" \n"));
        for (Condition c : conditions) {
            System.out.println(c.getKey()+" = "+c.getValue());
        }


        Query q = this.entityManager.createQuery(hql.join(" "));
        for (Condition c : conditions) {
            q.setParameter(c.getKey(), c.getValue());
        }
        return q.getResultList();
    }
}
