import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Parser {
    private FileInputStream file;
    private Map<String,Product> products;
    static String query = "//Invoice/Line";

    public Parser(FileInputStream file) throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        this.file = file;
        products = new TreeMap<>();
    }

    private NodeList getNodeList() throws IOException, SAXException, ParserConfigurationException, XPathExpressionException {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        Document xmlDocument = builder.parse(file);
        XPath xPath = XPathFactory.newInstance().newXPath();
        return (NodeList) xPath.compile(query).evaluate(xmlDocument, XPathConstants.NODESET);
    }

    private String getProductName(NodeList nodeList,int i) {
        String name;
        for (int j = 0;;j++) {
            if(nodeList.item(i).getChildNodes().item(j).getNodeName().equalsIgnoreCase("ProductDescription")) {
                return nodeList.item(i).getChildNodes().item(j).getTextContent();
            }
        }
    }

    private double getProductQtt(NodeList nodeList, int i) {
        double qtt;
        for (int j = 0;;j++) {
            if(nodeList.item(i).getChildNodes().item(j).getNodeName().equalsIgnoreCase("Quantity")) {
                return Double.parseDouble(nodeList.item(i).getChildNodes().item(j).getTextContent().trim());
            }
        }
    }

    private double getProductUnitPrice(NodeList nodeList, int i) {
        double unitPrice;
        for (int j = 0;;j++) {
            if(nodeList.item(i).getChildNodes().item(j).getNodeName().equalsIgnoreCase("UnitPrice")) {
                return Double.parseDouble(nodeList.item(i).getChildNodes().item(j).getTextContent().trim());
            }
        }
    }

    private float getProductTaxPercentage(NodeList nodeList, int i) {
        float tax;
        for (int j = 0; ; j++) {
            if (nodeList.item(i).getChildNodes().item(j).getNodeName().equalsIgnoreCase("Tax")) {
                for (int k = 0; ; k++) {
                    if (nodeList.item(i).getChildNodes().item(j).getChildNodes().item(k).getNodeName().equalsIgnoreCase("TaxPercentage")) {
                        return Float.parseFloat(nodeList.item(i).getChildNodes().item(j).getChildNodes().item(k).getTextContent().trim());
                    }
                }
            }
        }
    }

    private void readNodeList(NodeList nodeList) {
        for (int i = 0; i < nodeList.getLength(); i++) {

            String name = getProductName(nodeList,i);
            double qtt = getProductQtt(nodeList,i);
            double unitPrice = getProductUnitPrice(nodeList,i);
            float taxPercentage = getProductTaxPercentage(nodeList,i);
            double soldValueNoVat = (unitPrice * qtt);
            double soldWithVat = soldValueNoVat + soldValueNoVat * (taxPercentage/100.0f);

            if(products.containsKey(name)) {
                Product p = products.get(name);
                p.addSoldQtt(qtt);
                p.addValueSold(soldWithVat);
            } else {
                products.put(name,new Product(name,qtt,soldWithVat));
            }
        }
    }

    public Map<String,Product> getList() throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
        List<String> list = new ArrayList<>();
        NodeList nodeList = getNodeList();
        readNodeList(nodeList);
        return products;
    }

}
