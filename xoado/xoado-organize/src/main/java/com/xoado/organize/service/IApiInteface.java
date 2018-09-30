package com.xoado.organize.service;
/**
* @author 作者
* @version 创建时间：2018年6月4日 下午2:21:07
* 类说明
*/

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xoado.common.XoadoResult;
import com.xoado.organize.bean.ApiIdUpdate;
import com.xoado.organize.bean.ApiIntefaceAdd;
import com.xoado.organize.bean.SeleApiInteface;
import com.xoado.protocol.XoadoException;

public interface IApiInteface {
	
//	XoadoResult sele_apiInteface(SeleApiInteface seleApiInteface,HttpServletRequest request, HttpServletResponse response) throws XoadoException;
	XoadoResult sele_apiInteface(Integer page,Integer rows,HttpServletRequest request ,HttpServletResponse response) throws XoadoException;
	
	XoadoResult sele_id_apiInteface(String apiId,HttpServletRequest request);
	
//	XoadoResult apiinteface_add(ApiIntefaceAdd apiIntefaceAdd,HttpServletResponse response) throws XoadoException;
	XoadoResult apiinteface_add(String appId, String apiPath, Long requestMethod, String header,String description, String parameter, String apistatus, String auditstatus,HttpServletResponse response) throws XoadoException;
	
//	XoadoResult apiid_update(String apiId,ApiIdUpdate apiIdUpdate,HttpServletResponse response) throws XoadoException;
	XoadoResult apiid_update(String apiId, String apiPath, Long requestMethod, String header, String description,String parameter,HttpServletResponse response) throws XoadoException;
	
	XoadoResult apiid_update_status(String apiId,String apistatus,HttpServletRequest request);
	
	XoadoResult api_package_add(String apiId,String appId,String description,HttpServletRequest request);
	
	XoadoResult api_package_dele(String apiId,String appId,HttpServletRequest request);
	
	
	
	

}
