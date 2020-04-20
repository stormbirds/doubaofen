package com.googlecode.mp4parser.authoring.tracks.ttml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TtmlSegmenter {
    public static List<Document> split(Document document, int i) throws XPathExpressionException {
        XPathExpression xPathExpression;
        XPath xPath;
        int i2 = i * 1000;
        XPath newXPath = XPathFactory.newInstance().newXPath();
        XPathExpression compile = newXPath.compile("//*[name()='p']");
        ArrayList arrayList = new ArrayList();
        while (true) {
            long size = (long) (arrayList.size() * i2);
            long size2 = (long) ((arrayList.size() + 1) * i2);
            Document document2 = (Document) document.cloneNode(true);
            NodeList nodeList = (NodeList) compile.evaluate(document2, XPathConstants.NODESET);
            int i3 = 0;
            boolean z = false;
            while (i3 < nodeList.getLength()) {
                int i4 = i2;
                Node item = nodeList.item(i3);
                long startTime = TtmlHelpers.getStartTime(item);
                long endTime = TtmlHelpers.getEndTime(item);
                if (startTime >= size || endTime <= size) {
                    xPath = newXPath;
                    xPathExpression = compile;
                } else {
                    xPath = newXPath;
                    xPathExpression = compile;
                    changeTime(item, "begin", size - startTime);
                    startTime = size;
                }
                if (startTime >= size && startTime < size2 && endTime > size2) {
                    changeTime(item, "end", size2 - endTime);
                    startTime = size;
                    endTime = size2;
                }
                if (startTime > size2) {
                    z = true;
                }
                if (startTime < size || endTime > size2) {
                    item.getParentNode().removeChild(item);
                } else {
                    long j = -size;
                    changeTime(item, "begin", j);
                    changeTime(item, "end", j);
                }
                i3++;
                newXPath = xPath;
                i2 = i4;
                compile = xPathExpression;
            }
            trimWhitespace(document2);
            Element element = (Element) newXPath.compile("/*[name()='tt']/*[name()='body'][1]").evaluate(document2, XPathConstants.NODE);
            String attribute = element.getAttribute("begin");
            String attribute2 = element.getAttribute("end");
            int i5 = i2;
            if (attribute == null || "".equals(attribute)) {
                element.setAttribute("begin", TtmlHelpers.toTimeExpression(size));
            } else {
                changeTime(element, "begin", size);
            }
            if (attribute2 == null || "".equals(attribute2)) {
                element.setAttribute("end", TtmlHelpers.toTimeExpression(size2));
            } else {
                changeTime(element, "end", size2);
            }
            arrayList.add(document2);
            if (!z) {
                return arrayList;
            }
            i2 = i5;
        }
    }

    public static void changeTime(Node node, String str, long j) {
        int i;
        if (node.getAttributes() != null && node.getAttributes().getNamedItem(str) != null) {
            String nodeValue = node.getAttributes().getNamedItem(str).getNodeValue();
            long time = TtmlHelpers.toTime(nodeValue) + j;
            if (nodeValue.contains(".")) {
                i = -1;
            } else {
                i = ((int) (time - ((time / 1000) * 1000))) / 44;
            }
            node.getAttributes().getNamedItem(str).setNodeValue(TtmlHelpers.toTimeExpression(time, i));
        }
    }

    public static Document normalizeTimes(Document document) throws XPathExpressionException {
        XPath newXPath = XPathFactory.newInstance().newXPath();
        newXPath.setNamespaceContext(TtmlHelpers.NAMESPACE_CONTEXT);
        NodeList nodeList = (NodeList) newXPath.compile("//*[name()='p']").evaluate(document, XPathConstants.NODESET);
        for (int i = 0; i < nodeList.getLength(); i++) {
            pushDown(nodeList.item(i));
        }
        for (int i2 = 0; i2 < nodeList.getLength(); i2++) {
            Node item = nodeList.item(i2);
            removeAfterPushDown(item, "begin");
            removeAfterPushDown(item, "end");
        }
        return document;
    }

    private static void pushDown(Node node) {
        long j = 0;
        Node node2 = node;
        while (true) {
            node2 = node2.getParentNode();
            if (node2 == null) {
                break;
            } else if (!(node2.getAttributes() == null || node2.getAttributes().getNamedItem("begin") == null)) {
                j += TtmlHelpers.toTime(node2.getAttributes().getNamedItem("begin").getNodeValue());
            }
        }
        if (!(node.getAttributes() == null || node.getAttributes().getNamedItem("begin") == null)) {
            node.getAttributes().getNamedItem("begin").setNodeValue(TtmlHelpers.toTimeExpression(TtmlHelpers.toTime(node.getAttributes().getNamedItem("begin").getNodeValue()) + j));
        }
        if (node.getAttributes() != null && node.getAttributes().getNamedItem("end") != null) {
            node.getAttributes().getNamedItem("end").setNodeValue(TtmlHelpers.toTimeExpression(j + TtmlHelpers.toTime(node.getAttributes().getNamedItem("end").getNodeValue())));
        }
    }

    private static void removeAfterPushDown(Node node, String str) {
        while (true) {
            node = node.getParentNode();
            if (node != null) {
                if (!(node.getAttributes() == null || node.getAttributes().getNamedItem(str) == null)) {
                    node.getAttributes().removeNamedItem(str);
                }
            } else {
                return;
            }
        }
    }

    public static void trimWhitespace(Node node) {
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node item = childNodes.item(i);
            if (item.getNodeType() == 3) {
                item.setTextContent(item.getTextContent().trim());
            }
            trimWhitespace(item);
        }
    }
}
