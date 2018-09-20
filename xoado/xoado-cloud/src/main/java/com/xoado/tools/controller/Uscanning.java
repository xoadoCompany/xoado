package com.xoado.tools.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import com.xoado.common.XoadoResult;
import com.xoado.tools.service.Iscanning;

@Controller
@RequestMapping("/tools")
public class Uscanning {
	
	@Autowired
	private Iscanning iscanning;
	
	@RequestMapping(value="/scanner",method=RequestMethod.POST)
	@ResponseBody
	public XoadoResult xoado_scanning(
			@RequestParam(value="base64Body",required=false) String base64Body,
			@RequestParam(value="type",required=false) String type,HttpServletRequest request){	
		XoadoResult result = iscanning.image_recognition( base64Body,type,request);
		return result;
		
	}

}
