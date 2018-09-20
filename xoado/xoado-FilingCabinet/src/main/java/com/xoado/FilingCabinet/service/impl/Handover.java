package com.xoado.FilingCabinet.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xoado.FilingCabinet.bean.TblCurrentArchives;
import com.xoado.FilingCabinet.bean.TblCurrentArchivesExample;
import com.xoado.FilingCabinet.bean.TblCurrentArchivesExample.Criteria;
import com.xoado.FilingCabinet.bean.TblHandoverRecord;
import com.xoado.FilingCabinet.bean.TblHandoverRecordExample;
import com.xoado.FilingCabinet.mapper.TblBoxesMapper;
import com.xoado.FilingCabinet.mapper.TblCurrentArchivesMapper;
import com.xoado.FilingCabinet.mapper.TblHandoverRecordMapper;
import com.xoado.FilingCabinet.service.IHandover;
import com.xoado.common.MD5;
import com.xoado.common.XoadoResult;
import com.xoado.protocol.AccessIdentity;
import com.xoado.protocol.ArchivesStatus;
import com.xoado.protocol.BaseRetCode;
import com.xoado.protocol.HandoverType;
import com.xoado.protocol.XoadoConstant;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
* @author 作者
* @version 创建时间：2018年8月5日 下午12:16:26
* 类说明
*/
@Service
public class Handover implements IHandover{

	@Autowired
	private TblHandoverRecordMapper handoverRecordMapper;
	@Autowired
	private TblCurrentArchivesMapper CurrentArchivesMapper;
	@Autowired
	private TblBoxesMapper tblBoxesMapper;
	/**
	 * 接收资料，资料放入档案柜，从档案柜中取出资料
	 * @param handout 交件方
	 * @param recipient 接收方
	 * @param task_id 任务单 null
	 * @param archives 档案
	 * @param Businesslabel 业务标签 null
	 * @param Comments 交接备注 null
	 * @param Location 地址  null
	 * @return
	 */
	
	@Override
	public XoadoResult ReceiveData(Object handout, Object recipient, String task_id, Object archives,
			String businesslabel, String comments, Object location,HttpServletRequest request) {
//		获取token信息
		AccessIdentity token = (AccessIdentity) request.getSession().getAttribute(XoadoConstant.XOADOTOKEN);
//		交接时间
		Date date = new Date();   
//		生成交接单id
		String handoverid = MD5.MD5Encode(UUID.randomUUID().toString());
//		json为空赋值null
		if(location==""){location = null;}
		if(comments==""){comments = null;}
		
		
//		获取交件方信息
		JSONObject handoutJson = JSONObject.fromObject(handout);
		String handoutType =(String) handoutJson.get("type");
		String handoutid =(String) handoutJson.get("id");		
//		获取收件方信息
		JSONObject recipientJson = JSONObject.fromObject(recipient);
		String recipientType =(String) recipientJson.get("type"); 
		String recipentid =(String) recipientJson.get("id");
//		获取资料信息
		JSONObject archiveJson = JSONObject.fromObject(archives);	
		String archiveid =(String) archiveJson.get("archiveid");
//		交接单表
		TblHandoverRecord handoverRecord = new TblHandoverRecord();
//		柜子中的用户
		String sqlId ="";

//		当前用户接收别人的资料
			if(handoutType.equals(ArchivesStatus.person.toString()) && recipientType.equals(ArchivesStatus.person.toString())){	
				System.out.println("人与人交接资料");
				if(!token.getUserId().equals(recipentid)){
//					CODE_ERROR_BREAK_THE_LAW  收件方id与当前token不一致
					return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_ERROR_BREAK_THE_LAW.getRetCode()), BaseRetCode.CODE_ERROR_BREAK_THE_LAW.getRetMsg());
				}		
			}else{
				
				if(handoutType.equals(ArchivesStatus.person.toString()) && recipientType.equals(ArchivesStatus.box.toString())){
					System.out.println("人与柜子交接");
					if(!token.getUserId().equals(handoutid)){
//						交件方id与token不一致
						return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_ERROR_BREAK_THE_LAW.getRetCode()), BaseRetCode.CODE_ERROR_BREAK_THE_LAW.getRetMsg());
					}	
					sqlId = recipentid;
				}else if(handoutType.equals(ArchivesStatus.box.toString()) && recipientType.equals(ArchivesStatus.person.toString())){
					System.out.println("柜子到人");
					if(!token.getUserId().equals(recipentid)){ 
//						收件方id与当前token不一致
						return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_ERROR_BREAK_THE_LAW.getRetCode()), BaseRetCode.CODE_ERROR_BREAK_THE_LAW.getRetMsg());
					}
					sqlId = handoutid;
				}
				
				String sql = "SELECT * FROM tbl_boxes AS box WHERE (JSON_CONTAINS(creater,'{\"creater_id\":\""+token.getUserId()+"\"}') || JSON_CONTAINS(shareto,'{\"userid\":\""+token.getUserId()+"\"}')) && box_id=\""+sqlId+"\"";
				List<LinkedHashMap<String,Object>> Boxlist = tblBoxesMapper.selectGis(sql);
				if(Boxlist==null || Boxlist.size()==0){
					
//					当前用户不是柜子的创建者或共享者/柜子id不存在
					return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_PROFESSIONAL_WORK_NOT_FOUND_RESOURCE.getRetCode()), BaseRetCode.CODE_PROFESSIONAL_WORK_NOT_FOUND_RESOURCE.getRetMsg());
				}
			}	
//			获取交接资料是否属于交件方
			TblCurrentArchivesExample example = new TblCurrentArchivesExample();
			example.createCriteria().andArchiveIdEqualTo(archiveid);
			example.createCriteria().andOwneridEqualTo(handoutid);	
			List<TblCurrentArchives> list = CurrentArchivesMapper.selectByExample(example);	

			if(list==null || list.size()==0){	
//				CODE_PROFESSIONAL_WORK_PARAMETER_NOT_LIKE  资料不属于交件方
				return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_PROFESSIONAL_WORK_PARAMETER_NOT_LIKE.getRetCode()), BaseRetCode.CODE_PROFESSIONAL_WORK_PARAMETER_NOT_LIKE.getRetMsg());
			}		
			JSONObject locationJson = JSONObject.fromObject(location);
			locationJson.put("userid", token.getUserId());
			locationJson.put("date", date);
			JSONArray jsonArray = new JSONArray();
			jsonArray.add(locationJson);
			JSONArray array = JSONArray.fromObject(jsonArray);
			for (TblCurrentArchives tblCurrentArchives : list) {
				TblHandoverRecord receiveData = handoverRecord.ReceiveData(handoverid, handoutJson, recipientJson, archiveJson, comments, businesslabel, task_id, date, array,location, request);
				tblCurrentArchives.setOwnerid(recipentid);
				handoverRecordMapper.insert(receiveData);
				CurrentArchivesMapper.updateByExample(tblCurrentArchives, example);
			}

		return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_SUCCESS.getRetCode()), BaseRetCode.CODE_SUCCESS.getRetMsg(),handoverid);
	}
	
	
	/**
	 * 发送快递
	 * @param handout
	 * @param recipient
	 * @param task_id
	 * @param express   快递详情
	 * @param archives
	 * @param Businesslabel
	 * @param Comments
	 * @param Location
	 * @return
	 */
	
	@Override
	public XoadoResult ToSendExpress(Object handout, Object recipient, String task_id, Object express, Object archives,
			String businesslabel, String comments, Object location,HttpServletRequest request) {
		if(location==""){location = null;}
		if(comments==""){comments = null;}
		AccessIdentity token = (AccessIdentity) request.getSession().getAttribute(XoadoConstant.XOADOTOKEN);
		String handover_id =MD5.MD5Encode( UUID.randomUUID().toString());
//		判断资料是否属于当前用户
		JSONArray archiveJson = JSONArray.fromObject(archives);

		for (int i = 0; i < archiveJson.size(); i++) {
			Object object = archiveJson.get(i);
//		获取用户传过来的资料Id是否属于当前用户	
			JSONObject fromObject = JSONObject.fromObject(object);
			Object archiveid = fromObject.get("archiveid");
			
			TblCurrentArchivesExample example = new TblCurrentArchivesExample();
			Criteria criteria = example.createCriteria();
			criteria.andArchiveIdEqualTo(archiveid.toString());
			List<TblCurrentArchives> list = CurrentArchivesMapper.selectByExample(example);
			for (TblCurrentArchives tblCurrentArchives : list) {
				if(!token.getUserId().equals(tblCurrentArchives.getOwnerid())){
//					CODE_PROFESSIONAL_WORK_PARAMETER_NOT_LIKE  资料与交件方不符
					return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_PROFESSIONAL_WORK_PARAMETER_NOT_LIKE.getRetCode()), BaseRetCode.CODE_PROFESSIONAL_WORK_PARAMETER_NOT_LIKE.getRetMsg());
				}
			}
		}
		JSONObject handoutJson = JSONObject.fromObject(handout);
		JSONObject recipientJson = JSONObject.fromObject(recipient);
		JSONObject expressJson = JSONObject.fromObject(express);
		JSONArray jsonArray = new JSONArray();
		jsonArray.add(comments);
		JSONObject locationJson = JSONObject.fromObject(location);
		TblHandoverRecord tblHandoverRecord = new TblHandoverRecord();
		tblHandoverRecord.setHandoverId(handover_id);
		tblHandoverRecord.setHandout(handoutJson.toString());
		tblHandoverRecord.setRecipient(recipientJson.toString());
		tblHandoverRecord.setHandoverType(HandoverType.express.toString());
		tblHandoverRecord.setHandoverContent(archiveJson.toString());
		tblHandoverRecord.setExpress(expressJson.toString());
		tblHandoverRecord.setExpressprogress(null);
		tblHandoverRecord.setProgress(new JSONArray().toString());
		tblHandoverRecord.setHandoverState(HandoverType.expressing.toString());
		tblHandoverRecord.setHandoverDate(new Date());
		tblHandoverRecord.setCompleteDate(null);
		tblHandoverRecord.setComments(jsonArray.toString());
		tblHandoverRecord.setBusinesslabel(businesslabel);
		tblHandoverRecord.setTaskid(task_id);
		tblHandoverRecord.setLocation(locationJson.toString());
		handoverRecordMapper.insert(tblHandoverRecord);	
		return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_SUCCESS.getRetCode()), BaseRetCode.CODE_SUCCESS.getRetMsg(),handover_id);
	}

	/**
	 * 接收快递确认
	 * @param handoverid 交接Id
	 * @param handoverdate 确认时间  ？？？？
	 * @param Businesslabel 业务标签
	 * @param Comments	交接备注
	 * @param Location  交接地址  ？？？
	 * @param request
	 * @return
	 */
	
	@Override
	public XoadoResult ReceiveExpress(String handoverid, String businesslabel, String comments,
			Object location, HttpServletRequest request) {
		AccessIdentity token = (AccessIdentity) request.getSession().getAttribute(XoadoConstant.XOADOTOKEN);
//		判断当前用户是否是收件方
		TblHandoverRecord tblHandoverRecord = handoverRecordMapper.selectByPrimaryKey(handoverid);
		JSONObject recipientJson = JSONObject.fromObject(tblHandoverRecord.getRecipient());
		String recipientid =(String) recipientJson.get("id");
		if(!token.getUserId().equals(recipientid)){
//			CODE_PROFESSIONAL_WORK_PARAMETER_NOT_LIKE  当前用户与收件方不符
			return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_PROFESSIONAL_WORK_PARAMETER_NOT_LIKE.getRetCode()), BaseRetCode.CODE_PROFESSIONAL_WORK_PARAMETER_NOT_LIKE.getRetMsg());
		}
//		获取交接进度
		Object object = tblHandoverRecord.getProgress();
		JSONArray array = JSONArray.fromObject(object);
		System.out.println(location);
		JSONObject fromObject = JSONObject.fromObject(location);
		fromObject.put("userid", token.getUserId());
		fromObject.put("date", new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss:SSS").format(new Date()));
		array.add(fromObject);
//		业务标签
		if(businesslabel=="" || businesslabel==null){
			businesslabel = tblHandoverRecord.getBusinesslabel();
		}
//		确认接收业务
		tblHandoverRecord.setBusinesslabel(businesslabel);
		tblHandoverRecord.setProgress(array.toString());
		tblHandoverRecord.setCompleteDate(new Date());
		tblHandoverRecord.setHandoverState(HandoverType.complete.toString());
//		获取资料Id
		JSONObject archiveJson = JSONObject.fromObject(tblHandoverRecord.getHandoverContent());
		TblCurrentArchivesExample example = new TblCurrentArchivesExample();
		Criteria criteria = example.createCriteria();
		criteria.andArchiveIdEqualTo(archiveJson.get("archiveid").toString());
		List<TblCurrentArchives> list = CurrentArchivesMapper.selectByExample(example);
		for (TblCurrentArchives tblCurrentArchives : list) {
			tblCurrentArchives.setOwnerid(token.getUserId());
			handoverRecordMapper.updateByPrimaryKey(tblHandoverRecord);
			CurrentArchivesMapper.updateByExample(tblCurrentArchives, example);
		}	
		
		return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_SUCCESS.getRetCode()), BaseRetCode.CODE_SUCCESS.getRetMsg());
	}
	/**
	 * 返回指定的快递单号记录
	 * @param expressid
	 * @param page
	 * @param rows
	 * @param request
	 * @return
	 */
	@Override
	public XoadoResult ExpressRecord(String expressid, Integer page, Integer rows, HttpServletRequest request) {
		
//		查询快递表，取出所有信息，生成jsonarray   将快递信息放入json，查询对应的交接单表，将交接单表中的信息放入json，分页
		
		String sql = "SELECT h.handover_id,h.handout,h.recipient,h.taskid,"
				+ "h.comments,h.handover_state,h.progress,h.express"
				+ " FROM tbl_handover_record AS h WHERE JSON_CONTAINS(express,'{\"expressid\":\""+expressid+"\"}')";
		if(page!=null && rows!=null){
			PageHelper.startPage(1, 10);
			List<LinkedHashMap<String,Object>> list = handoverRecordMapper.selectExpress(sql);
			PageInfo<LinkedHashMap<String,Object>> info = new PageInfo<>(list);
			return XoadoResult.build(200, "",info);
		}
			
		List<LinkedHashMap<String,Object>> list = handoverRecordMapper.selectExpress(sql);
		
		return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_SUCCESS.getRetCode()), BaseRetCode.CODE_SUCCESS.getRetMsg(),list);
	}

	/**
	 * 返回当前用户的交接单详情
	 * @param handoverid  交接Id
	 * @param page		 页数
	 * @param rows		行数
	 * @param scope		日期区间
	 * @param request
	 * @return
	 */
	
	@Override
	public XoadoResult userhandover(String handoverid, Integer page, Integer rows, Date scope,
			HttpServletRequest request) {
		AccessIdentity token = (AccessIdentity) request.getSession().getAttribute(XoadoConstant.XOADOTOKEN);
		if(handoverid==null){
//			xml暂时未找到使用mysql函数后不能接收到参数的问题，先定义sql函数的方法
			String sql = "SELECT * FROM tbl_handover_record where JSON_CONTAINS(recipient,'{\"id\":\""+token.getUserId()+"\"}') ORDER BY  complete_date";
			List<LinkedHashMap<String,Object>> list = handoverRecordMapper.selecthandovertoken(sql);
		return XoadoResult.build(200, "",list);
		}
		TblHandoverRecordExample example = new TblHandoverRecordExample();
		example.createCriteria().andHandoverIdEqualTo(handoverid);
		List<TblHandoverRecord> list = handoverRecordMapper.selectByExample(example);
		return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_SUCCESS.getRetCode()), BaseRetCode.CODE_SUCCESS.getRetMsg(),list);
	}

	/**
	 * 指定资料的交接记录
	 * @param archiveid
	 * @param request
	 * @return
	 */
	@Override
	public XoadoResult TheSpecifiedDataHandover(String archiveid, HttpServletRequest request) {
		// TODO Auto-generated method stub
//		创建查询条件，判断是否存在交接信息
		TblCurrentArchivesExample example = new TblCurrentArchivesExample();
		Criteria criteria = example.createCriteria();
		criteria.andArchiveIdEqualTo(archiveid);
		List<TblCurrentArchives> list = CurrentArchivesMapper.selectByExample(example);
		
		String handoverid=null;
		
		for (TblCurrentArchives tblCurrentArchives : list) {
			handoverid = tblCurrentArchives.getHandoverId();	
		}
		if(handoverid==null){
//			CODE_PROFESSIONAL_WORK_NOT_FOUND_RESOURCE   没有交接单
			return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_PROFESSIONAL_WORK_NOT_FOUND_RESOURCE.getRetCode()), BaseRetCode.CODE_PROFESSIONAL_WORK_NOT_FOUND_RESOURCE.getRetMsg());
		}
		List<LinkedHashMap<String,Object>> list2 = handoverRecordMapper.selectHandover(archiveid, handoverid);
		return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_SUCCESS.getRetCode()), BaseRetCode.CODE_SUCCESS.getRetMsg(),list2);
	}



	



}
