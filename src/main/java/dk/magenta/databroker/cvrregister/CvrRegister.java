package dk.magenta.databroker.cvrregister;

import dk.magenta.databroker.core.DataProviderConfiguration;
import dk.magenta.databroker.core.model.DataProviderEntity;
import dk.magenta.databroker.register.Register;
import dk.magenta.databroker.register.RegisterRun;
import dk.magenta.databroker.register.objectcontainers.Level1Container;
import dk.magenta.databroker.register.records.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
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
            this.obtain("secondaryIndustry1", "bibranche1/kode");
            this.obtain("secondaryIndustry2", "bibranche2/kode");
            this.obtain("secondaryIndustry3", "bibranche3/kode");
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
    }

    @Override
    protected void saveRunToDatabase(RegisterRun run, DataProviderEntity dataProviderEntity) {

    }

    @Override
    protected RegisterRun parse(InputStream input) {
        try {
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
            RegisterRun run = new RegisterRun();

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


    private XPathFactory xpathFactory = XPathFactory.newInstance();
    private NodeList getNodes(Node parentNode, String xpathExpression) {
        try {
            return (NodeList) this.xpathFactory.newXPath().evaluate(xpathExpression, parentNode, XPathConstants.NODESET);
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
