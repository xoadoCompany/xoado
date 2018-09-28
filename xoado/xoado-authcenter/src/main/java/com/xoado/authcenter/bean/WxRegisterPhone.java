package com.xoado.authcenter.bean;

public class WxRegisterPhone {
	private String openId;
	private String phoneNumber;
	private String Verification_code;
	/**
	 * @return the openId
	 */
	public String getopenId() {
		return openId;
	}
	/**
	 * @param openId the openId to set
	 */
	public void setopenId(String openId) {
		this.openId = openId;
	}
	/**
	 * @return the phoneNumber
	 */
	public String getphoneNumber() {
		return phoneNumber;
	}
	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setphoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	/**
	 * @return the verification_code
	 */
	public String getVerification_code() {
		return Verification_code;
	}
	/**
	 * @param verification_code the verification_code to set
	 */
	public void setVerification_code(String verification_code) {
		Verification_code = verification_code;
	}
	
	
}
