package com.xoado.FilingCabinet.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xoado.FilingCabinet.bean.TblArchives;
import com.xoado.FilingCabinet.mapper.TblArchivesMapper;
import com.xoado.FilingCabinet.service.IXDfileUpload;
import com.xoado.common.XoadoFileUpload;
import com.xoado.common.XoadoResult;
import com.xoado.protocol.BaseRetCode;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Service
public class XDfileUpload implements IXDfileUpload{
	
	@Autowired
	private TblArchivesMapper tblArchivesMapper;

	@Override
	public XoadoResult uploadBase64(String archiveid, String base64text) throws Exception {
		 TblArchives tblArchives = tblArchivesMapper.selectByPrimaryarchiveid(archiveid);
		 System.out.println("tblArchives:"+tblArchives);
		 Object attachmentPicture = tblArchives.getAttachmentPicture();
		 JSONArray attachmentPictureJson = JSONArray.fromObject(attachmentPicture);
		 if(base64text==null){
//			 CODE_PROFESSIONAL_WORK_PARAMETER_NOT_LIKE   未传参
				return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_PROFESSIONAL_WORK_PARAMETER_NOT_LIKE.getRetCode()), BaseRetCode.CODE_PROFESSIONAL_WORK_PARAMETER_NOT_LIKE.getRetMsg());
			}
		 JSONArray jsonArray = JSONArray.fromObject(base64text);
		 for (int i = 0; i < jsonArray.size(); i++) {
				Object jsonobject = jsonArray.get(i);
				JSONObject fromObject = JSONObject.fromObject(jsonobject);
				String imageName =(String) fromObject.get("imageName");
				String base64 =(String) fromObject.get("base64text");
				Map<Object, Object> map = XoadoFileUpload.base64upload(imageName, base64);
				JSONObject json = new JSONObject();
				
				json.put("imageName", map.get("imageName"));
				json.put("imageUrl", map.get("imageUrl"));
				json.put("imageZipName", map.get("imageZipName"));
				json.put("imageZipUrl", map.get("imageZipUrl"));
				 attachmentPictureJson.add(json.toString());
			}
		
		   tblArchives.setAttachmentPicture(attachmentPictureJson.toString());
		   System.out.println("tblArchives:"+tblArchives);
		 	tblArchivesMapper.updateByPrimaryKey(tblArchives);
			return XoadoResult.build(Integer.parseInt(BaseRetCode.CODE_SUCCESS.getRetCode()), BaseRetCode.CODE_SUCCESS.getRetMsg());
	}

}
