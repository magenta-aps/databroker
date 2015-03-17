package dk.magenta.databroker.cvrregister;

import dk.magenta.databroker.core.DataProviderConfiguration;
import dk.magenta.databroker.core.RegistreringInfo;
import dk.magenta.databroker.core.model.DataProviderEntity;
import dk.magenta.databroker.core.model.oio.VirkningEntity;
import dk.magenta.databroker.cvr.model.CvrModel;
import dk.magenta.databroker.cvr.model.companyunit.CompanyUnitEntity;
import dk.magenta.databroker.dawa.model.DawaModel;
import dk.magenta.databroker.dawa.model.SearchParameters;
import dk.magenta.databroker.dawa.model.enhedsadresser.EnhedsAdresseEntity;
import dk.magenta.databroker.dawa.model.vejstykker.VejstykkeEntity;
import dk.magenta.databroker.register.Register;
import dk.magenta.databroker.register.RegisterRun;
import dk.magenta.databroker.util.TimeRecorder;
import dk.magenta.databroker.util.Util;
import dk.magenta.databroker.util.objectcontainers.Level1Container;
import dk.magenta.databroker.register.records.Record;
import dk.magenta.databroker.util.objectcontainers.ListHash;
import dk.magenta.databroker.util.objectcontainers.StringList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.*;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by lars on 26-01-15.
 */
@Component
public class CvrRegister extends Register {

    private static final boolean REQUIRE_ROAD_EXIST = false;
    private static final boolean USE_NEARBY_ROAD_CODES = false;


    private class CvrRecord extends Record {
        private ListHash<String> hash;

        public CvrRecord(ListHash<String> hash) {
            this.hash = hash;
        }

        protected void obtain(String key, String path) {
            this.put(key, this.hash.getFirst(path));
        }

        protected List<String> getList(String path) {
            return this.hash.get(path);
        }
    }

    private class VirksomhedRecord extends CvrRecord {
        public VirksomhedRecord(ListHash<String> virksomhedHash) {
            super(virksomhedHash);
            this.obtain("cvrNummer", "cvrnr");
            this.obtain("advertProtection", "reklamebeskyttelse");
            this.obtain("name", "navn/tekst");
            this.obtain("form", "virksomhedsform/kode");
            this.obtain("formText", "virksomhedsform/tekst");
            this.obtain("primaryIndustry", "hovedbranche/kode");
            this.obtain("secondaryIndustry1", "bibranche1/kode");
            this.obtain("secondaryIndustry2", "bibranche2/kode");
            this.obtain("secondaryIndustry3", "bibranche3/kode");
            this.obtain("startDate", "livsforloeb/startdato");
            this.obtain("endDate", "livsforloeb/slutdato");
            this.obtain("ajourDate", "ajourfoeringsdato");
            this.obtain("kommunekode", "beliggenhedsadresse/kommune/kode");
            this.obtain("vejkode", "beliggenhedsadresse/vejkode");
            this.obtain("husnummerFra", "beliggenhedsadresse/husnummerFra");
            this.obtain("husnummerTil", "beliggenhedsadresse/husnummerTil");
            this.obtain("bogstavFra", "beliggenhedsadresse/bogstavFra");
            this.obtain("bogstavTil", "beliggenhedsadresse/bogstavTil");
            this.obtain("etage", "beliggenhedsadresse/etage");
            this.obtain("sidedoer", "beliggenhedsadresse/sidedoer");
            this.obtain("phone", "telefonnummer/kontaktoplysning");
        }
    }

    private class ProductionUnitRecord extends CvrRecord {
        public ProductionUnitRecord(ListHash<String> productionunitHash) {
            super(productionunitHash);
            this.obtain("pNummer", "pnr");
            this.obtain("cvrNummer", "virksomhed/virksomhed/cvrnr");
            this.obtain("advertProtection", "reklamebeskyttelse");
            this.obtain("name", "navn/tekst");
            this.obtain("primaryIndustry", "hovedbranche/kode");
            this.obtain("primaryIndustryText", "hovedbranche/tekst");
            this.obtain("secondaryIndustry1", "bibranche1/kode");
            this.obtain("secondaryIndustryText1", "bibranche1/tekst");
            this.obtain("secondaryIndustry2", "bibranche2/kode");
            this.obtain("secondaryIndustryText2", "bibranche2/tekst");
            this.obtain("secondaryIndustry3", "bibranche3/kode");
            this.obtain("secondaryIndustryText3", "bibranche3/tekst");
            this.obtain("startDate", "livsforloeb/startdato");
            this.obtain("endDate", "livsforloeb/slutdato");
            this.obtain("adresseDato", "beliggenhedsadresse/gyldigFra");
            this.obtain("vejnavn", "beliggenhedsadresse/vejnavn");
            this.obtain("vejkode", "beliggenhedsadresse/vejkode");
            this.obtain("husnummerFra", "beliggenhedsadresse/husnummerFra");
            this.obtain("husnummerTil", "beliggenhedsadresse/husnummerTil");
            this.obtain("bogstavFra", "beliggenhedsadresse/bogstavFra");
            this.obtain("bogstavTil", "beliggenhedsadresse/bogstavTil");
            this.obtain("etage", "beliggenhedsadresse/etage");
            this.obtain("sidedoer", "beliggenhedsadresse/sidedoer");
            this.obtain("postnr", "beliggenhedsadresse/postnr");
            this.obtain("kommunekode", "beliggenhedsadresse/kommune/kode");
            this.obtain("sidedoer", "beliggenhedsadresse/sidedoer");
            this.obtain("co", "beliggenhedsadresse/coNavn");
            this.obtain("phone", "telefonnummer/kontaktoplysning");
            this.obtain("fax", "telefax/kontaktoplysning");
            this.obtain("email", "email/kontaktoplysning");
            this.obtain("isPrimary", "hovedafdeling");

            /*if (this.get("kommunekode") == null || this.get("kommunekode").isEmpty()) {
                System.err.println(productionunitHash);
            }*/
        }
    }

    private class DeltagerRecord extends CvrRecord {
        public DeltagerRecord(ListHash<String> deltagerHash) {
            super(deltagerHash);
            this.obtain("nummer", "deltagernummer");
            this.obtain("ajourDato", "ajourfoeringsdato");
            this.obtain("gyldigDato", "deltagelseGyldigFra");
            this.obtain("cvrNummer", "cvrnr");
            this.obtain("type", "oplysninger/deltagertype");
            this.obtain("navn", "oplysninger/navn");
            this.obtain("status", "oplysninger/personstatus");
            this.obtain("rolle", "rolle");
        }
    }

    public class CvrRegisterRun extends RegisterRun {
        private Level1Container<VirksomhedRecord> virksomheder;
        private Level1Container<ProductionUnitRecord> productionUnits;
        private Level1Container<DeltagerRecord> deltagere;

        public CvrRegisterRun() {
            super();
            this.virksomheder = new Level1Container<VirksomhedRecord>();
            this.productionUnits = new Level1Container<ProductionUnitRecord>();
            this.deltagere = new Level1Container<DeltagerRecord>();
        }

        public boolean add(VirksomhedRecord virksomhed) {
            this.virksomheder.put(virksomhed.get("cvrNummer"), virksomhed);
            return this.add((Record) virksomhed);
        }

        public boolean add(ProductionUnitRecord productionUnit) {
            this.productionUnits.put(productionUnit.get("pNummer"), productionUnit);
            return this.add((Record) productionUnit);
        }

        public boolean add(DeltagerRecord deltager) {
            this.deltagere.put(deltager.get("nummer"), deltager);
            return this.add((Record) deltager);
        }

        public Level1Container<VirksomhedRecord> getVirksomheder() {
            return this.virksomheder;
        }

        public Level1Container<ProductionUnitRecord> getProductionUnits() {
            return this.productionUnits;
        }

        public Level1Container<DeltagerRecord> getDeltagere() {
            return this.deltagere;
        }

        public void clear() {
            this.virksomheder.clear();
            this.productionUnits.clear();
            this.deltagere.clear();
            super.clear();
        }
    }

    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private CvrModel cvrModel;

    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private DawaModel dawaModel;

    /*
    * Data source spec
    * */

    @Autowired
    private ConfigurableApplicationContext ctx;

    public Resource getRecordResource() {
        return this.ctx.getResource("classpath:/data/cvrDelta.zip");
    }

    public void pull(boolean forceFetch, boolean forceParse, DataProviderEntity dataProviderEntity) {
        super.pull(forceFetch, forceParse, dataProviderEntity);
    }


    @Override
    protected void importData(RegistreringInfo registreringInfo) {
        try {
            int chunkSize = 1000;//(int) (registreringInfo.getInputSize() / 50000L);
            //System.out.println("chunkSize: "+chunkSize);
            DefaultHandler handler = new VirksomhedDataHandler(registreringInfo, chunkSize);
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            parser.parse(registreringInfo.getInputStream(), handler);
            //this.generateAddresses(false);
            //this.bulkwireReferences();
            //this.bulkwireAdresses();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ensureIndustryInDatabase(int code, String text) {
        if (code > 0) {
            this.cvrModel.setIndustry(code, text, true);
        }
    }

    private void ensureFormInDatabase(int code, String text) {
        if (code > 0) {
            this.cvrModel.setForm(code, text, true);
        }
    }


    protected void onTransactionEnd() {
        this.dawaModel.onTransactionEnd();
    }




    @Override
    protected RegisterRun parse(InputStream input) {
        return null;
    }


    private static final int UNKNOWN_INDUSTRY = 999999;


    int totalCompanies = 0;
    int totalUnits = 0;
    int totalMembers = 0;

    private HashMap<String, Boolean> generatedAddresses = new HashMap<String, Boolean>();

    private synchronized String addNeededAddress(int kommuneKode, int vejKode, String husNr, String etage, String doer) {
        String descriptor = EnhedsAdresseEntity.generateDescriptor(kommuneKode, vejKode, husNr, etage, doer);
        if (!this.generatedAddresses.containsKey(descriptor)) {
            this.generatedAddresses.put(descriptor, false);
        }
        return descriptor;
    }


    int misses = 0;


    @Override
    protected void saveRunToDatabase(RegisterRun run, RegistreringInfo registreringInfo) {

        if (run.getClass() == CvrRegisterRun.class) {

            //this.beginTransaction();

            CvrRegisterRun cRun = (CvrRegisterRun) run;
            int companyCount = cRun.getVirksomheder().size();
            int unitCount = cRun.getProductionUnits().size();
            int memberCount = cRun.getDeltagere().size();

            this.log.info("Saving items to database: ");
            if (companyCount > 0) {
                this.log.info("    " + companyCount + " companies");
            }
            if (unitCount > 0) {
                this.log.info("    " + unitCount + " production units");
            }
            if (memberCount > 0) {
                this.log.info("    " + memberCount + " members");
            }

            try {

                double time;
                TimeRecorder timer = new TimeRecorder();

                if (companyCount > 0) {
                    //this.beginTransaction();

                    for (VirksomhedRecord virksomhed : cRun.getVirksomheder().getList()) {
                        TimeRecorder itemTimer = new TimeRecorder();
                        //this.beginTransaction();
                        // Make sure the referenced industries are present in the DB
                        itemTimer.record();
                        this.ensureIndustryInDatabase(virksomhed.getInt("primaryIndustry"), virksomhed.get("primaryIndustryText"));
                        this.ensureIndustryInDatabase(virksomhed.getInt("secondaryIndustry1"), virksomhed.get("secondaryIndustryText1"));
                        this.ensureIndustryInDatabase(virksomhed.getInt("secondaryIndustry2"), virksomhed.get("secondaryIndustryText2"));
                        this.ensureIndustryInDatabase(virksomhed.getInt("secondaryIndustry3"), virksomhed.get("secondaryIndustryText3"));
                        itemTimer.record();
                        this.ensureFormInDatabase(virksomhed.getInt("form"), virksomhed.get("formText"));

                        itemTimer.record();
                        // Fetch basic fields
                        String cvrNummer = virksomhed.get("cvrNummer");
                        boolean advertProtection = virksomhed.getInt("advertProtection") == 1;
                        String name = virksomhed.get("name");

                        itemTimer.record();
                        int form = virksomhed.getInt("form");
                        int primaryIndustry = virksomhed.getInt("primaryIndustry");

                        Date startDate = this.parseDate(virksomhed.get("startDate"));
                        Date endDate = this.parseDate(virksomhed.get("endDate"));
                        Date ajourDate = this.parseDate(virksomhed.get("ajourDate"));

                        itemTimer.record();
                        int vejkode = virksomhed.getInt("vejkode");
                        int kommunekode = virksomhed.getInt("kommunekode");
                        String husnr = virksomhed.get("husnummerFra");
                        String bogstav = virksomhed.get("bogstavFra");
                        String etage = virksomhed.get("etage");
                        String sidedoer = virksomhed.get("sidedoer");
                        String fullHusNr = husnr + (bogstav != null ? bogstav : "");

                        itemTimer.record();
                        String descriptor = EnhedsAdresseEntity.generateDescriptor(kommunekode, vejkode, fullHusNr, etage, sidedoer);

                        String phone = virksomhed.get("phone");

                        itemTimer.record();
                        List<Integer> secondaryIndustriesList = new ArrayList<Integer>();
                        String[] keys = new String[]{"secondaryIndustry1", "secondaryIndustry2", "secondaryIndustry3"};
                        for (String key : keys) {
                            int value = virksomhed.getInt(key);
                            if (value != 0) {
                                secondaryIndustriesList.add(value);
                            } else {
                                break;
                            }
                        }
                        itemTimer.record();
                        int[] secondaryIndustries = new int[secondaryIndustriesList.size()];
                        int i = 0;
                        for (Iterator<Integer> iIter = secondaryIndustriesList.iterator(); iIter.hasNext(); secondaryIndustries[i++] = iIter.next())
                            ;

                        itemTimer.record();
                        this.cvrModel.setCompany(cvrNummer, name,
                                primaryIndustry, secondaryIndustries, form,
                                startDate, endDate, ajourDate,
                                registreringInfo, new ArrayList<VirkningEntity>());
                        totalCompanies++;
                        itemTimer.record();

                        //if (totalCompanies % 10000 == 0) {
                        //System.out.println("flushing");
                        //    this.cvrModel.flush();
                        //System.out.println("flushed");
                        //}


                        //this.endTransaction();
                        timer.add(itemTimer);
                    }
                    //this.endTransaction(); // Dette virker ved førstegangsindlæsning

                    time = timer.sum();
                    this.log.info(companyCount + " companies created in " + time + " ms (avg " + (time / (double) companyCount) + " ms) " + timer.toString() + " " + this.cvrModel.getCompanyTimer());
                    this.cvrModel.resetCompanyTimer();
                }

                timer.reset();

                if (unitCount > 0) {
                    time = this.tic();
                    TimeRecorder sumTime = new TimeRecorder();
                    //this.beginTransaction();

                    for (ProductionUnitRecord unit : cRun.getProductionUnits().getList()) {
                        TimeRecorder itemTimer = new TimeRecorder();
                        //this.beginTransaction();
                        // Make sure the referenced industries are present in the DB
                        this.ensureIndustryInDatabase(unit.getInt("primaryIndustry"), unit.get("primaryIndustryText"));
                        this.ensureIndustryInDatabase(unit.getInt("secondaryIndustry1"), unit.get("secondaryIndustryText1"));
                        this.ensureIndustryInDatabase(unit.getInt("secondaryIndustry2"), unit.get("secondaryIndustryText2"));
                        this.ensureIndustryInDatabase(unit.getInt("secondaryIndustry3"), unit.get("secondaryIndustryText3"));

                        // Fetch basic fields
                        long pNummer = unit.getLong("pNummer");
                        String name = unit.get("name");
                        String cvrNummer = unit.get("cvrNummer");
                        String phone = unit.get("phone");
                        String fax = unit.get("fax");
                        String email = unit.get("email");
                        Date startDate = this.parseDate(unit.get("startDate"));
                        Date endDate = this.parseDate(unit.get("endDate"));
                        boolean advertProtection = unit.getBoolean("advertProtection");
                        boolean isPrimaryUnit = unit.getBoolean("isPrimary");

                        int primaryIndustry = unit.getInt("primaryIndustry");
                        if (primaryIndustry == 0) {
                            primaryIndustry = UNKNOWN_INDUSTRY;
                        }

                        List<Integer> secondaryIndustriesList = new ArrayList<Integer>();
                        String[] keys = new String[]{"secondaryIndustry1", "secondaryIndustry2", "secondaryIndustry3"};
                        for (String key : keys) {
                            int value = unit.getInt(key);
                            if (value != 0) {
                                secondaryIndustriesList.add(value);
                            } else {
                                break;
                            }
                        }
                        int[] secondaryIndustries = new int[secondaryIndustriesList.size()];
                        int i = 0;
                        for (Iterator<Integer> iIter = secondaryIndustriesList.iterator(); iIter.hasNext(); secondaryIndustries[i++] = iIter.next());


                        EnhedsAdresseEntity adresse = null;

                        //SearchParameters addressSearch = new SearchParameters();
                        int kommuneKode = unit.getInt("kommunekode");
                        int postNr = unit.getInt("postnr");
                        int vejKode = unit.getInt("vejkode");
                        String vejNavn = unit.get("vejnavn");
                        String husnr = unit.get("husnummerFra");
                        String bogstav = unit.get("bogstavFra");
                        String etage = unit.get("etage");
                        String sidedoer = unit.get("sidedoer");
                        String fullHusNr = husnr + (bogstav != null ? bogstav : "");
                        Date addressDate = this.parseDate(unit.get("adresseDato"));
                        String postnr = unit.get("postnr");
                        itemTimer.record();
                        if (kommuneKode > 0 && vejKode > 0) {
                            VejstykkeEntity vej = this.dawaModel.getVejstykke(kommuneKode, vejKode);
                            itemTimer.record();

                            if (vej == null) {
                                if (vejNavn == null || vejNavn.isEmpty()) {
                                    this.log.warn("Road " + kommuneKode + ":" + vejKode + " not found");
                                } else {
                                    this.log.warn("Road " + kommuneKode + ":" + vejKode + " not found; cvr names it " + vejNavn);
                                    if (USE_NEARBY_ROAD_CODES) {
                                        SearchParameters searchParameters = new SearchParameters();
                                        searchParameters.put(SearchParameters.Key.KOMMUNE, kommuneKode);
                                        searchParameters.put(SearchParameters.Key.VEJ, vejNavn);
                                        Collection<VejstykkeEntity> candidates = this.dawaModel.getVejstykke(searchParameters);
                                        if (candidates.size() == 1) {
                                            VejstykkeEntity candidate = candidates.iterator().next();
                                            if (Math.abs(candidate.getKode() - vejKode) < 4) {
                                                this.log.trace("Using " + kommuneKode + ":" + candidate.getKode() + " for " + vejNavn + ", because " + kommuneKode + ":" + vejKode + "was not found");
                                                vejKode = candidate.getKode();
                                                vej = candidate;
                                            }
                                        }
                                    }
                                }
                            }

                            boolean doAddUnit = false;

                            itemTimer.record();

                            if (REQUIRE_ROAD_EXIST) {
                                if (vej != null) {
                                    String cprNavn = this.normalizeVejnavn(vej.getLatestVersion().getVejnavn());
                                    String cvrNavn = this.normalizeVejnavn(vejNavn);
                                    if (!Util.compareNormalized(cprNavn, cvrNavn)) {
                                        this.log.warn("Mismatch on " + kommuneKode + ":" + vejKode + "; cpr names it " + cprNavn + " while cvr names it " + cvrNavn);
                                        misses++;
                                    } else {
                                        doAddUnit = true;
                                    }
                                } else {
                                    misses++;
                                }
                            } else {
                                doAddUnit = true;
                            }

                            if (doAddUnit) {
                                String addressDescriptor = this.addNeededAddress(kommuneKode, vejKode, husnr, etage, sidedoer);
                                //adresse = this.dawaModel.setAdresse(kommuneKode, vejKode, husnr, null, etage, sidedoer, registreringInfo, new ArrayList<VirkningEntity>(), false, true);
                                itemTimer.record();
                                CompanyUnitEntity companyUnitEntity = this.cvrModel.setCompanyUnit(pNummer, name, cvrNummer,
                                        primaryIndustry, secondaryIndustries,
                                        adresse, addressDate, addressDescriptor,
                                        phone, fax, email, isPrimaryUnit,
                                        startDate, endDate,
                                        advertProtection,
                                        registreringInfo, new ArrayList<VirkningEntity>()
                                );
                                itemTimer.record();
                            }
                            sumTime.add(itemTimer);
                        }
                        //this.endTransaction();
                    }

                    //this.endTransaction();

                    time = sumTime.sum();
                    this.log.info(unitCount + " production units created in " + time + " ms (avg " + (time / (double) unitCount) + " ms) " + sumTime + " " + this.cvrModel.getUnitTimer());
                    this.cvrModel.resetUnitTimer();
                }


                //this.beginTransaction();
                if (memberCount > 0) {
                    time = Util.getTime();
                    TimeRecorder sumTime = new TimeRecorder();
                    for (DeltagerRecord deltager : cRun.getDeltagere().getList()) {
                        TimeRecorder itemTimer = new TimeRecorder();
                        long deltagerNummer = deltager.getLong("nummer");
                        Date ajourDate = this.parseDate(deltager.get("ajourDato"));
                        Date gyldigDate = this.parseDate(deltager.get("gyldigDato"));
                        String cvrNummer = deltager.get("cvrNummer");
                        String typeName = deltager.get("type");
                        String name = deltager.get("navn");
                        String statusName = deltager.get("status");
                        String rolleName = deltager.get("rolle");
                        itemTimer.record();
                        this.cvrModel.setDeltager(deltagerNummer, name, cvrNummer, ajourDate, gyldigDate, typeName, rolleName, registreringInfo, new ArrayList<VirkningEntity>());
                        itemTimer.record();
                        sumTime.add(itemTimer);
                    }
                    time = sumTime.sum();
                    this.log.info(memberCount + " members created in " + time + " ms (avg " + (time / (double) memberCount) + " ms) " + sumTime + " " + this.cvrModel.getDeltagerTimer());
                    this.cvrModel.resetDeltagerTimer();
                }
                //this.endTransaction();

                //this.cvrModel.flushCompanies();
                //this.dawaModel.flush();
                //this.endTransaction();
                this.cvrModel.flush();
            }
            catch (Exception ex) {
                this.log.error("Transaction failed: "+ex.getMessage());
                ex.printStackTrace();
                //this.rollbackTransaction();
                return;
            }
            this.log.info("Update completed (" + companyCount + " companies, " + unitCount + " units and " + memberCount + " members)");
            registreringInfo.logProcess(this.log);
        }
    }

    private void bulkwireReferences() {
        this.dawaModel.resetAllCaches();
        //this.beginTransaction();
        this.cvrModel.bulkWireReferences();
        //this.endTransaction();
    }

    private void bulkwireAdresses() {
        this.beginTransaction();
        this.dawaModel.bulkwireAdresser();
        this.endTransaction();
    }

    private void generateAddresses(boolean bulkwire) {
        int created = 0;


        this.dawaModel.resetAllCaches();
        //this.beginTransaction();
        this.log.info("Adding " + this.generatedAddresses.size() + " addresses");
        for (String descriptor : this.generatedAddresses.keySet()) {
            if (this.generatedAddresses.get(descriptor) == false) {
                try {
                    String[] parts = descriptor.split(":",descriptor.length());
                    int kommuneKode = Integer.parseInt(parts[0]);
                    int vejKode = Integer.parseInt(parts[1]);
                    String husNr = parts[2];
                    String etage = parts[3];
                    String doer = parts[4];
                    this.dawaModel.setAdresse(kommuneKode, vejKode, husNr, null, etage, doer, null,
                            registreringInfo, new ArrayList<VirkningEntity>(), bulkwire, true);
                    created++;
                } catch (ArrayIndexOutOfBoundsException e) {
                }
/*
                if (created >= 50000) {
                    created = 0;
                    this.dawaModel.flush();
                    this.endTransaction();
                    if (bulkwire) {
                        this.bulkwireAdresses();
                    }
                    this.beginTransaction();

                }
*/

            }
        }
        this.dawaModel.flush();
        //this.endTransaction();
        if (bulkwire) {
            this.bulkwireAdresses();
        }



    }

    private Date parseDate(String date) {
        if (date != null && !date.isEmpty()) {
            final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                return format.parse(date);
            } catch (ParseException e) {
            }
        }
        return null;
    }

    private String normalizeVejnavn(String vejnavn) {
        if (vejnavn != null && !vejnavn.isEmpty()) {
            vejnavn = vejnavn.toLowerCase().replaceAll("\\bv\\b|vejen\\b", "vej");
            //vejnavn = vejnavn.replaceAll("[s|e]\\s", " ");
            vejnavn = vejnavn.replaceAll("ö", "ø").replaceAll("aa", "å").replaceAll("é", "e");
            vejnavn = vejnavn.replaceAll("\\s","");
            return vejnavn;
        } else {
            return "";
        }
    }



    private class VirksomhedDataHandler extends DefaultHandler {

        private CvrRegisterRun currentRun;
        private RegistreringInfo registreringInfo;
        private int chunkSize;
        private Stack<String> tags;
        private boolean inVirksomhed = false;
        private boolean inProductionUnit = false;
        private boolean inDeltager = false;
        private StringList textChunk = new StringList();
        private int depth = 0;
        private ListHash<String> parameters;

        private int recordCount = 0;



        public VirksomhedDataHandler(RegistreringInfo registreringInfo, int chunkSize) {
            this.registreringInfo = registreringInfo;
            this.chunkSize = chunkSize;
            this.currentRun = new CvrRegisterRun();
            this.tags = new Stack<String>();
            this.parameters = new ListHash<String>();
        }

        private void onRecordStart() {
            this.parameters = new ListHash<String>();
            this.depth = 0;
        }

        private void onRecordSave() {
            this.onRecordSave(false);
        }
        private void onRecordSave(boolean force) {
            this.recordCount++;
            if (force || this.recordCount >= this.chunkSize) {
                    CvrRegister.this.saveRunToDatabase(this.currentRun, this.registreringInfo);
                this.currentRun.clear();
                this.currentRun = new CvrRegisterRun();
                this.recordCount = 0;
                System.gc();
            }
            this.parameters.clear();
        }

        @Override
        public void startDocument() throws SAXException {
        }

        @Override
        public void endDocument() throws SAXException {
            this.onRecordSave(true);
        }

        private boolean checkTagAndParent(String test, String tagName, String parentName) {
            return (test != null && tagName != null && parentName != null &&
                    test.equals(tagName) && !this.tags.isEmpty() && this.tags.peek().equals(parentName));
        }

        @Override
        public void startElement(String uri, String localName, String qName,
                                 Attributes attributes) throws SAXException {

            if (!inVirksomhed && !inProductionUnit && !inDeltager && this.checkTagAndParent(qName, "virksomhed","virksomheder")) {
                inVirksomhed = true;
                this.onRecordStart();
            }
            if (!inVirksomhed && !inProductionUnit && !inDeltager && this.checkTagAndParent(qName, "produktionsenhed","produktionsenheder")) {
                inProductionUnit = true;
                this.onRecordStart();
            }
            if (!inVirksomhed && !inProductionUnit && !inDeltager && this.checkTagAndParent(qName, "deltager","deltagere")) {
                inDeltager = true;
                this.onRecordStart();
            }

            if ((inVirksomhed && !qName.equals("virksomhed")) ||
                    (inProductionUnit && !qName.equals("produktionsenhed")) ||
                    (inDeltager && !qName.equals("deltager"))) {
                this.depth++;
            }

            this.tags.push(qName);
            this.textChunk = new StringList();
        }

        @Override
        public void endElement(String uri, String localName, String qName)
                throws SAXException {

            if (qName.equals(this.tags.peek())) {
                this.tags.pop();
            } else {
                CvrRegister.this.log.error("XML Inconsistency: trying to pop " + qName + ", but top of stack is " + this.tags.peek());
            }

            if (this.inVirksomhed && !this.inProductionUnit && !this.inDeltager && this.checkTagAndParent(qName, "virksomhed","virksomheder")) {
                this.inVirksomhed = false;
                VirksomhedRecord virksomhedRecord = new VirksomhedRecord(this.parameters);
                this.currentRun.add(virksomhedRecord);
                this.onRecordSave();
            }
            if (!this.inVirksomhed && this.inProductionUnit && !this.inDeltager && this.checkTagAndParent(qName, "produktionsenhed","produktionsenheder")) {
                this.inProductionUnit = false;
                ProductionUnitRecord productionUnitRecord = new ProductionUnitRecord(this.parameters);
                this.currentRun.add(productionUnitRecord);
                this.onRecordSave();
            }
            if (!this.inVirksomhed && !this.inProductionUnit && this.inDeltager && this.checkTagAndParent(qName, "deltager","deltagere")) {
                this.inDeltager = false;
                DeltagerRecord deltagerRecord = new DeltagerRecord(this.parameters);
                this.currentRun.add(deltagerRecord);
                this.onRecordSave();
            }

            if (this.inVirksomhed || this.inProductionUnit || this.inDeltager) {
                StringList path = new StringList();
                for (int i = this.tags.size() - this.depth + 1; i < this.tags.size(); i++) {
                    String a = this.tags.get(i);
                    path.append(a);
                }
                path.append(qName);
                this.parameters.put(path.join("/"), this.textChunk.join().replaceAll("\\s\\s+", " ").trim());
                this.depth--;
            }
            this.textChunk = new StringList();
        }

        // To take specific actions for each chunk of character data (such as
        // adding the data to a node or buffer, or printing it to a file).
        @Override
        public void characters(char ch[], int start, int length)
                throws SAXException {
            this.textChunk.append(new String(ch, start, length));
        }

    }

    //------------------------------------------------------------------------------------------------------------------

    public String getTemplatePath() {
        return "CvrRegisterForm";
    }

    public DataProviderConfiguration getDefaultConfiguration() {
        return new DataProviderConfiguration("{\"sourceType\":\"upload\"}");
    }

    @Override
    public List<String> getUploadFields() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("sourceUpload");
        return list;
    }

    public boolean wantUpload(DataProviderConfiguration configuration) {
        List<String> sourceType = configuration.get("sourceType");
        return sourceType != null && sourceType.contains("upload");
    }

}
