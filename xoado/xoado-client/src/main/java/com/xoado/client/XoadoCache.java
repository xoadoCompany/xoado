package com.xoado.client;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xoado.client.http.ApplicationRequest;
import com.xoado.common.MD5;
import com.xoado.common.XoadoResult;
import com.xoado.protocol.AccessIdApplication;
import com.xoado.protocol.AccessIdentity;
import com.xoado.protocol.BaseRetCode;
import com.xoado.protocol.OrganizationStauts;
import com.xoado.protocol.XoadoConstant;

/**
 * 本地缓存控制器
 */
public class XoadoCache {

	private static Map<String, Object> map = new HashMap<>();

	public static Map<String, Object> getMap() {
		return map;
	}

	public static void setMap(Map<String, Object> map) {
		XoadoCache.map = map;
	}

	public static AccessIdentity getToken(HttpServletRequest request, HttpServletResponse response,String XOADOXOADOTOKENID,String Organizeid) {
		AccessIdentity tokenValue = null;
		tokenValue = TokenCache.getCacheByKey(XOADOXOADOTOKENID);
		// 判断本地是否存在token，如不存在，远程查找认证中心
		if (null == tokenValue.getXOADOTOKENID()||"".equals(tokenValue.getXOADOTOKENID())) {

			AccessIdentity gettoken = ApplicationRequest.gettoken(request, response, XOADOXOADOTOKENID,Organizeid);
			// System.out.println("cliengettoken:"+gettoken);
			if (gettoken == null) {

				return null;
			}
			Boolean bool = TokenCache.insertCache(XOADOXOADOTOKENID, gettoken, XOADOXOADOTOKENID);
			if(bool==false){
				System.out.println("The Message Insert Cache Failed");
			}
			return gettoken;

		}
		return tokenValue;

	}

	/**
	 * 优化getToken方法 解决if语句冗余
	 * 
	 * @param request
	 * @param response
	 * @param XOADOXOADOTOKENID
	 * @return
	 */
	public static Object getToken2(HttpServletRequest request, HttpServletResponse response, String XOADOTOKENID) {
		Map<String, Object> map2 = XoadoCache.getMap();

		AccessIdentity token = (AccessIdentity) map2.get(XOADOTOKENID);
		String Organizeid=null;
		if (token == null) {
			token = ApplicationRequest.gettoken(request, response, XOADOTOKENID,Organizeid);
			if (token == null) {
				return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_ERROR_AUTH_ACCOUNT_LOCK.getRetCode()),
						BaseRetCode.CODE_ERROR_AUTH_ACCOUNT_LOCK.getRetMsg());
			}
		}
		return token;
	}

	/**
	 * 开往认证中心获取code
	 * 
	 * @param request
	 * @param response
	 * @param appid
	 * @return
	 */
	public static AccessIdApplication getAppCodeByCache(HttpServletRequest request, HttpServletResponse response, String appCode){
		// 先在本地缓存中查找
		AccessIdApplication codeValue = null;
		codeValue = AppLocalCache.getCacheByKey(appCode);
		//当缓存找不到则去认证中心寻找
		if (codeValue.getcode() == null) {

			// 发送自己的appid，code以及第三方的appid到认证中心
			codeValue = ApplicationRequest.getAppCodeByAuth(request, response, appCode);
			
			// 将认证中心查询到的内容加入本地缓存
			if (null == codeValue.getcode()||"".equals(codeValue.getcode())) {// code不在认证中心1.set进去value,2.抛出异常
				 codeValue.setcode(MD5.MD5Encode(UUID.randomUUID().toString()));
				 codeValue.setvalue("认证中心无此应用");
				 codeValue.setappId("null");
				//throw new NullPointerException("Application does not exist Certification Center.");
			}
			Boolean b = AppLocalCache.insertCache(codeValue.getcode(), codeValue.getvalue(), codeValue.getappId());
			String jsonobj = AppLocalCache.getAllCache();
			//System.out.println(jsonobj);
			if(b==false){
				System.out.println("插入失败");
			}

		}
		return codeValue;

	}
	
/*	public static AccessIdPermission getPermission(HttpServletRequest request, HttpServletResponse response,String XOADOXOADOTOKENID) {
		AccessIdPermission permissionvalue = null;
		permissionvalue = null;               //TokenCache.getCacheByKey(XOADOXOADOTOKENID);
		// 判断本地是否存在token，如不存在，远程查找认证中心
		if (null == permissionvalue.getPermissionsId()||"".equals(permissionvalue.getPermissionsId())) {

			AccessIdPermission getpermission = null;           //ApplicationRequest.gettoken(request, response, XOADOXOADOTOKENID);
			if (getpermission == null) {
				return null;
			}
			Boolean bool = true;                       //TokenCache.insertCache(XOADOXOADOTOKENID, getpermission, XOADOXOADOTOKENID);
			if(bool==false){
				System.out.println("The Message Insert Cache Failed");
			}
			return getpermission;
		}
		return permissionvalue;

	}*/

}
