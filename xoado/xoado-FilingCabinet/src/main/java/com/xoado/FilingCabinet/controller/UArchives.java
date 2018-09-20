package com.xoado.FilingCabinet.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xoado.FilingCabinet.service.IArchives;
import com.xoado.common.XoadoResult;

/**
* @author 作者
* @version 创建时间：2018年8月3日 上午10:43:45
* 类说明
*/
@Controller
@RequestMapping("/temporary")
public class UArchives {
	
	@Autowired
	private IArchives iArchives;
	/**
	 * 1,当前用户添加一项新资料到临时档案区
	 * @param archiveName   档案名称
	 * @param ownerOrgid   组织Id
	 * @param ownerOrgname  组织名称
	 * @param comments  备注
	 * @param request
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	public XoadoResult insertArchives(@RequestParam(value="archive_name",required = false)String archive_name,@RequestParam(value="owner_orgid",required = false) String owner_orgid,@RequestParam(value="owner_orgname",required = false) String owner_orgname,@RequestParam(value="comments",required = false) Object comments,HttpServletRequest request) {
		XoadoResult result = iArchives.insertArchives(archive_name, owner_orgid, owner_orgname, comments, request);
		return result;
	}
	/**
	 * 
	 * @param Page  页数		null
	 * @param Rows   行数	null
	 * @param request
	 * @return   2:返回当前用户所有临时资料区的资料信息【列表分页】
	 */
	@RequestMapping(method=RequestMethod.GET) 
	@ResponseBody
	public XoadoResult getarchives(@RequestParam(value="page",required = false) Integer page,@RequestParam(value="rows",required = false) Integer rows, HttpServletRequest request) {
		XoadoResult result = iArchives.getarchives(page, rows, request);
		return result;
	}
	/**
	 * 3:指定资料放入准备交接区
	 * @param archiveId   资料Id
	 * @param request
	 * @return	3:XoadoResult
	 */
	@RequestMapping(value="/ready/{archiveid}",method=RequestMethod.PUT)
	@ResponseBody
	public XoadoResult Add_data_ready(@PathVariable("archiveid") String archiveId, HttpServletRequest request) {
		XoadoResult result = iArchives.Add_data_ready(archiveId, request);
		return result;
	}
	/**
	 * 4:指定资料移除交接区
	 * @param archiveId
	 * @param request
	 * @return   4: XoadoResult
	 */
	@RequestMapping(value="/ready/{archiveid}",method=RequestMethod.DELETE)
	@ResponseBody
	public XoadoResult Remove_data_ready(@PathVariable("archiveid") String archiveid, HttpServletRequest request) {
		XoadoResult result = iArchives.Remove_data_ready(archiveid, request);
		return result;
	}

	@RequestMapping(value="user",method=RequestMethod.GET)
	@ResponseBody
	public XoadoResult allTheInformation(@RequestParam(value="archiveid" ,required=false) String archiveid,@RequestParam(value="page" ,required=false) Integer page,@RequestParam(value="rows" ,required=false) Integer rows, HttpServletRequest request) {
		XoadoResult result = iArchives.allTheInformation(archiveid, page, rows, request);
		return result;
		
	}
	@RequestMapping(value="/{archiveid}",method=RequestMethod.GET)
	@ResponseBody
	public XoadoResult SpecifiedInformation(@PathVariable(value="archiveid") String archiveid,HttpServletRequest request) {
		XoadoResult result = iArchives.SpecifiedInformation(archiveid, request);
		return result;
	}
//	查询准备区的资料
	@RequestMapping(value="/ready",method=RequestMethod.GET)
	@ResponseBody
	public XoadoResult temporary(HttpServletRequest request) {
		XoadoResult result = iArchives.temporary(request);
		return result;
		
	}
	
	@RequestMapping(value="/{archiveid}",method=RequestMethod.DELETE)
	@ResponseBody
	public XoadoResult deleteArchive(@PathVariable(value="archiveid") String archiveid,HttpServletRequest request){
		XoadoResult result = iArchives.deleteArchive(archiveid, request);
		return result;
	}
	

}
