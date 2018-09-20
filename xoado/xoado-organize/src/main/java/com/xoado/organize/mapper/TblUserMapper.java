package com.xoado.organize.mapper;

import com.xoado.organize.bean.TblUser;
import com.xoado.organize.bean.TblUserExample;

import java.util.LinkedHashMap;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblUserMapper {
    int countByExample(TblUserExample example);

    int deleteByExample(TblUserExample example);

    int deleteByPrimaryKey(String userid);

    int insert(TblUser record);

    int insertSelective(TblUser record);

    List<TblUser> selectByExample(TblUserExample example);
    
//    自定义查询方法
    List<LinkedHashMap<String, Object>> selectTheCurrentUser(String sql);

    TblUser selectByPrimaryKey(String userid);

    int updateByExampleSelective(@Param("record") TblUser record, @Param("example") TblUserExample example);

    int updateByExample(@Param("record") TblUser record, @Param("example") TblUserExample example);

    int updateByPrimaryKeySelective(TblUser record);

    int updateByPrimaryKey(TblUser record);
    
    List<TblUser> selectAll();
}