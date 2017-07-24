package com.hust.mining.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hust.mining.model.Result;
import com.hust.mining.model.params.StatisticParams;

public interface ResultService {

    public String getCurrentResultId(HttpServletRequest request);

    public List<String[]> getCountResultById(String resultId, String issueId, HttpServletRequest request);

    public boolean deleteSets(int[] sets, HttpServletRequest request);

    public boolean combineSets(int[] sets, HttpServletRequest request);

    public boolean reset(HttpServletRequest request);

    Map<String, Object> statistic(StatisticParams params,HttpServletRequest request);

    public int delResultById(String resultId);

    public List<Result> queryResultsByIssueId(String issueId);

    public List<String[]> exportService(String issueId, String resultId,HttpServletRequest request);
    
    public String exportAbstract(List<String[]> count);

	public List<String[]> getClusterResultById( String clusterIndex,String resultId, String issueId, HttpServletRequest request);

	public boolean deleteClusterItems(String clusterIndex, int[] sets, HttpServletRequest request);

	public boolean resetCluster(String index, HttpServletRequest request);

	public List<Integer> getMarked(List<String[]> cluster);
}
