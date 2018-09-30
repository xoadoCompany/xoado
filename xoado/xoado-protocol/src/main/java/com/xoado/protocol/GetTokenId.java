package com.xoado.protocol;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class GetTokenId {
	
	public static String getXOADOTOKENID(HttpServletRequest request){
	

		String XOADOTOKENID = request.getHeader("XOADOTOKENID");
		
		if(XOADOTOKENID==null){

			Cookie[] cookies = request.getCookies();
		
			Map<String,Object> map = new HashMap<>();
		
			if(cookies!=null){
			
				for (Cookie cookie : cookies) {
					
				
						map.put(cookie.getName(), cookie.getValue());
				
				}	
						XOADOTOKENID =(String) map.get("XOADOTOKENID");
						
						if(XOADOTOKENID==null){
							
							return null;
							
						}
						
			}
	
		
		}
			return XOADOTOKENID;
	}
	
	/**
	 * 从header中获取XOADOAPPACCESSCODE
	 * 
	 * @param request
	 * @return
	 */
	/*public static String getAppAccessHeader(HttpServletRequest request) {
		//从header中获取XOADOAPPACCESSCODE
		String XOADOAPPACCESSCODE = request.getHeader("XOADOAPPACCESSCODE");
		//为空则去cookie中找
		if (XOADOAPPACCESSCODE == null) {
			Cookie[] cookies = request.getCookies();
			Map<String, Object> map = new HashMap<>();
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					map.put(cookie.getName(), cookie.getValue());
				}
				XOADOAPPACCESSCODE = (String) map.get("XOADOAPPACCESSCODE");
				if (XOADOAPPACCESSCODE == null) {
					return null;
				}
			}
		
			
			  }
		return XOADOAPPACCESSCODE;
	}*/
	
	/**
	 * 从header中获取
	 * 
	 * @param request
	 * @return
	 */
	public static String getPermissionAccessHeader(HttpServletRequest request) {
		//从header中获取XOADOAPPACCESSCODE
		String XOADOPERMISSION = request.getHeader("XOADOPERMISSION");
		if (XOADOPERMISSION == null) {
			return null;
			}

		return XOADOPERMISSION;
	}
	
}
