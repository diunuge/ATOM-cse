package com.project.traceability.model;

import java.util.List;

public class ArtefactElement {
	
	private String artefactElementId = null;
	private String name = null;
	private String type = null;
	private List<ArtefactSubElement> artefactSubElements = null;
	
	public ArtefactElement() {
		super();
	}

	public ArtefactElement(String artefactElementId, String name, String type,
			List<ArtefactSubElement> artefactSubElements) {
		super();
		this.artefactElementId = artefactElementId;
		this.name = name;
		this.type = type;
		this.setArtefactSubElements(artefactSubElements);
	}


	public String getArtefactElementId() {
		return artefactElementId;
	}

	public void setArtefactElementId(String artefactElementId) {
		this.artefactElementId = artefactElementId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<ArtefactSubElement> getArtefactSubElements() {
		return artefactSubElements;
	}

	public void setArtefactSubElements(List<ArtefactSubElement> artefactSubElements) {
		this.artefactSubElements = artefactSubElements;
	}
	
	

}
