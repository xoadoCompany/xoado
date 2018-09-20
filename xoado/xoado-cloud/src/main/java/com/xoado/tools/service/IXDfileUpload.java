package com.xoado.tools.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import com.xoado.common.XoadoResult;

public interface IXDfileUpload {
	
	XoadoResult uploadBase64(String base64text,HttpServletRequest request) throws Exception;
	/**
	 * 5:指定资料添加一个图片附件
	 * @param archiveId
	 * @param attachmentPicture
	 * @param request
	 * @return 	XoadoResult
	 */
	XoadoResult Add_Appoint_Data_Image(MultipartFile[] attachmentpicture,HttpServletRequest request);

}
