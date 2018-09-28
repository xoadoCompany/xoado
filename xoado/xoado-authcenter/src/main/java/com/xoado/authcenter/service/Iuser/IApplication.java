package com.xoado.authcenter.service.Iuser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xoado.authcenter.bean.AccessVerify;
import com.xoado.authcenter.bean.ApplicationInitialize;
import com.xoado.authcenter.bean.RefreshCode;
import com.xoado.common.XoadoResult;
import com.xoado.protocol.XoadoException;

public interface IApplication {
	
//	应用初始化
	XoadoResult applicationInitialize(ApplicationInitialize initialize, HttpServletRequest request, HttpServletResponse response) throws XoadoException;
	
//	应用刷新code
	XoadoResult refreshCode(RefreshCode reshCode, HttpServletRequest request, HttpServletResponse response) throws XoadoException;
	
//	应用访问验证
	XoadoResult accessVerify(AccessVerify accessVerify, HttpServletRequest request, HttpServletResponse response) throws XoadoException;
}
