package com.xoado.authcenter.service.Iuser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xoado.authcenter.bean.AccessVerify;
import com.xoado.authcenter.bean.ApplicationInitialize;
import com.xoado.authcenter.bean.RefreshCode;
import com.xoado.common.XoadoResult;
import com.xoado.protocol.AccessIdApplication;
import com.xoado.protocol.XoadoException;

public interface IApplication {
	
//	应用初始化
	String applicationInitialize(ApplicationInitialize initialize, HttpServletRequest request, HttpServletResponse response) throws XoadoException;
	
//	应用刷新code
	String refreshCode(RefreshCode reshCode, HttpServletRequest request, HttpServletResponse response) throws XoadoException;
	
//	应用访问验证
	AccessIdApplication accessVerify(AccessVerify accessVerify, HttpServletRequest request, HttpServletResponse response) throws XoadoException;
}
