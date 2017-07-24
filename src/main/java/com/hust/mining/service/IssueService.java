package com.hust.mining.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hust.mining.model.Issue;
import com.hust.mining.model.params.IssueQueryCondition;

public interface IssueService {

	/**
	 * 创建任务
	 * @param issueName 任务名
	 * @param issueType 任务类型
	 * @param request 请求，用来获取redis中存储的信息
	 * @return
	 */
    int createIssue(String issueName, String issueType, HttpServletRequest request);
   
    int createIssueWithLink(String linkedIssueId, String issueType, String stdResId, HttpServletRequest request);
   
    String createCoreIssue(String linkedIssueId, String stdResId, HttpServletRequest request);

    int deleteIssueById(String issueId,String issueType,HttpServletRequest request);

    int deleteIssueAllById(String issueId, HttpServletRequest request);
    
    String getCurrentIssueId(HttpServletRequest request);

    List<Issue> queryIssue(IssueQueryCondition con);
    
    long queryIssueCount(IssueQueryCondition con);

    Issue queryIssueById(String issueId);
    
    String queryLinkedIssue(String issueId, String issueType);

    int updateIssueInfo(Issue issue,HttpServletRequest request);

    List<String[]> miningByTime(Date start, Date end,HttpServletRequest request);

    /**
     * 根据文件id选择文件聚类，包含了去重部分
     * @param fileIds 文件id
     * @param request 用来读取redis里面的内容
     * @return
     */
    List<String[]> miningByFileIds(List<String> fileIds, HttpServletRequest request);

}
