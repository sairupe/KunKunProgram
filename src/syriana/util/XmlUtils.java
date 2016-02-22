package syriana.util;

import java.io.CharArrayReader;
import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * <p>
 * Title: XmlUtils
 * </p>
 * <p>
 * Description: XML
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * 
 * @author bluesky
 * @version 1.0
 */
public class XmlUtils {
	enum Type{
		BOOLEAN,
		BYTE,
		SHORT,
		INT,
		LONG,
		FLOAT,
		DOUBLE,
		STRING;
		public static Type getEnum(String s) {
			try {
				return Type.valueOf(s.trim().toUpperCase());
			} catch (IllegalArgumentException ex) {
				ex.printStackTrace();
				return null;
			}
		}
	}
	
	public static Object getValueFromStr(String type, String str){
		switch(Type.getEnum(type)){
		case BOOLEAN:
			return Boolean.valueOf(str);
		case BYTE:
			return Byte.valueOf(str);
		case SHORT:
			return Short.valueOf(str);
		case INT:
			return Integer.valueOf(str);
		case LONG:
			return Long.valueOf(str);
		case FLOAT:
			return Float.valueOf(str);
		case DOUBLE:
			return Double.valueOf(str);
		case STRING:
			return str;
		}
		return null;
	}
	private static final String BR = System.getProperty("line.separator");

	/**
	 * load a xml file from OS file system and interpret it into a Document no
	 * charset limited
	 * 
	 * @param xmlfile
	 * @return Document
	 * @throws Exception
	 */
	public static Document load(String xmlfile) throws Exception {
		javax.xml.parsers.DocumentBuilderFactory factory = javax.xml.parsers.DocumentBuilderFactory
				.newInstance();
		factory.setIgnoringComments(false);
		factory.setIgnoringElementContentWhitespace(false);
		factory.setValidating(false);
		factory.setCoalescing(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder.parse(xmlfile);
	}

	/**
	 * load a xml file from OS file system and interpret it into a Document no
	 * charset limited
	 * 
	 * @param xmlfile
	 *            String
	 * @return Document
	 * @throws Exception
	 */
	public static Document load(File xmlfile) throws Exception {
		javax.xml.parsers.DocumentBuilderFactory factory = javax.xml.parsers.DocumentBuilderFactory
				.newInstance();
		factory.setIgnoringComments(false);
		factory.setIgnoringElementContentWhitespace(false);
		factory.setValidating(false);
		factory.setCoalescing(true);
		DocumentBuilder builder = factory.newDocumentBuilder();

		return builder.parse(xmlfile);
	}
	
	/**
	 * load a xml file from OS file system and interpret it into a Document no
	 * charset limited
	 * 
	 * @param xmlfile
	 *            String
	 * @return Document
	 * @throws Exception
	 */
	public static Document load(InputStream xmlfile) throws Exception {
		javax.xml.parsers.DocumentBuilderFactory factory = javax.xml.parsers.DocumentBuilderFactory
				.newInstance();
		factory.setIgnoringComments(false);
		factory.setIgnoringElementContentWhitespace(false);
		factory.setValidating(false);
		factory.setCoalescing(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder.parse(xmlfile);
	}

	public static boolean isXml(String file) {
		if (file.toLowerCase().endsWith("xml")) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isDat(String file) {
		if (file.toLowerCase().endsWith("dat")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * load a String without the title tag of xml into a Document
	 * 
	 * @param domContent
	 *            String 濞岋繝锟斤拷銈嗭拷head锟斤拷锟斤拷锟組L锟斤拷锟斤拷鐑斤拷锟姐倖锟?
	 * @return Document
	 * @throws Exception
	 */
	public static Document loadStringWithoutTitle(String domContent)
			throws Exception {
		domContent = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" + BR
				+ domContent;
		return XmlUtils.loadString(domContent);
	}

	/**
	 * load a String with a title tag of xml into a Document
	 * 
	 * @param domContent
	 *            String XML锟斤拷锟斤拷鐑斤拷锟姐倖锟?
	 * @return Document
	 * @throws Exception
	 */
	public static Document loadString(String domContent) throws Exception {
		javax.xml.parsers.DocumentBuilderFactory factory = javax.xml.parsers.DocumentBuilderFactory
				.newInstance();
		factory.setIgnoringComments(false);
		factory.setIgnoringElementContentWhitespace(false);
		factory.setValidating(false);
		factory.setCoalescing(false);
		DocumentBuilder builder = factory.newDocumentBuilder();

		char[] chars = new char[domContent.length()];
		domContent.getChars(0, domContent.length(), chars, 0);
		InputSource is = new InputSource(new CharArrayReader(chars));
		return (builder.parse(is));
	}

	/**
	 * 
	 * @param doc
	 * @param fullname
	 * @return
	 */
	public static String getTextByFullName(Document doc, String fullname) {
		String path[] = StringUtils.toStringArray(fullname, ".");
		Element e = doc.getDocumentElement();
		for (int i = 1; i < path.length; i++) {
			e = getChildByName(e, path[i]);
		}
		return getText(e);
	}

	/**
	 * 
	 * @param parent
	 * @param fullname
	 * @return
	 */
	public static String getTextByFullName(Element parent, String fullname) {
		String path[] = StringUtils.toStringArray(fullname, ".");
		Element e = parent;
		for (int i = 0; i < path.length; i++) {
			e = getChildByName(e, path[i]);
		}
		return getText(e);
	}

	/**
	 * @param parent
	 *            Element
	 * @param name
	 *            String
	 * @return String
	 */
	public static String getChildText(Element parent, String name) {
		Element e = getChildByName(parent, name);
		if (e == null) {
			return "";
		}
		return getText(e);
	}

	/**
	 * 
	 * @param e
	 * @param name
	 * @return
	 */
	public static String[] getChildrenText(Element e, String name) {
		NodeList nl = e.getChildNodes();
		int max = nl.getLength();
		LinkedList<String> list = new LinkedList<String>();
		for (int i = 0; i < max; i++) {
			Node n = nl.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE
					&& n.getNodeName().equals(name)) {
				list.add(getText((Element)n));
			}
		}
		return list.toArray(new String[list.size()]);
	}
	
	/**
	 * 
	 * @param e
	 * @param name
	 * @return
	 */
	public static Element[] getChildrenByName(Element e, String name) {
		NodeList nl = e.getChildNodes();
		int max = nl.getLength();
		LinkedList<Node> list = new LinkedList<Node>();
		for (int i = 0; i < max; i++) {
			Node n = nl.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE
					&& n.getNodeName().equals(name)) {
				list.add(n);
			}
		}
		return list.toArray(new Element[list.size()]);
	}

	/**
	 * 
	 * @param e
	 * @param name
	 * @return
	 */
	public static Element getChildByName(Element e, String name) {
		Element[] list = getChildrenByName(e, name);
		if (list.length == 0) {
			return null;
		}
		if (list.length > 1) {
			throw new IllegalStateException("Too many (" + list.length + ") '"
					+ name + "' elements found!");
		}
		return list[0];
	}

	/**
	 * 
	 * @param e
	 * @return
	 */
	public static String getText(Element e) {
		NodeList nl = e.getChildNodes();
		int max = nl.getLength();
		for (int i = 0; i < max; i++) {
			Node n = nl.item(i);
			if (n.getNodeType() == Node.TEXT_NODE) {
				return n.getNodeValue().trim();
			}
		}
		return "";
	}

	public static String getAttribute(Element e, String name) {
		return e.getAttribute(name);
	}

	/**
	 * get Int value
	 * 
	 * @param player
	 * @param title
	 * @return
	 */
	public static int getIntValue(Element e) {
		return Integer.valueOf(getText(e));
	}

	public static Long getLongValue(Element e) {
		return Long.valueOf(getText(e));
	}

	/**
	 * get byte value
	 * 
	 * @param player
	 * @param title
	 * @return
	 */
	public static byte getByteValue(Element e) {
		return Byte.valueOf(getText(e));
	}

	/**
	 * 
	 * @param xmlfile
	 * @param doc
	 * @throws Exception
	 */
	public static void save(String xmlfile, Document doc) throws Exception {
		DOMSource doms = new DOMSource(doc);

		File f = new File(xmlfile);
		File dir = f.getParentFile();
		dir.mkdirs();
		StreamResult sr = new StreamResult(f);

		try {
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer();
			Properties properties = t.getOutputProperties();
			properties.setProperty(OutputKeys.ENCODING, "UTF-8");
			properties.setProperty(OutputKeys.INDENT, "yes");
			t.setOutputProperties(properties);
			t.transform(doms, sr);

		} catch (TransformerConfigurationException tce) {
			tce.printStackTrace();
		} catch (TransformerException te) {
			te.printStackTrace();
		}

	}

	/**
	 * create a blank Document.
	 * 
	 * @param rootElementName
	 *            String
	 * @return Document
	 * @throws Exception
	 */
	public static Document blankDocument(String rootElementName)
			throws Exception {
		javax.xml.parsers.DocumentBuilderFactory factory = javax.xml.parsers.DocumentBuilderFactory
				.newInstance();
		factory.setIgnoringComments(false);
		factory.setIgnoringElementContentWhitespace(false);
		factory.setValidating(false);
		factory.setCoalescing(false);
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.newDocument();
		Element root = doc.createElement(rootElementName);
		doc.appendChild(root);
		return doc;
	}

	public static Element createChild(Document doc, Element root, String name) {
		Element elem = doc.createElement(name);
		root.appendChild(elem);
		return elem;
	}
	
	public static Element createChildBefore(Document doc, Element sourceNode, Element afterNode, String name) {
		Element elem = doc.createElement(name);
		sourceNode.insertBefore(elem, afterNode);
		return elem;
	}
	
	public static Element createChildBefore(Document doc, Element sourceNode, String afterNodeName, String name) {
		Element elem = doc.createElement(name);
		Element afterNode = getChildByName(sourceNode, afterNodeName);
		sourceNode.insertBefore(elem, afterNode);
		return elem;
	}

	public static Element createChildText(Document doc, Element elem, String name,
			String value) {
		Element child = doc.createElement(name);
		child.appendChild(doc.createTextNode(value == null ? "" : value));
		elem.appendChild(child);
		return child;
	}
	
	public static void deleteChildNodeByName(Element parentNode, String deleteNodeName){
		Element deleteNode = getChildByName(parentNode, deleteNodeName);
		parentNode.removeChild(deleteNode);
	}

	/**
	 * 
	 * @param doc
	 * @param elem
	 * @param name
	 * @param value
	 * @param comment
	 */
	public static void createChildTextWithComment(Document doc, Element elem,
			String name, String value, String comment) {
		Element child = doc.createElement(name);
		child.appendChild(doc.createTextNode(value == null ? "" : value));
		Comment c = doc.createComment(comment);
		elem.appendChild(c);
		elem.appendChild(child);

	}

	/**
	 * 缁欐寚瀹氳妭鐐规坊鍔犳敞閲?
	 * @param doc
	 * @param elem
	 * @param comment
	 */
	public static void createComment(Document doc, Element elem,String comment) {
		Comment c = doc.createComment(comment);
		elem.appendChild(c);
	}

	public static void createOptionalChildText(Document doc, Element elem,
			String name, String value) {
		if (value == null || value.length() == 0) {
			return;
		}
		Element child = doc.createElement(name);
		child.appendChild(doc.createTextNode(value));
		elem.appendChild(child);
	}

	static final FileExtFilter xmlFileExtFilter = new FileExtFilter(".xml");

	/**
	 * @return the xmlFileExtFilter
	 */
	public static final FileExtFilter getXmlFileExtFilter() {
		return xmlFileExtFilter;
	}

	/**
	 * 
	 * @param doc
	 * @return
	 */
	public static String document2String(Document doc) {
		try {
			DOMSource domSource = new DOMSource(doc);
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			// transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
			transformer.transform(domSource, result);
			return writer.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param e
	 * @return
	 */
	public static String elememt2String(Element e) {
		return null;
	}
}
