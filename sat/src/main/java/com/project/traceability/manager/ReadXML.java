package com.project.traceability.manager;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.project.traceability.db.GraphDB;
import com.project.traceability.model.ArtefactElement;


public class ReadXML {

	public static void main(String argv[]) {
	
		try {
			
			SourceCodeArtefactManager.readXML();
			UMLArtefactManager.readXML();		
			
		    Map<String, ArtefactElement> UMLAretefactElements = UMLArtefactManager.UMLAretefactElements;
		    Map<String, ArtefactElement> sourceCodeAretefactElements = SourceCodeArtefactManager.sourceCodeAretefactElements;
		   		    
		    GraphDB graphDB = new GraphDB();
	        graphDB.initiateGraphDB();
	        graphDB.addNodeToGraphDB(sourceCodeAretefactElements);
		    graphDB.addNodeToGraphDB(UMLAretefactElements);
		  
	        List<String> relationNodes = ClassCompareManager.compareClassNames();
			graphDB.addRelationTOGraphDB(relationNodes);
			graphDB.setVisible(true);
			
			
			relationNodes.addAll(AttributeManager.mapAttributes());
			relationNodes.addAll(MethodManager.mapAttributes());
			RelationManager.createXML(relationNodes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}	

}
