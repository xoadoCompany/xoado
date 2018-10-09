package com.xoado.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import javax.swing.Timer;

import org.apache.http.client.methods.HttpHead;

import com.xoado.client.http.ApplicationRequest;
import com.xoado.common.MD5;
import com.xoado.protocol.XoadoConstant;




public class ApplicationRegister implements ServletContextListener{

	
	@Override
	public void contextInitialized(ServletContextEvent sc) {
		
//		ApplicationRegister.heart_beat();
		/*
		 * 1:获取认证中心的appid<将appid写入指定的路径src/main/resou>
		 */
		
//		启动扫描配置文件
		PropertiesGetFile.getProperties();
//		获取全局缓存
		final ServletContext context = sc.getServletContext();
		
		final String appid = (String) PropertiesGetFile.getMap().get("APPID");
		
		if(appid!=null){
//		初始化本地缓存
			XoadoCache cache = new XoadoCache();
			context.setAttribute(XoadoConstant.XOADOCACHE, cache);
//		生成code
			String code = null;
//		存全局code
			//context.setAttribute(XoadoConstant.XOADOCACHE, code);
			
//		发送	到中转站
			String string = ApplicationRequest.headerbeat(code,appid);
//			HttpHead httpHead = new HttpHead();
//			httpHead.addHeader(XoadoConstant.XOADOAUTHCETERDOMAIN,code);
			context.setAttribute(XoadoConstant.XOADOAUTHCETERDOMAIN, string);
			System.out.println("code------"+string);
			String isstring = "true";
			//boolean b = string.equals(isstring);
			if(string==null){
				ApplicationRequest.headerbeat(code,appid);
			}
//		启动定时器
			Timer clock = new Timer(1000*60*5, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String code=context.getAttribute(XoadoConstant.XOADOAUTHCETERDOMAIN).toString();
					String newCode=ApplicationRequest.headerbeat(code,appid);
					context.setAttribute(XoadoConstant.XOADOAUTHCETERDOMAIN, newCode);
//					httpHead.addHeader(XoadoConstant.XOADOAUTHCETERDOMAIN,newCode);
				}
			});
			clock.start();
			
		}

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		
	}
	/*
	
	public static void heart_beat() {
		
//		final Object host = PropertiesGetFile.getMap().get("XOADOAUTHCETERDOMAIN");
		String code = "123465789";
		String url ="http://localhost:8081/xoado_test/test12?code="+code;
		ApplicationRegister.httpPackage(url);
		
		
		Timer clock = new Timer(1000*10, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				String code = MD5.MD5Encode(UUID.randomUUID().toString());
				String url ="http://localhost:8081/xoado_test/test12?code="+code;
				
				ApplicationRegister.httpPackage(url);

			}
		});
		
		clock.start();	
		
	}
	*/
/*
	 public static String httpPackage(String url){
		  
		  
		  CloseableHttpClient httpClient = HttpClients.createDefault();
		  
		  HttpPost post = new HttpPost(url);

		  UrlEncodedFormEntity entity;	  

			try {
				CloseableHttpResponse response = httpClient.execute(post);
				HttpEntity etity = response.getEntity();

				if(etity != null){
					//打印响应内容
					String a = EntityUtils.toString(etity, "UTF-8");
					System.out.println(a);
					
							}
						response.close();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return url;

	  }

*/

}
