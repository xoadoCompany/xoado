package com.xoado.authcenter.controller.users;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xoado.authcenter.bean.RegisterPhoneCode;
import com.xoado.authcenter.bean.TblWeixinUser;
import com.xoado.authcenter.bean.WxRegisterPhone;
import com.xoado.authcenter.jedis.RedisCache;
import com.xoado.authcenter.service.Iuser.IUserWxLogin;
import com.xoado.client.http.XoadoHttpRemote;
import com.xoado.cloud.frame.authcenter.access.UserInfoUtil;
import com.xoado.common.AES;
import com.xoado.common.HttpsUtil;
import com.xoado.common.JsonUtils;
import com.xoado.common.MD5;
import com.xoado.common.ParamCheack;
import com.xoado.common.XoadoResult;
import com.xoado.protocol.AccessIdentity;
import com.xoado.protocol.BaseRetCode;
import com.xoado.protocol.OrganizationStauts;
import com.xoado.protocol.XoadoException;



@Controller
@RequestMapping("/access")
public class UUserWxLogin {
	
	@Autowired
	private RedisCache redisCache;
	@Autowired
	private IUserWxLogin wxlogin;
	
	private static XoadoHttpRemote xhr;
	
	@Value("${MESSAGETEST}")
	private String MESSAGETEST;
	
//	微信登录
	@RequestMapping(value="/wxtoken",method=RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> GetOpenId(@RequestParam("code") String code,HttpServletRequest request){
		System.out.println(code);
		String url = "https://api.weixin.qq.com/sns/jscode2session?appid="+UserInfoUtil.getAppid()+"&secret="+UserInfoUtil.getAppsecret()+"&js_code="+code+"&grant_type=authorization_code";
		xhr= new XoadoHttpRemote("get", url);
		String send = xhr.send();
		net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(send);
		String openId = (String)jsonObject.get("openid");
	
		
		 List<TblWeixinUser> list = wxlogin.WXuser_login_select(openId);
		 AccessIdentity identity = new AccessIdentity();
		 ArrayList<Object> xoadolist = new ArrayList<>();
		 String XOADOTOKENID = MD5.MD5Encode(UUID.randomUUID().toString());
		 Map<String,Object> map = new HashMap<>();
		 
		if(list.size()==0){
			 TblWeixinUser user = new TblWeixinUser();
			 user.setOpenid(openId);
			 user.setXdappid("XDAPPID");
			 user.setWxappid(UserInfoUtil.getAppid());
			 user.setNickName(null);
			 user.setSex(null);
			 user.setCity(null);
			 user.setCountry(null);
			 user.setProvince(null);
			 user.setUserid(null);
			 user.setBindingTime(new Date());
			 user.setCreateTime(new Date());
			 wxlogin.register_Wxuser(user);
			 
			    XOADOTOKENID=MD5.MD5Encode(UUID.randomUUID().toString());
			  
			 	identity.setDate(new Date().getTime());
				
				identity.setXOADOTOKENID(XOADOTOKENID);
				
				identity.setUserId(null);
				
				identity.setUserName(null);
				
				identity.setUserType(OrganizationStauts.NORMAL.getDescribe());
				
				identity.setOpenId(openId);
				
				xoadolist.add(identity);
				
				String json = JsonUtils.objectToJson(xoadolist);
				
				System.out.println("微信表没有该用户:"+json);
				
				redisCache.set(XOADOTOKENID,json);
				
				redisCache.expire(XOADOTOKENID, 1800);
				
				map.put("XOADOTOKENID", XOADOTOKENID);
				
				return map;
		}
		for (TblWeixinUser tblWeixinUser : list) {
			
			identity.setDate(new Date().getTime());
			
			identity.setXOADOTOKENID(MD5.MD5Encode(UUID.randomUUID().toString()));
			
			identity.setUserId(tblWeixinUser.getUserid());
			
			identity.setUserName(tblWeixinUser.getNickName());
			
			identity.setUserType(OrganizationStauts.NORMAL.getDescribe());
			
			identity.setOpenId(openId);
			
			xoadolist.add(identity);
			
			String json = JsonUtils.objectToJson(xoadolist);
			
			redisCache.set(XOADOTOKENID,json);
			
			redisCache.expire(XOADOTOKENID, 1800);
			
			map.put("XOADOTOKENID", XOADOTOKENID);
			map.put("openid", openId);
		}
			
			
		return map;	
		
	}
	@SuppressWarnings("null")
	@RequestMapping(value="wxlogin",method=RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> wx_login(String appId,@RequestParam("code") String code,@RequestParam("iv") String iv,@RequestParam("encryptedData") String encryptedData,@RequestParam("xdappid") String xdappid,HttpServletRequest request,HttpServletResponse response){
		System.out.println("wxlogin:"+code);
		Map<String,Object> map = new HashMap<>();
		
		if(code!=null || code.equals("")){
			
			String CODE = code;
			
			String session_key="";
			
			String openId = "";
			
			String token =UserInfoUtil.getWebAccess(CODE);
			
			String xoado_response = HttpsUtil.httpsRequestToString(token, "GET", null);
			
			JSONObject jsonObject = JSON.parseObject(xoado_response);
			
			session_key = jsonObject.getString("session_key");
			
			try {
				
				String result = AES.decrypt(encryptedData, session_key, iv, "UTF-8");
				
				if(null != result && result.length() > 0){
//					生成一个XOADOTOKENID
					String XOADOTOKENID = MD5.MD5Encode(UUID.randomUUID().toString());
					
					JSONObject userInfoJSON = JSON.parseObject(result);
					
					 openId =(String) userInfoJSON.get("openId");//获取openid
					 
					 String nickname = (String)userInfoJSON.get("nickName");
					 
					 String city = (String)userInfoJSON.get("city");
					 
					 String country = (String)userInfoJSON.get("country");
					 
					 String province = (String)userInfoJSON.get("province");
					 
					 if(null !=userInfoJSON && openId!=null){
						 
						 List<TblWeixinUser> list = wxlogin.WXuser_login_select(openId);
						 
						 List<AccessIdentity> xoadolist = new ArrayList<AccessIdentity>();
						 
						 AccessIdentity identity = new AccessIdentity();
						 
						 if(list==null || list.size()==0){
							 
							 TblWeixinUser user = new TblWeixinUser();
							 
							 user.setOpenid(openId);
							 user.setXdappid(xdappid);
							 user.setWxappid(appId);
							 user.setNickName(nickname);
							 user.setSex(null);
							 user.setCity(city);
							 user.setCountry(country);
							 user.setProvince(province);
							 user.setUserid(null);
							 user.setBindingTime(new Date());
							 user.setCreateTime(new Date());
							 wxlogin.register_Wxuser(user);
//							添加完成后生成token并保存
							 
								 
								 	identity.setDate(new Date().getTime());
									
									identity.setXOADOTOKENID(MD5.MD5Encode(UUID.randomUUID().toString()));
									
									identity.setUserId(null);
									
									identity.setUserName(nickname);
									
									identity.setUserType(OrganizationStauts.NORMAL.getDescribe());
									
									identity.setOpenId(openId);
									
									xoadolist.add(identity);
									
									String json = JsonUtils.objectToJson(xoadolist);
									
									System.out.println("微信表没有该用户:"+json);
									
									redisCache.set(XOADOTOKENID,json);
									
									redisCache.expire(XOADOTOKENID, 1800);
							
							 return map; 
						 }else {

							 for (TblWeixinUser tblwxUser : list) {
									
									identity.setDate(new Date().getTime());
									
									identity.setXOADOTOKENID(MD5.MD5Encode(UUID.randomUUID().toString()));

									identity.setUserId(tblwxUser.getUserid());
									
									identity.setUserName(tblwxUser.getNickName());
									
									identity.setUserType(OrganizationStauts.NORMAL.getDescribe());
									
									identity.setOpenId(tblwxUser.getOpenid());
									
									xoadolist.add(identity);
									
									String json = JsonUtils.objectToJson(xoadolist);
									
									System.out.println("微信表有该用户:"+json);
									
									redisCache.set(XOADOTOKENID,json);
									
									redisCache.expire(XOADOTOKENID, 1800);
									
								}
	
							map.put("XOADOTOKENID", XOADOTOKENID); 
								
							map.put("status", 0);
								
							map.put("msg", "解密成功"); 
						 }
						 
					 }
//					 Map<String, Object> userInfo = new HashMap<String, Object>();
					 
//					 userInfo.put("openId", userInfoJSON.get("openId"));
					 
//					 map.put("userInfo", userInfo);
					 
					 return map;
				}
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			 map.put("status", 0);  
			 map.put("msg", "解密失败");
		}
		
		return map;
		
	}
	/**
	 * 微信用户绑定手机
	 * @param openId
	 * @param phoneNumber
	 * @param Verification_code
	 * @param request
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value="/bindingphone",method=RequestMethod.PUT)
	@ResponseBody
	public XoadoResult Wx_register_phone(String openId,String phoneNumber,String Verification_code,HttpServletRequest request, HttpServletResponse response) throws IOException{
		Map<Object,Object> map = new HashMap<>();
		map.put("openId", openId);
		map.put("phoneNumer",phoneNumber);
		map.put("Verification_code",Verification_code);
		Map<Object,Object> mustMap = new HashMap<>();
		mustMap.put("openId", "openId");
		mustMap.put("phoneNumber", "phoneNumber");
		mustMap.put("Verification_code", "Verification_code");
		WxRegisterPhone wxRegisterPhone = new WxRegisterPhone();
		ParamCheack paramCheack = new ParamCheack();
		wxRegisterPhone = (WxRegisterPhone)paramCheack.membercheack(map, wxRegisterPhone, mustMap);
		XoadoResult result=null;
		try {
			result = wxlogin.Wx_register_phone(wxRegisterPhone, request,response);
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
	 * 
	 * @param phoneNumber
	 * @param request
	 * @return 发送短信
	 */
	
	@RequestMapping(value="/verificationcode",method=RequestMethod.POST)
	@ResponseBody
	public XoadoResult register_phone_code(String phoneNumber, HttpServletRequest request){
		Map<Object,Object> map = new HashMap<>();
		map.put("phoneNumber", phoneNumber);
		Map<Object,Object> mustMap = new HashMap<>();
		mustMap.put("phoneNumber","phoneNumber");
		RegisterPhoneCode registerPhoneCode = new RegisterPhoneCode();
		ParamCheack paramCheack = new ParamCheack();
		registerPhoneCode =(RegisterPhoneCode) paramCheack.membercheack(map, registerPhoneCode, mustMap);
		if(registerPhoneCode==null){
			return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_PROFESSIONAL_WORK_PARAMETER_NOT_LIKE.getRetCode()), BaseRetCode.CODE_PROFESSIONAL_WORK_PARAMETER_NOT_LIKE.getRetMsg());
		}
//	生成验证码		
		int nextInt = new Random().nextInt(999999);
//		System.out.println("验证码:"+nextInt);
		request.getSession().setAttribute(phoneNumber, nextInt);
//	存验证码	
		
		redisCache.set(phoneNumber, nextInt+"");
		redisCache.expire(phoneNumber, 1000*60*5);
//		获取应用之间互访的code
//		appid = "123456789"   短信应用的的apppid
//		String appid = "000003";
//		String code = redisCache.get(appid);
		String text = "小多验证码";
		String code = "123";
		String url = MESSAGETEST+"?text="+text+"&xoado_message="+nextInt+"&phoneNumber="+phoneNumber+"&code="+code;
		System.out.println(url);
		xhr = new XoadoHttpRemote("post", url);
		String send = xhr.send();

		return XoadoResult.build(200, send);
		
	}
	
	/**
	 * 获取小多平台应用访问码
	 * @param XDappid
	 * @return
	 */
	@RequestMapping(value="accesscode")
	@ResponseBody
	public String getxoadoCode(String XDappid){
			String code = null;
			code = MD5.MD5Encode( UUID.randomUUID().toString());		
//			code = redisCache.get(XDappid);
//			if(code==null){
//			    code = MD5.MD5Encode( UUID.randomUUID().toString());
//				redisCache.set(XDappid, code);
//				redisCache.expire(XDappid, 600*5);
//			}
		
		return code;
		
	}

	
}
