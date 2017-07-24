package com.hust.mining.dao.mapper;

import com.hust.mining.model.DomainTwo;
import com.hust.mining.model.DomainTwoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DomainTwoMapper {
    long countByExample(DomainTwoExample example);

    int deleteByExample(DomainTwoExample example);

    int deleteByPrimaryKey(String uuid);

    int insert(DomainTwo record);
    
    int insertBatch(List<DomainTwo> list);
    
    int insertIgnore(List<DomainTwo> list);

    int insertSelective(DomainTwo record);

    List<DomainTwo> selectByExample(DomainTwoExample example);

    DomainTwo selectByPrimaryKey(String uuid);

    int updateByExampleSelective(@Param("record") DomainTwo record, @Param("example") DomainTwoExample example);

    int updateByExample(@Param("record") DomainTwo record, @Param("example") DomainTwoExample example);

    int updateByPrimaryKeySelective(DomainTwo record);

    int updateByPrimaryKey(DomainTwo record);
}