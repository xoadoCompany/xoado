package com.xoado.organize.service.impl;


import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.xoado.common.MD5;
import com.xoado.common.XoadoResult;
import com.xoado.organize.bean.TblApiInteface;
import com.xoado.organize.bean.TblApiIntefaceExample;
import com.xoado.organize.bean.TblAppInformation;
import com.xoado.organize.bean.TblAppInformationExample;
import com.xoado.organize.bean.TblAppPackageExample;
import com.xoado.organize.bean.TblAppInformationExample.Criteria;
import com.xoado.organize.bean.TblAppPackage;
import com.xoado.organize.bean.TblUser;
import com.xoado.organize.mapper.TblApiIntefaceMapper;
import com.xoado.organize.mapper.TblAppInformationMapper;
import com.xoado.organize.mapper.TblAppPackageMapper;
import com.xoado.organize.mapper.TblUserMapper;

import com.xoado.organize.service.IApplication;
import com.xoado.protocol.AccessIdentity;
import com.xoado.protocol.BaseRetCode;
import com.xoado.protocol.XoadoConstant;

/**
* @author 作者
* @version 创建时间：2018年6月1日 下午3:55:48
* 类说明
*/
@Service
public class Aplication implements IApplication {
	
	@Autowired
	private TblAppInformationMapper tblAppInformationMapper;
	@Autowired
	private TblApiIntefaceMapper apiIntefaceMapper;
	@Autowired
	private TblUserMapper tblUserMapper;
	@Autowired
	private TblAppPackageMapper tblAppPackageMapper;
	
	@Value("${XOADO_IMAGE}")
	private String XOADO_IMAGE;
	
	@Value("${XOADO_APPLICATION_APPIMG}")
	private String XOADO_APPLICATION_APPIMG;
	
	@Value("${XOADO_URL}")
	private String XOADO_URL;

	@Override
	public XoadoResult application_add(String organizeId, String appName, Long appType,
			String description,HttpServletRequest request) {
		TblAppInformation information = new TblAppInformation();
		information.setAppId(MD5.MD5Encode(UUID.randomUUID().toString()));
		AccessIdentity token = (AccessIdentity) request.getSession().getAttribute(XoadoConstant.XOADOTOKEN);
		String userId = token.getUserId();
		String createrName = token.getUserName();
		information.setCreater(userId);
		information.setCreaterName(createrName);
		information.setOrgid(organizeId);
		information.setAppName(appName);
		information.setAppType(appType);
		information.setDescription(description);
		information.setAppImgUrl(null);
		information.setAppPrivateKey(null);
		information.setAppSigningKey(null);
		information.setAppPublicKey(null);
		information.setAuditFlag(null);
		information.setMessage(null);
		information.setAuditTime(null);
		information.setAppPath(null);
		information.setRedirectUri(null);
		information.setAppDownloadUrl(null);
		information.setCreatetime(new Date());
		tblAppInformationMapper.insert(information);
		return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_SUCCESS.getRetCode()), BaseRetCode.CODE_SUCCESS.getRetMsg());
	}

	@Override
	public XoadoResult application_userid(HttpServletRequest request) {
		AccessIdentity token = (AccessIdentity) request.getSession().getAttribute(XoadoConstant.XOADOTOKEN);
		String userId = token.getUserId();
		TblAppInformationExample example = new TblAppInformationExample();
		Criteria criteria = example.createCriteria();
		criteria.andCreaterEqualTo(userId);
		List<TblAppInformation> list = tblAppInformationMapper.selectByExample(example);
//		当前用户下的所有应用
		return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_SUCCESS.getRetCode()), BaseRetCode.CODE_SUCCESS.getRetMsg(),list);
	}

	@Override
	public XoadoResult application_all(HttpServletRequest request) {
		TblAppInformationExample example = new TblAppInformationExample();
		List<TblAppInformation> list = tblAppInformationMapper.selectByExample(example);
//		展示所有的应用
		return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_SUCCESS.getRetCode()), BaseRetCode.CODE_SUCCESS.getRetMsg(),list);
	}

//	c)	[get] /applications/{id}           返回指定应用详细信息
	@Override
	public XoadoResult aplications_id(String appId, HttpServletRequest request) {
		TblAppInformation tblAppInformation = tblAppInformationMapper.selectByPrimaryKey(appId);
		return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_SUCCESS.getRetCode()), BaseRetCode.CODE_SUCCESS.getRetMsg(), tblAppInformation);
	}

	@Override
	public XoadoResult aplication_id_logo(String appId, HttpServletRequest request) {
		TblAppInformation tblAppInformation = tblAppInformationMapper.selectByPrimaryKey(appId);
		if(tblAppInformation != null){
			String url = tblAppInformation.getAppImgUrl();
			return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_SUCCESS.getRetCode()), BaseRetCode.CODE_SUCCESS.getRetMsg(), url);
		}
		return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_PROFESSIONAL_WORK_NOT_FOUND_RESOURCE.getRetCode()), BaseRetCode.CODE_PROFESSIONAL_WORK_NOT_FOUND_RESOURCE.getRetMsg());
	}
//	e)	[put] /applications/{id}            修改应用信息
	@Override
	public XoadoResult application_update(String appId,String appName, Long appType, String description, String appDomain,HttpServletRequest request) {
		TblAppInformation tblAppInformation = tblAppInformationMapper.selectByPrimaryKey(appId);
		tblAppInformation.setAppName(appName);
		tblAppInformation.setDescription(description);
		tblAppInformation.setAppType(appType);
		tblAppInformation.setAppDomain(appDomain);
		tblAppInformationMapper.updateByPrimaryKey(tblAppInformation);
	
		return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_SUCCESS.getRetCode()), BaseRetCode.CODE_SUCCESS.getRetMsg());
	}
// g)	[put] /applications/{id}/logo       更新应用LOGO
	@Override
	public XoadoResult application_update_logo(String appId, MultipartFile appImgUrl, HttpServletRequest request) {

		String appImgUrlName = XOADO_APPLICATION_APPIMG+UUID.randomUUID()+appImgUrl.getOriginalFilename();
		File file = new File(XOADO_IMAGE+XOADO_APPLICATION_APPIMG);
		System.out.println(XOADO_IMAGE);
		if(!file.exists()){
			file.mkdirs();
		}
		File newapppImgUrl = new File(XOADO_IMAGE+appImgUrlName);
		try {
			appImgUrl.transferTo(newapppImgUrl);
		} catch (IllegalStateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TblAppInformation tblAppInformation = tblAppInformationMapper.selectByPrimaryKey(appId);
		tblAppInformation.setAppImgUrl(XOADO_URL+appImgUrlName);
		tblAppInformationMapper.updateByPrimaryKey(tblAppInformation);
		return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_SUCCESS.getRetCode()), BaseRetCode.CODE_SUCCESS.getRetMsg());
	}
//	i)	[put] /applications/{id}/managers     变更应用负责人
	@Override
	public XoadoResult application_update_member(String appId, String creater, HttpServletRequest request) {
		TblAppInformation tblAppInformation = tblAppInformationMapper.selectByPrimaryKey(appId);
		tblAppInformation.setCreater(creater);
		TblUser tblUser = tblUserMapper.selectByPrimaryKey(creater);
		String name = tblUser.getName();
		tblAppInformation.setCreaterName(name);
		tblAppInformationMapper.updateByPrimaryKey(tblAppInformation);
		return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_SUCCESS.getRetCode()), BaseRetCode.CODE_SUCCESS.getRetMsg());
	}
//	h)	[get] /applications/apis      返回应用下的接口列表
	@Override
	public XoadoResult application_myappid(String appId, HttpServletRequest request) {
		TblApiIntefaceExample example = new TblApiIntefaceExample();
		com.xoado.organize.bean.TblApiIntefaceExample.Criteria criteria = example.createCriteria();
		criteria.andAppIdEqualTo(appId);
		List<TblApiInteface> list = apiIntefaceMapper.selectByExample(example);
		
		return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_SUCCESS.getRetCode()), BaseRetCode.CODE_SUCCESS.getRetMsg(), list);
	}
//	 [get] /applications/packageapis/{appid}   返回当前应用需要的开发API列表
	@Override
	public XoadoResult app_package_id(String appId, HttpServletRequest request) {
		TblAppPackageExample example = new TblAppPackageExample();
		com.xoado.organize.bean.TblAppPackageExample.Criteria criteria = example.createCriteria();
		criteria.andAppIdEqualTo(appId);
		List<TblAppPackage> list = tblAppPackageMapper.selectByExample(example);
		
		return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_SUCCESS.getRetCode()), BaseRetCode.CODE_SUCCESS.getRetMsg(), list);
	}



}
