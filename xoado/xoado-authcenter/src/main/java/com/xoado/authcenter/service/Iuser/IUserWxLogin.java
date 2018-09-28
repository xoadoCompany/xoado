package com.xoado.authcenter.service.Iuser;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xoado.authcenter.bean.TblWeixinUser;
import com.xoado.authcenter.bean.WxRegisterPhone;
import com.xoado.common.XoadoResult;
import com.xoado.protocol.XoadoException;



public interface IUserWxLogin {
	
//	微信登录
	List<TblWeixinUser> WXuser_login_select(String openId);
	XoadoResult register_Wxuser(TblWeixinUser user);
//	小程序绑定手机号
	XoadoResult Wx_register_phone(WxRegisterPhone wxRegisterPhone,HttpServletRequest request,HttpServletResponse response) throws XoadoException;

}
