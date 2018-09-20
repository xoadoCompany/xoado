package com.xoado.FilingCabinet.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.ResponseBody;


import com.xoado.FilingCabinet.service.IArchives;
import com.xoado.FilingCabinet.service.IHandover;
import com.xoado.common.XoadoResult;


@Controller
@RequestMapping("/")
public class UGetAllTheInformation {

	@Autowired
	private IArchives iArchives;
	@Autowired 
	private IHandover iHandover;
	
	/**
	 * 6：返回指定资料的图片附件
	 * @param archiveId
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/{archiveid}/pictures",method=RequestMethod.GET)
	@ResponseBody
	public XoadoResult appointImage(@PathVariable("archiveid") String archiveid, HttpServletRequest request) {
		XoadoResult result = iArchives.appointImage(archiveid, request);
		return result;
	}
	@RequestMapping(value="/{archiveid}/pictures",method=RequestMethod.POST)
	@ResponseBody
	public XoadoResult Add_Appoint_Data_Image(@PathVariable("archiveid") String archiveid, String data, HttpServletRequest request) {
		XoadoResult result = iArchives.Add_Appoint_Data_Image(archiveid, data,request);
		return result;
	}
	/**
	 * 
	 * @param archiveId
	 * @param request
	 * @return  返回指定资料的文档附件
	 */
	@RequestMapping(value="/{archiveid}/docments",method=RequestMethod.GET)
	@ResponseBody
	public XoadoResult appointDocument(@PathVariable("archiveid") String archiveid, HttpServletRequest request) {
		XoadoResult result = iArchives.appointDocument(archiveid, request);
		return result;
	}
	/**
	 * 返回指定资料到的交接记录
	 * @param archiveid
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/{archiveid}/handover",method=RequestMethod.GET)
	@ResponseBody
	public XoadoResult TheSpecifiedDataHandover(@PathVariable("archiveid") String archiveid, HttpServletRequest request) {
		XoadoResult result = iHandover.TheSpecifiedDataHandover(archiveid, request);
		return result;
	}
	
}
