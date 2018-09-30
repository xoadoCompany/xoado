package com.xoado.organize.bean;

public class ApiIntefaceAdd {
	private String appId;
	private String apiPath;
	private Long requestMethod;
	private String header;
	private String description;
	private String parameter;
	private String apistatus;
	private String auditstatus;
	
	public String getappId() {
		return appId;
	}
	public void setappId(String appId) {
		this.appId = appId;
	}
	public String getapiPath() {
		return apiPath;
	}
	public void setapiPath(String apiPath) {
		this.apiPath = apiPath;
	}
	public Long getrequestMethod() {
		return requestMethod;
	}
	public void setrequestMethod(Long requestMethod) {
		this.requestMethod = requestMethod;
	}
	public String getheader() {
		return header;
	}
	public void setheader(String header) {
		this.header = header;
	}
	public String getdescription() {
		return description;
	}
	public void setdescription(String description) {
		this.description = description;
	}
	public String getparameter() {
		return parameter;
	}
	public void setparameter(String parameter) {
		this.parameter = parameter;
	}
	public String getapistatus() {
		return apistatus;
	}
	public void setapistatus(String apistatus) {
		this.apistatus = apistatus;
	}
	public String getauditstatus() {
		return auditstatus;
	}
	public void setauditstatus(String auditstatus) {
		this.auditstatus = auditstatus;
	}
	
	
}
