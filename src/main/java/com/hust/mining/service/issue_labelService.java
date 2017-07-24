package com.hust.mining.service;

import java.util.List;

import com.hust.mining.model.Label;

public interface issue_labelService {
	
public List<Label> selectLabelsForStandResult(String issueID);

	boolean delete(String issueID, int labelid);

	public boolean attachlabels(String issueID, Integer label);

	List<String[]> countURL(String issueId);

	List<String> selectIssuesBylabel(int labelid); 

}
