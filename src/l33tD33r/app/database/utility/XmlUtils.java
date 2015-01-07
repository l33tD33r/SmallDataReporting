package l33tD33r.app.database.utility;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

public class XmlUtils {

    private static DocumentBuilder docBuilder;

    static {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            docBuilder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException("Error creating DocumentBuilder", e);
        }
    }

    public static Document createDocument(InputStream is) throws SAXException, IOException {
        try {
            return docBuilder.parse(is);
        } finally {
            is.close();
        }
    }

    public static Document deserializeDocument(String xml) throws SAXException, IOException {
        return docBuilder.parse(xml);
    }

    public static Document createDocument() {
        return docBuilder.newDocument();
    }

    public static String getElementName(Element element) {
        if (element == null || element.getNodeType() != Node.ELEMENT_NODE) {
            return null;
        }
        return element.getNodeName();
    }

    public static Element getChildElement(Element parent, String childName) {
        ArrayList<Element> childElements = getChildElements(parent, childName);
        return childElements.size() > 0 ? childElements.get(0) : null;
    }

    public static ArrayList<Element> getChildElements(Element parent) {
        return getChildElements(parent, null);
    }

    public static ArrayList<Element> getChildElements(Element parent, String childName) {
        ArrayList<Element> childElements = new ArrayList<Element>();
        NodeList childNodes = parent.getChildNodes();

        for (int i=0; i < childNodes.getLength(); i++) {
            Node childNode = childNodes.item(i);
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                if (childName == null || childNode.getNodeName().equals(childName)) {
                    childElements.add((Element)childNode);
                }
            }
        }

        return childElements;
    }

    public static String getElementStringValue(Element parentElement, String childElementName) {
        Element childElement = getChildElement(parentElement, childElementName);
        return getStringValue(childElement);
    }

    public static Integer getElementIntegerValue(Element parentElement, String childElementName) {
        return getElementIntegerValue(parentElement, childElementName, 0);
    }
    public static Integer getElementIntegerValue(Element parentElement, String childElementName, int defaultValue) {
        Element childElement = getChildElement(parentElement, childElementName);
        if (childElement == null) {
            return defaultValue;
        }
        return getIntegerValue(childElement);
    }

    public static Boolean getElementBooleanValue(Element parentElement, String childElementName) {
        return getElementBooleanValue(parentElement, childElementName, false);
    }

    public static Boolean getElementBooleanValue(Element parentElement, String childElementName, boolean defaultValue) {
        Element childElement = getChildElement(parentElement, childElementName);
        if (childElement == null) {
            return defaultValue;
        }
        return getBooleanValue(childElement);
    }

    public static String getStringValue(Element stringElement) {
        return getTextContent(stringElement);
    }

    public static Integer getIntegerValue(Element integerElement) throws NumberFormatException {
        String elementValue = getTextContent(integerElement);
        return Integer.valueOf(elementValue);
    }

    public static boolean getBooleanValue(Element booleanElement) {
        String elementValue = getTextContent(booleanElement);
        return Boolean.valueOf(elementValue);
    }

    private static String getTextContent(Element element) {
        if (element == null) {
            return null;
        }
        Node textNode = element.getFirstChild();
        if (textNode == null) {
            return "";
        }
        if (textNode.getNodeType() != Node.TEXT_NODE) {
            throw new RuntimeException("Element " + element.getTagName() + "does not have text content");
        }
        return textNode.getNodeValue();
    }

    private static Element createChildElement(Document document, Element parentElement, String elementTagName) {
        Element stringElement = document.createElement(elementTagName);
        parentElement.appendChild(stringElement);
        return stringElement;
    }

    public static void writeElement(Document document, Element parentElement, String elementTagName) {
        createChildElement(document, parentElement, elementTagName);
    }

    public static void writeStringElement(Document document, Element parentElement, String elementTagName, String elementValue) {
        Element stringElement = createChildElement(document, parentElement, elementTagName);
        Text textNode = document.createTextNode(elementValue);
        stringElement.appendChild(textNode);
    }

    public static void writeStringValue(Document document, Element element, String elementValue) {
        Text textNode = document.createTextNode(elementValue);
        element.appendChild(textNode);
    }

    public static String getAttributeStringValue(Element element, String attributeName) {
        return element.getAttribute(attributeName);
    }

    public static Boolean getAttributeBooleanValue(Element element, String attributeName) {
        String stringValue = getAttributeStringValue(element, attributeName);
        return Boolean.parseBoolean(stringValue);
    }

    public static String serializeDocumentToXml(Document document) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        Element documentElement = document.getDocumentElement();
        serializeElement(sb, documentElement, 0);
        sb.append("\n");
        return sb.toString();
    }

    private static void serializeElement(StringBuilder sb, Element element, int tabIndex) {
        sb.append('\n');
        for (int i=0; i < tabIndex; i++) {
            sb.append('\t');
        }
        sb.append("<");
        sb.append(element.getTagName());
        if (element.hasAttributes()) {
            NamedNodeMap attributes = element.getAttributes();
            for (int i=0; i < attributes.getLength(); i++) {
                Node attribute = attributes.item(i);

                sb.append(" ");
                sb.append(attribute.getNodeName());
                sb.append("=");
                sb.append("\"");
                String value = attribute.getNodeValue();
                sb.append(value.replace("\"", "\\\""));
                sb.append("\"");
            }
        }
        sb.append(">");
        NodeList nodeList = element.getChildNodes();
        ArrayList<Element> childElements = new ArrayList<Element>();
        for (int i=0; i < nodeList.getLength(); i++) {
            Node childNode = nodeList.item(i);
            if (childNode instanceof Element) {
                childElements.add((Element)childNode);
            }
        }
        if (childElements.size() == 0) {
            sb.append(escapeInvalidCharacters(getTextContent(element)));
        } else {
            for (Element childElement : childElements) {
                serializeElement(sb, childElement, tabIndex+1);
            }
            sb.append('\n');
            for (int i=0; i < tabIndex; i++) {
                sb.append('\t');
            }
        }
        sb.append("</");
        sb.append(element.getTagName());
        sb.append(">");
    }

    private static String escapeInvalidCharacters(String textContent) {
        if (textContent == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i=0; i < textContent.length(); i++) {
            char c = textContent.charAt(i);
            switch (c) {
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                case '\"':
                    sb.append("&quot;");
                    break;
                case '\'':
                    sb.append("&apos;");
                    break;
                case '&':
                    sb.append("&amp;");
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();
    }
}
