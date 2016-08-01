package com.framework.qa.utils.util;

import com.framework.qa.utils.exception.FrameworkException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * FrameworkProperties.java -  Framework related all data processing and collecting class
 *
 * @author Shiwantha Lakmal
 * @version 1.0-SNAPSHOT Last modified 04_23_2016
 * @since 04/23/2016.
 */
public class FrameworkProperties {
    private static final Logger LOGGER = LoggerFactory.getLogger(FrameworkProperties.class);

    private File projectXmlFile;//environment.xml file
    private LinkedHashMap<String, String> lnkMapLoadProperties;//load all xml values to hash map
    private LinkedHashMap<String, String> lnkMapLoadElements;//load all json values to hash map

    private LinkedHashMap<String, LinkedHashMap<String, String>> lnkMapOuter;
    private LinkedHashMap<String, String> lnkMapInner;
    private File elementXmlFile;//elements.xml file
    private DocumentBuilderFactory dbFactory;
    private DocumentBuilder dBuilder;
    private Document document;
    private String strDefault;
    private NodeList nodeLstNodes;
    private NodeList nodeLstElements;
    private Node nodeProp;
    private int intCount;
    private String strVariableName;
    private String strTagValue;
    private Node environmentType;


    static File objectRepositoryFile;
    private URL inputStream;//file path extractor



    /**
     * Process project environment.json file and store all attribute in collection
     * @param projectSettingsFile File location as a string
     * @return HashMap
     * @throws FrameworkException
     */
    public HashMap readProjEnvConfig(String projectSettingsFile) throws FrameworkException {
        projectXmlFile = new File(projectSettingsFile);
        lnkMapLoadProperties = new LinkedHashMap<String, String>();
        dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setIgnoringComments(true);
        dbFactory.setIgnoringElementContentWhitespace(true);
        dBuilder = null;

        try {
            dBuilder = dbFactory.newDocumentBuilder();
            document = dBuilder.parse(projectXmlFile);
            document.getDocumentElement().normalize();
            environmentType = document.getDocumentElement().getElementsByTagName("environmentType").item(0);
            nodeLstElements = document.getDocumentElement().getElementsByTagName("environment");
            nodeLstNodes = null;
            nodeProp = null;

            for (int x = 0; x < nodeLstElements.getLength(); ++x) {
                nodeProp = document.getDocumentElement().getElementsByTagName("environment").item(x);
                if (nodeProp.getAttributes().getNamedItem("name").getTextContent().equals(environmentType.getTextContent())) {
                    lnkMapLoadProperties.put("environmentType", environmentType.getTextContent());
                    nodeLstNodes = nodeProp.getChildNodes();
                    for (int i = 0; i < nodeLstNodes.getLength(); i++) {
                        if (nodeLstNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                            if (nodeLstNodes.item(i).getChildNodes().getLength() > 1) {
                                System.out.println();
                                NodeList subNodes = nodeLstNodes.item(i).getChildNodes();
                                for (int j = 0; j < subNodes.getLength(); j++) {
                                    if (subNodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
                                        lnkMapLoadProperties.put(subNodes.item(j).getNodeName(), subNodes.item(j).getTextContent());
                                        System.out.println(subNodes.item(j).getNodeName() + " = " + subNodes.item(j).getTextContent());
                                    }
                                }
                            } else {
                                lnkMapLoadProperties.put(nodeLstNodes.item(i).getNodeName(), nodeLstNodes.item(i).getTextContent());
                                System.out.println(nodeLstNodes.item(i).getNodeName() + " = " + nodeLstNodes.item(i).getTextContent());
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            throw new FrameworkException("exception at:" + getClass().getSimpleName() + "-" +
                    Thread.currentThread().getStackTrace()[1].getLineNumber() + " Message:" + e.getMessage());
        }
        return lnkMapLoadProperties;
    }

    /**
     * Process project application.json file and store all attribute in collection
     * @param projectSettingsFile File location as a string
     * @return HashMap
     * @throws FrameworkException
     */
    public HashMap readProjectAppConfig(String projectSettingsFile) throws FrameworkException {
        projectXmlFile = new File(projectSettingsFile);
        lnkMapLoadProperties = new LinkedHashMap<String, String>();
        dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setIgnoringComments(true);
        dbFactory.setIgnoringElementContentWhitespace(true);
        dBuilder = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            document = dBuilder.parse(projectXmlFile);

            document.getDocumentElement().normalize();
            nodeLstElements = document.getDocumentElement().getElementsByTagName("environment");
            nodeLstNodes = null;
            nodeProp = null;
            for (int x = 0; x < nodeLstElements.getLength(); ++x) {
                nodeProp = document.getDocumentElement().getElementsByTagName("environment").item(x);
                if (nodeProp.getAttributes().getNamedItem("name").getTextContent().equals(environmentType)) {
                    nodeLstNodes = nodeProp.getChildNodes();
                    for (int i = 0; i < nodeLstNodes.getLength(); i++) {
                        if (nodeLstNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                            if (nodeLstNodes.item(i).getChildNodes().getLength() > 1) {
                                System.out.println();
                                NodeList subNodes = nodeLstNodes.item(i).getChildNodes();
                                for (int j = 0; j < subNodes.getLength(); j++) {
                                    if (subNodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
                                        lnkMapLoadProperties.put(subNodes.item(j).getNodeName(), subNodes.item(j).getTextContent());
                                        System.out.println(subNodes.item(j).getNodeName() + " = " + subNodes.item(j).getTextContent());
                                    }
                                }
                            } else {
                                lnkMapLoadProperties.put(nodeLstNodes.item(i).getNodeName(), nodeLstNodes.item(i).getTextContent());
                                System.out.println(nodeLstNodes.item(i).getNodeName() + " = " + nodeLstNodes.item(i).getTextContent());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new FrameworkException("exception at:" + getClass().getSimpleName() + "-" +
                    Thread.currentThread().getStackTrace()[1].getLineNumber() + " Message:" + e.getMessage());
        }
        return lnkMapLoadProperties;
    }

    /**
     * Return details of specified element for a given class
     *
     * @param strElementFile file name path
     * @param strClassName   class name
     * @param strTagName     element name
     * @return hashMap details of specific element
     */
    public LinkedHashMap<String, LinkedHashMap<String, String>> readElementsForTagFromFile(String strElementFile, String strClassName, String strTagName) throws FrameworkException {
        inputStream = getClass().getClassLoader().getResource(strElementFile);
        elementXmlFile = new File(inputStream.getPath());
        strDefault = "";
        intCount = 0;
        strVariableName = "";
        lnkMapInner = new LinkedHashMap<String, String>();
        lnkMapOuter = new LinkedHashMap<String, LinkedHashMap<String, String>>();
        dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setIgnoringComments(true);
        dbFactory.setIgnoringElementContentWhitespace(true);
        dBuilder = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            document = dBuilder.parse(elementXmlFile);

            document.getDocumentElement().normalize();
            nodeLstElements = document.getDocumentElement().getElementsByTagName("page");
            nodeLstNodes = null;
            nodeProp = null;
            for (int k = 0; k < nodeLstElements.getLength(); ++k) {
                nodeProp = document.getDocumentElement().getElementsByTagName("page").item(k);
                if (nodeProp.getAttributes().getNamedItem("name").getTextContent().equals(strClassName)) {
                    nodeLstNodes = nodeProp.getChildNodes();
                    for (int i = 0; i < nodeLstNodes.getLength(); i++) {
                        if (nodeLstNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                            if (nodeLstNodes.item(i).getChildNodes().getLength() > 1) {
                                System.out.println();
                                NodeList subNodes = nodeLstNodes.item(i).getChildNodes();
                                for (int j = 0; j < subNodes.getLength(); j++) {
                                    if (subNodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
                                        strVariableName = subNodes.item(j).getAttributes().getNamedItem("name").getNodeValue();
                                        intCount = subNodes.item(j).getAttributes().getLength();
                                        if (strTagName != null && strTagName.equals(strVariableName)) {
                                            lnkMapInner.put("findBy", subNodes.item(j).getAttributes().getNamedItem("findBy").getNodeValue());
                                            LOGGER.debug(strVariableName + "-findBy" + " = " + subNodes.item(j).getAttributes().getNamedItem("findBy").getNodeValue());
                                            lnkMapInner.put("value", subNodes.item(j).getTextContent());
                                            LOGGER.debug(strVariableName + "-value" + " = " + subNodes.item(j).getTextContent());

                                            if (intCount == 3) {
                                                strDefault = subNodes.item(j).getAttributes().getNamedItem("default").getNodeValue();
                                                lnkMapInner.put("default", strDefault);
                                                strDefault = null;

                                            }
                                            lnkMapOuter.put(strVariableName, lnkMapInner);
                                            break;
                                        }
                                    }
                                }
                            } else {
                                strVariableName = nodeLstNodes.item(i).getAttributes().getNamedItem("name").getNodeValue();
                                intCount = nodeLstNodes.item(i).getAttributes().getLength();
                                if (strTagName != null && strTagName.equals(strVariableName)) {
                                    lnkMapInner.put("findBy", nodeLstNodes.item(i).getAttributes().getNamedItem("findBy").getNodeValue());
                                    LOGGER.debug(strVariableName + "-findBy" + " = " + nodeLstNodes.item(i).getAttributes().getNamedItem("findBy").getNodeValue());
                                    lnkMapInner.put("value", nodeLstNodes.item(i).getTextContent());
                                    LOGGER.debug(strVariableName + "-value" + " = " + nodeLstNodes.item(i).getTextContent());

                                    if (intCount == 3) {
                                        strDefault = nodeLstNodes.item(i).getAttributes().getNamedItem("default").getNodeValue();
                                        lnkMapInner.put("default", strDefault);
                                        strDefault = null;

                                    }
                                    lnkMapOuter.put(strVariableName, lnkMapInner);
                                    break;
                                }
                            }
                        }
                    }
                    break;
                }

            }

        } catch (Exception e) {
            throw new FrameworkException("exception at:" + getClass().getSimpleName() + "-" +
                    Thread.currentThread().getStackTrace()[1].getLineNumber() + " Message:" + e.getMessage());
        }
        if (lnkMapOuter.size() == 0) {
            throw new FrameworkException("exception at:" + getClass().getSimpleName() + "-" +
                    Thread.currentThread().getStackTrace()[1].getLineNumber() + " Message: element " + strTagName + " not found");
        } else {
            return lnkMapOuter;
        }

    }
}
