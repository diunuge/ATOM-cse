package com.project.traceability.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.project.traceability.model.ArtefactElement;
import com.project.traceability.model.ArtefactSubElement;

public class ClassCompareManager {

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
		SourceCodeArtefactManager.readXML();
		UMLArtefactManager.readXML();
		Map<String, ArtefactElement> UMLMap = UMLArtefactManager.UMLAretefactElements;
		Iterator<Entry<String, ArtefactElement>> UMLIterator = UMLMap
				.entrySet().iterator();
		Map<String, ArtefactElement> artefactMap = SourceCodeArtefactManager.sourceCodeAretefactElements;
		Iterator<Entry<String, ArtefactElement>> sourceIterator = null;
		int count = 0;
		while (UMLIterator.hasNext()) {
			Map.Entry pairs = UMLIterator.next();
			ArtefactElement UMLArtefactElement = (ArtefactElement) pairs
					.getValue();
			String name = UMLArtefactElement.getName();
			if (UMLArtefactElement.getType().equalsIgnoreCase("Class")) {
				sourceIterator = artefactMap.entrySet().iterator();
				while (sourceIterator.hasNext()) {
					Map.Entry pairs1 = sourceIterator.next();
					ArtefactElement sourceArtefactElement = (ArtefactElement) pairs1
							.getValue();
					if (sourceArtefactElement.getType().equalsIgnoreCase(
							"Class")
							&& sourceArtefactElement.getName()
									.equalsIgnoreCase(name)) {
						count++;
						relationNodes.add(UMLArtefactElement
								.getArtefactElementId());
						relationNodes.add(sourceArtefactElement
								.getArtefactElementId());
						artefactMap.remove(sourceArtefactElement
								.getArtefactElementId());
						UMLMap.remove(UMLArtefactElement.getArtefactElementId());
						UMLIterator = UMLMap.entrySet().iterator();
						break;
					}
				}
			}
		}
		if (artefactMap.size() > 0 || UMLMap.size() > 0) {
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
		}

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
