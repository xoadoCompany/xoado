package com.xoado.client;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

import com.xoado.protocol.AccessIdentity;
import com.xoado.protocol.XoadoException;

import net.sf.json.JSONObject;

/*
 * 小多过滤器
 */

public class XoadoFilter implements Filter {
	/**
	 * 需要排除的path
	 */
	private String excludedPages;

	private String[] excludedPageArray;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		System.out.println("项目创建启动过滤器");
		// 项目创建启动过滤器
		/**/

		excludedPages = filterConfig.getInitParameter("excludedPages");
		if (StringUtils.isNotEmpty(excludedPages)) {
			excludedPageArray = excludedPages.split(",");
		}
		return;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		boolean isExcludedPage = false;
		for (String page : excludedPageArray) {
			if (((HttpServletRequest) request).getServletPath().equals(page)) {
				isExcludedPage = true;
				break;
			}
		}
		if (isExcludedPage) {
			chain.doFilter(request, response);
		} else {
			// 获取到httprequestsession
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			HttpServletResponse httpresponse = (HttpServletResponse) response;
			try {
				httpresponse.setContentType("text/html;charset=UTF-8");
				//httpresponse.setCharacterEncoding("UTF-8");// 解决中文乱码
				httpRequest.setCharacterEncoding("UTF-8");

				// 处理日志
				XoadoLog.getlog(httpRequest);
				// 获取token
				AccessIdentity token = TokenVerify.verfiyToken(httpRequest, httpresponse);
				// String empId = (String) session.getAttribute("userid");
				if (token != null) {
				/*	if("REGISTED".equals(token.getPermissionMask())){
						System.out.println("该用户为登记用户+++++不可访问+++++++");
					}if("VISITOR".equals(token.getPermissionMask())){
						System.out.println("该用户为游客++++不可访问+++++++");
					}*/
					chain.doFilter(httpRequest, httpresponse);
				} else {
					JSONObject json = new JSONObject();
					json.put("status", 301);
					json.put("msg", "Token");
					throw new Exception(json.toString());
				}

			} catch (XoadoException e) {
				JSONObject json = new JSONObject();
				json.put("status",e.getCode());
				json.put("msg",e.getMessage());
				httpresponse.getWriter().write(json.toString());
			} catch (Exception e) {
				JSONObject json = new JSONObject();
				json.put("Exception", e.getMessage());
				httpresponse.getWriter().write(json.toString());
			}

		}
	}

	@Override
	public void destroy() {

	}

}
