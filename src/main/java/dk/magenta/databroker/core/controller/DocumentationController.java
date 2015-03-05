package dk.magenta.databroker.core.controller;

import dk.magenta.databroker.cvr.model.CvrModel;
import dk.magenta.databroker.cvr.model.company.CompanyEntity;
import dk.magenta.databroker.dawa.model.DawaModel;
import dk.magenta.databroker.dawa.model.SearchParameters;
import dk.magenta.databroker.dawa.model.SearchParameters.Key;
import dk.magenta.databroker.dawa.model.adgangsadresse.AdgangsAdresseEntity;
import dk.magenta.databroker.dawa.model.enhedsadresser.EnhedsAdresseEntity;
import dk.magenta.databroker.dawa.model.lokalitet.LokalitetEntity;
import dk.magenta.databroker.dawa.model.vejstykker.VejstykkeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by lars on 09-01-15.
 */
@Controller
public class DocumentationController {

    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private DawaModel dawaModel;

    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private CvrModel cvrModel;

    @RequestMapping("/doc/kommune")
    public ModelAndView kommune() {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("nav","kommune");

        return new ModelAndView("doc/kommune", model);
    }

    @RequestMapping("/doc/vej")
    public ModelAndView veje() {
        Map<String, Object> model = new HashMap<String, Object>();
        VejstykkeEntity vejstykkeEntity = null;
        try {
            vejstykkeEntity = this.dawaModel.getVejstykke(101, 7064);
        } catch (Exception e) {}
        model.put("uuid", vejstykkeEntity != null ? vejstykkeEntity.getUuid() : "e9032c69-98bf-4e18-bc94-1a5e5f8901e4");
        model.put("nav","vej");
        return new ModelAndView("doc/vej", model);
    }

    @RequestMapping("/doc/postnr")
    public ModelAndView postnr() {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("nav","postnr");
        return new ModelAndView("doc/postnummer", model);
    }


    @RequestMapping("/doc/adgangsadresse")
    public ModelAndView adgangsadresse() {
        Map<String, Object> model = new HashMap<String, Object>();
        AdgangsAdresseEntity adgangsAdresseEntity = null;
        try {
            SearchParameters parameters = new SearchParameters();
            parameters.put(Key.LAND, "gl");
            parameters.put(Key.KOMMUNE, 958);
            parameters.put(Key.VEJ, 18);
            parameters.put(Key.HUSNR, 2);
            Collection<AdgangsAdresseEntity> adgangsAdresseEntities = this.dawaModel.getAdgangsAdresse(parameters);
            adgangsAdresseEntity = adgangsAdresseEntities.size() > 0 ? adgangsAdresseEntities.iterator().next() : null;
        } catch (Exception e) {}
        model.put("uuid", adgangsAdresseEntity != null ? adgangsAdresseEntity.getUuid() : "bf37eed9-5f28-4c45-acf6-41c932794a6b");
        model.put("nav","adgangsadresse");

        return new ModelAndView("doc/adgangsadresse", model);
    }


    @RequestMapping("/doc/adresse")
    public ModelAndView adresse() {
        Map<String, Object> model = new HashMap<String, Object>();
        EnhedsAdresseEntity enhedsAdresseEntity = null;
        try {
            SearchParameters parameters = new SearchParameters();
            parameters.put(Key.KOMMUNE, 330);
            parameters.put(Key.VEJ, 779);
            parameters.put(Key.HUSNR, 35);
            parameters.put(Key.ETAGE, 2);
            Collection<EnhedsAdresseEntity> enhedsAdresseEntities = this.dawaModel.getEnhedsAdresse(parameters);
            enhedsAdresseEntity = enhedsAdresseEntities.size() > 0 ? enhedsAdresseEntities.iterator().next() : null;
        } catch (Exception e) {}
        model.put("uuid", enhedsAdresseEntity != null ? enhedsAdresseEntity.getUuid() : "dd308d33-a60f-4506-b410-4397b038d20c");
        model.put("nav","adresse");

        return new ModelAndView("doc/adresse", model);
    }


    @RequestMapping("/doc/lokalitet")
    public ModelAndView lokalitet() {
        Map<String, Object> model = new HashMap<String, Object>();
        LokalitetEntity lokalitetEntity = null;
        try {
            Collection<LokalitetEntity> lokalitetEntities = this.dawaModel.getLokalitet(new SearchParameters(null, null, null, new String[]{"Nuuk"}, null, null, null, null, null));
            lokalitetEntity = lokalitetEntities.size() > 0 ? lokalitetEntities.iterator().next() : null;
        } catch (Exception e) {}
        model.put("uuid", lokalitetEntity != null ? lokalitetEntity.getUuid() : "d718816e-d054-4cd4-85ec-85f50e5a6779");
        model.put("nav","lokalitet");

        return new ModelAndView("doc/lokalitet", model);
    }


    @RequestMapping("/doc/virksomhed")
    public ModelAndView virksomhed() {

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("cvr", "25052943");
        model.put("nav","virksomhed");

        return new ModelAndView("doc/virksomhed", model);
    }
/*
    @RequestMapping("/doc/produktionsEnhed")
    public ModelAndView produktionsEnhed() {

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("pNummer", "1019601052");
        model.put("nav","produktionsEnhed");

        return new ModelAndView("doc/produktionsEnhed", model);
    }*/
}
