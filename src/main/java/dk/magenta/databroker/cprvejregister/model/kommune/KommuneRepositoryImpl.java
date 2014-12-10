package dk.magenta.databroker.cprvejregister.model.kommune;

import dk.magenta.databroker.cprvejregister.dataproviders.objectcontainers.StringList;
import dk.magenta.databroker.cprvejregister.model.Condition;
import dk.magenta.databroker.cprvejregister.model.GlobalCondition;
import dk.magenta.databroker.cprvejregister.model.RepositoryUtil;
import dk.magenta.databroker.cprvejregister.model.navngivenvej.NavngivenVejEntity;

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

interface KommuneRepositoryCustom {
    public Collection<KommuneEntity> search(String kommune, GlobalCondition globalCondition);
}

public class KommuneRepositoryImpl implements KommuneRepositoryCustom {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public Collection<KommuneEntity> search(String kommune, GlobalCondition globalCondition) {
        StringList hql = new StringList();
        StringList join = new StringList();
        Condition.resetCounter();
        ArrayList<Condition> conditions = new ArrayList<Condition>();

        hql.append("select kommune from KommuneEntity as kommune");
        join.setPrefix("join ");

        if (kommune != null) {
            conditions.add(RepositoryUtil.whereField(kommune, "kommune.kommunekode", "kommune.latestVersion.navn"));
        }
        if (globalCondition != null) {
            conditions.addAll(globalCondition.whereField("kommune"));
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
        hql.append("order by kommune.kommunekode");

        System.out.println(hql.join(" \n"));
        Query q = this.entityManager.createQuery(hql.join(" "));
        for (Condition c : conditions) {
            q.setParameter(c.getKey(), c.getValue());
            System.out.println(c.getKey()+" = "+c.getValue());
        }
        return q.getResultList();
    }
}
