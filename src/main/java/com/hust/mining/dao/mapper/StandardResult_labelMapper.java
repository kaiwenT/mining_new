package com.hust.mining.dao.mapper;

import com.hust.mining.model.StandardResult_label;
import com.hust.mining.model.StandardResult_labelExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface StandardResult_labelMapper {
    long countByExample(StandardResult_labelExample example);

    int deleteByExample(StandardResult_labelExample example);

    int deleteByPrimaryKey(@Param("stdRid") String stdRid, @Param("labelid") Integer labelid);

    int insert(StandardResult_label record);

    int insertSelective(StandardResult_label record);

    List<StandardResult_label> selectByExample(StandardResult_labelExample example);

    int updateByExampleSelective(@Param("record") StandardResult_label record, @Param("example") StandardResult_labelExample example);

    int updateByExample(@Param("record") StandardResult_label record, @Param("example") StandardResult_labelExample example);
}