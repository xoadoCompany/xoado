package com.xoado.client;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xoado.protocol.AccessIdApplication;
import com.xoado.protocol.ApplicationStatus;
import com.xoado.protocol.BaseRetCode;

import com.xoado.protocol.XoadoConstant;
import com.xoado.protocol.XoadoHeader;



/**   
 * @ClassName:  Application   
 * @Description:TODO  获取APPCode 
 * @author: c
 * 流程：session->hearder-->Cache-->SSO-->Judge
 * @date:   2018年9月27日 下午3:40:40     
 */
public class ApplicationVerify {

	public static AccessIdApplication getApplicationCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 从当前session中获取code
		AccessIdApplication accessidapplication = (AccessIdApplication) request.getSession().getAttribute(XoadoConstant.XOADOAUTHCETERDOMAIN);
		if (accessidapplication == null) {
			// 当前session无将从header中获取XOADOAPPACCESSCODE
			String code = request.getHeader(XoadoConstant.XOADOAUTHCETERDOMAIN);
			if (code == null) {
				throw new com.xoado.protocol.XoadoException(Integer.parseInt(BaseRetCode.CODE_PROFESSIONAL_WORK_PARAMETER_NOT_LIKE.getRetCode()), BaseRetCode.CODE_PROFESSIONAL_WORK_PARAMETER_NOT_LIKE.getRetMsg());

				//throw new NullPointerException("Header中没有Code的信息");// header没有code
			}
			// code 不为空则去缓存查询该code信息
			accessidapplication = (AccessIdApplication) XoadoCache.getAppCodeByCache(request, response, code);
		}

		// 为当前session更新code
		request.getSession().setAttribute(XoadoConstant.XOADOAUTHCETERDOMAIN, accessidapplication);
		// 获取app的类型
		String appType = accessidapplication.getvalue();
		// String appType =
		// AppLocalCache.cacheTypeStatus(XoadoConstant.XOADOCACHE);
		// 判断是否为合法应用
		if (appType.equals(ApplicationStatus.NOTEXIST.getStauts())) {// 认证中心无此应用
			throw new com.xoado.protocol.XoadoException(Integer.parseInt(BaseRetCode.CODE_FRAME_APP_UNREGISTER.getRetCode()), BaseRetCode.CODE_FRAME_APP_UNREGISTER.getRetMsg());
		}
		// 判断是是否为黑名单
		if (appType.equals(ApplicationStatus.BLACKLIST.getStauts())) {
			throw new com.xoado.protocol.XoadoException(Integer.parseInt(BaseRetCode.CODE_PROFESSIONAL_WORK_PARAMETER_NOT_LIKE.getRetCode()), BaseRetCode.CODE_PROFESSIONAL_WORK_PARAMETER_NOT_LIKE.getRetMsg());// 应用在黑名单里
		}

		return accessidapplication;
	}

}
