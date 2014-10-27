package com.project.traceability.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import opennlp.tools.util.InvalidFormatException;

import com.project.traceability.model.ArtefactElement;
import com.project.traceability.model.ArtefactSubElement;

public class RequirementUMLClassManager {

	List<String> sourceCodeClasses = new ArrayList<String>();
	List<String> UMLClasses = new ArrayList<String>();
	static List<String> relationNodes = new ArrayList<String>();

	/**
	 * check whether the designed classes are implemented in sourcecode
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List<String> compareClassNames() {
		RequirementsManger.readXML();
		UMLArtefactManager.readXML();
		Map<String, ArtefactElement> UMLMap = UMLArtefactManager.UMLAretefactElements;
		Iterator<Entry<String, ArtefactElement>> UMLIterator = UMLMap
				.entrySet().iterator();
		try {
			Map<String, String> requirementsClass = ExtractClass.extractClass(RequirementsManger.requirementAretefactElements);
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int count = 0;
		/*while (UMLIterator.hasNext()) {
			Map.Entry pairs = UMLIterator.next();
			ArtefactElement UMLArtefactElement = (ArtefactElement) pairs
					.getValue();
			String name = UMLArtefactElement.getName();
			if (UMLArtefactElement.getType().equalsIgnoreCase("Class")) {
				int i = 0;
				while(requirementsClass.get(i).equalsIgnoreCase(name)){
					relationNodes.add(UMLArtefactElement
							.getArtefactElementId());
					relationNodes.add(ArtefactElement.getArtefactElementId());
					artefactMap.remove(sourceArtefactElement.getArtefactElementId());
					UMLMap.remove(UMLArtefactElement.getArtefactElementId());
					UMLIterator = UMLMap.entrySet().iterator();
					break;
				}
			}
		}*/
		/*if (artefactMap.size() > 0 || UMLMap.size() > 0) {
			UMLIterator = UMLMap.entrySet().iterator();
			sourceIterator = artefactMap.entrySet().iterator();
			System.out
					.println("UMLArtefactFile has following different classes from SourceCodeArtefactFile:");
			while (UMLIterator.hasNext()) {
				Map.Entry<String, ArtefactElement> artefact = UMLIterator
						.next();
				System.out.println(artefact.getValue().getName());
			}
			System.out
					.println("SourceCodeArtefactFile has following different classes from UMLArtefactFile:");
			while (sourceIterator.hasNext()) {
				Map.Entry<String, ArtefactElement> artefact = sourceIterator
						.next();
				System.out.println(artefact.getValue().getName());
			}
		}*/

		return relationNodes;
	}

	@SuppressWarnings("rawtypes")
	public static int compareClassCount() {
		SourceCodeArtefactManager.readXML();
		UMLArtefactManager.readXML();
		Iterator it = SourceCodeArtefactManager.sourceCodeAretefactElements
				.entrySet().iterator();
		int countSourceClass = 0;
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			ArtefactElement artefactElement = (ArtefactElement) pairs
					.getValue();
			if (artefactElement.getType().equalsIgnoreCase("Class")) {

				countSourceClass++;
			}
			List<ArtefactSubElement> artefactSubElements = artefactElement
					.getArtefactSubElements();
			it.remove(); // avoids a ConcurrentModificationException
		}
		// UMLArtefactManager.readXML();
		Iterator it1 = UMLArtefactManager.UMLAretefactElements.entrySet()
				.iterator();
		int countUMLClass = 0;
		while (it1.hasNext()) {
			Map.Entry pairs = (Entry) it1.next();
			ArtefactElement artefactElement = (ArtefactElement) pairs
					.getValue();
			if (artefactElement.getType().equalsIgnoreCase("Class")) {
				countUMLClass++;
			}
			List<ArtefactSubElement> artefactSubElements = artefactElement
					.getArtefactSubElements();
			it1.remove(); // avoids a ConcurrentModificationException
		}

		if (countSourceClass == countUMLClass) {
			System.out.println("class compared");
		}
		return countSourceClass;
	}

}
