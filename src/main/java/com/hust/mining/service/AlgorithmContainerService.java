package com.hust.mining.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hust.mining.model.params.Condition;

public interface AlgorithmContainerService {

	  public List<double[]> Converter(List<String[]> list, int converterType);
	  
	  public List<List<Integer>> Sort(List<String[]> list, List<List<Integer>> resultIndexSetList,int indexOfTitle, int indexOfTime);
	  
	  public String UseKmeans(List<String[]> list, int k,int granularity, HttpServletRequest request);

	  int getContent(Condition con, HttpServletRequest request);
	  
	  public List<String[]> Downloade(HttpServletRequest request);

	  void storeResult(List<String[]> list, List<List<Integer>> result1, String[] attrs, HttpServletRequest request);

	  List<String[]> getResult(HttpServletRequest request);

	 String UseDBScan(List<String[]> list, float Eps, int MinPts, int granularity, HttpServletRequest request);

	 String UseCanopy(List<String[]> list, float Threshold, int granularity, HttpServletRequest request);
}
