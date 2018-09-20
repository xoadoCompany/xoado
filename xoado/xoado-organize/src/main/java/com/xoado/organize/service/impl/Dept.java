package com.xoado.organize.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.xoado.common.JsonUtils;
import com.xoado.common.MD5;
import com.xoado.common.XoadoResult;
import com.xoado.organize.bean.TblOrganizeDept;
import com.xoado.organize.bean.TblOrganizeDeptExample;
import com.xoado.organize.bean.TblOrganizeDeptExample.Criteria;
import com.xoado.organize.bean.TblOrganizeMember;
import com.xoado.organize.bean.TblOrganizeMemberExample;
import com.xoado.organize.mapper.TblOrganizeDeptMapper;

import com.xoado.organize.mapper.TblOrganizeMemberMapper;
import com.xoado.organize.service.IDept;
import com.xoado.protocol.BaseRetCode;

import net.sf.json.JSONObject;

@Service
public class Dept implements IDept{
	@Autowired
	private TblOrganizeDeptMapper tblOrganizeDeptMapper;
	@Autowired
	private TblOrganizeMemberMapper tblOrganizeMemberMapper;

	
	
	
	@Override
	public XoadoResult id_dept_all(String organizeId,HttpServletRequest request) {
		TblOrganizeDeptExample example = new TblOrganizeDeptExample();
		Criteria criteria = example.createCriteria();
		criteria.andOrganizeidEqualTo(organizeId);
		List<TblOrganizeDept> list = tblOrganizeDeptMapper.selectByExample(example);
		return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_SUCCESS.getRetCode()), BaseRetCode.CODE_SUCCESS.getRetMsg(),list);
	}
	
	@Override
	public XoadoResult id_deptDetails(String deptId,HttpServletRequest request) {
		TblOrganizeDept dept = tblOrganizeDeptMapper.selectByPrimaryKey(deptId);
		return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_SUCCESS.getRetCode()), BaseRetCode.CODE_SUCCESS.getRetMsg(),dept);
	}
	
	@Override
	public XoadoResult dele_id_dept(String deptId,HttpServletRequest request) {
		TblOrganizeDept primaryKey = tblOrganizeDeptMapper.selectByPrimaryKey(deptId);
		if(primaryKey != null){
			tblOrganizeDeptMapper.deleteByPrimaryKey(deptId);
			return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_SUCCESS.getRetCode()), BaseRetCode.CODE_SUCCESS.getRetMsg());
		}
		return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_PROFESSIONAL_WORK_NOT_FOUND_RESOURCE.getRetCode()), BaseRetCode.CODE_PROFESSIONAL_WORK_NOT_FOUND_RESOURCE.getRetMsg());
		
	}
	
	@Override
	public XoadoResult id_dept_users(String deptId,HttpServletRequest request) {
		TblOrganizeDept dept = tblOrganizeDeptMapper.selectByPrimaryKey(deptId);
		Object member = dept.getMember();
		return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_SUCCESS.getRetCode()), BaseRetCode.CODE_SUCCESS.getRetMsg(),member);
	}
	
	@Override
	public XoadoResult id_dept(String organizeId,String deptName,String managerid,Object member,String createrid, HttpServletRequest request) {
		
		TblOrganizeDept dept = new TblOrganizeDept();
		dept.setOrganizeid(organizeId);
		dept.setDeptid(MD5.MD5Encode(UUID.randomUUID().toString()));
		dept.setDeptName(deptName);
		dept.setManagerid(managerid);
		dept.setMember(member);
		dept.setCreaterid(createrid);
		dept.setCreateTime(new Date());
		tblOrganizeDeptMapper.insert(dept);
		
		return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_SUCCESS.getRetCode()), BaseRetCode.CODE_SUCCESS.getRetMsg());
	}
	
	@Override
	public XoadoResult update_dept(String deptId,String deptName, Object member,HttpServletRequest request) {
		TblOrganizeDept dept = tblOrganizeDeptMapper.selectByPrimaryKey(deptId);
		dept.setDeptName(deptName);
		dept.setMember(member);
		tblOrganizeDeptMapper.updateByPrimaryKey(dept);
		return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_SUCCESS.getRetCode()), BaseRetCode.CODE_SUCCESS.getRetMsg());
	}
	
	@Override
	public XoadoResult update_dapt_managerId(String deptId, String managerid,HttpServletRequest request) {
		TblOrganizeDept dept = tblOrganizeDeptMapper.selectByPrimaryKey(deptId);
		dept.setManagerid(managerid);
		tblOrganizeDeptMapper.updateByPrimaryKey(dept);
		return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_SUCCESS.getRetCode()), BaseRetCode.CODE_SUCCESS.getRetMsg());
	}
	
	/**
	 * 根据部门id给指定部门添加人员
	 */
	@Override
	public XoadoResult dept_member_add(String deptId,Object member, HttpServletRequest request) {
		TblOrganizeDept dept = tblOrganizeDeptMapper.selectByPrimaryKey(deptId);
		System.out.println("member:"+member);
//		获取部门信息
//			获取部门所有成员
			Object user = dept.getMember();
			System.out.println(user);
//			将json转成String格式
			String string = JsonUtils.objectToJson(user);
			
//			将用户的信息转换成String
			String stringmember = JsonUtils.objectToJson(member);
//			将已转换的String的双引号去掉
			char fir = string.charAt(1);
			String substring= string.substring(1, string.length()-1);
			if(fir=='['){
			substring = substring.substring(1, substring.length()-1);
			}
			String substring2 = stringmember.substring(1, stringmember.length()-1);
			
			
//			将客户端传来的进行拼接
			String newuser = "\""+"["+substring+","+substring2+"]"+"\"";
//			System.out.println("--------------"+substring);
//			System.out.println("--------------"+substring2);
			
//			拼接完成转成json数组
			Object parse = JSON.parse(newuser);
//			System.out.println("---------"+parse);
//			存入实体类
			dept.setMember(parse);
//			更新
			tblOrganizeDeptMapper.updateByPrimaryKey(dept);
			return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_SUCCESS.getRetCode()), BaseRetCode.CODE_SUCCESS.getRetMsg());
	}
	
	public XoadoResult test(){
		return null;
		/*
		String deptId = "999999";
		TblOrganizeDept dept = tblOrganizeDeptMapper.selectByPrimaryKey(deptId);
		Object user = dept.getMember();
		Map<String, Object> map1 = JSON.parseObject((String) user);  
		List<String> a = (List) map1.get("userid");
//		a.remove("123");
		a.add("18292967656");
		String jsonString = JSON.toJSONString(map1);
		dept.setMember(jsonString);
		tblOrganizeDeptMapper.updateByPrimaryKey(dept);
		return XoadoResult.build(200, "",user);
		*/
	}
//	27.	[put] base/organizations/depts/members   成员换部门,角色不变
	@Override
	public XoadoResult update_id_dept(String YdeptId, String XdeptId, String userId, HttpServletRequest request) {
		TblOrganizeDept tblOrganizeDept = tblOrganizeDeptMapper.selectByPrimaryKey(YdeptId);
		Object member = tblOrganizeDept.getMember();
		System.out.println(member);
	
//		Map<String,Object> map=JSON.parseObject((String)member);
		net.sf.json.JSONArray array = net.sf.json.JSONArray.fromObject(member);
		Map<String,Object> map = new HashMap<String,Object>();
		net.sf.json.JSONArray jsonArray = new net.sf.json.JSONArray();
		for(int i=0;i<array.size();i++){
			JSONObject fromObject = JSONObject.fromObject(array.get(i));		
			map = JSON.parseObject(fromObject.toString());
			if(map.get("userid").equals(userId)){
				map.remove("userid");
				continue;
			}
			jsonArray.add(map);
		}
		Object jsonString = JSONArray.toJSONString(jsonArray);
		tblOrganizeDept.setMember(jsonString);
		tblOrganizeDeptMapper.updateByPrimaryKey(tblOrganizeDept);
		
		
		TblOrganizeDept newtblOrganizeDept = tblOrganizeDeptMapper.selectByPrimaryKey(XdeptId);
		Object newmember = newtblOrganizeDept.getMember();
		net.sf.json.JSONArray array2 = net.sf.json.JSONArray.fromObject(newmember);
		Map<String,Object> map2 = new HashMap<String,Object>();
		net.sf.json.JSONArray jsonArray2 = new net.sf.json.JSONArray();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("userid", userId);
		for(int i=0;i<array2.size();i++){
			JSONObject fromObject2 = JSONObject.fromObject(array2.get(i));	
			map2 = JSON.parseObject(fromObject2.toString());
			jsonArray2.add(map2);
		}
		jsonArray2.add(jsonObject);
		Object jsonString2 = JSONArray.toJSONString(jsonArray2);
		newtblOrganizeDept.setMember(jsonString2);
		tblOrganizeDeptMapper.updateByPrimaryKey(newtblOrganizeDept);
//		Map<String,Object> map2 = new HashMap<String,Object>();
//		net.sf.json.JSONArray jsonArray2 = new net.sf.json.JSONArray();
//		map2.put("userid", userId);
//		jsonArray2.add(map2);
//		System.out.println("----jsonArray2----------");
//		System.out.println(jsonArray2.toString());
		
//		System.out.println(list2.get(1));
//		System.out.println(list2.toString());
//		@SuppressWarnings({ "unchecked", "rawtypes" })
//		List<String> list =(List) map.get("userid");
//		list2.remove(userId);
		
//		String jsonString = JSON.toJSONString(map);
//		tblOrganizeDept.setMember(jsonString);
//		tblOrganizeDeptMapper.updateByPrimaryKey(tblOrganizeDept);
//		
//		
//		TblOrganizeDept newtblOrganizeDept = tblOrganizeDeptMapper.selectByPrimaryKey(XdeptId);
//		Object newmember = tblOrganizeDept.getMember();
//		@SuppressWarnings("unused")
//		Map<String,Object> newmap = JSON.parseObject((String)newmember);
//		@SuppressWarnings({ "unchecked", "unused", "rawtypes" })
//		List<String> newlist =(List) map.get("userid");
//		list.add(userId);
//		String newjsonString = JSON.toJSONString(map);
//		newtblOrganizeDept.setMember(newjsonString);
//		tblOrganizeDeptMapper.updateByPrimaryKey(newtblOrganizeDept);
		return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_SUCCESS.getRetCode()), BaseRetCode.CODE_SUCCESS.getRetMsg());
	}
//	增加组织成员
	@Override
	public XoadoResult orgAddmember(String organizeId, String userId, String role, Long parttimejob, Date joinTime) {
		TblOrganizeMember member = new TblOrganizeMember();
		member.setOrganizeid(organizeId);
		member.setUserid(userId);
		member.setRole(role);
		member.setParttimejob(parttimejob);
		member.setJoinTime(new Date());
		System.out.println("---------------------");
		try {
			tblOrganizeMemberMapper.insert(member);
		} catch (Exception e) {
			// TODO: handle exception
//			若userId已存在  则添加失败
			return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_FRAME_RESOURCE_IMPLEMETHTATION_ERRED.getRetCode()), BaseRetCode.CODE_FRAME_RESOURCE_IMPLEMETHTATION_ERRED.getRetMsg());
		}
		return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_SUCCESS.getRetCode()), BaseRetCode.CODE_SUCCESS.getRetMsg());
	}
//	30.	[delete] base/organizations/{id}/role        组织成员撤销管理角色
	@Override
	public XoadoResult update_org_role(String userId, String role, HttpServletRequest request) {
		TblOrganizeMemberExample example = new TblOrganizeMemberExample();
		com.xoado.organize.bean.TblOrganizeMemberExample.Criteria criteria = example.createCriteria();
		criteria.andUseridEqualTo(userId);
		List<TblOrganizeMember> list = tblOrganizeMemberMapper.selectByExample(example);
		
		for (TblOrganizeMember tblOrganizeMember : list) {
			tblOrganizeMember.setRole(role);
			tblOrganizeMemberMapper.updateByPrimaryKey(tblOrganizeMember);
		}
		return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_SUCCESS.getRetCode()), BaseRetCode.CODE_SUCCESS.getRetMsg());
	}

	

	


}
