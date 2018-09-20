 package com.xoado.authcenter.controller.users;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xoado.authcenter.jedis.XoadoSession;

@SuppressWarnings("unused")
@Controller
@RequestMapping("usercode")
public class Appsession {
	

	
	@Autowired
	private XoadoSession xoadosession;
	
	@RequestMapping(value="setcode",method=RequestMethod.POST)
	@ResponseBody
	public boolean test(String code,String appid){
		
		try {
		xoadosession.set(appid, code);
		xoadosession.expire(appid, 1000*10*20);
		xoadosession.hset("code", appid, code);
		System.out.println(appid+":"+code);
		
		String a = xoadosession.get(appid);
//		System.out.println(a);
		} catch (Exception e) {
//			链接重置   Connection reset
			return false;
		}
		return true;
	}
	
	@RequestMapping("getcode")
	@ResponseBody
	public String getcode(String appid){
		
		String code = xoadosession.get(appid);
		
		return code;
		
	}
	
	@RequestMapping(value="gettoken",method=RequestMethod.POST)
	@ResponseBody
	public String gettoken(String XOADOTOKENID){
		System.out.println("tokenid="+XOADOTOKENID);
		String token = xoadosession.get(XOADOTOKENID);
		System.out.println("认证中心获取到的token是："+token);
		return token;
		
	}
	


	


}
