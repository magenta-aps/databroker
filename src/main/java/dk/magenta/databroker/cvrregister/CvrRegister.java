package dk.magenta.databroker.cvrregister;

import dk.magenta.databroker.core.DataProviderConfiguration;
import dk.magenta.databroker.cvr.model.company.CompanyEntity;
import dk.magenta.databroker.util.Util;
import dk.magenta.databroker.core.model.DataProviderEntity;
import dk.magenta.databroker.core.model.oio.VirkningEntity;
import dk.magenta.databroker.cvr.model.CvrModel;
import dk.magenta.databroker.dawa.model.DawaModel;
import dk.magenta.databroker.dawa.model.SearchParameters;
import dk.magenta.databroker.dawa.model.SearchParameters.Key;
import dk.magenta.databroker.dawa.model.enhedsadresser.EnhedsAdresseEntity;
import dk.magenta.databroker.register.Register;
import dk.magenta.databroker.register.RegisterRun;
import dk.magenta.databroker.util.objectcontainers.Level1Container;
import dk.magenta.databroker.register.records.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by lars on 26-01-15.
 */
@Component
public class CvrRegister extends Register {

    private class NullResolver implements EntityResolver {
        public InputSource resolveEntity(String publicId, String systemId) throws SAXException,
                IOException {
            return new InputSource(new StringReader(""));
        }
    }

    private class CvrRecord extends Record {
        private Node virksomhedNode;

        public CvrRecord(Node virksomhedNode) {
            this.virksomhedNode = virksomhedNode;
        }

        protected String getFirstNodeValue(String xpath) {
            return CvrRegister.this.getFirstNodeValue(this.virksomhedNode, xpath);
        }
        protected List<String> getNodeValues(String xpath) {
            return CvrRegister.this.getNodeValues(this.virksomhedNode, xpath);
        }

        protected void obtain(String key, String xpath) {
            this.put(key, this.getFirstNodeValue(xpath));
        }
    }

    private class VirksomhedRecord extends CvrRecord {

        private List<Long> productionUnits;

        public VirksomhedRecord(Node virksomhedNode) {
            super(virksomhedNode);
            this.obtain("cvrNummer", "cvrnr");
            this.obtain("advertProtection", "reklamebeskyttelse");
            this.obtain("name", "navn/tekst");
            this.obtain("form", "virksomhedsform/kode");
            this.obtain("primaryIndustry", "hovedbranche/kode");
            this.obtain("secondaryIndustry1", "bibranche1/kode");
            this.obtain("secondaryIndustry2", "bibranche2/kode");
            this.obtain("secondaryIndustry3", "bibranche3/kode");
            this.obtain("startDate", "livsforloeb/startdato");
            this.obtain("endDate", "livsforloeb/slutdato");

            this.obtain("vejkode", "beliggenhedsadresse/vejkode");
            this.obtain("kommunekode", "beliggenhedsadresse/kommune/kode");
            this.obtain("phone", "telefonnummer/kontaktoplysning");

            this.productionUnits = new ArrayList<Long>();
            for (String pNummer : this.getNodeValues("produktionsenheder/produktionsenhed/pnr")) {
                try {
                    this.productionUnits.add(Long.parseLong(pNummer));
                } catch (NumberFormatException e) {}
            }
        }

        public List<Long> getProductionUnits(){
            return this.productionUnits;
        }
    }

    private class ProductionUnitRecord extends CvrRecord {

        public ProductionUnitRecord(Node virksomhedNode) {
            super(virksomhedNode);
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
    private CvrModel cvrModel;

    @Autowired
    private DawaModel dawaModel;

    @Transactional
    public void pull(boolean forceFetch, boolean forceParse, DataProviderEntity dataProviderEntity) {
        super.pull(forceFetch, forceParse, dataProviderEntity);
    }

    private void ensureIndustryInDatabase(int code, String text) {
        if (code > 0 && text != null) {
            this.cvrModel.setIndustry(code, text, true);
        }
    }

    @Override
    protected void saveRunToDatabase(RegisterRun run, DataProviderEntity dataProviderEntity) {
        if (run.getClass() == CvrRegisterRun.class) {
            CvrRegisterRun cRun = (CvrRegisterRun) run;
            this.dawaModel.resetAllCaches();

            for (VirksomhedRecord virksomhed : cRun.getVirksomheder().getList()) {
                this.ensureIndustryInDatabase(virksomhed.getInt("primaryIndustry"), virksomhed.get("primaryIndustryText"));
                this.ensureIndustryInDatabase(virksomhed.getInt("secondaryIndustry1"), virksomhed.get("secondaryIndustryText1"));
                this.ensureIndustryInDatabase(virksomhed.getInt("secondaryIndustry2"), virksomhed.get("secondaryIndustryText2"));
                this.ensureIndustryInDatabase(virksomhed.getInt("secondaryIndustry3"), virksomhed.get("secondaryIndustryText3"));

                String cvrNummer = virksomhed.get("cvrNummer");
                boolean advertProtection = virksomhed.getInt("advertProtection") == 1;
                String name = virksomhed.get("name");
                int form = virksomhed.getInt("form");

                int primaryIndustry = virksomhed.getInt("primaryIndustry");
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

                Date startDate = this.parseDate(virksomhed.get("startDate"));
                Date endDate = this.parseDate(virksomhed.get("endDate"));

                int vejkode = virksomhed.getInt("vejkode");
                int kommunekode = virksomhed.getInt("kommunekode");
                String phone = virksomhed.get("phone");

                List<Long> productionUnitsList = virksomhed.getProductionUnits();
                long primaryProductionUnit = 0;

                if (!productionUnitsList.isEmpty()) {
                    if (productionUnitsList.size() > 1) {
                        for (Iterator<Long> iIter = productionUnitsList.iterator(); iIter.hasNext(); ) {
                            long pNummer = iIter.next();
                            if (primaryProductionUnit == 0) {
                                ProductionUnitRecord unit = cRun.getProductionUnits().get(pNummer);
                                if (name.equals(unit.get("name")) && vejkode == unit.getInt("vejkode") && kommunekode == unit.getInt("kommunekode") && Util.compare(phone, unit.get("phone"))) {
                                    primaryProductionUnit = pNummer;
                                }
                            }
                        }
                    }
                    if (primaryProductionUnit == 0) {
                        primaryProductionUnit = productionUnitsList.get(0);
                    }
                }

                System.out.println("primaryProductionUnit: "+primaryProductionUnit);

                this.tic();

                this.cvrModel.setCompany(cvrNummer, name, primaryProductionUnit,
                        primaryIndustry, secondaryIndustries, form,
                        startDate, endDate,
                        this.getCreateRegistrering(dataProviderEntity), this.getUpdateRegistrering(dataProviderEntity), new ArrayList<VirkningEntity>());

                System.out.println("Crated new company in "+this.toc()+" ms");
            }




            for (ProductionUnitRecord unit : cRun.getProductionUnits().getList()) {
                this.ensureIndustryInDatabase(unit.getInt("primaryIndustry"), unit.get("primaryIndustryText"));
                this.ensureIndustryInDatabase(unit.getInt("secondaryIndustry1"), unit.get("secondaryIndustryText1"));
                this.ensureIndustryInDatabase(unit.getInt("secondaryIndustry2"), unit.get("secondaryIndustryText2"));
                this.ensureIndustryInDatabase(unit.getInt("secondaryIndustry3"), unit.get("secondaryIndustryText3"));

                long pNummer = unit.getLong("pNummer");
                String name = unit.get("name");
                String cvrNummer = unit.get("cvrNummer");

                CompanyEntity company = this.cvrModel.getCompany(cvrNummer);
                int primaryIndustry = unit.getInt("primaryIndustry");
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


                SearchParameters addressSearch = new SearchParameters();
                int kommuneKode = unit.getInt("kommunekode");
                int postNr = unit.getInt("postnr");
                int vejKode = unit.getInt("vejkode");
                String husnr = unit.get("husnummerFra");
                String bogstav = unit.get("bogstavFra");
                String etage = unit.get("etage");
                String sidedoer = unit.get("sidedoer");
                String fullHusNr = husnr + (bogstav != null ? bogstav : "");
                addressSearch.put(Key.KOMMUNE, kommuneKode);
                //addressSearch.put(Key.POST, postNr);
                addressSearch.put(Key.VEJ, vejKode);
                addressSearch.put(Key.HUSNR, fullHusNr);
                if (etage != null) {
                    addressSearch.put(Key.ETAGE, etage);
                }
                if (sidedoer != null) {
                    addressSearch.put(Key.DOER, sidedoer);
                }
                this.tic();
                EnhedsAdresseEntity adresse = dawaModel.getSingleEnhedsAdresse(addressSearch, false);
                System.out.println("Searched for adresse in "+this.toc()+" ms");
                if (adresse == null) {
                    this.tic();
                    adresse = dawaModel.setAdresse(kommuneKode, vejKode, fullHusNr, null, etage, sidedoer, this.getCreateRegistrering(dataProviderEntity), this.getUpdateRegistrering(dataProviderEntity));
                    System.out.println("Created new adresse " + adresse.getLatestVersion().getAdgangsadresse().getVejstykke().getLatestVersion().getVejnavn() + " " + fullHusNr + " in "+this.toc()+" ms");
                }

                String phone = unit.get("phone");
                String fax = unit.get("fax");
                String email = unit.get("email");

                Date startDate = this.parseDate(unit.get("startDate"));
                Date endDate = this.parseDate(unit.get("endDate"));

                boolean advertProtection = unit.getInt("advertProtection") == 1;
                this.tic();
                this.cvrModel.setCompanyUnit(pNummer, name, company,
                        primaryIndustry, secondaryIndustries,
                        adresse, phone, fax, email,
                        startDate, endDate,
                        advertProtection,
                        createRegistrering, updateRegistrering, new ArrayList<VirkningEntity>()
                );
                System.out.println("Created company unit in "+this.toc()+" ms");
            }
        }

    }

    private Date parseDate(String date) {
        if (date != null && !date.isEmpty()) {
            final SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd");
            try {
                return dateParser.parse(date);
            } catch (ParseException e) {
            }
        }
        return null;
    }

    @Override
    protected RegisterRun parse(InputStream input) {
        try {
            this.tic();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            //dbf.setValidating(false);
            //dbf.setIgnoringComments(false);
            //dbf.setIgnoringElementContentWhitespace(true);
            //dbf.setNamespaceAware(true);
            // dbf.setCoalescing(true);
            // dbf.setExpandEntityReferences(true);

            DocumentBuilder builder = null;
            builder = dbf.newDocumentBuilder();
            builder.setEntityResolver(new NullResolver());
            // builder.setErrorHandler( new MyErrorHandler());

            Document document = builder.parse(input);
            CvrRegisterRun run = new CvrRegisterRun();

            NodeList virksomhedNodes = this.getNodes(document, "/report/virksomheder/virksomhed");
            if (virksomhedNodes != null) {
                for (int i=0; i<virksomhedNodes.getLength(); i++) {
                    Node virksomhedNode = virksomhedNodes.item(i);
                    VirksomhedRecord record = new VirksomhedRecord(virksomhedNode);
                    run.add(record);
                }
            }

            NodeList productionUnitNodes = this.getNodes(document, "/report/produktionsenheder/produktionsenhed");
            if (productionUnitNodes != null) {
                for (int i=0; i<productionUnitNodes.getLength(); i++) {
                    Node productionUnitNode = productionUnitNodes.item(i);
                    ProductionUnitRecord record = new ProductionUnitRecord(productionUnitNode);
                    run.add(record);
                }
            }
            System.out.println("Parsed in "+this.toc()+" ms");
            return run;

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    //------------------------------------------------------------------------------------------------------------------

    private XPath xpath = XPathFactory.newInstance().newXPath();
    private NodeList getNodes(Node parentNode, String xpathExpression) {
        try {
            return (NodeList) this.xpath.evaluate(xpathExpression, parentNode, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
            return null;
        }
    }
    private List<String> getNodeValues(Node parentNode, String xpathExpression) {
        ArrayList<String> values = new ArrayList<String>();
        NodeList nodes = this.getNodes(parentNode, xpathExpression);
        if (nodes != null) {
            for (int i = 0; i < nodes.getLength(); i++) {
                String nodeValue = nodes.item(i).getTextContent();
                if (nodeValue != null) {
                    values.add(nodeValue);
                }
            }
        }
        return values;
    }
    private Node getFirstNode(Node parentNode, String xpathExpression) {
        NodeList nodes = this.getNodes(parentNode, xpathExpression);
        return (nodes != null && nodes.getLength() > 0) ? nodes.item(0) : null;
    }
    private String getFirstNodeValue(Node parentNode, String xpathExpression) {
        Node node = this.getFirstNode(parentNode, xpathExpression);
        return node != null ? node.getTextContent() : null;
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public String getTemplatePath() {
        return null;
    }

    @Override
    public DataProviderConfiguration getDefaultConfiguration() {
        return null;
    }

    @Autowired
    private ConfigurableApplicationContext ctx;

    @Override
    public Resource getRecordResource() {
        return this.ctx.getResource("classpath:/data/cvr.xml");
    }

}
