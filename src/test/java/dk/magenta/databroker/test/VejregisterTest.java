package dk.magenta.databroker.test;

import dk.magenta.databroker.Application;
import dk.magenta.databroker.core.model.DataProviderEntity;
import dk.magenta.databroker.core.model.DataProviderRepository;
import dk.magenta.databroker.core.testmodel.TestAddressRepository;
import dk.magenta.databroker.cprvejregister.dataproviders.LokalitetsRegister;
import dk.magenta.databroker.cprvejregister.dataproviders.MyndighedsRegister;
import dk.magenta.databroker.cprvejregister.dataproviders.PostnummerRegister;
import dk.magenta.databroker.cprvejregister.dataproviders.VejRegister;
import dk.magenta.databroker.cprvejregister.model.*;
import org.json.JSONArray;


import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertTrue;

/**
 * Created by lars on 05-11-14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@IntegrationTest
@SpringApplicationConfiguration(classes = Application.class)
public class VejregisterTest {

    //@SuppressWarnings("SpringJavaAutowiringInspection")

    @Autowired
    private KommuneRepository kommuneRepository;

    public KommuneRepository getKommuneRepository() {
        return kommuneRepository;
    }

    @Autowired
    private KommunedelAfNavngivenVejRepository kommunedelAfNavngivenVejRepository;

    public KommunedelAfNavngivenVejRepository getKommunedelAfNavngivenVejRepository() {
        return kommunedelAfNavngivenVejRepository;
    }

    @Autowired
    private NavngivenVejRepository navngivenVejRepository;

    public NavngivenVejRepository getNavngivenVejRepository() {
        return navngivenVejRepository;
    }

    @Autowired
    private HusnummerRepository husnummerRepository;

    public HusnummerRepository getHusnummerRepository() {
        return husnummerRepository;
    }

    @Autowired
    private AdresseRepository adresseRepository;

    public AdresseRepository getAdresseRepository() {
        return adresseRepository;
    }

    @Autowired
    private PostnummerRepository postnummerRepository;

    public PostnummerRepository getPostnummerRepository() {
        return postnummerRepository;
    }

    public VejregisterTest(){
    }
    @Test
    public void testVejregister() {

        HashMap<String, JpaRepository> repositories = new HashMap<String, JpaRepository>();
        repositories.put("kommuneRepository", this.kommuneRepository);
        repositories.put("kommunedelAfNavngivenVejRepository", this.kommunedelAfNavngivenVejRepository);
        repositories.put("navngivenVejRepository", this.navngivenVejRepository);
        repositories.put("husnummerRepository", this.husnummerRepository);
        repositories.put("adresseRepository", this.adresseRepository);
        repositories.put("postnummerRepository", this.postnummerRepository);

        MyndighedsRegister myndighedsregister = new MyndighedsRegister(new DataProviderEntity());
        myndighedsregister.pull(repositories);

        VejRegister vejregister = new VejRegister(new DataProviderEntity());
        vejregister.pull(repositories);

        LokalitetsRegister lokalitetsregister = new LokalitetsRegister(new DataProviderEntity());
        lokalitetsregister.pull(repositories);

        PostnummerRegister postnummerRegister = new PostnummerRegister(new DataProviderEntity());
        postnummerRegister.pull(repositories);



/*
        String sampleData = "00037071520141031\n" +
                "001040050042003062812000000000000000000190001011200Åbogade             Åbogade                                 \n" +
                "00201010886054 01  tv200010011108 199803190752000000000000Blok T  \n" +
                "00301014870001 999 U200311250832Hf. Amager Strand                 \n" +
                "00401014696002 998 L2002090912002300København S \n" +
                "0050955022801Iliviteqqat                             201004121409190001011409\n" +
                "00601851210028 040ZL199109231200050D03      \n" +
                "00701010004001 999 U1995021018051207  Vesterbro                     \n" +
                "00803360001001 999 U2006122212001Vest                          \n" +
                "00901870958001 025 U20081010115802Egholmskolen, Festsalen       \n" +
                "01002501248002 020 L20140812135820Falkenborgskolen              \n" +
                "01101570007001 999 U1999122910351204Tranegård                     \n" +
                "01205619762019 043 U20061004120661Esbjerg                       \n" +
                "01304612994001 013 U1991092312007777Thomas Kingos,Odense\n" +
                "01401010040001 999 U200708290757072. Sundbyvester               \n" +
                "01501510060001 027 U1991092312005100\n" +
                "01605500542200612221200190001011200200612221200Gånsager            Gånsager\n" +
                "99902086782";

        VejRegister register = new VejRegister(sampleData);
        JSONArray parsedData = register.toJSON();

        JSONArray comparison = new JSONArray("[{\"prodDato\":\"20141031\",\"opgaveNr\":\"370715\",\"type\":\"Start\"}," +
                "{\"vejNavn\":\"Åbogade\",\"vejAdresseringsnavn\":\"Åbogade\",\"timestamp\":\"200306281200\",\"tilVejKode\":\"0000\",\"tilKommuneKode\":\"0000\",\"fraKommuneKode\":\"0000\",\"vejKode\":\"5004\",\"fraVejKode\":\"0000\",\"kommuneKode\":\"0400\",\"startDato\":\"190001011200\",\"type\":\"AktivVej\"}," +
                "{\"lokalitet\":\"Blok T\",\"timestamp\":\"200010011108\",\"sidedoer\":\"tv\",\"husNr\":\"054\",\"vejKode\":\"0886\",\"kommuneKode\":\"0101\",\"startDato\":\"199803190752\",\"type\":\"Bolig\",\"etage\":\"01\"}," +
                "{\"ligeUlige\":\"U\",\"timestamp\":\"200311250832\",\"distriktsTekst\":\"Hf. Amager Strand\",\"bynavn\":\"Hf. Amager Strand\",\"vejKode\":\"4870\",\"kommuneKode\":\"0101\",\"type\":\"ByDistrikt\",\"husNrFra\":\"001\",\"husNrTil\":\"999\"}," +
                "{\"ligeUlige\":\"L\",\"timestamp\":\"200209091200\",\"postNr\":\"2300\",\"distriktsTekst\":\"København S\",\"vejKode\":\"4696\",\"kommuneKode\":\"0101\",\"type\":\"PostDistrikt\",\"husNrFra\":\"002\",\"husNrTil\":\"998\"}," +
                "{\"timestamp\":\"201004121409\",\"notatNr\":\"01\",\"vejKode\":\"0228\",\"kommuneKode\":\"0955\",\"notatLinie\":\"Iliviteqqat\",\"startDato\":\"190001011409\",\"type\":\"NotatVej\"}," +
                "{\"ligeUlige\":\"L\",\"timestamp\":\"199109231200\",\"distriktsTekst\":\"\",\"vejKode\":\"1210\",\"byfornyKode\":\"050D03\",\"kommuneKode\":\"0185\",\"type\":\"ByfornyelsesDistrikt\",\"husNrFra\":\"028\",\"husNrTil\":\"040Z\"}," +
                "{\"ligeUlige\":\"U\",\"timestamp\":\"199502101805\",\"distriktsTekst\":\"Vesterbro\",\"vejKode\":\"0004\",\"kommuneKode\":\"0101\",\"diverseDistriktsKode\":\"07\",\"type\":\"DiverseDistrikt\",\"husNrFra\":\"001\",\"distriktType\":\"12\",\"husNrTil\":\"999\"}," +
                "{\"ligeUlige\":\"U\",\"timestamp\":\"200612221200\",\"distriktsTekst\":\"Vest\",\"evakueringsKode\":\"1\",\"vejKode\":\"0001\",\"kommuneKode\":\"0336\",\"type\":\"EvakueringsDistrikt\",\"husNrFra\":\"001\",\"husNrTil\":\"999\"}," +
                "{\"ligeUlige\":\"U\",\"timestamp\":\"200810101158\",\"kirkeKode\":\"02\",\"distriktsTekst\":\"Egholmskolen, Festsalen\",\"vejKode\":\"0958\",\"kommuneKode\":\"0187\",\"type\":\"KirkeDistrikt\",\"husNrFra\":\"001\",\"husNrTil\":\"025\"}," +
                "{\"ligeUlige\":\"L\",\"timestamp\":\"201408121358\",\"distriktsTekst\":\"Falkenborgskolen\",\"vejKode\":\"1248\",\"kommuneKode\":\"0250\",\"type\":\"SkoleDistrikt\",\"husNrFra\":\"002\",\"skoleKode\":\"20\",\"husNrTil\":\"020\"}," +
                "{\"ligeUlige\":\"U\",\"timestamp\":\"199912291035\",\"distriktsTekst\":\"Tranegård\",\"befolkningsKode\":\"1204\",\"vejKode\":\"0007\",\"kommuneKode\":\"0157\",\"type\":\"BefolkningsDistrikt\",\"husNrFra\":\"001\",\"husNrTil\":\"999\"}," +
                "{\"ligeUlige\":\"U\",\"timestamp\":\"200610041206\",\"distriktsTekst\":\"Esbjerg\",\"vejKode\":\"9762\",\"kommuneKode\":\"0561\",\"socialKode\":\"61\",\"type\":\"SocialDistrikt\",\"husNrFra\":\"019\",\"husNrTil\":\"043\"}," +
                "{\"ligeUlige\":\"U\",\"timestamp\":\"199109231200\",\"distriktsTekst\":\"Thomas Kingos,Odense\",\"vejKode\":\"2994\",\"kommuneKode\":\"0461\",\"myndighedsNavn\":\"Thomas Kingos,Odense\",\"myndighedsKode\":\"7777\",\"type\":\"SogneDistrikt\",\"husNrFra\":\"001\",\"husNrTil\":\"013\"}," +
                "{\"ligeUlige\":\"U\",\"timestamp\":\"200708290757\",\"valgKode\":\"07\",\"distriktsTekst\":\"2. Sundbyvester\",\"vejKode\":\"0040\",\"kommuneKode\":\"0101\",\"type\":\"ValgDistrikt\",\"husNrFra\":\"001\",\"husNrTil\":\"999\"}," +
                "{\"ligeUlige\":\"U\",\"timestamp\":\"199109231200\",\"distriktsTekst\":\"\",\"vejKode\":\"0060\",\"kommuneKode\":\"0151\",\"type\":\"VarmeDistrikt\",\"husNrFra\":\"001\",\"varmeKode\":\"5100\",\"husNrTil\":\"027\"}," +
                "{\"vejNavn\":\"Gånsager\",\"vejAdresseringsnavn\":\"Gånsager\",\"timestamp\":\"200612221200\",\"slutDato\":\"200612221200\",\"vejKode\":\"0542\",\"kommuneKode\":\"0550\",\"startDato\":\"190001011200\",\"type\":\"HistoriskVej\"}," +
                "{\"taeller\":\"02086782\",\"type\":\"Slut\"}]\n");

        assertTrue(compareObjects(parsedData, comparison));
*/

    }

/*    public static void main(String[] args) {
        new VejregisterTest().testVejregister();
    }*/

    private boolean compareObjects(Object obj1, Object obj2) {
        if (obj1 != obj2) {
            if (obj1 != null && obj2 == null) {
                return false;
            }
            if (obj1 == null && obj2 != null) {
                return false;
            }
            Class c = obj1.getClass();
            if (c != obj2.getClass()) {
                return false;
            }

            if (c.getName() == "org.json.JSONObject") {
                JSONObject jobj1 = (JSONObject) obj1;
                JSONObject jobj2 = (JSONObject) obj2;
                if (jobj1.length() != jobj2.length()) {
                    return false;
                }
                if (jobj1.keySet() != null) {
                    for (Object okey : jobj1.keySet()) {
                        String key = (String) okey;
                        if (!jobj2.has(key)) {
                            return false;
                        }
                        if (!compareObjects(jobj1.get(key), jobj2.get(key))) {
                            return false;
                        }
                    }
                }
            } else if (c.getName() == "org.json.JSONArray") {
                JSONArray jarr1 = (JSONArray) obj1;
                JSONArray jarr2 = (JSONArray) obj2;
                if (jarr1.length() != jarr2.length()) {
                    return false;
                }
                for (int i = 0; i < jarr1.length(); i++) {
                    if (!compareObjects(jarr1.get(i), jarr2.get(i))) {
                        return false;
                    }
                }
            } else if (c.getName() == "java.lang.String") {
                return obj1.equals(obj2);
            } else {
                return false;
            }


        }
        return true;
    }

}
