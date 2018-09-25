package com.xoado.authcenter.service.Iuser;




import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xoado.authcenter.bean.AccountLogin;
import com.xoado.authcenter.bean.PhoneVerificationCodeLogin;
import com.xoado.authcenter.bean.Register;
import com.xoado.common.XoadoResult;




public interface IUserLogin {
//	密码登录
//	XoadoResult select(String phoneNumber,String userPassword,HttpServletRequest request,HttpServletResponse response);
	XoadoResult select(AccountLogin accountLogin, HttpServletRequest request, HttpServletResponse response);
//	短信登录
//	XoadoResult phone_VerificationCode_login(String phoneNumber,String verification_code,HttpServletRequest request,HttpServletResponse response);
	XoadoResult phone_VerificationCode_login(PhoneVerificationCodeLogin phoneVerificationCodeLogin,HttpServletRequest request,HttpServletResponse response);
//	注册新用户
//	XoadoResult user_register(String phoneNumber,String userPassword,String Verification_code,HttpServletRequest request);
	XoadoResult user_register(Register register,HttpServletRequest request);
	




}
