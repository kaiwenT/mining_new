package com.hust.mining.service;

import java.util.List;

import com.hust.mining.model.Label;

public interface LabelService {
	
	public List<Label> selectAllLable(int start, int limit);
	
	public boolean insertLable(String label);
	
	public boolean deleteLabelById(int labelid);
	
	public boolean updateLabel(Label label);

	List<Label> selectLableLikeName(String labelname, int start, int limit);

	long selectLabelCount(String name);

	long selectallcount();

	Label selectByname(String labelname);

	Label selectByID(int labelid);
	

}
