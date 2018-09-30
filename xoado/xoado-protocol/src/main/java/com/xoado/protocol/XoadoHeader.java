package com.xoado.protocol;

import javax.servlet.http.HttpServletRequest;


public class XoadoHeader {

	/**
	 * 获取tokenHeader
	 * @param request
	 * @return
	 */
	 public static String getToken(HttpServletRequest request) {

	      String XOADOTOKENID = GetTokenId.getXOADOTOKENID(request);
    
	      return XOADOTOKENID;
	   }
	 /**
	  * 获取PermissionHeader
	  * @param request
	  * @return
	  */
	 public static String getPermissionHeader(HttpServletRequest request) {

	      String XOADOPERMISSION = GetTokenId.getPermissionAccessHeader(request);
   
	      return XOADOPERMISSION;
	   }


}
