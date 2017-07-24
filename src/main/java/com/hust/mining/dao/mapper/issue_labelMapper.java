package com.hust.mining.dao.mapper;

import com.hust.mining.model.issue_label;
import com.hust.mining.model.issue_labelExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface issue_labelMapper {
	
    long countByExample(issue_labelExample example);

    int deleteByExample(issue_labelExample example);

    int deleteByPrimaryKey(@Param("issueid") String issueid, @Param("labelid") Integer labelid);

    int insert(issue_label record);

    int insertSelective(issue_label record);

    List<issue_label> selectByExample(issue_labelExample example);

    int updateByExampleSelective(@Param("record") issue_label record, @Param("example") issue_labelExample example);

    int updateByExample(@Param("record") issue_label record, @Param("example") issue_labelExample example);
}