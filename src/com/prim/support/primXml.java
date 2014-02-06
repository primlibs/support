/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prim.support;

import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


/**
 *
 * @author кот
 */
public class primXml {

  private primXml() {
  }

  /**
   * @param doc документ
   * @param root корневой элемент, в который будет добавлен новый
   * @param name имя нового элемента
   * @param value значение нового элемента
   * @return
   */
  public static Element createElement(Document doc, Element root, String name, Object value) {
    Element nm = doc.createElement(name);
    nm.appendChild(doc.createTextNode(MyString.getString(value)));
    root.appendChild(nm);
    return nm;
  }

  /**
   * @param doc документ
   * @param root корневой элемент, в который будет добавлен новый
   * @param name имя нового элементаа
   * @return
   */
  public static Element createEmptyElement(Document doc, Element root, String name) {
    Element nm = doc.createElement(name);
    root.appendChild(nm);
    return nm;
  }
  
  public static Document getDocumentByFile(File file) throws Exception {
     DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = dbf.newDocumentBuilder();
    InputSource is = new InputSource(); 
    FileExecutor exec = new FileExecutor(file);
    is.setCharacterStream(new StringReader(exec.readString("UTF-8")));

    return builder.parse(is);
  }

  /**
   * в родительском элементе parentElement находит первый дочерний элемент с
   * именем childElementName, и возвращает его значение. <br/> Если не найдено
   * дочернего элемента с именем childElementName, то возвращает null <br/> Если
   * у элемента нет значения, то возвращает пустую строку
   *
   * @param parentElement родительский элемент
   * @param childElementName название дочернего элемента
   * @return
   */
  public static String getValue(Element parentElement, String childElementName) {

    if (parentElement == null || childElementName == null) {
      return null;
    }

    // если нет элемента return ""
    if (parentElement.getElementsByTagName(childElementName) == null
            || parentElement.getElementsByTagName(childElementName).item(0) == null) {
      return "";
    }

    // если у элемента пустое значение return ""
    if (parentElement.getElementsByTagName(childElementName).item(0).getChildNodes() == null
            || parentElement.getElementsByTagName(childElementName).item(0).getChildNodes().item(0) == null
            || parentElement.getElementsByTagName(childElementName).item(0).getChildNodes().item(0).getNodeValue() == null) {
      return "";
    }

    return parentElement.getElementsByTagName(childElementName).item(0).getChildNodes().item(0).getNodeValue();
  }
  
  /**
   * возвращает текстовое значение элемента
   * @param elem
   * @return 
   */
  public static String getValue(Element elem) {

    if (elem == null) {
      return null;
    }

    // если у элемента пустое значение return ""
    if (elem.getChildNodes() == null
            || elem.getChildNodes().item(0) == null
            || elem.getChildNodes().item(0).getNodeValue() == null) {
      return "";
    }

    return elem.getChildNodes().item(0).getNodeValue();
  }

  /**
   * возвращает корневой элемент из xml
   *
   * @param xml строка в формате xml
   * @return
   */
  public static Element getParentElement(String xml) throws Exception {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = dbf.newDocumentBuilder();
    InputSource is = new InputSource();
    is.setCharacterStream(new StringReader(xml));

    Document doc = builder.parse(is);

    NodeList list = doc.getChildNodes();

    return (Element) list.item(0);
  }

  /**
   * получить новый document
   *
   * @return
   */
  public static Document getNewDocument() throws Exception {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = dbf.newDocumentBuilder();
    Document doc = db.newDocument();
    return doc;
  }

  /**
   * добавить корневой элемент к документу. Имя добавленного элемента - "root".
   * Возвращает добавленный элемент.
   *
   * @param doc
   * @return
   */
  public static Element addRootElement(Document doc) {
    Element root = doc.createElement("root");
    doc.appendChild(root);
    return root;
  }

  public static String documentToString(Document doc) throws Exception {
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();
    DOMSource source = new DOMSource(doc);
    CharArrayWriter ch = new CharArrayWriter();
    StreamResult result = new StreamResult(ch);
    transformer.transform(source, result);
    return ch.toString();
  }
}