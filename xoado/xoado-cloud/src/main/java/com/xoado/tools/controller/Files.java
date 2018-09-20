package com.xoado.tools.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.xoado.common.XoadoResult;
import com.xoado.tools.service.IXDfileUpload;

@Controller
@RequestMapping("/files")
public class Files {
	
	@Autowired
	private IXDfileUpload ixDfileUpload;
	
	@RequestMapping(value="/base64",method=RequestMethod.POST)
	@ResponseBody
	public XoadoResult uploadBase64(String base64text,HttpServletRequest request) throws Exception {
		XoadoResult result = ixDfileUpload.uploadBase64(base64text, request);
		return result;
	}
	@RequestMapping(value="/stream",method=RequestMethod.POST)
	@ResponseBody
	public XoadoResult Add_Appoint_Data_Image(@RequestParam("attachmentpicture") MultipartFile[] attachmentpicture,
			HttpServletRequest request) {
		XoadoResult result = ixDfileUpload.Add_Appoint_Data_Image(attachmentpicture, request);
		return result;
	}

}
