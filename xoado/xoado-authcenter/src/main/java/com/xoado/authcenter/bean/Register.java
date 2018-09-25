package com.xoado.authcenter.bean;

public class Register {
	private String phoneNumber;
	private String userPassword;
	private String Verification_code;
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
	 * @return the userPassword
	 */
	public String getuserPassword() {
		return userPassword;
	}
	/**
	 * @param userPassword the userPassword to set
	 */
	public void setuserPassword(String userPassword) {
		this.userPassword = userPassword;
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
