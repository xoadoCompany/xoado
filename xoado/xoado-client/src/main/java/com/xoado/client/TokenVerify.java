package com.xoado.client;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xoado.protocol.AccessIdentity;
import com.xoado.protocol.BaseRetCode;
import com.xoado.protocol.OrganizationStauts;
import com.xoado.protocol.XoadoConstant;
import com.xoado.protocol.XoadoHeader;

/**   
 * @ClassName:  TokenVerify   
 * @Description:TODO  求证Token
 */
public class TokenVerify {
	 
	public static AccessIdentity verfiyToken(HttpServletRequest request,HttpServletResponse response) throws Exception{
//		当前会话获session获取token
		AccessIdentity token =(AccessIdentity) request.getSession().getAttribute(XoadoConstant.XOADOTOKEN);
		if(token ==null){
			String XOADOTOKENID = XoadoHeader.getToken(request);
			String Organizeid = null;
			if(XOADOTOKENID==null){
				throw new com.xoado.protocol.XoadoException(Integer.parseInt(BaseRetCode.CODE_PROFESSIONAL_WORK_PARAMETER_NOT_LIKE.getRetCode()), BaseRetCode.CODE_PROFESSIONAL_WORK_PARAMETER_NOT_LIKE.getRetMsg());
			}
//			本地缓存中获取token
			token = XoadoCache.getToken(request,response,XOADOTOKENID,Organizeid);
			if(token==null){
//			本地缓存中找不到当前用户的token，返回错误，从新登录	
				throw new com.xoado.protocol.XoadoException(Integer.parseInt(BaseRetCode.CODE_FRAME_REQUEST_NOT_TOKEN.getRetCode()), BaseRetCode.CODE_FRAME_REQUEST_NOT_TOKEN.getRetMsg());
			}
			request.getSession().setAttribute(XoadoConstant.XOADOTOKEN, token);
//			判断当前账户是否是受控状态
			 if(token.getUserType().equals(OrganizationStauts.CONTROLLED.getDescribe())){
				 throw new com.xoado.protocol.XoadoException(Integer.parseInt(BaseRetCode.CODE_FRAME_NO_ACCESS.getRetCode()), BaseRetCode.CODE_FRAME_NO_ACCESS.getRetMsg());
			 }
			
		}
			return token;
	}

}
