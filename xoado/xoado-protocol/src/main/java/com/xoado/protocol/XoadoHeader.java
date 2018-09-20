package com.xoado.protocol;

import javax.servlet.http.HttpServletRequest;


public class XoadoHeader {

	
	 public static String getToken(HttpServletRequest request) {

	      String XOADOTOKENID = GetTokenId.getXOADOTOKENID(request);
    
	      return XOADOTOKENID;
	   }


}
