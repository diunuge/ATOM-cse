package com.project.traceability.model;

import java.util.List;

public class MethodModel extends ArtefactSubElement{
	
	private String returnType;
	private List<ParameterModel> parameters;
	public MethodModel() {
		super();
	}
	
	public MethodModel(String subElementId, String name, String visibility,
			String returnType, String type, String returnType2,
			List<ParameterModel> parameters) {
		super(subElementId, name, visibility, returnType, type);
		returnType = returnType2;
		this.parameters = parameters;
	}
	
	public String getReturnType() {
		return returnType;
	}
	
	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
	
	public List<ParameterModel> getParameters() {
		return parameters;
	}
	public void setParameters(List<ParameterModel> parameters) {
		this.parameters = parameters;
	}
	
	

}
