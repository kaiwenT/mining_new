package com.hust.mining.service;

import java.util.List;

import org.apache.xmlbeans.impl.xb.xsdschema.Public;

import com.hust.mining.model.Label;

public interface StandardResult_labelService {
	
	public List<Label> selectLabelsForStandResult(String stdResId);
	
	public List<String> selectStandResultsBylabel(int labelid);

	boolean delete(String stdResId, int labelid);

	public boolean attachlabels(String staResId, Integer label); 

	
	
}
