package com.xoado.tools.service;

import javax.servlet.http.HttpServletRequest;



import com.xoado.common.XoadoResult;

public interface Iscanning {

	
	XoadoResult image_recognition(String base64Body,String type,HttpServletRequest request);
}
