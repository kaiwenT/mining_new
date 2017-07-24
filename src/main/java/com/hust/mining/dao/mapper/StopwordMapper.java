package com.hust.mining.dao.mapper;

import com.hust.mining.model.Stopword;
import com.hust.mining.model.StopwordExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface StopwordMapper {
    int countByExample(StopwordExample example);

    int deleteByExample(StopwordExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Stopword record);
    
    int insertBatch(List<Stopword> list);

    int insertSelective(Stopword record);

    List<Stopword> selectByExample(StopwordExample example);

    Stopword selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Stopword record, @Param("example") StopwordExample example);

    int updateByExample(@Param("record") Stopword record, @Param("example") StopwordExample example);

    int updateByPrimaryKeySelective(Stopword record);

    int updateByPrimaryKey(Stopword record);
}