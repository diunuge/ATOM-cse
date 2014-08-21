package com.project.traceability.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.project.traceability.model.ArtefactElement;
import com.project.traceability.model.ArtefactSubElement;
import com.project.traceability.model.AttributeModel;
import com.project.traceability.utils.Constants.ArtefactSubElementType;

public class AttributeManager {

	static List<String> relationNodes = new ArrayList<String>();

	@SuppressWarnings("unchecked")
	public static List<String> mapAttributes() {
		SourceCodeArtefactManager.readXML();
		UMLArtefactManager.readXML();
		Map<ArtefactElement, List<? extends ArtefactSubElement>> sourceCodeattributeArtefactMap = SourceCodeArtefactManager
				.manageArtefactSubElements(ArtefactSubElementType.ATTRIBUTE);
		Map<ArtefactElement, List<ArtefactSubElement>> UMLattributeArtefactMap = UMLArtefactManager
				.manageArtefactSubElements(ArtefactSubElementType.ATTRIBUTE);

		
		Iterator<Entry<ArtefactElement, List<ArtefactSubElement>>> UMLIterator = UMLattributeArtefactMap
				.entrySet().iterator();
		//boolean isCompare = false;
		while (UMLIterator.hasNext()) {
			Map.Entry UMLPairs = UMLIterator.next();
			ArtefactElement UMLArtefactElement = (ArtefactElement) UMLPairs
					.getKey();
			List<AttributeModel> UMLAttributeElements = (List<AttributeModel>) UMLPairs
					.getValue();
			Iterator<Entry<ArtefactElement, List<? extends ArtefactSubElement>>> sourceCodeIterator = sourceCodeattributeArtefactMap
					.entrySet().iterator();
			while (sourceCodeIterator.hasNext()) {
				Map.Entry sourcePairs = sourceCodeIterator.next();
				ArtefactElement sourceArtefactElement = (ArtefactElement) sourcePairs
						.getKey();
				List<AttributeModel> sourceAttributeElements = (List<AttributeModel>) sourcePairs
						.getValue();
				if (sourceArtefactElement.getName().equalsIgnoreCase(
						UMLArtefactElement.getName())) {
					for(int i = 0; i < UMLAttributeElements.size(); i++){
						for(int j = 0; j < sourceAttributeElements.size(); j++){
							if(UMLAttributeElements.get(i).getName().equalsIgnoreCase
									(sourceAttributeElements.get(j).getName())){
								relationNodes.add(UMLAttributeElements.get(i).getSubElementId());
								relationNodes.add(sourceAttributeElements.get(j).getSubElementId());
								break;
							}
						}
					}
				}
			}
			UMLIterator.remove();
		}
		//RelationManager.createXML(relationNodes);
		return relationNodes;
	}
}
