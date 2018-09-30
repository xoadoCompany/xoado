package com.xoado.authcenter.service.user.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.xoado.authcenter.bean.AccessVerify;
import com.xoado.authcenter.bean.ApplicationInitialize;
import com.xoado.authcenter.bean.RefreshCode;
import com.xoado.authcenter.bean.TblAppInformation;
import com.xoado.authcenter.jedis.RedisCache;
import com.xoado.authcenter.mapper.TblAppInformationMapper;
import com.xoado.common.JsonUtils;
import com.xoado.common.MD5;
import com.xoado.common.XoadoResult;
import com.xoado.protocol.AccessIdApplication;
import com.xoado.protocol.ApplicationStatus;
import com.xoado.protocol.BaseRetCode;
import com.xoado.protocol.XoadoConstant;
import com.xoado.protocol.XoadoException;

import net.sf.json.JSONObject;


@Service
public class ApplicationImpl implements com.xoado.authcenter.service.Iuser.IApplication {
	
	@Autowired
	private TblAppInformationMapper tblAppInformationMapper;
	@Autowired
	private RedisCache redisCache;
	
	@Override
	public String applicationInitialize(ApplicationInitialize initialize, HttpServletRequest request, HttpServletResponse response) throws  XoadoException {
		// TODO Auto-generated method stub
		String codeValue=MD5.MD5Encode(UUID.randomUUID().toString());
		response.setHeader(XoadoConstant.XOADOAUTHCETERDOMAIN, codeValue);
		if(initialize==null){
			throw new XoadoException(Integer.parseInt(BaseRetCode.CODE_PROFESSIONAL_WORK_PARAMETER_NOT_LIKE.getRetCode()),BaseRetCode.CODE_PROFESSIONAL_WORK_PARAMETER_NOT_LIKE.getRetMsg());
		}
		TblAppInformation key = tblAppInformationMapper.selectByPrimaryKey(initialize.getappId());
		if(key==null){
			throw new XoadoException(Integer.parseInt(BaseRetCode.CODE_PROFESSIONAL_WORK_NOT_FOUND_RESOURCE.getRetCode()), BaseRetCode.CODE_PROFESSIONAL_WORK_NOT_FOUND_RESOURCE.getRetMsg());
		}
			List<AccessIdApplication> applicationList = new ArrayList<AccessIdApplication>();
			AccessIdApplication applicationIdentity = new AccessIdApplication();
			applicationIdentity.setappId(initialize.getappId());
			applicationIdentity.setcode(codeValue);
			applicationIdentity.setvalue(ApplicationStatus.WHITELIST.getStauts());
			applicationList.add(applicationIdentity);
			String json = JsonUtils.objectToJson(applicationList);
			request.getSession().setAttribute(XoadoConstant.XOADOAUTHCETERDOMAIN, applicationIdentity.getcode());   
			redisCache.set(applicationIdentity.getcode(), json);
			
			return applicationIdentity.getcode();
	}

	@Override
	public String refreshCode(RefreshCode reshCode, HttpServletRequest request, HttpServletResponse response) throws XoadoException {
		// TODO Auto-generated method stub
		if(reshCode==null){
			throw new XoadoException(Integer.parseInt(BaseRetCode.CODE_PROFESSIONAL_WORK_PARAMETER_NOT_LIKE.getRetCode()), BaseRetCode.CODE_PROFESSIONAL_WORK_PARAMETER_NOT_LIKE.getRetMsg());
		}
		TblAppInformation key = tblAppInformationMapper.selectByPrimaryKey(reshCode.getappId());
		if(key==null){
			throw new XoadoException(Integer.parseInt(BaseRetCode.CODE_PROFESSIONAL_WORK_NOT_FOUND_RESOURCE.getRetCode()), BaseRetCode.CODE_PROFESSIONAL_WORK_NOT_FOUND_RESOURCE.getRetMsg());
		}
//		String code = (String) request.getSession().getAttribute(XoadoConstant.XOADOAUTHCETERDOMAIN);
		if(reshCode.getcode()==null){
			throw new XoadoException(Integer.parseInt(BaseRetCode.CODE_FRAME_APP_UNREGISTER.getRetCode()), BaseRetCode.CODE_FRAME_APP_UNREGISTER.getRetMsg());
		}
		String string = redisCache.get(reshCode.getcode());
		System.out.println(string+"88888888888888");
		AccessIdApplication applicationIdentity = new AccessIdApplication();
		net.sf.json.JSONArray array = net.sf.json.JSONArray.fromObject(string);
		for(int i=0;i<array.size();i++){
			JSONObject object = JSONObject.fromObject(array.get(i));
			if(object.get("appId").equals(reshCode.getappId())){
				List<AccessIdApplication> applicationList = new ArrayList<AccessIdApplication>();
				applicationIdentity.setappId(object.get("appId").toString());
				applicationIdentity.setcode(MD5.MD5Encode(UUID.randomUUID().toString()));
				applicationIdentity.setvalue(object.get("value").toString());
				applicationList.add(applicationIdentity);
				String json = JsonUtils.objectToJson(applicationList);
				response.setHeader(XoadoConstant.XOADOAUTHCETERDOMAIN, applicationIdentity.getcode());   //存入header
				request.getSession().setAttribute(XoadoConstant.XOADOAUTHCETERDOMAIN, applicationIdentity.getcode());    
				redisCache.set(applicationIdentity.getcode(), json);
				redisCache.del(reshCode.getcode());
			}
		}
		return applicationIdentity.getcode();
	}
	
	/**
	 * code为第三方应用的code
	 * @throws XoadoException 
	 * @throws  
	 */
	@Override
	public AccessIdApplication accessVerify(AccessVerify accessVerify, HttpServletRequest request, HttpServletResponse response) throws XoadoException {
		// TODO Auto-generated method stub
		AccessIdApplication applicationIdentity = new AccessIdApplication();
		if(accessVerify==null){
			throw new XoadoException(Integer.parseInt(BaseRetCode.CODE_PROFESSIONAL_WORK_PARAMETER_NOT_LIKE.getRetCode()), BaseRetCode.CODE_PROFESSIONAL_WORK_PARAMETER_NOT_LIKE.getRetMsg());
		}
		String string = redisCache.get(accessVerify.getcode());
		if(string==null){
			applicationIdentity.setcode(accessVerify.getcode());
			applicationIdentity.setvalue(ApplicationStatus.NOTEXIST.getStauts());
			applicationIdentity.setappId("");
			return applicationIdentity;
		}
		net.sf.json.JSONArray array = net.sf.json.JSONArray.fromObject(string);
		JSONObject object=null;
		for(int i=0;i<array.size();i++){
			object = JSONObject.fromObject(array.get(i));
			Object status = object.get("value");
			applicationIdentity.setappId(object.get("appId").toString());
			applicationIdentity.setcode(object.get("code").toString());
			applicationIdentity.setvalue(status.toString());
			if(!status.equals(ApplicationStatus.WHITELIST.getStauts())){
				throw new XoadoException(Integer.parseInt(BaseRetCode.CODE_ERROR_BREAK_THE_LAW.getRetCode()), BaseRetCode.CODE_ERROR_BREAK_THE_LAW.getRetMsg(),object);
			}
			
		}
		return applicationIdentity;
	}

}
