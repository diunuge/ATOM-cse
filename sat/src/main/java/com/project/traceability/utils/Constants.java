package com.project.traceability.utils;

public class Constants {
	
	public Constants(){
		
	}
	
	public enum ArtefactType {
    	SOURCECODE("SourceCode"),
    	REQUIREMENT("Requirement"),
    	UMLDiagram("UMLDiagram");

    	private final String value;

        private ArtefactType(String val) {
        	this.value = val;
		}
        
        @Override
        public String toString() {
            return value;
        }
        
        public String getValue() {
        	return value;
        }

        public static ArtefactType parseEnum(final String val) {
 
        	ArtefactType artefactType = null;
             for(ArtefactType type : ArtefactType.values()) {
            	 if (type.getValue().equals(val)) {
            		 artefactType = type;
            	 }
             }
            return artefactType;
        }
    }
	
	public enum ArtefactSubElementType {
    	ATTRIBUTE("Attribute"),
    	METHOD("Method");

    	private final String value;

        private ArtefactSubElementType(String val) {
        	this.value = val;
		}
        
        @Override
        public String toString() {
            return value;
        }
        
        public String getValue() {
        	return value;
        }

        public static ArtefactSubElementType parseEnum(final String val) {
 
        	ArtefactSubElementType artefactSubElementType = null;
             for(ArtefactSubElementType type : ArtefactSubElementType.values()) {
            	 if (type.getValue().equals(val)) {
            		 artefactSubElementType = type;
            	 }
             }
            return artefactSubElementType;
        }
    }

}
