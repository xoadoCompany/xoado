package com.xoado.authcenter.service.user.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
import com.xoado.protocol.ApplicationIdentity;
import com.xoado.protocol.BaseRetCode;
import com.xoado.protocol.XoadoException;

import net.sf.json.JSONObject;


@Service
public class ApplicationImpl implements com.xoado.authcenter.service.Iuser.IApplication {
	
	@Autowired
	private TblAppInformationMapper tblAppInformationMapper;
	@Autowired
	private RedisCache redisCache;
	
	@Override
	public XoadoResult applicationInitialize(ApplicationInitialize initialize, HttpServletRequest request, HttpServletResponse response) throws  XoadoException {
		// TODO Auto-generated method stub
		if(initialize==null){
			throw new XoadoException(Integer.parseInt(BaseRetCode.CODE_PROFESSIONAL_WORK_PARAMETER_NOT_LIKE.getRetCode()),BaseRetCode.CODE_PROFESSIONAL_WORK_PARAMETER_NOT_LIKE.getRetMsg());
		}
		TblAppInformation key = tblAppInformationMapper.selectByPrimaryKey(initialize.getappId());
		if(key==null){
			throw new XoadoException(Integer.parseInt(BaseRetCode.CODE_PROFESSIONAL_WORK_NOT_FOUND_RESOURCE.getRetCode()), BaseRetCode.CODE_PROFESSIONAL_WORK_NOT_FOUND_RESOURCE.getRetMsg());
		}
			List<ApplicationIdentity> applicationList = new ArrayList<ApplicationIdentity>();
			ApplicationIdentity applicationIdentity = new ApplicationIdentity();
			applicationIdentity.setappId(initialize.getappId());
			applicationIdentity.setcode(MD5.MD5Encode(UUID.randomUUID().toString()));
			applicationIdentity.setvalue("whiteList");
			applicationList.add(applicationIdentity);
			String json = JsonUtils.objectToJson(applicationList);
//			System.out.println("-------------"+json.toString());
			request.getSession().setAttribute("XOADOAPPACCESSCODE", applicationIdentity.getcode());    //存入header
			redisCache.set(applicationIdentity.getcode(), json);
			
			return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_SUCCESS.getRetCode()), BaseRetCode.CODE_SUCCESS.getRetMsg(),applicationIdentity.getcode());
	}

	@Override
	public XoadoResult refreshCode(RefreshCode reshCode, HttpServletRequest request, HttpServletResponse response) throws XoadoException {
		// TODO Auto-generated method stub
		if(reshCode==null){
			throw new XoadoException(Integer.parseInt(BaseRetCode.CODE_PROFESSIONAL_WORK_PARAMETER_NOT_LIKE.getRetCode()), BaseRetCode.CODE_PROFESSIONAL_WORK_PARAMETER_NOT_LIKE.getRetMsg());
		}
		TblAppInformation key = tblAppInformationMapper.selectByPrimaryKey(reshCode.getappId());
		if(key==null){
			throw new XoadoException(Integer.parseInt(BaseRetCode.CODE_PROFESSIONAL_WORK_NOT_FOUND_RESOURCE.getRetCode()), BaseRetCode.CODE_PROFESSIONAL_WORK_NOT_FOUND_RESOURCE.getRetMsg());
		}
		
		String code = (String) request.getSession().getAttribute("XOADOAPPACCESSCODE");
		if(code==null){
			throw new XoadoException(Integer.parseInt(BaseRetCode.CODE_FRAME_APP_UNREGISTER.getRetCode()), BaseRetCode.CODE_FRAME_APP_UNREGISTER.getRetMsg());
		}
//		System.out.println("--------"+code);
		String string = redisCache.get(code);
		ApplicationIdentity applicationIdentity = new ApplicationIdentity();
		net.sf.json.JSONArray array = net.sf.json.JSONArray.fromObject(string);
		for(int i=0;i<array.size();i++){
			JSONObject object = JSONObject.fromObject(array.get(i));
			if(object.get("appId").equals(reshCode.getappId())){
				List<ApplicationIdentity> applicationList = new ArrayList<ApplicationIdentity>();
				applicationIdentity.setappId(object.get("appId").toString());
				applicationIdentity.setcode(MD5.MD5Encode(UUID.randomUUID().toString()));
				applicationIdentity.setvalue(object.get("value").toString());
				applicationList.add(applicationIdentity);
				String json = JsonUtils.objectToJson(applicationList);
				request.getSession().setAttribute("XOADOAPPACCESSCODE", applicationIdentity.getcode());    //存入header
				redisCache.set(applicationIdentity.getcode(), json);
				redisCache.del(code);
			}
		}
		return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_SUCCESS.getRetCode()), BaseRetCode.CODE_SUCCESS.getRetMsg(),applicationIdentity.getcode());
	}
	
	/**
	 * code为第三方应用的code
	 * @throws XoadoException 
	 * @throws  
	 */
	@Override
	public XoadoResult accessVerify(AccessVerify accessVerify, HttpServletRequest request, HttpServletResponse response) throws XoadoException {
		// TODO Auto-generated method stub
		if(accessVerify==null){
			throw new XoadoException(Integer.parseInt(BaseRetCode.CODE_PROFESSIONAL_WORK_PARAMETER_NOT_LIKE.getRetCode()), BaseRetCode.CODE_PROFESSIONAL_WORK_PARAMETER_NOT_LIKE.getRetMsg());
		}
		String string = redisCache.get(accessVerify.getcode());
		if(string==null){
			throw new XoadoException(Integer.parseInt(BaseRetCode.CODE_ERROR_BREAK_THE_LAW.getRetCode()), BaseRetCode.CODE_ERROR_BREAK_THE_LAW.getRetMsg(),"未知应用");
		}
		net.sf.json.JSONArray array = net.sf.json.JSONArray.fromObject(string);
		JSONObject object=null;
		for(int i=0;i<array.size();i++){
			object = JSONObject.fromObject(array.get(i));
			Object status = object.get("value");
			if(!status.equals("whiteList")){
				throw new XoadoException(Integer.parseInt(BaseRetCode.CODE_ERROR_BREAK_THE_LAW.getRetCode()), BaseRetCode.CODE_ERROR_BREAK_THE_LAW.getRetMsg(),object);
			}
		}
		return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_SUCCESS.getRetCode()), BaseRetCode.CODE_SUCCESS.getRetMsg(),object);
	}

}
