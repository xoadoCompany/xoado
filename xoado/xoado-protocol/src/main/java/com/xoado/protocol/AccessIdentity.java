package com.xoado.protocol;


import java.util.List;



public class AccessIdentity {

	private String XOADOTOKENID;
	
	private String userId;
	
	private String userName;
	
	private long date;
	
	private String userType;
	
	private String openId;
	
	

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getXOADOTOKENID() {
		return XOADOTOKENID;
	}

	public void setXOADOTOKENID(String XOADOTOKENID) {
		this.XOADOTOKENID = XOADOTOKENID;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
	
	public static AccessIdentity add(String token){
//		System.out.println("请求过来的参数"+token);
		AccessIdentity ad = new AccessIdentity();
		/*
		ad.setDate(new Date().getTime());
		ad.setXOADOTOKENID("123456798");
		ad.setUserId("987654321");
		ad.setUserName("zhangsan");
		ad.setUserType("正常");
		*/
		
		
		List<AccessIdentity> list = JsonUtils.jsonToList(token, AccessIdentity.class);
		for (AccessIdentity accessIdentity : list) {
			ad.setDate(accessIdentity.getDate());
			ad.setXOADOTOKENID(accessIdentity.getXOADOTOKENID());
			ad.setUserId(accessIdentity.getUserId());
			ad.setUserName(accessIdentity.getUserName());
			ad.setUserType(accessIdentity.getUserType());
		}
//		AccessIdentity pojo = JsonUtils.jsonToPojo(token, AccessIdentity.class);
//		System.out.println(pojo);
//		long a = pojo.getDate();
//		System.out.println("data:"+a);

	
		
		return ad;
		
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AccessIdentity [XOADOTOKENID=" + XOADOTOKENID + ", userId=" + userId + ", userName=" + userName
				+ ", date=" + date + ", userType=" + userType + ", openId=" + openId + "]";
	}
	

	

	
	
}
