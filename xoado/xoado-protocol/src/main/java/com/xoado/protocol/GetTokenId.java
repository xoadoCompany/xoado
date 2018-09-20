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
	
}
