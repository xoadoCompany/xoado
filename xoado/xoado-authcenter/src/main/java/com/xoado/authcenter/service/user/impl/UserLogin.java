package com.xoado.authcenter.service.user.impl;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.xoado.authcenter.bean.AccountLogin;
import com.xoado.authcenter.bean.PhoneVerificationCodeLogin;
import com.xoado.authcenter.bean.Register;
import com.xoado.authcenter.bean.TblUser;
import com.xoado.authcenter.bean.TblUserExample;
import com.xoado.authcenter.bean.TblUserExample.Criteria;
import com.xoado.authcenter.jedis.XoadoSession;
import com.xoado.authcenter.mapper.TblUserMapper;
import com.xoado.authcenter.service.Iuser.IUserLogin;

import com.xoado.common.JsonUtils;
import com.xoado.common.MD5;
import com.xoado.common.XoadoResult;




import com.xoado.protocol.AccessIdentity;
import com.xoado.protocol.BaseRetCode;

import com.xoado.protocol.OrganizationStauts;





@Service
public class UserLogin implements IUserLogin {
	
	@Autowired
	private TblUserMapper tblUserMapper;
	
	@Autowired
	private XoadoSession xoadoSession;
	
	
	
	@Value("${XOADO_TOKEN}")
	private String XOADOTOKEN;

	
	@Override
	public XoadoResult select(AccountLogin accountLogin, HttpServletRequest request,
			HttpServletResponse response) {
		
		if(accountLogin == null){
			return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_PROFESSIONAL_WORK_PARAMETER_NOT_LIKE.getRetCode()), BaseRetCode.CODE_PROFESSIONAL_WORK_PARAMETER_NOT_LIKE.getRetMsg());
		}
		TblUserExample example = new TblUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andPhoneNumberEqualTo(accountLogin.getphoneNumber());
		List<TblUser> list = tblUserMapper.selectByExample(example);
/*
 * 查询用户是否存在
 */
		if(list==null||list.size()==0){
			
			return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_FRAME_ACCOUNT_NOT_EXIST.getRetCode()), BaseRetCode.CODE_FRAME_ACCOUNT_NOT_EXIST.getRetMsg());
		
		}
		
		TblUser user = list.get(0);
		
		if(!user.getUserPassword().equals(MD5.MD5Encode(accountLogin.getuserPassword()))){
				
			return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_FRAME_PASSWORD_ERROR.getRetCode()), BaseRetCode.CODE_FRAME_PASSWORD_ERROR.getRetMsg());
			
		}
			
/*
 * key=XOADOTOKENID  value=token
 */
		List<AccessIdentity> xoadolist = new ArrayList<AccessIdentity>();
		
		AccessIdentity identity = new AccessIdentity();
		
		for (TblUser tblUser : list) {
			
			identity.setDate(new Date().getTime());
			
			identity.setXOADOTOKENID(MD5.MD5Encode(UUID.randomUUID().toString()));
			
			identity.setUserId(tblUser.getUserid());
			
			identity.setUserName(tblUser.getName());
			
			identity.setUserType(OrganizationStauts.NORMAL.getDescribe());
			
			identity.setOpenId(null);
			
			xoadolist.add(identity);
			
		}
		
		String json = JsonUtils.objectToJson(xoadolist);
		
		Cookie cookie = new Cookie("XOADOTOKENID", identity.getXOADOTOKENID());
		cookie.setPath("/");
		cookie.setMaxAge(1000*60*5);
		response.addCookie(cookie);
	
		request.getSession().setAttribute(identity.getXOADOTOKENID(), json);
//		根据用户Id取到XOADOTOKENID
//		xoadoSession.set(identity.getUserId(), identity.getXOADOTOKENID());

		xoadoSession.set(identity.getXOADOTOKENID(), json);
		
		xoadoSession.expire(identity.getUserId(), 1800);
		
		return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_SUCCESS.getRetCode()),BaseRetCode.CODE_SUCCESS.getRetMsg(),identity.getXOADOTOKENID());
		
	}

	@Override
	public XoadoResult phone_VerificationCode_login(PhoneVerificationCodeLogin phoneVerificationCodeLogin,
			HttpServletRequest request,HttpServletResponse response) {
		
		if(phoneVerificationCodeLogin==null){
			return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_PROFESSIONAL_WORK_PARAMETER_NOT_LIKE.getRetCode()),BaseRetCode.CODE_PROFESSIONAL_WORK_PARAMETER_NOT_LIKE.getRetMsg());
		}
//		查询此手机号是否存在，如果不存在则提示用户注册
		TblUserExample example = new TblUserExample();	
		Criteria criteria = example.createCriteria();
		criteria.andPhoneNumberEqualTo(phoneVerificationCodeLogin.getphoneNumber());
		List<TblUser> list = tblUserMapper.selectByExample(example);
		if(list.size()==0 || list==null){
			return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_FRAME_ACCOUNT_NOT_EXIST.getRetCode()),BaseRetCode.CODE_FRAME_ACCOUNT_NOT_EXIST.getRetMsg());	
		}

		String phone_code = xoadoSession.get(phoneVerificationCodeLogin.getphoneNumber());

		boolean b = phone_code.equals(phoneVerificationCodeLogin.getVerification_code());

		if(phone_code==""){
			
			return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_FRAME_VERIFICATION_CODE_ERROR.getRetCode()), BaseRetCode.CODE_FRAME_VERIFICATION_CODE_ERROR.getRetMsg());
			
		}else{
			
			if(phoneVerificationCodeLogin.getVerification_code()==null){
				
				return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_ERROR_CHECKCODE_EXPIRE.getRetCode()), BaseRetCode.CODE_ERROR_CHECKCODE_EXPIRE.getRetMsg());
			
			}if(b==false){
				
				return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_FRAME_VERIFICATION_CODE_ERROR.getRetCode()), BaseRetCode.CODE_FRAME_VERIFICATION_CODE_ERROR.getRetMsg());
			}
			
			List<AccessIdentity> xoadolist = new ArrayList<AccessIdentity>();
			
			AccessIdentity identity = new AccessIdentity();
			
			for (TblUser tblUser : list) {
				
				identity.setDate(new Date().getTime());
				
				identity.setXOADOTOKENID(MD5.MD5Encode(UUID.randomUUID().toString()));
				
				identity.setUserId(tblUser.getUserid());
				
				identity.setUserName(tblUser.getName());
				
				identity.setUserType(OrganizationStauts.NORMAL.getDescribe());
				
				identity.setOpenId(null);
				
				xoadolist.add(identity);
				
			}
			
			String json = JsonUtils.objectToJson(xoadolist);
			
			Cookie cookie = new Cookie("XOADOTOKENID", identity.getXOADOTOKENID());
			cookie.setPath("/");
			cookie.setMaxAge(1000*60*5);
			response.addCookie(cookie);
		
			request.getSession().setAttribute(identity.getXOADOTOKENID(), json);
//			根据用户Id取到XOADOTOKENID
//			xoadoSession.set(identity.getUserId(), identity.getXOADOTOKENID());

			xoadoSession.set(identity.getXOADOTOKENID(), json);
			
			xoadoSession.expire(identity.getUserId(), 1800);
			
			return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_SUCCESS.getRetCode()),BaseRetCode.CODE_SUCCESS.getRetMsg(),identity.getXOADOTOKENID());
		}
		
	}
//	注册新用户
	@Override
	public XoadoResult user_register(Register register, HttpServletRequest request
			) {
		
		if(register==null){
			return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_PROFESSIONAL_WORK_PARAMETER_NOT_LIKE.getRetCode()),BaseRetCode.CODE_PROFESSIONAL_WORK_PARAMETER_NOT_LIKE.getRetMsg());
		}
//		String str = "[0-9]{8,11}";
		String str="^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\d{8}$";
		
		Pattern pattern = Pattern.compile(str);
		Matcher matcher = pattern.matcher(register.getphoneNumber());
		boolean b = matcher.matches();

		if(b==false){
//			手机格式不正确，请求参数错误	
				return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_PROFESSIONAL_WORK_PARAMETER_NOT_LIKE.getRetCode()), BaseRetCode.CODE_PROFESSIONAL_WORK_PARAMETER_NOT_LIKE.getRetMsg());
		}
//	查询用户是否存在
		else{
			TblUserExample example = new TblUserExample();	
			Criteria criteria = example.createCriteria();
			criteria.andPhoneNumberEqualTo(register.getphoneNumber());
			List<TblUser> list = tblUserMapper.selectByExample(example);
			if(list.size()==0 || list==null){
				String phone_code = xoadoSession.get(register.getphoneNumber());
				boolean c= phone_code.equals(register.getVerification_code());
				if(c!=false){
					TblUser tblUser = new TblUser();
					tblUser.setPhoneNumber(register.getphoneNumber());
					String md5Encode = MD5.MD5Encode(register.getuserPassword());
					tblUser.setUserPassword(md5Encode);
					tblUser.setUserid(MD5.MD5Encode(UUID.randomUUID().toString())); 
					tblUser.setUnionid(null);
					tblUser.setName(null);
					tblUser.setNickName(null);
					tblUser.setHeadImgUrl(null);
					tblUser.setRegisterTime(new Date());
					tblUser.setAccountstatus("CONTROLLED");
					tblUser.setAccounttype(null);
					tblUser.setSex(null);
					tblUser.setCity(null);
					tblUser.setCountry(null);
					tblUser.setProvince(null);
					tblUser.setIdcard(null);
					tblUser.setPositiveImgUrl(null);
					tblUser.setReverseImgUrl(null);
					tblUser.setMessage(null);
					tblUser.setAuditTime(null);
					tblUserMapper.insert(tblUser);
					return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_SUCCESS.getRetCode()), BaseRetCode.CODE_SUCCESS.getRetMsg());
				}
				return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_FRAME_VERIFICATION_CODE_ERROR.getRetCode()), BaseRetCode.CODE_FRAME_VERIFICATION_CODE_ERROR.getRetMsg());
			}
		}
		return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_FRAME_ACCOUNT_EXIST.getRetCode()), BaseRetCode.CODE_FRAME_ACCOUNT_EXIST.getRetMsg());
	}



}
