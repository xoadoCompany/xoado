package com.xoado.client;

import java.util.HashMap;
import java.util.Map;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xoado.client.http.ApplicationRequest;
import com.xoado.common.XoadoResult;
import com.xoado.protocol.AccessIdentity;
import com.xoado.protocol.BaseRetCode;



/*
 * 本地缓存控制器
 */
public class XoadoCache {
	
	private static Map<String , Object> map = new HashMap<>();

	public static Map<String, Object> getMap() {
		return map;
	}

	public static void setMap(Map<String, Object> map) {
		XoadoCache.map = map;
	}
	
	public static AccessIdentity getToken(HttpServletRequest request,HttpServletResponse response,String XOADOXOADOTOKENID){
		
		
		Map<String, Object> map2 = XoadoCache.getMap();

		AccessIdentity token = (AccessIdentity) map2.get(XOADOXOADOTOKENID);
		
//		判断本地是否存在token，如不存在，远程查找认证中心
		if(token ==null){

			AccessIdentity gettoken = ApplicationRequest.gettoken(request,response,XOADOXOADOTOKENID);
//			System.out.println("cliengettoken:"+gettoken);
			if(gettoken==null){
				
				return null;
			}
			
			return gettoken;
	
		}
		return token;

		
		
	}
	
	/**
	 * 优化getToken方法  解决if语句冗余
	 * @param request
	 * @param response
	 * @param XOADOXOADOTOKENID
	 * @return
	 */
	public static Object getToken2(HttpServletRequest request,HttpServletResponse response,String XOADOTOKENID){
		Map<String, Object> map2 = XoadoCache.getMap();

		AccessIdentity token = (AccessIdentity) map2.get(XOADOTOKENID);
		
		if(token==null){
			 token = ApplicationRequest.gettoken(request, response, XOADOTOKENID);
			if(token==null){
				return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_ERROR_AUTH_ACCOUNT_LOCK.getRetCode()), BaseRetCode.CODE_ERROR_AUTH_ACCOUNT_LOCK.getRetMsg());
			}
		}
		return token;
	}
	
	
	public static String getcode(HttpServletRequest request,HttpServletResponse response,String body,String method,String parmer,String appid,String url){
		
//		在认证中心获取appid相对应的code
//		认证中心获取code地址
		String authcenterurl =(String) PropertiesGetFile.getMap().get("GETCODE");
//		发送自己的appid，code以及第三方的appid到认证中心
		ApplicationRequest.access(request,response, body, authcenterurl, appid, parmer, method);	
		
		
		
		
//		认证中心发送请求携带对应方的code
		
		return appid;
		
	}

}
