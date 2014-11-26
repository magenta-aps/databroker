package dk.magenta.databroker.test;

import dk.magenta.databroker.Application;
import dk.magenta.databroker.core.model.DataProviderEntity;
import dk.magenta.databroker.core.model.oio.RegistreringRepository;
import dk.magenta.databroker.core.model.oio.VirkningEntity;
import dk.magenta.databroker.cprvejregister.dataproviders.LokalitetsRegister;
import dk.magenta.databroker.cprvejregister.dataproviders.MyndighedsRegister;
import dk.magenta.databroker.cprvejregister.dataproviders.PostnummerRegister;
import dk.magenta.databroker.cprvejregister.dataproviders.VejRegister;
import dk.magenta.databroker.cprvejregister.model.RepositoryCollection;
import dk.magenta.databroker.cprvejregister.model.adresse.AdresseEntity;
import dk.magenta.databroker.cprvejregister.model.adresse.AdresseRepository;


import dk.magenta.databroker.cprvejregister.model.husnummer.HusnummerEntity;
import dk.magenta.databroker.cprvejregister.model.husnummer.HusnummerRepository;
import dk.magenta.databroker.cprvejregister.model.kommune.KommuneEntity;
import dk.magenta.databroker.cprvejregister.model.kommune.KommuneRepository;
import dk.magenta.databroker.cprvejregister.model.kommunedelafnavngivenvej.KommunedelAfNavngivenVejEntity;
import dk.magenta.databroker.cprvejregister.model.kommunedelafnavngivenvej.KommunedelAfNavngivenVejRepository;
import dk.magenta.databroker.cprvejregister.model.navngivenvej.NavngivenVejEntity;
import dk.magenta.databroker.cprvejregister.model.navngivenvej.NavngivenVejRepository;
import dk.magenta.databroker.cprvejregister.model.postnummer.PostnummerEntity;
import dk.magenta.databroker.cprvejregister.model.postnummer.PostnummerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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


    @Autowired
    private RegistreringRepository registreringRepository;


    public VejregisterTest(){
    }
    @Test
    public void testVejregister() {

        RepositoryCollection repositories = new RepositoryCollection();
        repositories.kommuneRepository = this.kommuneRepository;
        repositories.kommunedelAfNavngivenVejRepository = this.kommunedelAfNavngivenVejRepository;
        repositories.navngivenVejRepository = this.navngivenVejRepository;
        repositories.husnummerRepository = this.husnummerRepository;
        repositories.adresseRepository = this.adresseRepository;
        repositories.postnummerRepository = this.postnummerRepository;
        repositories.registreringRepository = this.registreringRepository;

        DataProviderEntity kommuneProvider = new DataProviderEntity();
        kommuneProvider.setUuid(UUID.randomUUID().toString());
        MyndighedsRegister myndighedsregister = new MyndighedsRegister(kommuneProvider);
        //myndighedsregister.pull(repositories);
        myndighedsregister.read(new File("/home/lars/Projekt/databroker/src/test/resources/a370716.txt"), repositories);

        DataProviderEntity vejProvider = new DataProviderEntity();
        vejProvider.setUuid(UUID.randomUUID().toString());
        VejRegister vejregister = new VejRegister(vejProvider);
        //vejregister.pull(repositories);
        vejregister.read(new File("/home/lars/Projekt/databroker/src/test/resources/vejregister_hele_landet_pr_141101.zip"), repositories);
/*
        LokalitetsRegister lokalitetsregister = new LokalitetsRegister(new DataProviderEntity());
        lokalitetsregister.pull(repositories);

        PostnummerRegister postnummerRegister = new PostnummerRegister(new DataProviderEntity());
        postnummerRegister.pull(repositories);*/
    }



/*

    @Test
    public void testMyndighedsRegister() {
        HashMap<String, JpaRepository> repositories = new HashMap<String, JpaRepository>();
        repositories.put("kommuneRepository", this.kommuneRepository);
        MyndighedsRegister myndighedsregister = new MyndighedsRegister(new DataProviderEntity());
        myndighedsregister.read(new File("src/test/resources/vejregister/kommuner1.txt"), repositories);

        KommuneEntity entry;

        entry = this.kommuneRepository.getByKommunekode(751);
        assertNotNull(entry);
        assertEquals(entry.getKommunekode(), 751);
        assertEquals(entry.getNavn(), "Aarhus");

        myndighedsregister.read(new File("src/test/resources/vejregister/kommuner2.txt"), repositories);

        entry = this.kommuneRepository.getByKommunekode(751);
        assertNotNull(entry);
        assertEquals(entry.getKommunekode(), 751);
        assertEquals(entry.getNavn(), "Århus");
    }

    @Test
    public void testVejRegister() {
        HashMap<String, JpaRepository> repositories = new HashMap<String, JpaRepository>();
        repositories.put("kommuneRepository", this.kommuneRepository);
        repositories.put("kommunedelAfNavngivenVejRepository", this.kommunedelAfNavngivenVejRepository);
        repositories.put("navngivenVejRepository", this.navngivenVejRepository);

        VejRegister vejregister = new VejRegister(new DataProviderEntity());
        vejregister.read(new File("src/test/resources/vejregister/aktiveveje.txt"), repositories);
        NavngivenVejEntity entry1;
        KommunedelAfNavngivenVejEntity entry2;

        entry1 = this.navngivenVejRepository.getByKommunekodeAndVejkode(751, 9651);
        assertNotNull(entry1);
        assertEquals(entry1.getVejnavn(), "Åbogade");

        entry2 = this.kommunedelAfNavngivenVejRepository.getByKommunekodeAndVejkode(751, 9651);
        assertNotNull(entry2);
        assertEquals(entry2.getVejkode(), 9651);
        assertEquals(entry2.getNavngivenVej(), entry1);
    }

    @Test
    public void testLokalitetsRegister() {
        HashMap<String, JpaRepository> repositories = new HashMap<String, JpaRepository>();
        repositories.put("kommunedelAfNavngivenVejRepository", this.kommunedelAfNavngivenVejRepository);
        repositories.put("navngivenVejRepository", this.navngivenVejRepository);
        repositories.put("husnummerRepository", this.husnummerRepository);
        repositories.put("adresseRepository", this.adresseRepository);

        LokalitetsRegister lokalitetsregister = new LokalitetsRegister(new DataProviderEntity());
        lokalitetsregister.read(new File("src/test/resources/vejregister/lokaliteter.txt"), repositories);

        HusnummerEntity entry1;
        AdresseEntity entry2;

        entry1 = this.husnummerRepository.findFirstByKommunekodeAndVejkodeAndHusnr(751, 9651, "15");
        assertNotNull(entry1);
        assertEquals(entry1.getHusnummerbetegnelse(), "15");

        entry2 = this.adresseRepository.findByHusnummerAndDoerbetegnelseAndEtagebetegnelse(entry1, "", "");
        assertNotNull(entry2);
        //assertEquals(entry2.getHusnummer(), entry1);
    }

    @Test
    public void testPostnummerRegister() {
        HashMap<String, JpaRepository> repositories = new HashMap<String, JpaRepository>();
        repositories.put("postnummerRepository", this.postnummerRepository);

        PostnummerRegister postnummerRegister = new PostnummerRegister(new DataProviderEntity());
        postnummerRegister.read(new File("src/test/resources/vejregister/postnumre1.txt"), repositories);
        PostnummerEntity entry;

        entry = this.postnummerRepository.findByNummer(2100);
        assertNotNull(entry);
        assertEquals(entry.getNavn(), "København Ø");
        assertEquals(entry.getNummer(), 2100);

        entry = this.postnummerRepository.findByNummer(3953);
        assertNotNull(entry);
        assertEquals(entry.getNavn(), "Qeqertarssuaq");
        assertEquals(entry.getNummer(), 3953);

        postnummerRegister.read(new File("src/test/resources/vejregister/postnumre2.txt"), repositories);
        entry = this.postnummerRepository.findByNummer(3953);
        assertNotNull(entry);
        assertEquals(entry.getNavn(), "Qeqertarssuaq City");
        assertEquals(entry.getNummer(), 3953);
    }
*/



/*
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
*/
}
