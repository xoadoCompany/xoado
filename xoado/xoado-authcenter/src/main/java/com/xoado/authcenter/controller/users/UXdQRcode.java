package com.xoado.authcenter.controller.users;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import com.xoado.authcenter.service.Iuser.IXdQRcode;
import com.xoado.common.XoadoResult;




/**
* @author 作者
* @version 创建时间：2018年8月8日 下午3:12:00
* 类说明
*/
@Controller
@RequestMapping("/qrcode")
public class UXdQRcode {
	
	@Autowired
	private IXdQRcode iXdQRcode;

	/**
	 * 创建一个新的二维码
	 * @param QRcode
	 * @param Data
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/creating",method=RequestMethod.POST)
	@ResponseBody
	public XoadoResult createQRcode(@RequestParam("QRcode") String QRcode,@RequestParam(value="Data",required=true) String Data,HttpServletRequest request) throws Exception{
		
		XoadoResult result = iXdQRcode.createQRcode(QRcode, Data,request);
		
		return result;	
	}
	@RequestMapping(value="/scanned",method=RequestMethod.POST)
	@ResponseBody
	public XoadoResult scannedQRcode(String QRcode, String Data,HttpServletRequest request) throws Exception {
		XoadoResult result = iXdQRcode.scannedQRcode(QRcode, Data, request);
		return result;
		
	}
	@RequestMapping(value="/scannde",method=RequestMethod.GET)
	@ResponseBody
	public XoadoResult ThequeryScannedQrcode(String QRcode, HttpServletRequest request) {
		XoadoResult result = iXdQRcode.ThequeryScannedQrcode(QRcode, request);
		return result;	
	}
	@RequestMapping(value="/confirmed",method=RequestMethod.POST)
	@ResponseBody
	public XoadoResult TheReceiverGetData(String QRcode, HttpServletRequest request) {
		XoadoResult result = iXdQRcode.TheReceiverGetData(QRcode, request);
		return result;
		
	}
	@RequestMapping(value="/confirmed",method=RequestMethod.GET)
	@ResponseBody
	public XoadoResult HandOverThequery(String QRcode, HttpServletRequest request) {
		XoadoResult result = iXdQRcode.HandOverThequery(QRcode, request);
		return result;
		
	}

	
	
}
