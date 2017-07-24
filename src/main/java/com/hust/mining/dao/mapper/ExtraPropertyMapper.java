package com.hust.mining.dao.mapper;

import com.hust.mining.model.ExtraProperty;
import com.hust.mining.model.ExtraPropertyExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ExtraPropertyMapper {
    long countByExample(ExtraPropertyExample example);

    int deleteByExample(ExtraPropertyExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ExtraProperty record);
    
    int insertBatch(List<ExtraProperty> list);

    int insertSelective(ExtraProperty record);

    List<ExtraProperty> selectByExample(ExtraPropertyExample example);

    ExtraProperty selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ExtraProperty record, @Param("example") ExtraPropertyExample example);

    int updateByExample(@Param("record") ExtraProperty record, @Param("example") ExtraPropertyExample example);

    int updateByPrimaryKeySelective(ExtraProperty record);

    int updateByPrimaryKey(ExtraProperty record);
}