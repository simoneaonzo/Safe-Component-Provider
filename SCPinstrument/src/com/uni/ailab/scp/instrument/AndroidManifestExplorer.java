package com.uni.ailab.scp.instrument;


import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AndroidManifestExplorer {

    private final File androidManifestXml;
    private final Document parsedDoc;
    private final ArrayList<String> permissions = new ArrayList<>();

    public AndroidManifestExplorer(Path manifestPath) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        this.androidManifestXml = new File(manifestPath.toUri());
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        parsedDoc = dBuilder.parse(androidManifestXml);
        parsedDoc.getDocumentElement().normalize();

        XPath xPath = XPathFactory.newInstance().newXPath();
        NodeList nodes = (NodeList)xPath.evaluate("/manifest/uses-permission",
                parsedDoc.getDocumentElement(), XPathConstants.NODESET);
        for (int i = 0; i < nodes.getLength(); ++i) {
            Element e = (Element) nodes.item(i);
            permissions.add(e.getAttribute("android:name"));
        }
    }

    public Collection<String> getPermissions() {
        return permissions;
    }
}
