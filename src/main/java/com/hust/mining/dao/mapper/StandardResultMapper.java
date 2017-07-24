package com.hust.mining.dao.mapper;

import com.hust.mining.model.StandardResult;
import com.hust.mining.model.StandardResultExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface StandardResultMapper {
    long countByExample(StandardResultExample example);

    int deleteByExample(StandardResultExample example);

    int deleteByPrimaryKey(String stdRid);

    int insert(StandardResult record);

    int insertSelective(StandardResult record);

    List<StandardResult> selectByExample(StandardResultExample example);

    StandardResult selectByPrimaryKey(String stdRid);

    int updateByExampleSelective(@Param("record") StandardResult record, @Param("example") StandardResultExample example);

    int updateByExample(@Param("record") StandardResult record, @Param("example") StandardResultExample example);

    int updateByPrimaryKeySelective(StandardResult record);

    int updateByPrimaryKey(StandardResult record);
}