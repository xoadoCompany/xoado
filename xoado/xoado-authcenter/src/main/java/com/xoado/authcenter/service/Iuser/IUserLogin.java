package com.xoado.authcenter.service.Iuser;




import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xoado.authcenter.bean.AccountLogin;
import com.xoado.authcenter.bean.PhoneVerificationCodeLogin;
import com.xoado.authcenter.bean.Register;
import com.xoado.common.XoadoResult;
import com.xoado.protocol.XoadoException;




public interface IUserLogin {
//	密码登录
	XoadoResult select(AccountLogin accountLogin, HttpServletRequest request, HttpServletResponse response) throws XoadoException;
//	短信登录
	XoadoResult phone_VerificationCode_login(PhoneVerificationCodeLogin phoneVerificationCodeLogin,HttpServletRequest request,HttpServletResponse response) throws XoadoException;
//	注册新用户
	XoadoResult user_register(Register register,HttpServletRequest request,HttpServletResponse response) throws XoadoException;
	




}
