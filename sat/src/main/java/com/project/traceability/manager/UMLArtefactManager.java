package com.project.traceability.manager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.project.traceability.model.ArtefactElement;
import com.project.traceability.model.ArtefactSubElement;
import com.project.traceability.model.AttributeModel;
import com.project.traceability.model.MethodModel;
import com.project.traceability.utils.Constants;
import com.project.traceability.utils.Constants.ArtefactSubElementType;
import com.project.traceability.utils.Constants.ArtefactType;

public class UMLArtefactManager {
	
	private ArtefactType artefactType = Constants.ArtefactType.UMLDiagram;
	
	public static Map<String, ArtefactElement> UMLAretefactElements = null;
	
	
	/**
	 * read UMLXml file and store data in a map
	 */
	public static void readXML(){
		//get the xml file
		File umlXmlFile = new File("E:\\Uni\\Semi-7\\RnD\\Product overview documents\\UMLArtefactFile.xml"); 	
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document UMLDoc = (Document) dBuilder.parse(umlXmlFile);
			UMLDoc.getDocumentElement().normalize();
						
			NodeList artefactList = UMLDoc.getElementsByTagName("Artefact");	//find the "Artefact" tag
			 
			for (int temp = 0; temp < artefactList.getLength(); temp++) {
				
				 
				Node artefactNode = (Node) artefactList.item(temp);  	//identify a node
		 
				if (artefactNode.getNodeType() == Node.ELEMENT_NODE) {
		 
					NodeList artefactElementList = UMLDoc.getElementsByTagName("ArtefactElement");		//get all "Artefact" elements
					UMLAretefactElements = new HashMap<String, ArtefactElement>();
					ArtefactElement artefactElement = null;
					List<ArtefactSubElement> artefactsSubElements = null;
					for (int temp1 = 0; temp1 < artefactElementList.getLength(); temp1++) {
						 
						artefactsSubElements = new ArrayList<ArtefactSubElement>();
						Node artefactElementNode = (Node) artefactElementList.item(temp1);	
						Element artefact = (Element) artefactElementNode;
						String id = artefact.getAttribute("id");		//get all the attributes of an artefactelement
						String name = artefact.getAttribute("name");
						String type = artefact.getAttribute("type");
						NodeList artefactSubElementList = artefact.getElementsByTagName("ArtefactSubElement"); //find artefactsubelements in 
																												//an artefactelements
						artefactsSubElements = readArtefactSubElement(artefactSubElementList);
						artefactElement = new ArtefactElement(id, name, type, artefactsSubElements);
						UMLAretefactElements.put(id, artefactElement);
					}
					
					NodeList intraConnectionsList = UMLDoc.getElementsByTagName("IntraConnections");
					readIntraConnectionsXML(intraConnectionsList);
				}
			}
		} catch (ParserConfigurationException | SAXException | IOException e) {
			
			e.printStackTrace();
		}
		
	}
	
	public static void readIntraConnectionsXML(NodeList intraConnectionsList) {
		for (int temp1 = 0; temp1 < intraConnectionsList.getLength(); temp1++) {
			 
			Node intraConnectionNode = (Node) intraConnectionsList.item(temp1);
			Element intraConnectionElement = (Element) intraConnectionNode;
			NodeList connectionsList = intraConnectionElement.getElementsByTagName("Connection");
			/*for (int temp2 = 0; temp2 < nList14.getLength(); temp2++) {
				//Node node = (Node) nList14.item(temp1);
				//Element eement2 = (Element) node;
				System.out.println("Type of Coneection : " + eement1.getElementsByTagName("Type").item(0).getTextContent());
				System.out.println("Start Point : " + eement1.getElementsByTagName("StartPoint").item(0).getTextContent());
				System.out.println("Start Point : " + eement1.getAttribute("Multiplicity"));
			}*/
		}
		
	}

	
	/**
	 * get all artefactelements
	 * @param artefactSubElementList
	 * @return
	 */
	public static List<ArtefactSubElement> readArtefactSubElement(NodeList artefactSubElementList){
		
		AttributeModel attributeElement = null;
		MethodModel methodAttribute = null;
		List<ArtefactSubElement> artefactSubElements = new ArrayList<ArtefactSubElement>();
		for (int temp1 = 0; temp1 < artefactSubElementList.getLength(); temp1++) {
			
			Node artefactSubElementNode = (Node) artefactSubElementList.item(temp1); 
			Element artefact = (Element) artefactSubElementNode;
			String id = artefact.getAttribute("id");
			String name = artefact.getAttribute("name");
			String type = artefact.getAttribute("type");
			String visibility = artefact.getAttribute("visibility");
			if(type.equalsIgnoreCase("UMLOperation")){
				String parameters = artefact.getAttribute("parameters");
				String returnType = artefact.getAttribute("returnType");
				methodAttribute = new MethodModel();
				methodAttribute.setSubElementId(id);
				methodAttribute.setName(name);
				methodAttribute.setType(type);
				methodAttribute.setVisibility(visibility);
				methodAttribute.setReturnType(returnType);
				if (!parameters.equals(""))
					methodAttribute.setParameters(ParameterManager
							.listParameters(parameters));					
				artefactSubElements.add(methodAttribute);
			}
			else if(type.equalsIgnoreCase("UMLAttribute")){
				attributeElement = new AttributeModel();
				String variableType = artefact.getAttribute("variableType");
				attributeElement.setSubElementId(id);
				attributeElement.setName(name);
				attributeElement.setType(type);
				attributeElement.setVisibility(visibility);
				attributeElement.setVariableType(variableType);
				artefactSubElements.add(attributeElement);
			}
			
		}
		
		return artefactSubElements;
	}

	/**
	 * @param attribute 
	 * 
	 */
	public static Map<ArtefactElement, List<ArtefactSubElement>> manageArtefactSubElements(ArtefactSubElementType attribute){		
		List<ArtefactSubElement> artefactSubElements = null;
		List<ArtefactSubElement> methodArtefactSubElements = null;
		List<ArtefactSubElement> attributeArtefactSubElements = null;
		Map<ArtefactElement, List<ArtefactSubElement>> attributeArtefactMap = null;
		Map<ArtefactElement, List<ArtefactSubElement>> methodArtefactMap = null;
		readXML();
		Iterator it = UMLAretefactElements.entrySet().iterator();
		attributeArtefactMap = new HashMap<ArtefactElement, List<ArtefactSubElement>>();
		methodArtefactMap = new HashMap<ArtefactElement, List<ArtefactSubElement>>();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			ArtefactElement artefactElement = (ArtefactElement) pairs
					.getValue();
			artefactSubElements = artefactElement.getArtefactSubElements();
			attributeArtefactSubElements = new ArrayList<ArtefactSubElement>();
			methodArtefactSubElements = new ArrayList<ArtefactSubElement>();
			for(int i = 0; i < artefactSubElements.size(); i++){
				if(artefactSubElements.get(i).getType().equalsIgnoreCase("UMLAttribute")){
					
					attributeArtefactSubElements.add(artefactSubElements.get(i));
				}
				else if(artefactSubElements.get(i).getType().equalsIgnoreCase("UMLOperation")){
					methodArtefactSubElements.add(artefactSubElements.get(i));
				}
			}
			it.remove(); // avoids a ConcurrentModificationException
			attributeArtefactMap.put(artefactElement, attributeArtefactSubElements);
			methodArtefactMap.put(artefactElement, methodArtefactSubElements);
		}
		if(attribute.equals(ArtefactSubElementType.ATTRIBUTE))
			return attributeArtefactMap;
		else 
			return methodArtefactMap;
	}
	
}

