package com.xoado.client.http;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.message.BasicNameValuePair;

import com.xoado.client.PropertiesGetFile;
import com.xoado.protocol.AccessIdApplication;
import com.xoado.protocol.AccessIdentity;

import com.xoado.protocol.XoadoConstant;

/**
 * 
 * @ClassName: ApplicationRequest
 * @Description:TODO 去认证中心的请求类
 */
public class ApplicationRequest {

	private static XoadoHttpRemote xhr;

	static Object host = "";

	public static AccessIdentity gettoken(HttpServletRequest request, HttpServletResponse response, String XOADOTOKENID,
			String Organizeid) {
		if (XOADOTOKENID != null) {
			// 去认证中心 post http://localhost:8085
			String token = null;
			host = PropertiesGetFile.getMap().get("XOADOAUTHCETERDOMAIN");
//			host="http://192.168.1.60:8085";
			String url = host + "/usercode/gettoken";
			xhr = new XoadoHttpRemote("post", url);
			// 获取app之间存的code
			String code = (String) request.getSession().getAttribute(XoadoConstant.LOCALAPPCODE);
			xhr.setHeader(request, response, code);
			// 创建集合放入将要传的参数
			List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
			// 加入参数，BasicNameValuePair会自动转换格式""=""&""="";
			list.add(new BasicNameValuePair("XOADOTOKENID", XOADOTOKENID));
			if (null != Organizeid) {
				list.add(new BasicNameValuePair("Organizeid", Organizeid));
				token = xhr.send(list);
			} else {
				token = xhr.send(list);
			}
			if (token != null) {
				AccessIdentity accessIdentity = AccessIdentity.add(token);
				return accessIdentity;
			}
			return null;
		}
		return null;

	}

	/**
	 * 获取程序心跳
	 * 
	 * @param code
	 *            令牌
	 * @param appid
	 *            程序id
	 * @return
	 */
	public static String headerbeat(String code, String appid) {
		host = PropertiesGetFile.getMap().get("XOADOAUTHCETERDOMAIN");
//		host="http://192.168.1.60:8085";
		if (code != null) {
			String url = host + "/application/refresh?code=" + code + "&appId=" + appid;
			xhr = new XoadoHttpRemote("get", url);
			String send = xhr.send();
			return send;
		} else {
			String url = host + "/application/register?appId=" + appid;
			xhr = new XoadoHttpRemote("get", url);
			String send = xhr.send();
			return send;
		}

	}

	/**
	 * TODO 去认证中心根据code获取信息
	 * 
	 * @param body
	 * @param url
	 *            请求路径
	 * @param appid
	 *            程序id
	 * @param parmer
	 * @param
	 * @return
	 */
	public static AccessIdApplication getAppCodeByAuth(HttpServletRequest request, HttpServletResponse response,
			String code) {
		AccessIdApplication accessidapplication = new AccessIdApplication();
		if (code != null) {
			// 获取认证中心的地址
			host = PropertiesGetFile.getMap().get("XOADOAUTHCETERDOMAIN");
//			host="http://192.168.1.60:8085";
			// 带参拼接
			String url = host + "/application/verify";
			xhr = new XoadoHttpRemote("post", url);
			// 获取session内header的ID
			String codes = (String) request.getSession().getAttribute(XoadoConstant.XOADOAUTHCETERDOMAIN);
			// 去认证中心获取信息
			xhr.setHeader(request, response, codes);
			// 创建集合放入将要传的参数
			List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
			// 加入参数，BasicNameValuePair会自动转换格式""=""&""="";
			list.add(new BasicNameValuePair("code", code));
			String codeValue = xhr.send(list);
			if (codeValue == null || codeValue.equals("")) {

				return accessidapplication;
			}
			accessidapplication = AccessIdApplication.add(codeValue);
		}
		return accessidapplication;

	}

}
