package com.xoado.authcenter.bean;

public class RefreshCode {
	private String appId;
	private String code;

	/**
	 * @return the appId
	 */
	public String getappId() {
		return appId;
	}

	/**
	 * @param appId the appId to set
	 */
	public void setappId(String appId) {
		this.appId = appId;
	}

	public String getcode() {
		return code;
	}

	public void setcode(String code) {
		this.code = code;
	}
	
}
