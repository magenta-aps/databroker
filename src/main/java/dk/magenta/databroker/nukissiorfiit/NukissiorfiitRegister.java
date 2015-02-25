package dk.magenta.databroker.nukissiorfiit;

import dk.magenta.databroker.core.DataProviderConfiguration;
import dk.magenta.databroker.core.model.DataProviderEntity;
import dk.magenta.databroker.dawa.model.DawaModel;
import dk.magenta.databroker.dawa.model.SearchParameters;
import dk.magenta.databroker.dawa.model.SearchParameters.Key;
import dk.magenta.databroker.dawa.model.postnummer.PostNummerEntity;
import dk.magenta.databroker.dawa.model.temaer.KommuneEntity;
import dk.magenta.databroker.dawa.model.vejstykker.VejstykkeEntity;
import dk.magenta.databroker.register.LineRegister;
import dk.magenta.databroker.register.RegisterRun;
import dk.magenta.databroker.util.objectcontainers.Level2Container;
import dk.magenta.databroker.util.objectcontainers.ModelUpdateCounter;
import dk.magenta.databroker.util.objectcontainers.Pair;
import dk.magenta.databroker.register.records.Record;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by lars on 13-01-15.
 */
@Component
public class NukissiorfiitRegister extends LineRegister {

    /*
    * Constructors
    * */

     private class NukissiorfiitRecord extends Record {
    }


    /*
    * Data source spec
    * */

     @Autowired
    private ConfigurableApplicationContext ctx;

    @Override
    public Resource getRecordResource() {
        return this.ctx.getResource("classpath:/data/nukissiorfiit.csv");
    }

    @Override
    protected String getEncoding() {
        return "UTF-8";
    }


    /*
    * Logging
     */
    private Logger log = Logger.getLogger(NukissiorfiitRegister.class);


    /*
    * Parse definition
    * */

    protected Record parseTrimmedLine(String line) {
        String[] parts = line.split(",");
        if (parts.length >= 9) {
            NukissiorfiitRecord record = new NukissiorfiitRecord();
            record.put("by", parts[0]);
            record.put("bolignr", parts[1]);
            record.put("bnr", parts[2]);
            record.put("gadenavn", parts[3]);
            record.put("gadenr", parts[4]);
            record.put("lejlighedsnr", parts[5]);
            record.put("boligbogstav", parts[6]);
            record.put("postnummer", parts[7]);
            record.put("bynavn", parts[8]);
            return record;
        }
        return null;
    }

    protected RegisterRun createRun() {
        return new RegisterRun();
    }


    @Transactional
    public void pull(boolean forceFetch, boolean forceParse, DataProviderEntity dataProviderEntity) {
        super.pull(forceFetch, forceParse, dataProviderEntity);
    }


    /*
    * Database save
    * */

    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private DawaModel model;

    @Transactional
    @Override
    public void handlePush(DataProviderEntity dataProviderEntity, HttpServletRequest request) {
        InputStream input = null;
        try {
            Part uploadPart = request.getPart("sourceUpload");
            if (uploadPart != null) {
                input = uploadPart.getInputStream();
            }
        } catch (IOException e) {
        } catch (ServletException e) {
        }
        if (input != null) {
            this.handlePush(true, dataProviderEntity, input);
        }
    }

    private HashMap<Integer, ArrayList<Integer>> aliasMap = new HashMap<Integer, ArrayList<Integer>>();
    private void addAlias(int... aliases) {
        for (int alias : aliases) {
            if (!this.aliasMap.containsKey(alias)) {
                this.aliasMap.put(alias, new ArrayList<Integer>());
            }
            for (int otheralias : aliases) {
                if (otheralias != alias) {
                    this.aliasMap.get(alias).add(otheralias);
                }
            }
        }
    }
    private List<Integer> getAliases(int key) {
        return this.aliasMap.get(key);
    }

    @PostConstruct
    private void createAliases() {
        this.addAlias(3900, 3905);
    }



    @Override
    protected void saveRunToDatabase(RegisterRun run, DataProviderEntity dataProviderEntity) {

        this.log.info("Preparatory linking");
        long time = this.indepTic();
        ModelUpdateCounter counter = new ModelUpdateCounter();

        Collection<KommuneEntity> kommuner = this.model.getKommune(new SearchParameters(new String[]{"gl"}, null, null, null, null, null, null, null), false);
        Level2Container<VejstykkeEntity> vejMap = new Level2Container<VejstykkeEntity>();
        for (KommuneEntity kommuneEntity : kommuner) {
            Collection<VejstykkeEntity> veje = kommuneEntity.getVejstykkeVersioner();
            for (VejstykkeEntity vej : veje) {
                String navn = this.normalize(vej.getLatestVersion().getVejnavn());
                for (PostNummerEntity post : vej.getLatestVersion().getPostnumre()) {
                    vejMap.put(navn, post.getLatestVersion().getNr(), vej);
                    counter.printEntryProcessed();
                }
            }
        }
        counter.printFinalEntriesProcessed();
        this.log.info("Links created in " + this.toc(time) + " ms");


        this.log.info("Storing AdresseEntities in database");
        counter.reset();
        //this.model.resetAllCaches();
        HashSet<Pair<String,String>> failures = new HashSet<Pair<String, String>>();
        HashSet<Pair<String,String>> successes = new HashSet<Pair<String, String>>();
        for (Record r : run) {
            if (r instanceof NukissiorfiitRecord) {
                NukissiorfiitRecord record = (NukissiorfiitRecord) r;

                String vejNavn = record.get("gadenavn");
                int postnr = record.getInt("postnummer");

                VejstykkeEntity vejstykkeEntity = vejMap.get(this.normalize(vejNavn), postnr);
                if (vejstykkeEntity == null) {
                    List<Integer> aliases = this.getAliases(postnr);
                    if (aliases != null) {
                        for (int alias : aliases) {
                            vejstykkeEntity = vejMap.get(this.normalize(vejNavn), alias);
                            //if (vejstykkeEntity != null) {
                            //  System.out.println("Found "+vejstykkeEntity.getLatestVersion().getVejnavn()+" by alias "+postnr+" => "+alias);
                            //  break;
                            //}
                        }
                    }
                }

                if (vejstykkeEntity == null) {
                    failures.add(new Pair<String, String>(""+postnr, vejNavn));
                } else {
                    int kommuneKode = vejstykkeEntity.getKommune().getKode();
                    int vejKode = vejstykkeEntity.getKode();
                    String husnr = record.get("gadenr");
                    if (husnr == null || husnr.isEmpty()) {
                        husnr = "0";
                    }
                    String bnr = record.get("bnr");
                    String etage = "";
                    String doer = record.get("lejlighedsnr");
                    if (doer == null || doer.isEmpty()) {
                        doer = "";
                    }
                    this.model.setAdresse(kommuneKode, vejKode, husnr, bnr, etage, doer, //postnr,
                            this.getCreateRegistrering(dataProviderEntity), this.getUpdateRegistrering(dataProviderEntity)
                    );
                    successes.add(new Pair<String,String>(""+postnr, vejNavn));
                }
                counter.printEntryProcessed();
            }
        }

        counter.printFinalEntriesProcessed();
        this.log.info("AdresseEntities stored in " + this.toc(time) + " ms");
        /*
        for (Pair<String, String> success : successes) {
            System.out.println("Genkendt: " + success.getLeft() + " / " + success.getRight());
        }

        for (Pair<String, String> failure : failures) {
            String vejnavn = failure.getRight();

            System.out.println("Ikke genkendt: "+failure.getLeft()+" / "+vejnavn);

            if (vejnavn.length() > 3) {
                SearchParameters parameters = new SearchParameters();
                parameters.put(Key.LAND, "gl");
                //parameters.put(Key.POST, failure.getLeft());
                parameters.put(Key.VEJ, "*" + this.longestWord(vejnavn) + "*");
                System.out.println("  gætter udfra længste ord: "+this.longestWord(vejnavn));

                Collection<VejstykkeEntity> guesses = this.model.getVejstykke(parameters, false);
                if (guesses.size() > 0) {
                    //System.out.println("  Search for "+this.longestWord(vejnavn));

                    //if (guesses.size() == 1) {
                        //for (VejstykkeEntity guess : guesses) {
                        //  System.out.println("---------------------------------");
                        //  System.out.println("not found: "+failure.getLeft()+" : "+vejnavn + "("+this.normalize(vejnavn,true)+")");
                        //  System.out.println("Guessing by longest word: "+this.longestWord(vejnavn));
                        //  System.out.println("Single Guess: " + guess.getLatestVersion().getVejnavn()+"/"+guess.getLatestVersion().getVejadresseringsnavn() + "("+this.normalize(guess.getLatestVersion().getVejnavn(), true)+")"+(vejMap.get(this.normalize(guess.getLatestVersion().getVejnavn()))==null ? "NOT found":"found"));
                        //}
                    //} else {
                        for (VejstykkeEntity guess : guesses) {
                            for (PostNummerEntity postNummerEntity : guess.getLatestVersion().getPostnumre()) {
                                System.out.println("    gæt: " + postNummerEntity.getLatestVersion().getNr() + " / " + guess.getLatestVersion().getVejnavn());
                            }
                        }
                    //}
                }
            }
        }*/
    }

    private String ignoredSuffixes = "\\b(aqquser(na)?)|(aqqulaa)|(avquserna)|(avquta+t?)|(aqquta+t?)|(aqqut*)|(aq+)|([av]vq)|(kujalleq)$";

    private String longestWord(String sentence) {
        String[] words = sentence.split("[\\s\\-]");
        String longest = "";

        for (String word : words) {
            word = word.replaceAll("[\\-\\.:]","").toLowerCase();
            if (!word.matches(ignoredSuffixes)) {
                if (word.length() > longest.length()) {
                    longest = word;
                }
            }
        }
        return longest;
    }

    private String normalize(String navn) {
        return this.normalize(navn, false);
    }

    private String normalize(String navn, boolean print) {
        String a = navn;
        navn = navn.toLowerCase();
        navn = navn.replaceAll("[\":'\\*\\+]", "");
        navn = navn.replaceAll("\\b[a-z]?\\d[a-z]?\\b"," ");
        navn = navn.replaceAll("-", " ");
        navn = navn.replaceAll("\\.+\\s*", " ");

        navn = navn.trim();

        String b = navn;
        navn = navn.replaceAll(ignoredSuffixes, "");

        navn = navn.replaceAll("\\ssletten$", "");

        String c = navn;
        navn = navn.replaceAll("\\s+([eiu]p)", "$1");
        navn = navn.replaceAll("[eiu]p(\\s|$)", " ");
        navn = navn.replaceAll("\\s+vej", "vej");
        String d = navn;

        navn = navn.replaceAll("aa+","a");
        navn = navn.replaceAll("ee+","e");
        navn = navn.replaceAll("ii+","i");
        navn = navn.replaceAll("oo+","o");
        navn = navn.replaceAll("uu+","u");


        navn = navn.replaceAll("\\sd\\s"," ");
        navn = navn.replaceAll("prinsesse","p ");
        navn = navn.replaceAll("prins","p ");
        navn = navn.replaceAll("prs[\\.\\s]","p ");
        navn = navn.replaceAll("pr[\\.\\s]","p ");
        navn = navn.replaceAll("dronning","dr ");
        navn = navn.replaceAll("dron","dr ");

        //navn = navn.replaceAll("\\s+","");

        navn = navn.trim();

        String e = navn;
        if (print) {
            System.out.println("\"" + a + "\" => \"" + b + "\" => \"" + c + "\" => \"" + d + "\" => \"" + e + "\"");
        }
        return navn;
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public String getTemplatePath() {
        return "NukissiorfiitRegisterForm";
    }

    @Override
    public DataProviderConfiguration getDefaultConfiguration() {
        return new DataProviderConfiguration("{\"sourceType\":\"upload\"}");
    }

    public boolean wantUpload(DataProviderConfiguration configuration) {
        List<String> sourceType = configuration.get("sourceType");
        return sourceType != null && sourceType.contains("upload");
    }
}
