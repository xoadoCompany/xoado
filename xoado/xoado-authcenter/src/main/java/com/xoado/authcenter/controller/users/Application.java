package com.xoado.authcenter.controller.users;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xoado.authcenter.bean.AccessVerify;
import com.xoado.authcenter.bean.ApplicationInitialize;
import com.xoado.authcenter.bean.RefreshCode;
import com.xoado.authcenter.service.Iuser.IApplication;
import com.xoado.common.ParamCheack;
import com.xoado.common.XoadoResult;
import com.xoado.protocol.AccessIdApplication;
import com.xoado.protocol.XoadoConstant;
import com.xoado.protocol.XoadoException;

import net.sf.json.JSONObject;


@Controller
@RequestMapping("/application")
public class Application {
	
	@Autowired
	private IApplication Iapplication;
	
	/**
	 * 获取code
	 * @param appId
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value="/register",method=RequestMethod.GET)
	@ResponseBody
	public String applicationInitialize(String appId, HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<Object,Object> map = new HashMap<>();
		map.put("appId", appId);
		Map<Object,Object> mustMap = new HashMap<>();
		mustMap.put("appId", "appId");
		ApplicationInitialize initialize = new ApplicationInitialize();
		ParamCheack paramCheack = new ParamCheack();
		initialize =(ApplicationInitialize) paramCheack.membercheack(map, initialize, mustMap);
		String result=null;
		try {
			result = Iapplication.applicationInitialize(initialize, request, response);
//			System.out.println("---header:----"+request.getHeader(XoadoConstant.XOADOAUTHCETERDOMAIN));
			Enumeration headerNames = request.getHeaderNames();
	 		System.out.println("---headerNames-----"+headerNames);
		} catch (XoadoException e) {
			// TODO Auto-generated catch block
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("status", e.getCode());
			jsonObject.put("msg", e.getMessage());
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write(jsonObject.toString());
		}
		return result;
	}
	
	
	/**
	 * 应用刷新code
	 * @param appId
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value="/refresh",method=RequestMethod.GET)
	@ResponseBody
	public String refreshCode(String appId,String code, HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<Object,Object> map = new HashMap<>();
		map.put("appId", appId);
		map.put("code", code);
		Map<Object,Object> mustMap = new HashMap<>();
		mustMap.put("appId", "appId");
		mustMap.put("code", "code");
		RefreshCode refreshCode = new RefreshCode();
		ParamCheack paramCheack = new ParamCheack();
//		refreshCode.setappId(appId);
		refreshCode = (RefreshCode)paramCheack.membercheack(map, refreshCode, mustMap);
		String result=null;
		try {
			result = Iapplication.refreshCode(refreshCode, request, response);
			
			
		} catch (XoadoException e) {
			// TODO Auto-generated catch block
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("status", e.getCode());
			jsonObject.put("msg", e.getMessage());
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write(jsonObject.toString());
		}
		return result;
	}
	
	/**
	 * 应用访问验证
	 * @param code   第三方应用的code
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value="/verify",method=RequestMethod.POST)
	@ResponseBody
	public AccessIdApplication accessVerify(String code, HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<Object,Object> map = new HashMap<>();
		map.put("code", code);
		Map<Object,Object> mustMap = new HashMap<>();
		mustMap.put("code", "code");
		AccessVerify accessVerify = new AccessVerify();
		ParamCheack paramCheack = new ParamCheack();
		accessVerify =(AccessVerify) paramCheack.membercheack(map, accessVerify, mustMap);
		AccessIdApplication applicationIdentity=null;
		try {
			applicationIdentity = Iapplication.accessVerify(accessVerify, request, response);
		} catch (XoadoException e) {
			// TODO Auto-generated catch block
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("status", e.getCode());
			jsonObject.put("msg", e.getMessage());
			jsonObject.put("data", e.getObject());
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write(jsonObject.toString());
		}
		return applicationIdentity;
		
	}
}
