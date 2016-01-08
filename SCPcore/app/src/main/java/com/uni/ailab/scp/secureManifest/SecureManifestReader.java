package com.uni.ailab.scp.secureManifest;

import com.uni.ailab.scp.cnf.Formula;
import com.uni.ailab.scp.policy.Policy;
import com.uni.ailab.scp.policy.Scope;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class SecureManifestReader extends XPathQueries {

    private final String packageName = singleStringQuery("/securemanifest/@package");
    private final String applicationName = singleStringQuery("/securemanifest/application/@name");

    public SecureManifestReader(String xmlSecureManifest) {
        super(loadXMLFromString(xmlSecureManifest));
    }


    public Collection<Component> getComponents() {
        Collection<Component> components = new ArrayList<>();

        String packName = getValueOfAttribute(multipleNodeQuery("/securemanifest").item(0), "name");
        Node application = multipleNodeQuery("/securemanifest/application").item(0);
        NodeList nodesComponents = application.getChildNodes();
        for (int i=0; i<nodesComponents.getLength(); i++) {
            Node nodeComponent = nodesComponents.item(i);
            if (nodeComponent instanceof Element) {
                Element elementComponent = (Element) nodeComponent;
                String fqdnComponentName = packName + '.' + elementComponent.getAttribute("name");
                String componentTypeName = elementComponent.getNodeName();
                Collection<String> permissions = new ArrayList<>();
                Collection<Policy> policies = new ArrayList<>();
                NodeList permsAndPolicies = elementComponent.getChildNodes();
                for (int j=0; j<permsAndPolicies.getLength(); j++) {
                    Node nodePermOrPolicy = permsAndPolicies.item(j);
                    if (nodePermOrPolicy instanceof Element) {
                        Element elementPermOrPolicy = (Element) nodePermOrPolicy;
                        String componentType = elementPermOrPolicy.getNodeName();
                        switch (componentType) {
                            case "comp-permission":
                                permissions.add(elementPermOrPolicy.getAttribute("name"));
                                break;
                            case "policy":
                                String scope = elementPermOrPolicy.getAttribute("scope");
                                String formula = elementPermOrPolicy.getTextContent();
                                boolean sticky = Boolean.parseBoolean(elementPermOrPolicy.getAttribute("sticky"));
                                policies.add(new Policy(
                                        Scope.getEnum(scope),
                                        new Formula(formula),
                                        sticky
                                ));
                                break;
                        }

                    }
                }
                ComponentType ct = ComponentType.getEnum(componentTypeName);
                components.add(new Component(
                        fqdnComponentName,
                        ct,
                        permissions,
                        policies
                        ));
            }
        }
        return components;
    }

    private static Document loadXMLFromString(String xml) {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            InputSource inputSource = new InputSource(new StringReader(xml));
            return documentBuilder.parse(inputSource);
        } catch (ParserConfigurationException |SAXException |IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
