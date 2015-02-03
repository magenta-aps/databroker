package dk.magenta.databroker.dawa.model.enhedsadresser;

import dk.magenta.databroker.dawa.model.RepositoryImplementation;
import dk.magenta.databroker.dawa.model.SearchParameters;
import dk.magenta.databroker.dawa.model.SearchParameters.Key;
import dk.magenta.databroker.dawa.model.adgangsadresse.AdgangsAdresseEntity;
import dk.magenta.databroker.dawa.model.postnummer.PostNummerEntity;
import dk.magenta.databroker.dawa.model.temaer.KommuneEntity;
import dk.magenta.databroker.dawa.model.vejstykker.VejstykkeEntity;
import dk.magenta.databroker.register.RepositoryUtil;
import dk.magenta.databroker.register.conditions.ConditionList;
import dk.magenta.databroker.util.objectcontainers.StringList;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by lars on 09-12-14.
 */

interface EnhedsAdresseRepositoryCustom {
    //public EnhedsAdresseVersionEntity getByValues(int kommuneKode, int vejKode, String husnr, String etage, String doer);
    public Collection<EnhedsAdresseEntity> search(SearchParameters parameters, boolean printQuery);
}

public class EnhedsAdresseRepositoryImpl extends RepositoryImplementation<EnhedsAdresseEntity> implements EnhedsAdresseRepositoryCustom {

    /*public EnhedsAdresseVersionEntity getByValues(int kommuneKode, int vejKode, String husnr, String etage, String doer) {
        String descriptor = EnhedsAdresseVersionEntity.buildDescriptor(kommuneKode,vejKode,husnr,etage,doer);
        Query query = this.entityManager.createQuery("select distinct version from EnhedsAdresseVersionEntity as version where version.descriptor=:descriptor and version.entity.latestVersion=version");
        query.setParameter("descriptor", descriptor);
        query.setMaxResults(1);
        List list = query.getResultList();
        if (list.size() > 0) {
            return (EnhedsAdresseVersionEntity) list.get(0);
        }
        return null;
    }*/



    @Override
    public Collection<EnhedsAdresseEntity> search(SearchParameters parameters, boolean printQuery) {

        StringList hql = new StringList();
        StringList join = new StringList();
        ConditionList conditions = new ConditionList(ConditionList.Operator.AND);

        join.setPrefix("join ");
        hql.append("select distinct " + EnhedsAdresseEntity.databaseKey + " from EnhedsAdresseEntity as " + EnhedsAdresseEntity.databaseKey);

        if (parameters.hasAny(Key.LAND, Key.KOMMUNE, Key.VEJ, Key.POST, Key.HUSNR, Key.BNR)) {

            join.append(EnhedsAdresseEntity.joinAdgangsAdresse());

            if (parameters.hasAny(Key.LAND, Key.KOMMUNE, Key.VEJ)) {
                join.append(AdgangsAdresseEntity.joinVej());

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

        return this.query(hql, conditions, parameters.getGlobalCondition(), printQuery);
    }

    public void generateDescriptors() {
        Query query = this.entityManager.createNativeQuery("update dawa_enhedsadresse_version enhed" +
                " join dawa_adgangsadresse adresse on enhed.adgangsadresse_id = adresse.id " +
                " join dawa.vejstykke vej on adresse.vejstykke.id = vej.id " +
                " join dawa.kommune kommune on vej.kommune_id = kommune.id " +
                "set enhed.descriptor=CONCAT(CAST(kommune.kode as CHAR), ':', CAST(vej.kode as CHAR), ':', adresse.husnr, ':', enhed.etage, ':', enhed.doer)");
        query.executeUpdate();
    }
}
