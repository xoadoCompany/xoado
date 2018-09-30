package com.xoado.protocol;
/**
*App状态枚举定义
*/
public enum ApplicationStatus {
	
	
	WHITELIST("1","白名单"),
	BLACKLIST("2","黑名单"),
	NOTEXIST("3","认证中心无此应用");
	
	private String stauts;
	
	private String describe;

	public String getStauts() {
		return stauts;
	}

	public void setStauts(String stauts) {
		this.stauts = stauts;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}
	
	ApplicationStatus(String stauts,String describe){
		this.stauts = stauts;
		this.describe = describe;
	}

}
