package com.xoado.protocol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;


/**   
 * @ClassName:  AccessIdApplication   
 * @Description:TODO  App认证实体类
 * @author: c
 * @date:   2018年9月22日 下午13:55:35     
 */
public class AccessIdApplication {

	public AccessIdApplication() {
		super();
	}

	private String code;
	
	private String value;
	
	private String appId;

	public String getcode() {
		return code;
	}

	public void setcode(String code) {
		this.code = code;
	}

	public String getvalue() {
		return value;
	}

	public void setvalue(String value) {
		this.value = value;
	}

	public String getappId() {
		return appId;
	}

	public void setappId(String appId) {
		this.appId = appId;
	}

	public AccessIdApplication(String code, String value, String appId) {
		super();
		this.code = code;
		this.value = value;
		this.appId = appId;
	}

	@Override
	public String toString() {
		return "AccessIdApplication [code=" + code + ", value=" + value + ", appId=" + appId + "]";
	}
	/**
	 * TODO 根据传的内容解析并植入
	 * @param codeValue
	 * @return
	 */
	public static AccessIdApplication add(String codeValue){
		AccessIdApplication ad = new AccessIdApplication();
		Object object = JSON.parse(codeValue);
		Map<Object,Object> map = (Map<Object, Object>) object;
		List<AccessIdApplication> list = JsonUtils.jsonToList(codeValue, AccessIdApplication.class);
		for (Object key:map.entrySet()) {
			ad.setappId(map.get("appId").toString());
			ad.setcode(map.get("code").toString());
			ad.setvalue(map.get("value").toString());
		}
		
		return ad;
		
	}

	

	
	
}
