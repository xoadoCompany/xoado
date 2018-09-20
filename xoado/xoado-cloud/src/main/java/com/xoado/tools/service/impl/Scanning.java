package com.xoado.tools.service.impl;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import com.xoado.client.http.XoadoHttpRemote;
import com.xoado.common.XoadoResult;
import com.xoado.protocol.BaseRetCode;

import com.xoado.tools.service.Iscanning;

import net.sf.json.JSONObject;

@Service
public class Scanning implements Iscanning{
	
	/*
	 * 营业执照
	 */
	@Value("${THEBUSINESSLICENSEURL}")
	private String THEBUSINESSLICENSEURL;
	/*
	 * 通用文字识别
	 */
	@Value("${TEXTURL}")
	private String TEXTURL;
	/*
	 * 身份证识别
	 */
	@Value("${IDCARD}")
	private String IDCARD;
	/*
	 * 人脸识别
	 */
	@Value("${FACERECOGNITION}")
	private String FACERECOGNITION;
	/*
	 * 银行卡识别
	 */
	@Value("${BANKCARD}")
	private String BANKCARD;
	/*
	 * 阿里code
	 */
	@Value("${APPCODE}")
	private String APPCODE;
	/*
	 * 存储路径
	 */
	@Value("${XOADO_SCANNING}")
	private String XOADO_SCANNING;
	
	@Override
	public XoadoResult image_recognition(String base64Body,String type,HttpServletRequest request) {

//		生成map
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Authorization", "APPCODE " + APPCODE);
		headers.put("Content-Type", "application/json; charset=UTF-8");
		/*
		if(base64Body==null){
	try {
				
		        InputStream in = null;
		        String imagepath = XOADO_SCANNING+new Date().getTime()+file.getOriginalFilename();
		        File file2 = new File(XOADO_SCANNING);
		        if(!file2.exists()){
					 file2.mkdirs();
					}
		        try {
		        	 File newpositiveImgUrlname = new File(imagepath);
		        	 file.transferTo(newpositiveImgUrlname);
		        	 in = new FileInputStream(newpositiveImgUrlname);
//		        	 进行base64编码
		        	 byte[] bytes=new byte[(int)newpositiveImgUrlname.length()]; in.read(bytes);
		        	 base64Body = Base64.getEncoder().encodeToString(bytes); 
		        	
				} catch (Exception e) {
					e.printStackTrace();
				}finally {
					if (in != null) {
						try {
							 in.close();
						} catch (Exception e) {
							 e.printStackTrace();
						}
					}
				}
			} catch (Exception e) {
				
			}
		}*/
		
		JSONObject json = new JSONObject();
        json.put("image", base64Body);
        String imaText = json.toString();
        if(type=="IDcard"){
//        	如果是身份证
        	String result1 = XoadoHttpRemote.xoadoDopost(IDCARD, imaText, headers);
        	JSONObject jsonObject2 =JSONObject.fromObject(result1);
    		return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_SUCCESS.getRetCode()), BaseRetCode.CODE_SUCCESS.getRetMsg(),jsonObject2);
        }
        else if(type=="TBL"){
//          如果是营业执照
        	 String result1 = XoadoHttpRemote.xoadoDopost(THEBUSINESSLICENSEURL, imaText, headers);
        	 JSONObject jsonObject2 =JSONObject.fromObject(result1);
     		return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_SUCCESS.getRetCode()), BaseRetCode.CODE_SUCCESS.getRetMsg(),jsonObject2);

        }

        String result = XoadoHttpRemote.xoadoDopost(TEXTURL, imaText, headers);
        JSONObject jsonObject =JSONObject.fromObject(result);

		return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_SUCCESS.getRetCode()), BaseRetCode.CODE_SUCCESS.getRetMsg(),jsonObject);
		
	}

}
