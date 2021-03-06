package com.xoado.organize.mapper;

import com.xoado.organize.bean.TblAppPackage;
import com.xoado.organize.bean.TblAppPackageExample;
import com.xoado.organize.bean.TblAppPackageKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblAppPackageMapper {
    int countByExample(TblAppPackageExample example);

    int deleteByExample(TblAppPackageExample example);

    int deleteByPrimaryKey(TblAppPackageKey key);

    int insert(TblAppPackage record);

    int insertSelective(TblAppPackage record);

    List<TblAppPackage> selectByExample(TblAppPackageExample example);

    TblAppPackage selectByPrimaryKey(TblAppPackageKey key);

    int updateByExampleSelective(@Param("record") TblAppPackage record, @Param("example") TblAppPackageExample example);

    int updateByExample(@Param("record") TblAppPackage record, @Param("example") TblAppPackageExample example);

    int updateByPrimaryKeySelective(TblAppPackage record);

    int updateByPrimaryKey(TblAppPackage record);
}