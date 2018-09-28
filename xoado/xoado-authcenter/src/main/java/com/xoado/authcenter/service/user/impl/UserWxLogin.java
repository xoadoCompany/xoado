package com.xoado.authcenter.service.user.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xoado.authcenter.bean.TblUser;
import com.xoado.authcenter.bean.TblUserExample;
import com.xoado.authcenter.bean.TblWeixinUser;
import com.xoado.authcenter.bean.TblWeixinUserExample;
import com.xoado.authcenter.bean.WxRegisterPhone;
import com.xoado.authcenter.bean.TblWeixinUserExample.Criteria;
import com.xoado.authcenter.jedis.RedisCache;
import com.xoado.authcenter.mapper.TblUserMapper;
import com.xoado.authcenter.mapper.TblWeixinUserMapper;
import com.xoado.authcenter.service.Iuser.IUserWxLogin;
import com.xoado.common.MD5;
import com.xoado.common.XoadoResult;
import com.xoado.protocol.BaseRetCode;
import com.xoado.protocol.XoadoException;




@Service
public class UserWxLogin implements IUserWxLogin{
	
	@Autowired
	private TblWeixinUserMapper tblWeixinUserMapper;
	@Autowired
	private TblUserMapper tblUserMapper;
	@Autowired
	private RedisCache redisCache;

	@Override
	public List<TblWeixinUser> WXuser_login_select(String openId) {
		TblWeixinUserExample example = new TblWeixinUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andOpenidEqualTo(openId);
		List<TblWeixinUser> list = tblWeixinUserMapper.selectByExample(example);
		return list;
	}

	@Override
	public XoadoResult register_Wxuser(TblWeixinUser user) {
		tblWeixinUserMapper.insert(user);
		return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_SUCCESS.getRetCode()), BaseRetCode.CODE_SUCCESS.getRetMsg());
	}
	/**
	 * 微信绑定手机号
	 * @throws XoadoException 
	 * @throws  
	 */
	public XoadoResult Wx_register_phone(WxRegisterPhone wxRegisterPhone,HttpServletRequest request,HttpServletResponse response) throws XoadoException{
		if(wxRegisterPhone==null){
			throw new XoadoException(Integer.parseInt(BaseRetCode.CODE_PROFESSIONAL_WORK_PARAMETER_NOT_LIKE.getRetCode()), BaseRetCode.CODE_PROFESSIONAL_WORK_PARAMETER_NOT_LIKE.getRetMsg());
		}
		/**
		 * 判断手机号是否存在
		 */
		String phone_code = redisCache.get(wxRegisterPhone.getphoneNumber());
		System.out.println("--"+phone_code);
		boolean c= phone_code.equals(wxRegisterPhone.getVerification_code());
		String userid = MD5.MD5Encode(UUID.randomUUID().toString());
		if(c!=false){
			
		TblUserExample example = new TblUserExample();	
		com.xoado.authcenter.bean.TblUserExample.Criteria criteria = example.createCriteria();
		criteria.andPhoneNumberEqualTo(wxRegisterPhone.getphoneNumber());
		List<TblUser> list = tblUserMapper.selectByExample(example);
		
		if(list.size()==0 || list==null){
			TblUser tblUser = new TblUser();
			tblUser.setPhoneNumber(wxRegisterPhone.getphoneNumber());
			tblUser.setUserPassword("123456");
			tblUser.setUserid(userid); 
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
			
			TblWeixinUser user = new TblWeixinUser();
			TblWeixinUser weixinUser = tblWeixinUserMapper.selectByPrimaryKey(wxRegisterPhone.getopenId());
			user.setOpenid(wxRegisterPhone.getopenId());
			user.setWxappid(weixinUser.getWxappid());
			user.setXdappid(weixinUser.getXdappid());
			user.setNickName(weixinUser.getNickName());
			user.setSex(weixinUser.getSex());
			user.setCity(weixinUser.getCity());
			user.setCountry(weixinUser.getCountry());
			user.setProvince(weixinUser.getProvince());
			user.setUserid(userid);
			user.setBindingTime(new Date());
			user.setCreateTime(weixinUser.getCreateTime());
			
			tblWeixinUserMapper.updateByPrimaryKey(user);
			return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_SUCCESS.getRetCode()),BaseRetCode.CODE_SUCCESS.getRetMsg());	
		}
		
//		手机号已经注册，但未绑定微信小程序
		for (TblUser tblUser : list) {
			userid = tblUser.getUserid();
		}
		TblWeixinUser user = new TblWeixinUser();
		TblWeixinUser weixinUser = tblWeixinUserMapper.selectByPrimaryKey(wxRegisterPhone.getopenId());
		user.setOpenid(wxRegisterPhone.getopenId());
		user.setWxappid(weixinUser.getWxappid());
		user.setXdappid(weixinUser.getXdappid());
		user.setNickName(weixinUser.getNickName());
		user.setSex(weixinUser.getSex());
		user.setCity(weixinUser.getCity());
		user.setCountry(weixinUser.getCountry());
		user.setProvince(weixinUser.getProvince());
		user.setUserid(userid);
		user.setBindingTime(new Date());
		user.setCreateTime(weixinUser.getCreateTime());
		tblWeixinUserMapper.updateByPrimaryKey(user);
		return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_SUCCESS.getRetCode()),BaseRetCode.CODE_SUCCESS.getRetMsg());	
		}
		return null;
		
	
	}
}
