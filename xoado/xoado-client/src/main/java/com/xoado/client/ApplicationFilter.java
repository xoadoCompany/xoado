package com.xoado.client;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;

import com.xoado.protocol.AccessIdApplication;
import com.xoado.protocol.XoadoException;

import net.sf.json.JSONObject;

/**
 * 服务过滤器
 */

public class ApplicationFilter implements Filter {
	/**
	 * 需要排除的path
	 */
	private String excludedPages;

	private String[] excludedPageArray;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		System.out.println("项目创建启动服务过滤器");
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
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			HttpServletResponse httpresponse = (HttpServletResponse) response;
			try {
				httpRequest.setCharacterEncoding("UTF-8");
				httpresponse.setContentType("text/html;charset=UTF-8");
				// 获取结果
				AccessIdApplication accessidapplication = ApplicationVerify.getApplicationCode(httpRequest, httpresponse);
				if (accessidapplication != null) {
					chain.doFilter(httpRequest, httpresponse);
				} else {
					JSONObject json = new JSONObject();
					json.put("code", 301);
					json.put("msg", "codeIsException");
					throw new Exception(json.toString());
				}
			} catch (XoadoException e) {
				JSONObject json = new JSONObject();
				json.put("status",e.getCode());
				json.put("msg",e.getMessage());
				//httpresponse.setContentType("text/html;charset=UTF-8");
				httpresponse.getWriter().write(json.toString());
				/*PrintWriter writer = httpresponse.getWriter();
				writer.write(json.toString());*/
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
