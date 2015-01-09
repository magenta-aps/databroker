package dk.magenta.databroker.core.controller;

import dk.magenta.databroker.dawa.model.DawaModel;
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
    private DawaModel model;

    @RequestMapping("/doc/kommune")
    public ModelAndView kommune() {
        return new ModelAndView("doc/kommune");
    }

    @RequestMapping("/doc/vej")
    public ModelAndView veje() {
        VejstykkeEntity vejstykkeEntity = this.model.getVejstykke(101, 7064);

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("uuid", vejstykkeEntity != null ? vejstykkeEntity.getUuid() : "e9032c69-98bf-4e18-bc94-1a5e5f8901e4");

        return new ModelAndView("doc/vej", model);
    }

    @RequestMapping("/doc/postnr")
    public ModelAndView postnr() {
        return new ModelAndView("doc/postnummer");
    }


    @RequestMapping("/doc/adgangsadresse")
    public ModelAndView adgangsadresse() {
        Collection<AdgangsAdresseEntity> adgangsAdresseEntities = this.model.getAdgangsAdresse("gl", null, new String[]{"958"}, new String[]{"18"}, new String[]{"2"}, null);
        AdgangsAdresseEntity adgangsAdresseEntity = adgangsAdresseEntities.size() > 0 ? adgangsAdresseEntities.iterator().next() : null;

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("uuid", adgangsAdresseEntity != null ? adgangsAdresseEntity.getUuid() : "hephey");

        return new ModelAndView("doc/adgangsadresse", model);
    }


    @RequestMapping("/doc/adresse")
    public ModelAndView adresse() {
        Collection<EnhedsAdresseEntity> enhedsAdresseEntities = this.model.getEnhedsAdresse(null, null, new String[]{"330"}, new String[]{"779"}, new String[]{"35"}, new String[]{"2"}, null, null);
        EnhedsAdresseEntity enhedsAdresseEntity = enhedsAdresseEntities.size() > 0 ? enhedsAdresseEntities.iterator().next() : null;

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("uuid", enhedsAdresseEntity != null ? enhedsAdresseEntity.getUuid() : "hephey");

        return new ModelAndView("doc/adresse", model);
    }


    @RequestMapping("/doc/lokalitet")
    public ModelAndView lokalitet() {
        Collection<LokalitetEntity> lokalitetEntities = this.model.getLokalitet(null, null, null, null, new String[]{"Nuuk"}, null);
        LokalitetEntity lokalitetEntity = lokalitetEntities.size() > 0 ? lokalitetEntities.iterator().next() : null;

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("uuid", lokalitetEntity != null ? lokalitetEntity.getUuid() : "hephey");

        return new ModelAndView("doc/lokalitet", model);
    }
}
