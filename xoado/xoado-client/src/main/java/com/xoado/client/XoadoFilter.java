package com.xoado.client;


import java.io.IOException;
import java.io.PrintWriter;


import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
//import javax.servlet.ServletContext;
import javax.servlet.ServletException;
//import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.xoado.protocol.AccessIdentity;


import net.sf.json.JSONObject;


/*
 * 小多过滤器
 */

public class XoadoFilter implements Filter{
	/**
	 * 需要排除的path
	 */
	private String excludedPages;       

	private String[] excludedPageArray;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		System.out.println("项目创建启动过滤器");
//	项目创建启动过滤器
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
				if(((HttpServletRequest) request).getServletPath().equals(page)){ 
					isExcludedPage = true;
					break;
				}
			}
			if(isExcludedPage){
				chain.doFilter(request, response);  
			}else {
				
			}
			
			/**/
			
//			获取到httprequestsession
			 HttpServletRequest httpRequest = (HttpServletRequest)request;	 
			 HttpServletResponse httpresponse =  (HttpServletResponse)response;
			 try {
			 httpresponse.setCharacterEncoding("UTF-8");//解决中文乱码
			 httpRequest.setCharacterEncoding("UTF-8");
			 
//			 处理日志
			 XoadoLog.getlog(httpRequest);
//			 获取token
			 AccessIdentity token = TokenVerify.verfiy(httpRequest, httpresponse);
			 if(token!=null){
				 chain.doFilter(httpRequest, httpresponse);
				
			 }else {
				 JSONObject json = new JSONObject();
				 json.put("code", 301);
				 json.put("msg", "Token");
				 throw new Exception(json.toString());
			 }

		} catch (Exception e) {
			 String message = e.getMessage();
			 PrintWriter writer = httpresponse.getWriter();
			 writer.write(message);		 
			 
		}
		
		
	}

	@Override
	public void destroy() {
		
		
	}

}
