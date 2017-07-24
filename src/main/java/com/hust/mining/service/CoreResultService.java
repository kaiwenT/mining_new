package com.hust.mining.service;

import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hust.mining.model.CoreResult;
import com.hust.mining.model.params.CoreResultQueryCondition;

public interface CoreResultService {
	int insert(CoreResultQueryCondition con, HttpServletRequest request);

	String insertCore(CoreResultQueryCondition con,String contentName, HttpServletRequest request);
	
	int update(CoreResultQueryCondition con, HttpServletRequest request);
	
    int deleteById(String coreResId);
    
    CoreResult queryCoreResById(String coreResId);
    
    boolean export(String coreResId,OutputStream outputStream);
    
    List<CoreResult> queryCoreRessByIssueId(String issueId);
}
