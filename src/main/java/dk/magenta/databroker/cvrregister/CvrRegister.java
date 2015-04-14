package dk.magenta.databroker.cvrregister;

import dk.magenta.databroker.core.DataProviderConfiguration;
import dk.magenta.databroker.core.RegistreringInfo;
import dk.magenta.databroker.core.Session;
import dk.magenta.databroker.core.model.DataProviderEntity;
import dk.magenta.databroker.core.model.oio.VirkningEntity;
import dk.magenta.databroker.cvr.model.CvrModel;
import dk.magenta.databroker.cvr.model.companyunit.CompanyUnitEntity;
import dk.magenta.databroker.cvr.model.embeddable.*;
import dk.magenta.databroker.dawa.model.DawaModel;
import dk.magenta.databroker.dawa.model.enhedsadresser.EnhedsAdresseEntity;
import dk.magenta.databroker.register.Register;
import dk.magenta.databroker.register.RegisterRun;
import dk.magenta.databroker.util.TimeRecorder;
import dk.magenta.databroker.util.TransactionCallback;
import dk.magenta.databroker.util.Util;
import dk.magenta.databroker.util.objectcontainers.Level1Container;
import dk.magenta.databroker.register.records.Record;
import dk.magenta.databroker.util.objectcontainers.ListHash;
import dk.magenta.databroker.util.objectcontainers.StringList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.*;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

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
            this.obtain("ajourDato", "ajourfoeringsdato");
        }

        protected void obtain(String key, String path) {
            this.put(key, this.hash.getFirst(path));
        }

        protected List<String> getList(String path) {
            return this.hash.get(path);
        }

        protected Date parseDate(String dateStr) {
            if (dateStr != null && !dateStr.isEmpty()) {
                final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    return format.parse(dateStr);
                } catch (ParseException e) {
                }
            }
            return null;
        }

        public Date getDate(String key) {
            return this.parseDate(this.get(key));
        }

        protected void loadIndustries() {
            this.obtain("primaryIndustry", "hovedbranche/kode");
            this.obtain("primaryIndustryText", "hovedbranche/tekst");
            this.obtain("secondaryIndustry1", "bibranche1/kode");
            this.obtain("secondaryIndustryText1", "bibranche1/tekst");
            this.obtain("secondaryIndustry2", "bibranche2/kode");
            this.obtain("secondaryIndustryText2", "bibranche2/tekst");
            this.obtain("secondaryIndustry3", "bibranche3/kode");
            this.obtain("secondaryIndustryText3", "bibranche3/tekst");
        }

        protected void loadLocationAddress() {
            this.loadLocationAddress(null);
        }
        protected void loadLocationAddress(String prefix) {
            if (prefix == null || prefix.isEmpty()) {
                prefix = "";
            } else {
                prefix += "/";
            }
            prefix += "beliggenhedsadresse";
            this.obtain("beliggenhed_valid", prefix+"/gyldigFra");
            this.obtain("beliggenhed_kommunekode", prefix+"/kommune/kode");
            this.obtain("beliggenhed_kommunenavn", prefix+"/kommune/tekst");
            this.obtain("beliggenhed_vejkode", prefix+"/vejkode");
            this.obtain("beliggenhed_vejnavn", prefix+"/vejnavn");
            this.obtain("beliggenhed_husnummerFra", prefix+"/husnummerFra");
            this.obtain("beliggenhed_husnummerTil", prefix+"/husnummerTil");
            this.obtain("beliggenhed_bogstavFra", prefix+"/bogstavFra");
            this.obtain("beliggenhed_bogstavTil", prefix+"/bogstavTil");
            this.obtain("beliggenhed_etage", prefix+"/etage");
            this.obtain("beliggenhed_sidedoer", prefix+"/sidedoer");
            this.obtain("beliggenhed_postnr", prefix+"/postnr");
            this.obtain("beliggenhed_postdistrikt", prefix+"/postdistrikt");
            this.obtain("beliggenhed_bynavn", prefix+"/bynavn");
            this.obtain("beliggenhed_co", prefix+"/coNavn");
            this.obtain("beliggenhed_postboks", prefix+"/postboks");
            this.obtain("beliggenhed_text", prefix+"/adresseFritekst");
        }
        protected void loadPostalAddress() {
            this.loadPostalAddress(null);
        }
        protected void loadPostalAddress(String prefix) {
            if (prefix == null || prefix.isEmpty()) {
                prefix = "";
            } else {
                prefix += "/";
            }
            prefix += "postadresse";
            this.obtain("post_valid", prefix+"/gyldigFra");
            this.obtain("post_kommunekode", prefix+"/kommune/kode");
            this.obtain("post_kommunenavn", prefix+"/kommune/tekst");
            this.obtain("post_vejkode", prefix+"/vejkode");
            this.obtain("post_vejnavn", prefix+"/vejnavn");
            this.obtain("post_husnummerFra", prefix+"/husnummerFra");
            this.obtain("post_husnummerTil", prefix+"/husnummerTil");
            this.obtain("post_bogstavFra", prefix+"/bogstavFra");
            this.obtain("post_bogstavTil", prefix+"/bogstavTil");
            this.obtain("post_etage", prefix+"/etage");
            this.obtain("post_sidedoer", prefix+"/sidedoer");
            this.obtain("post_postnr", prefix+"/postnr");
            this.obtain("post_postdistrikt", prefix+"/postdistrikt");
            this.obtain("post_bynavn", prefix+"/bynavn");
            this.obtain("post_co", prefix+"/coNavn");
            this.obtain("post_postboks", prefix+"/postboks");
            this.obtain("post_text", prefix+"/adresseFritekst");
        }

        protected void loadContactInfo() {
            this.obtain("phone", "telefonnummer/kontaktoplysning");
            this.obtain("phone_valid", "telefonnummer/gyldigFra");
            this.obtain("fax", "telefax/kontaktoplysning");
            this.obtain("fax_valid", "telefax/gyldigFra");
            this.obtain("email", "email/kontaktoplysning");
            this.obtain("email_valid", "email/gyldigFra");
        }


        protected void loadYearlyEmploy() {
            this.loadYearlyEmploy(null);
        }
        protected void loadYearlyEmploy(String prefix) {
                if (prefix == null || prefix.isEmpty()) {
                prefix = "";
            } else {
                prefix += "/";
            }
            prefix += "aarsbeskaeftigelse";
            this.obtain("årsbeskæftigelse_år", prefix+"/aar");
            this.obtain("årsbeskæftigelse_ansatte", prefix+"/antalAnsatte");
            this.obtain("årsbeskæftigelse_årsværk", prefix+"/antalAarsvaerk");
            this.obtain("årsbeskæftigelse_ansatteInklEjere", prefix+"/antalInclEjere");
            this.obtain("årsbeskæftigelse_ansatteInterval", prefix+"/antalAnsatteInterval");
            this.obtain("årsbeskæftigelse_årsværkInterval", prefix+"/antalAarsvaerkInterval");
            this.obtain("årsbeskæftigelse_ansatteInklEjereInterval", prefix+"/antalInclEjereInterval");
        }

        protected void loadQuarterlyEmploy() {
            this.loadQuarterlyEmploy(null);
        }
        protected void loadQuarterlyEmploy(String prefix) {
                if (prefix == null || prefix.isEmpty()) {
                prefix = "";
            } else {
                prefix += "/";
            }
            prefix += "kvartalsbeskaeftigelse";
            this.obtain("kvartalsbeskæftigelse_år", prefix+"/aar");
            this.obtain("kvartalsbeskæftigelse_kvartal", prefix+"/kvartal");
            this.obtain("kvartalsbeskæftigelse_ansatte", prefix+"/antalAnsatte");
            this.obtain("kvartalsbeskæftigelse_aarsværk", prefix+"/antalAarsvaerk");
            this.obtain("kvartalsbeskæftigelse_ansatteInklEjere", prefix+"/antalInclEjere");
            this.obtain("kvartalsbeskæftigelse_ansatteInterval", prefix+"/antalAnsatteInterval");
            this.obtain("kvartalsbeskæftigelse_aarsværkInterval", prefix+"/antalAarsvaerkInterval");
            this.obtain("kvartalsbeskæftigelse_ansatteInklEjereInterval", prefix+"/antalInclEjereInterval");
        }
    }

    private class CompanyDataRecord extends CvrRecord {
        public CompanyDataRecord(ListHash<String> hash) {
            super(hash);
            this.obtain("name", "navn/tekst");
            this.obtain("advertProtection", "reklamebeskyttelse");

            this.obtain("startDate", "livsforloeb/startdato");
            this.obtain("endDate", "livsforloeb/slutdato");

            this.loadIndustries();
            this.loadLocationAddress();
            this.loadPostalAddress();
            this.loadContactInfo();
            this.loadYearlyEmploy();
            this.loadQuarterlyEmploy();
        }

        public CompanyInfo toCompanyInfo() {
            CompanyInfo info = new CompanyInfo();

            info.setUpdateDate(this.getDate("ajourDato"));
            info.setName(this.get("name"));
            info.setAdvertProtection(this.getBoolean("advertProtection"));

            if (this.containsKey("startDate") || this.containsKey("endDate")) {
                LifeCycle lifeCycle = new LifeCycle();
                lifeCycle.setStartDate(this.getDate("startDate"));
                lifeCycle.setEndDate(this.getDate("endDate"));
                info.setLifeCycle(lifeCycle);
            }

            info.setPrimaryIndustryCode(this.getInt("primaryIndustry"));
            info.addSecondaryIndustryCode(this.getInt("secondaryIndustry1"));
            info.addSecondaryIndustryCode(this.getInt("secondaryIndustry2"));
            info.addSecondaryIndustryCode(this.getInt("secondaryIndustry3"));


            if (this.containsKey("beliggenhed_valid")) {

                CvrAddress address = new CvrAddress(
                        this.getDate("beliggenhed_valid"),
                        this.get("beliggenhed_vejnavn"),
                        this.getInt("beliggenhed_vejkode"),
                        this.getInt("beliggenhed_husnummerFra"),
                        this.getInt("beliggenhed_husnummerTil"),
                        this.getChar("beliggenhed_bogstavFra"),
                        this.getChar("beliggenhed_bogstavTil"),
                        this.get("beliggenhed_etage"),
                        this.get("beliggenhed_sidedoer"),
                        this.getInt("beliggenhed_postnr"),
                        this.get("beliggenhed_postdistrikt"),
                        this.get("beliggenhed_bynavn"),
                        this.getInt("beliggenhed_kommunekode"),
                        this.get("beliggenhed_kommunenavn"),
                        this.getInt("post_postboks"),
                        this.get("beliggenhed_co"),
                        this.get("beliggenhed_text"),
                        null);
                info.setLocationAddress(address);
            }

            if (this.containsKey("post_valid")) {
                CvrAddress address = new CvrAddress(
                        this.getDate("post_valid"),
                        this.get("post_vejnavn"),
                        this.getInt("post_vejkode"),
                        this.getInt("post_husnummerFra"),
                        this.getInt("post_husnummerTil"),
                        this.getChar("post_husnummerFra"),
                        this.getChar("post_husnummerTil"),
                        this.get("post_etage"),
                        this.get("post_sidedoer"),
                        this.getInt("post_postnr"),
                        this.get("post_postdistrikt"),
                        this.get("post_bynavn"),
                        this.getInt("post_kommunekode"),
                        this.get("post_kommunenavn"),
                        this.getInt("post_postboks"),
                        this.get("post_co"),
                        this.get("post_text"),
                        null);
                info.setPostalAddress(address);
            }


            if (this.containsKey("phone") && this.containsKey("phone_valid")) {
                info.setTelephoneNumber(new ValidFromField(this.get("phone"), this.getDate("phone_valid")));
            }
            if (this.containsKey("fax") && this.containsKey("fax_valid")) {
                info.setTelefaxNumber(new ValidFromField(this.get("fax"), this.getDate("fax_valid")));
            }
            if (this.containsKey("email") && this.containsKey("email_valid")) {
                info.setEmail(new ValidFromField(this.get("email"), this.getDate("email_valid")));
            }

            if (this.containsKey("årsbeskæftigelse_ansatte")) {
                YearlyEmployeeNumbers yearlyEmployeeNumbers = new YearlyEmployeeNumbers();
                yearlyEmployeeNumbers.setEmployees(this.getInt("årsbeskæftigelse_ansatte"));
                yearlyEmployeeNumbers.setEmployeesInterval(this.get("årsbeskæftigelse_ansatteInterval"));
                yearlyEmployeeNumbers.setIncludingOwners(this.getInt("årsbeskæftigelse_ansatteInklEjere"));
                yearlyEmployeeNumbers.setIncludingOwnersInterval(this.get("årsbeskæftigelse_ansatteInklEjereInterval"));
                yearlyEmployeeNumbers.setFullTimeEquivalent(this.getInt("årsbeskæftigelse_årsværk"));
                yearlyEmployeeNumbers.setFullTimeEquivalentInterval(this.get("årsbeskæftigelse_årsværkInterval"));
                yearlyEmployeeNumbers.setYear(this.getInt("årsbeskæftigelse_år"));
                info.setYearlyEmployeeNumbers(yearlyEmployeeNumbers);
            }
            if (this.containsKey("kvartalsbeskæftigelse_ansatte")) {
                QuarterlyEmployeeNumbers quarterlyEmployeeNumbers = new QuarterlyEmployeeNumbers();
                quarterlyEmployeeNumbers.setEmployees(this.getInt("kvartalsbeskæftigelse_ansatte"));
                quarterlyEmployeeNumbers.setEmployeesInterval(this.get("kvartalsbeskæftigelse_ansatteInterval"));
                quarterlyEmployeeNumbers.setIncludingOwners(this.getInt("kvartalsbeskæftigelse_ansatteInklEjere"));
                quarterlyEmployeeNumbers.setIncludingOwnersInterval(this.get("kvartalsbeskæftigelse_ansatteInklEjereInterval"));
                quarterlyEmployeeNumbers.setFullTimeEquivalent(this.getInt("kvartalsbeskæftigelse_årsværk"));
                quarterlyEmployeeNumbers.setFullTimeEquivalentInterval(this.get("kvartalsbeskæftigelse_årsværkInterval"));
                quarterlyEmployeeNumbers.setYear(this.getInt("kvartalsbeskæftigelse_år"));
                quarterlyEmployeeNumbers.setQuarter(this.getInt("kvartalsbeskæftigelse_kvartal"));
                info.setQuarterlyEmployeeNumbers(quarterlyEmployeeNumbers);
            }

            return info;
        }
    }

    private class VirksomhedRecord extends CompanyDataRecord {
        private HashMap<Long, Date> deltagerMap;
        public VirksomhedRecord(ListHash<String> virksomhedHash) {
            super(virksomhedHash);
            this.obtain("cvrNummer", "cvrnr");
            this.obtain("form", "virksomhedsform/kode");
            this.obtain("formText", "virksomhedsform/tekst");

            List<String> gyldigFra = this.getList("deltagere/deltager/gyldigFra");
            List<String> deltagere = this.getList("deltagere/deltager/deltagernummer");
            if (gyldigFra != null && deltagere != null && gyldigFra.size() == deltagere.size()) {
                this.deltagerMap = new HashMap<Long, Date>();
                for (int i=0; i<gyldigFra.size(); i++) {
                    try {
                        this.deltagerMap.put(Long.parseLong(deltagere.get(i)), this.parseDate(gyldigFra.get(i)));
                    } catch (NumberFormatException e) {}
                }
            }
        }
        public Map<Long, Date> getDeltagerMap() {
            return deltagerMap;
        }
    }

    private class ProductionUnitRecord extends CompanyDataRecord {
        public ProductionUnitRecord(ListHash<String> productionunitHash) {
            super(productionunitHash);
            this.obtain("pNummer", "pnr");
            this.obtain("cvrNummer", "virksomhed/virksomhed/cvrnr");
            this.obtain("isPrimary", "hovedafdeling");
        }
    }

    private class DeltagerRecord extends CvrRecord {
        public DeltagerRecord(ListHash<String> deltagerHash) {
            super(deltagerHash);
            this.obtain("nummer", "deltagernummer");
            this.obtain("gyldigDato", "deltagelseGyldigFra");
            this.obtain("cvrNummer", "cvrnr");
            this.obtain("type", "oplysninger/deltagertype");
            this.obtain("navn", "oplysninger/navn");
            this.obtain("status", "oplysninger/personstatus");
            this.obtain("rolle", "rolle");

            loadLocationAddress("oplysninger");
        }

        public CvrAddress getLocationAddress() {
            CvrAddress address = new CvrAddress(
                    this.getDate("beliggenhed_valid"),
                    this.get("beliggenhed_vejnavn"),
                    this.getInt("beliggenhed_vejkode"),
                    this.getInt("beliggenhed_husnummerFra"),
                    this.getInt("beliggenhed_husnummerTil"),
                    this.getChar("beliggenhed_husnummerFra"),
                    this.getChar("beliggenhed_husnummerTil"),
                    this.get("beliggenhed_etage"),
                    this.get("beliggenhed_sidedoer"),
                    this.getInt("beliggenhed_postnr"),
                    this.get("beliggenhed_postdistrikt"),
                    this.get("beliggenhed_bynavn"),
                    this.getInt("beliggenhed_kommunekode"),
                    this.get("beliggenhed_kommunenavn"),
                    this.getInt("post_postboks"),
                    this.get("beliggenhed_co"),
                    this.get("beliggenhed_text"),
                    null);
            return address;
        }
    }

    public class CvrRegisterRun extends RegisterRun {
        private Level1Container<VirksomhedRecord> virksomheder;
        private Level1Container<ProductionUnitRecord> productionUnits;
        private Level1Container<DeltagerRecord> deltagere;

        public CvrRegisterRun() {
            super();
            this.virksomheder = new Level1Container<VirksomhedRecord>(1000);
            this.productionUnits = new Level1Container<ProductionUnitRecord>(1000);
            this.deltagere = new Level1Container<DeltagerRecord>(1000);
        }

        public boolean add(VirksomhedRecord virksomhed) {
            this.virksomheder.put(virksomhed.get("cvrNummer"), virksomhed);
            return true;//this.add((Record) virksomhed);
        }

        public boolean add(ProductionUnitRecord productionUnit) {
            this.productionUnits.put(productionUnit.get("pNummer"), productionUnit);
            return true;//this.add((Record) productionUnit);
        }

        public boolean add(DeltagerRecord deltager) {
            this.deltagere.put(deltager.get("nummer"), deltager);
            return true;//this.add((Record) deltager);
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
            //super.clear();
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



    @Override
    protected void importData(RegistreringInfo registreringInfo) {
        try {
            int chunkSize = 1000;//(int) (registreringInfo.getInputSize() / 50000L);
            this.log.info("Importing data in chunks of size: " + chunkSize);
            DefaultHandler handler = new VirksomhedDataHandler(registreringInfo, chunkSize);
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            parser.parse(registreringInfo.getInputStream(), handler);
            //this.generateAddresses(false);

            // this.onTransactionEnd();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ensureIndustryInDatabase(int code, String text, Session session) {
        if (code > 0) {
            this.cvrModel.setIndustry(code, text, session, true);
        }
    }

    private void ensureFormInDatabase(int code, String text, Session session) {
        if (code > 0) {
            this.cvrModel.setForm(code, text, session, true);
        }
    }



    @Override
    protected RegisterRun parse(InputStream input) {
        return null;
    }


    private static final int UNKNOWN_INDUSTRY = 999999;




    private int totalCompanyCount = 0;
    private int totalUnitCount = 0;
    private int totalMemberCount = 0;

    @Override
    protected void saveRunToDatabase(RegisterRun run, RegistreringInfo registreringInfo) {

        if (run.getClass() == CvrRegisterRun.class) {

            Session session = registreringInfo.getSession();

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

                if (false && companyCount > 0) {
                    TimeRecorder sumTime = new TimeRecorder();
                    for (VirksomhedRecord virksomhed : cRun.getVirksomheder().getList()) {
                        TimeRecorder itemTimer = new TimeRecorder();

                        // Make sure the referenced industries are present in the DB
                        itemTimer.record();
                        this.ensureIndustryInDatabase(virksomhed.getInt("primaryIndustry"), virksomhed.get("primaryIndustryText"), session);
                        this.ensureIndustryInDatabase(virksomhed.getInt("secondaryIndustry1"), virksomhed.get("secondaryIndustryText1"), session);
                        this.ensureIndustryInDatabase(virksomhed.getInt("secondaryIndustry2"), virksomhed.get("secondaryIndustryText2"), session);
                        this.ensureIndustryInDatabase(virksomhed.getInt("secondaryIndustry3"), virksomhed.get("secondaryIndustryText3"), session);

                        itemTimer.record();

                        // Fetch basic fields
                        long cvrNummer = virksomhed.getLong("cvrNummer");
                        int form = virksomhed.getInt("form");
                        itemTimer.record();
                        this.ensureFormInDatabase(form, virksomhed.get("formText"), session);

                        itemTimer.record();
                        this.cvrModel.setCompany(cvrNummer, form,
                                virksomhed.toCompanyInfo(), virksomhed.getDeltagerMap(),
                                registreringInfo, new ArrayList<VirkningEntity>());
                        itemTimer.record();

                        sumTime.add(itemTimer);
                    }

                    double time = sumTime.sum();
                    totalCompanyCount += companyCount;
                    this.log.info(companyCount + " companies created in " + time + " ms (avg " + (time / (double) companyCount) + " ms) " + sumTime.toString() + " " + this.cvrModel.getCompanyTimer());
                    this.cvrModel.resetCompanyTimer();

                }


                if (false && unitCount > 0) {
                    TimeRecorder sumTime = new TimeRecorder();

                    for (ProductionUnitRecord unit : cRun.getProductionUnits().getList()) {
                        TimeRecorder itemTimer = new TimeRecorder();

                        // Make sure the referenced industries are present in the DB
                        this.ensureIndustryInDatabase(unit.getInt("primaryIndustry"), unit.get("primaryIndustryText"), session);
                        this.ensureIndustryInDatabase(unit.getInt("secondaryIndustry1"), unit.get("secondaryIndustryText1"), session);
                        this.ensureIndustryInDatabase(unit.getInt("secondaryIndustry2"), unit.get("secondaryIndustryText2"), session);
                        this.ensureIndustryInDatabase(unit.getInt("secondaryIndustry3"), unit.get("secondaryIndustryText3"), session);

                        // Fetch basic fields
                        long pNummer = unit.getLong("pNummer");
                        String cvrNummer = unit.get("cvrNummer");
                        boolean isPrimaryUnit = unit.getBoolean("isPrimary");

                        itemTimer.record();
                        this.cvrModel.setCompanyUnit(pNummer, cvrNummer,
                                unit.toCompanyInfo(),
                                isPrimaryUnit,
                                registreringInfo, new ArrayList<VirkningEntity>()
                        );
                        itemTimer.record();

                        sumTime.add(itemTimer);
                    }

                    double time = sumTime.sum();
                    totalUnitCount += unitCount;
                    this.log.info(unitCount + " production units created in " + time + " ms (avg " + (time / (double) unitCount) + " ms) " + sumTime + " " + this.cvrModel.getUnitTimer());
                    this.cvrModel.resetUnitTimer();
                }


                if (memberCount > 0) {

                    this.cvrModel.resetIndustryCache();


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
                        this.cvrModel.setDeltager(deltagerNummer, name, cvrNummer, ajourDate, gyldigDate, typeName, rolleName, statusName, deltager.getLocationAddress(),
                                registreringInfo, new ArrayList<VirkningEntity>());
                        itemTimer.record();
                        sumTime.add(itemTimer);
                    }
                    double time = sumTime.sum();
                    totalMemberCount += memberCount;
                    this.log.info(memberCount + " members created in " + time + " ms (avg " + (time / (double) memberCount) + " ms) " + sumTime + " " + this.cvrModel.getDeltagerTimer());
                    this.cvrModel.resetDeltagerTimer();
                }

                //this.cvrModel.flushCompanies();
                //this.dawaModel.flush();
                //this.cvrModel.flush();


                //this.cvrModel.printStatistics();
            }
            catch (Exception ex) {
                ex.printStackTrace();
                //this.rollbackTransaction();
                return;
            }
            this.log.info("Update completed (finished " + totalCompanyCount + " companies, " + totalUnitCount + " units and " + totalMemberCount + " members)");
            registreringInfo.logProcess(this.log);
        }
    }

    protected List<TransactionCallback> getBulkwireCallbacks(DataProviderEntity dataProviderEntity) {
        ArrayList<TransactionCallback> transactionCallbacks = new ArrayList<TransactionCallback>();
        transactionCallbacks.add(new TransactionCallback() {
            @Override
            public void run() {
                CvrRegister.this.dawaModel.resetAllCaches();
            }
        });
        transactionCallbacks.addAll(this.cvrModel.getBulkwireCallbacks());
        return transactionCallbacks;
    }



    private HashMap<String, Boolean> generatedAddresses = new HashMap<String, Boolean>();
    private synchronized String addNeededAddress(int kommuneKode, int vejKode, String husNr, String etage, String doer) {
        String descriptor = EnhedsAdresseEntity.generateDescriptor(kommuneKode, vejKode, husNr, etage, doer);
        if (!this.generatedAddresses.containsKey(descriptor)) {
            this.generatedAddresses.put(descriptor, false);
        }
        return descriptor;
    }
    private void generateAddresses(boolean bulkwire) {
        this.dawaModel.resetAllCaches();
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
                } catch (ArrayIndexOutOfBoundsException e) {
                }
            }
        }
        this.dawaModel.flush();
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

        TimeRecorder timeRecorder = new TimeRecorder();

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
            if (this.recordCount % 100 == 0) {
                this.timeRecorder.record();
            }
            if (force || this.recordCount >= this.chunkSize) {
                System.out.println("Saving records to database " + this.timeRecorder);
                    CvrRegister.this.saveRunToDatabase(this.currentRun, this.registreringInfo);
                this.currentRun.clear();
                this.currentRun = new CvrRegisterRun();
                this.recordCount = 0;
                this.timeRecorder.reset();

                //System.gc();
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
            this.textChunk = null;
            this.textChunk = new StringList();
        }

        private Pattern whitespace = Pattern.compile("\\s\\s+");

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
                    path.append(this.tags.get(i));
                }
                path.append(qName);
                String value = this.whitespace.matcher(this.textChunk.join()).replaceAll(" ").trim();
                this.parameters.put(path.join("/"), value);
                this.depth--;
                path = null;
            }
            this.textChunk = null;
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
