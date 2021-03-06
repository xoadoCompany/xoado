package com.xoado.FilingCabinet.service;
/**
* @author 作者
* @version 创建时间：2018年8月2日 上午11:52:51
* 类说明
*/

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import com.xoado.common.XoadoResult;

public interface IArchives {
	/**
	 * 1,当前用户添加一项新资料到临时档案区
	 * @param archiveName   档案名称
	 * @param ownerOrgid   组织Id
	 * @param ownerOrgname  组织名称
	 * @param comments  备注
	 * @param request
	 * @return
	 */
	XoadoResult insertArchives(String archive_Name,String owner_Orgid,String owner_Orgname,Object comments,HttpServletRequest request);	
	/**
	 * 
	 * @param Page  页数		null
	 * @param Rows   行数	null
	 * @param request
	 * @return   2:返回当前用户所有临时资料区的资料信息【列表分页】
	 */
	XoadoResult getarchives(Integer Page,Integer Rows,HttpServletRequest request);
	/**
	 * 3:指定资料放入准备交接区
	 * @param archiveId   资料Id
	 * @param request
	 * @return	3:XoadoResult
	 */
	XoadoResult Add_data_ready(String archiveId,HttpServletRequest request);
	/**
	 * 4:指定资料移除交接区
	 * @param archiveId
	 * @param request
	 * @return   4: XoadoResult
	 */
	XoadoResult Remove_data_ready(String archiveId,HttpServletRequest request);
	/**
	 * 指定资料添加图片附件
	 * @param archiveId
	 * @param data
	 * @param request
	 * @return
	 */
	XoadoResult Add_Appoint_Data_Image(String archiveId ,Object data,HttpServletRequest request);
	/**
	 * 6：返回指定资料的图片附件
	 * @param archiveId
	 * @param request
	 * @return
	 */
	
	XoadoResult appointImage(String archiveId,HttpServletRequest request);
	/**
	 * 7:指定资料添加一个文档附件
	 * @param archiveId   
	 * @param attachmentDocument
	 * @param request
	 * @return
	 */
	XoadoResult Add_Aoopint_Data_Doc(String archiveId,MultipartFile[] attachmentDocument,HttpServletRequest request);
	/**
	 * 
	 * @param archiveId
	 * @param request
	 * @return  返回指定资料的文档附件
	 */
	XoadoResult appointDocument(String archiveId,HttpServletRequest request);
	/**
	 * 
	 * @param archiveid
	 * @param page
	 * @param rows
	 * @param request
	 * @return 返回当前用户所有资料信息
	 */
	XoadoResult allTheInformation(String archiveid,Integer page,Integer rows,HttpServletRequest request);
	/**
	 * 
	 * @param archiveid
	 * @return 返回指定资料详情
	 */
	XoadoResult SpecifiedInformation(String archiveid,HttpServletRequest request);
	
	/**
	 * 资料放入准备区的详情
	 * @param request
	 * @return
	 */
	XoadoResult temporary(HttpServletRequest request);
	
	/**
	 * 删除一个资料
	 * @param archiveid
	 * @param request
	 * @return
	 */
	XoadoResult deleteArchive(String archiveid, HttpServletRequest request);
	
}
