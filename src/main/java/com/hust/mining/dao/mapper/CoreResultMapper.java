package com.hust.mining.dao.mapper;

import com.hust.mining.model.CoreResult;
import com.hust.mining.model.CoreResultExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CoreResultMapper {
    long countByExample(CoreResultExample example);

    int deleteByExample(CoreResultExample example);

    int deleteByPrimaryKey(String coreRid);

    int insert(CoreResult record);

    int insertSelective(CoreResult record);

    List<CoreResult> selectByExample(CoreResultExample example);

    CoreResult selectByPrimaryKey(String coreRid);

    int updateByExampleSelective(@Param("record") CoreResult record, @Param("example") CoreResultExample example);

    int updateByExample(@Param("record") CoreResult record, @Param("example") CoreResultExample example);

    int updateByPrimaryKeySelective(CoreResult record);

    int updateByPrimaryKey(CoreResult record);
}