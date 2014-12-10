package dk.magenta.databroker.cprvejregister.model.kommune;

import dk.magenta.databroker.cprvejregister.dataproviders.objectcontainers.StringList;
import dk.magenta.databroker.cprvejregister.model.RepositoryUtil;
import dk.magenta.databroker.cprvejregister.model.navngivenvej.NavngivenVejEntity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Collection;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Created by lars on 09-12-14.
 */

interface KommuneRepositoryCustom {
    public Collection<KommuneEntity> search(String kommune);
}

public class KommuneRepositoryImpl implements KommuneRepositoryCustom {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public Collection<KommuneEntity> search(String kommune) {
        StringList hql = new StringList();
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        Object[] params;

        hql.append("select kommune from KommuneEntity as kommune");

        StringList where = new StringList();
        if (kommune != null) {
            params = RepositoryUtil.whereField(kommune, "kommune.kommunekode", "kommune.latestVersion.navn");
            where.append(params[0] + " " + params[1] + " :kommune");
            parameters.put("kommune", params[2]);
        }

        if (where.size()>0) {
            hql.append("where ");
            hql.append(where.join(" and "));
        }
        hql.append("order by kommune.kommunekode");

        Query q = this.entityManager.createQuery(hql.join(" "));
        for (String key : parameters.keySet()) {
            q.setParameter(key, parameters.get(key));
        }
        return q.getResultList();
    }
}
