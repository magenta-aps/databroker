package dk.magenta.databroker.cvrregister;

import dk.magenta.databroker.core.DataProviderConfiguration;
import dk.magenta.databroker.core.model.oio.RegistreringEntity;
import dk.magenta.databroker.core.model.DataProviderEntity;
import dk.magenta.databroker.core.model.oio.VirkningEntity;
import dk.magenta.databroker.cvr.model.CvrModel;
import dk.magenta.databroker.dawa.model.DawaModel;
import dk.magenta.databroker.dawa.model.enhedsadresser.EnhedsAdresseEntity;
import dk.magenta.databroker.dawa.model.vejstykker.VejstykkeEntity;
import dk.magenta.databroker.register.Register;
import dk.magenta.databroker.register.RegisterRun;
import dk.magenta.databroker.util.Util;
import dk.magenta.databroker.util.objectcontainers.Level1Container;
import dk.magenta.databroker.register.records.Record;
import dk.magenta.databroker.util.objectcontainers.ListHash;
import dk.magenta.databroker.util.objectcontainers.StringList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
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

            this.obtain("vejkode", "beliggenhedsadresse/vejkode");
            this.obtain("kommunekode", "beliggenhedsadresse/kommune/kode");
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
        }

    }

    public class CvrRegisterRun extends RegisterRun {
        private Level1Container<VirksomhedRecord> virksomheder;
        private Level1Container<ProductionUnitRecord> productionUnits;
        public CvrRegisterRun() {
            super();
            this.virksomheder = new Level1Container<VirksomhedRecord>();
            this.productionUnits = new Level1Container<ProductionUnitRecord>();
        }

        public boolean add(VirksomhedRecord virksomhed) {
            this.virksomheder.put(virksomhed.get("cvrNummer"), virksomhed);
            return this.add((Record) virksomhed);
        }
        public boolean add(ProductionUnitRecord productionUnit) {
            this.productionUnits.put(productionUnit.get("pNummer"), productionUnit);
            return this.add((Record) productionUnit);
        }
        public Level1Container<VirksomhedRecord> getVirksomheder() {
            return this.virksomheder;
        }
        public Level1Container<ProductionUnitRecord> getProductionUnits() {
            return this.productionUnits;
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

    /*public URL getRecordUrl() throws MalformedURLException {
        return new URL("https://cpr.dk/media/152096/vejregister_hele_landet_pr_150101.zip");
    }*/
    public Resource getRecordResource() {
        return this.ctx.getResource("classpath:/data/cvrAll.zip");
    }

    public void pull(boolean forceFetch, boolean forceParse, DataProviderEntity dataProviderEntity) {
        super.pull(forceFetch, forceParse, dataProviderEntity);
    }


    @Override
    protected void importData(InputStream input, DataProviderEntity dataProviderEntity) {
        try {
            DefaultHandler handler = new VirksomhedDataHandler(dataProviderEntity, 50000);
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            parser.parse(input, handler);
            this.bulkWireReferences();
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



    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private PlatformTransactionManager txManager;


    @Override
    protected RegisterRun parse(InputStream input) {
        return null;
    }


    private static final int UNKNOWN_INDUSTRY = 999999;


    int totalCompanies = 0;
    int totalUnits = 0;

    @Override
    protected void saveRunToDatabase(RegisterRun run, DataProviderEntity dataProviderEntity) {

        if (run.getClass() == CvrRegisterRun.class) {

            this.cvrModel.resetIndustryCache(); // TODO: investigate how much really needs to be reset

            RegistreringEntity createRegistrering = this.getCreateRegistrering(dataProviderEntity);
            RegistreringEntity updateRegistrering = this.getUpdateRegistrering(dataProviderEntity);

            DefaultTransactionDefinition def = new DefaultTransactionDefinition();
            def.setName("CommandLineTransactionDefinition");
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
            TransactionStatus status = this.txManager.getTransaction(def);

            long lookupAddress = 0;
            long createAddress = 0;

            try {

                CvrRegisterRun cRun = (CvrRegisterRun) run;
                this.dawaModel.resetAllCaches();

                System.out.println("there are "+cRun.getVirksomheder().getList().size()+" virksomheder");
                System.out.println("there are "+cRun.getProductionUnits().getList().size()+" production units");

                for (VirksomhedRecord virksomhed : cRun.getVirksomheder().getList()) {

                    // Make sure the referenced industries are present in the DB
                    this.ensureIndustryInDatabase(virksomhed.getInt("primaryIndustry"), virksomhed.get("primaryIndustryText"));
                    this.ensureIndustryInDatabase(virksomhed.getInt("secondaryIndustry1"), virksomhed.get("secondaryIndustryText1"));
                    this.ensureIndustryInDatabase(virksomhed.getInt("secondaryIndustry2"), virksomhed.get("secondaryIndustryText2"));
                    this.ensureIndustryInDatabase(virksomhed.getInt("secondaryIndustry3"), virksomhed.get("secondaryIndustryText3"));
                    this.ensureFormInDatabase(virksomhed.getInt("form"), virksomhed.get("formText"));

                    // Fetch basic fields
                    String cvrNummer = virksomhed.get("cvrNummer");
                    boolean advertProtection = virksomhed.getInt("advertProtection") == 1;
                    String name = virksomhed.get("name");

                    int form = virksomhed.getInt("form");
                    int primaryIndustry = virksomhed.getInt("primaryIndustry");

                    Date startDate = this.parseDate(virksomhed.get("startDate"));
                    Date endDate = this.parseDate(virksomhed.get("endDate"));

                    int vejkode = virksomhed.getInt("vejkode");
                    int kommunekode = virksomhed.getInt("kommunekode");
                    String phone = virksomhed.get("phone");

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
                    int[] secondaryIndustries = new int[secondaryIndustriesList.size()];
                    int i = 0;
                    for (Iterator<Integer> iIter = secondaryIndustriesList.iterator(); iIter.hasNext(); secondaryIndustries[i++] = iIter.next());

                    this.cvrModel.setCompany(cvrNummer, name,
                            primaryIndustry, secondaryIndustries, form,
                            startDate, endDate,
                            createRegistrering, updateRegistrering, new ArrayList<VirkningEntity>());
                    totalCompanies++;
                }


                for (ProductionUnitRecord unit : cRun.getProductionUnits().getList()) {

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
                    boolean advertProtection = unit.getInt("advertProtection") == 1;

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

                    VejstykkeEntity vej = this.dawaModel.getVejstykke(kommuneKode, vejKode);
                    if (vej != null) {

                        String cprNavn = Util.emptyIfNull(vej.getLatestVersion().getVejnavn()).toLowerCase();
                        String cvrNavn = Util.emptyIfNull(vejNavn).toLowerCase();
                        if (!cprNavn.equals(cvrNavn)) {
                            System.err.println("Mismatch on "+kommuneKode+":"+vejKode+"; cpr names it "+cprNavn+" while cvr names it "+cvrNavn);
                        } else {

                            tic();
                            adresse = this.dawaModel.getEnhedsAdresse(kommuneKode, vejKode, fullHusNr, etage, sidedoer);
                            lookupAddress += toc();
                            if (adresse == null) {
                                tic();
                                adresse = dawaModel.setAdresse(kommuneKode, vejKode, fullHusNr, null, etage, sidedoer, createRegistrering, updateRegistrering);

                                if (dawaModel.getVejstykkeCache().get(kommuneKode, vejKode) == null) {
                                    System.err.println("Purportedly named '" + unit.get("vejnavn") + "'");
                                }
                                createAddress += toc();
                            }
                            this.cvrModel.setCompanyUnit(pNummer, name, cvrNummer,
                                    primaryIndustry, secondaryIndustries,
                                    adresse, addressDate, phone, fax, email,
                                    startDate, endDate,
                                    advertProtection,
                                    createRegistrering, updateRegistrering, new ArrayList<VirkningEntity>()
                            );
                            totalUnits++;
                        }
                    } else {
                        System.err.println("Vej "+kommuneKode+":"+vejKode+" not found; cvr names it "+vejNavn);
                    }
                }
                System.out.println("Addresses searched in "+lookupAddress+" ("+((float) lookupAddress / 50000.0)+")");
                System.out.println("Addresses created in "+createAddress+" ("+((float) createAddress / 50000.0)+")");
                this.cvrModel.flushCompanies();

                this.txManager.commit(status);
            }
            catch (Exception ex) {
                System.out.println("Transaction failed");
                ex.printStackTrace();
                this.txManager.rollback(status);
            }
        }
        System.out.println("ending transaction ("+totalCompanies+" companies and "+totalUnits+" units)");
    }

    private void bulkWireReferences() {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("CommandLineTransactionDefinition");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = this.txManager.getTransaction(def);
        this.cvrModel.bulkWireReferences();
        this.txManager.commit(status);
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



    private class VirksomhedDataHandler extends DefaultHandler {

        private CvrRegisterRun currentRun;
        private DataProviderEntity dataProviderEntity;
        private int chunkSize;
        private Stack<String> tags;
        private boolean inVirksomhed = false;
        private boolean inProductionUnit = false;
        private String textChunk = "";
        private int depth = 0;
        private ListHash<String> parameters;


        private int recordCount = 0;

        public VirksomhedDataHandler(DataProviderEntity dataProviderEntity, int chunkSize) {
            this.dataProviderEntity = dataProviderEntity;
            this.chunkSize = chunkSize;
            this.currentRun = new CvrRegisterRun();
            this.tags = new Stack<String>();
            this.parameters = new ListHash<String>();
        }

        private void onRecordSave() {
            this.onRecordSave(false);
        }
        private void onRecordSave(boolean force) {
            this.recordCount++;
            if (force || this.recordCount >= this.chunkSize) {
                CvrRegister.this.saveRunToDatabase(this.currentRun, this.dataProviderEntity);
                this.currentRun = new CvrRegisterRun();
                this.recordCount = 0;
                System.gc();
            }
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

            if (!inVirksomhed && this.checkTagAndParent(qName, "virksomhed","virksomheder")) {
                inVirksomhed = true;
                this.parameters = new ListHash<String>();
                this.depth = 0;
            }
            if (!inVirksomhed && !inProductionUnit && this.checkTagAndParent(qName, "produktionsenhed","produktionsenheder")) {
                inProductionUnit = true;
                this.parameters = new ListHash<String>();
                this.depth = 0;
            }
            if ((inVirksomhed && !qName.equals("virksomhed")) ||
                    (inProductionUnit && !qName.equals("produktionsenhed"))) {
                this.depth++;
            }

            this.tags.push(qName);
            this.textChunk = "";
        }

        @Override
        public void endElement(String uri, String localName, String qName)
                throws SAXException {

            if (qName.equals(this.tags.peek())) {
                this.tags.pop();
            } else {
                System.err.println("Inconsistency: trying to pop "+qName+", but to of stack is "+this.tags.peek());
            }

            if (this.inVirksomhed && this.checkTagAndParent(qName, "virksomhed","virksomheder")) {
                this.inVirksomhed = false;
                VirksomhedRecord virksomhedRecord = new VirksomhedRecord(this.parameters);
                this.currentRun.add(virksomhedRecord);
                this.onRecordSave();
            }
            if (!this.inVirksomhed && this.inProductionUnit && this.checkTagAndParent(qName, "produktionsenhed","produktionsenheder")) {
                this.inProductionUnit = false;
                ProductionUnitRecord productionUnitRecord = new ProductionUnitRecord(this.parameters);
                this.currentRun.add(productionUnitRecord);
                this.onRecordSave();
            }

            if (this.inVirksomhed || this.inProductionUnit) {
                StringList path = new StringList();
                for (int i = this.tags.size() - this.depth + 1; i < this.tags.size(); i++) {
                    String a = this.tags.get(i);
                    path.append(a);
                }
                path.append(qName);
                this.parameters.put(path.join("/"), this.textChunk.trim());
                this.depth--;
            }
            this.textChunk = "";
        }

        // To take specific actions for each chunk of character data (such as
        // adding the data to a node or buffer, or printing it to a file).
        @Override
        public void characters(char ch[], int start, int length)
                throws SAXException {
            this.textChunk += new String(ch, start, length);
        }

    }

    //------------------------------------------------------------------------------------------------------------------

    public String getTemplatePath() {
        return "/fragments/CvrRegisterForm.txt";
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
