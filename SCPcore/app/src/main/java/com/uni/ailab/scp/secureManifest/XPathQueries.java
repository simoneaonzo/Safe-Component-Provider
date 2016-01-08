package com.uni.ailab.scp.secureManifest;


import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.ArrayList;
import java.util.Collection;

public abstract class XPathQueries {
    private final XPath xpath = XPathFactory.newInstance().newXPath();
    private final Document document;

    public XPathQueries(Document document) {
        this.document = document;
    }

    protected void addAllNodes(Collection<Node> collection, NodeList nodeList) {
        for (int i=0; i<nodeList.getLength(); i++) {
            collection.add(nodeList.item(i));
        }
    }

    public String singleStringQuery(String query) {
        String ret = "";
        try {
            ret = (String)xpath.evaluate(query, document, XPathConstants.STRING);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public Collection<String> multipleStringQuery(String query, String attribute) {
        ArrayList<String> ret = new ArrayList<>();
        try {
            NodeList nodeList = (NodeList) xpath.evaluate(query, document, XPathConstants.NODESET);
            for (int i=0; i<nodeList.getLength(); i++) {
                ret.add(getValueOfAttribute(nodeList.item(i), attribute));
            }
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public NodeList multipleNodeQuery(String query) {
        try {
            return (NodeList) xpath.evaluate(query, document, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getValueOfAttribute(Node node, String attribute) {
        if (node != null) {
            NamedNodeMap nnm = node.getAttributes();
            if (nnm != null) {
                Node namedItem = nnm.getNamedItem(attribute);
                if (namedItem != null)
                    return namedItem.getNodeValue();
            }
        }
        throw new NullPointerException();
    }
}
