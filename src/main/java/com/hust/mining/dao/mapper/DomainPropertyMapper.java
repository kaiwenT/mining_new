package com.hust.mining.dao.mapper;

import com.hust.mining.model.DomainProperty;
import com.hust.mining.model.DomainPropertyExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DomainPropertyMapper {
    long countByExample(DomainPropertyExample example);

    int deleteByExample(DomainPropertyExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(DomainProperty record);

    int insertBatch(List<DomainProperty> list);
    
    int insertIgnore(List<DomainProperty> list);
    
    int insertSelective(DomainProperty record);

    List<DomainProperty> selectByExample(DomainPropertyExample example);

    DomainProperty selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") DomainProperty record, @Param("example") DomainPropertyExample example);

    int updateByExample(@Param("record") DomainProperty record, @Param("example") DomainPropertyExample example);

    int updateByPrimaryKeySelective(DomainProperty record);

    int updateByPrimaryKey(DomainProperty record);
}