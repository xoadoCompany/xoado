package com.xoado.authcenter.controller.users;

import java.util.HashMap;

import java.util.Map;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.ResponseBody;

import com.xoado.authcenter.jedis.XoadoSession;
import com.xoado.authcenter.service.Iuser.IUserLogin;
import com.xoado.client.http.XoadoHttpRemote;
import com.xoado.common.XoadoResult;




/*
 * 已测试
 */
@SuppressWarnings("unused")
@Controller
@RequestMapping("/access/login")
public class UUserLogin {
	
	@Autowired
	private XoadoSession xoadosession;
	@Autowired
	private IUserLogin iUserLogin;
	
	private static XoadoHttpRemote xhr;
	

	/**
	 * 
	 * @param phoneNumber
	 * @param userPassword
	 * @param request
	 * @param response
	 * @return 用户登录
	 */
	@RequestMapping(value="/account",method=RequestMethod.POST)
	@ResponseBody
	public XoadoResult account_Login(String phoneNumber,String userPassword,HttpServletRequest request,HttpServletResponse response){	
		XoadoResult result = iUserLogin.select(phoneNumber, userPassword, request, response);	
		return result;
		
	}
	
	
//	手机验证码登录
	@RequestMapping(value="VerificationCode",method=RequestMethod.POST)
	@ResponseBody
	public XoadoResult phone_VerificationCcode_login(String phoneNumber,String Verification_code,HttpServletRequest request){
		
		XoadoResult result = iUserLogin.phone_VerificationCode_login(phoneNumber, Verification_code, request);
		
		return result;	
		
	}
	
	/**
	 * 
	 * @param phoneNumber
	 * @param userPassword
	 * @param Verification_code
	 * @param request
	 * @return 注册新用户
	 */
	@RequestMapping(value="userRegister",method=RequestMethod.POST)
	@ResponseBody
	public XoadoResult Register(String phoneNumber,String userPassword,String Verification_code,HttpServletRequest request ){
		
		XoadoResult result = iUserLogin.user_register(phoneNumber, userPassword, Verification_code,request);
		return result;	
	}
	
	@RequestMapping("a")
	@ResponseBody
	public void test(HttpServletRequest request){
		
	Map<String,Object> map = new HashMap<>();
		
		Cookie[] cookies = request.getCookies();
		if(cookies!=null){
			for (Cookie cookie : cookies) {
				map.put(cookie.getName(), cookie.getValue());
			}
				String XOADOTOKENID = (String)map.get("XOADOTOKENID");
//				System.out.println("cokies:"+XOADOTOKENID);						
		}		
	}
	

	
	/*
	 * code测试
	 */
	@RequestMapping(value="setcode",method=RequestMethod.POST)
	@ResponseBody
	public Boolean setcode(String appid,String code){
		System.out.println("code:"+code);
		try {
			
			xoadosession.set(appid, code);
			String a = xoadosession.get(appid);
			
			System.out.println(a);
			return false;
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return false;
		
	}
	
//	应用访问应用，到认证中心获取对应的code
	@RequestMapping(value="getcode",method=RequestMethod.POST)
	public String getcode(String appid){

		String code = xoadosession.get(appid);
		
		return code;
		
	}
	
//	访问应用
	@RequestMapping(value="accesscode",method=RequestMethod.POST)	
	public String accesscode(String a){
		
		return a;
		
	}


}
