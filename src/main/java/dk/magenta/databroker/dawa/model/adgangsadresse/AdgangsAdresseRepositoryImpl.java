package dk.magenta.databroker.dawa.model.adgangsadresse;

import dk.magenta.databroker.dawa.model.RepositoryImplementation;
import dk.magenta.databroker.dawa.model.SearchParameters;
import dk.magenta.databroker.dawa.model.SearchParameters.Key;
import dk.magenta.databroker.dawa.model.lokalitet.LokalitetEntity;
import dk.magenta.databroker.dawa.model.postnummer.PostNummerEntity;
import dk.magenta.databroker.dawa.model.temaer.KommuneEntity;
import dk.magenta.databroker.dawa.model.vejstykker.VejstykkeEntity;
import dk.magenta.databroker.register.conditions.ConditionList;
import dk.magenta.databroker.register.conditions.GlobalCondition;
import dk.magenta.databroker.util.objectcontainers.StringList;

import java.util.Collection;

/**
 * Created by lars on 09-12-14.
 */

interface AdgangsAdresseRepositoryCustom {
    public Collection<AdgangsAdresseEntity> search(SearchParameters parameters, boolean printQuery);
    public void bulkWireReferences();
    public void clear();
    public AdgangsAdresseEntity getByDescriptor(String descriptor);
}

public class AdgangsAdresseRepositoryImpl extends RepositoryImplementation<AdgangsAdresseEntity> implements AdgangsAdresseRepositoryCustom {


    public AdgangsAdresseEntity getByDescriptor(String descriptor) {
        StringList hql = new StringList();
        hql.append("select distinct "+AdgangsAdresseEntity.databaseKey+" from AdgangsAdresseEntity as "+AdgangsAdresseEntity.databaseKey);
        ConditionList conditions = new ConditionList();
        conditions.addCondition(AdgangsAdresseEntity.descriptorCondition(descriptor));
        hql.append("where");
        hql.append(conditions.getWhere());
        Collection<AdgangsAdresseEntity> adgangsAdresseEntities = this.query(hql, conditions, new GlobalCondition(null,null,0,1), false);
        return adgangsAdresseEntities.size() > 0 ? adgangsAdresseEntities.iterator().next() : null;
    }


    @Override
    public Collection<AdgangsAdresseEntity> search(SearchParameters parameters, boolean printQuery) {

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

        return this.query(hql, conditions, parameters.getGlobalCondition(), printQuery);
    }

    public void bulkWireReferences() {
        System.out.println("Updating references");
        this.entityManager.createNativeQuery("update dawa_adgangsadresse adresse join dawa_vejstykke vej on adresse.vejstykke_descriptor=vej.descriptor set adresse.vejstykke_id=vej.id").executeUpdate();
        System.out.println("References updated");
    }
}
