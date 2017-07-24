package com.hust.mining.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.record.PageBreakRecord.Break;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hust.mining.dao.issue_labelDao;
import com.hust.mining.model.Label;
import com.hust.mining.model.StandardResult;
import com.hust.mining.model.issue_label;
import com.hust.mining.service.LabelService;
import com.hust.mining.service.StandardResultService;
import com.hust.mining.service.issue_labelService;
import com.hust.mining.urltags.URLTool;
import com.hust.mining.util.AttrUtil;
import com.hust.mining.util.ResultUtil;

@Service
public class issue_labelServiceImpl implements issue_labelService {

	private static final Logger logger = LoggerFactory.getLogger(issue_labelServiceImpl.class); 
	@Autowired
	private issue_labelDao issue_labeldao;
	@Autowired
	private LabelService labelservice;
	@Autowired
    private StandardResultService standardResultService;
	
	/**
	 * 返回的是标签的list
	 */
	@Override
	public List<Label> selectLabelsForStandResult(String issueID) {
		List<issue_label> issue_labels = issue_labeldao.selectLabelsForStandResult(issueID);
		List<Label> labels = new ArrayList<Label>();
		for (issue_label issue_label : issue_labels) {
			Integer integer = issue_label.getLabelid();
			Label label = new Label();
			label = labelservice.selectByID(integer);
			labels.add(label);
		}
		return labels;
	}

	@Override
	public List<String> selectIssuesBylabel(int labelid) {
		List<issue_label> issue_labels = issue_labeldao.selectIssuesBylabel(labelid);
		List<String> list = new ArrayList<String>();
		for (issue_label issue_label : issue_labels) {
			String string = issue_label.getIssueid();
			list.add(string);
		}
		return list;
	}

	@Override
	public boolean delete(String issueID, int labelid) {
		int status = issue_labeldao.delete(issueID, labelid);
		if (status==0) {
			logger.info("为任务删除标签失败！");
			return false;
		}
		return true;
	}

	/**
	 * 为准数据贴标签，参数为任务ID，以及标签ID
	 */
	@Override
	public boolean attachlabels(String issueID, Integer label) {
		issue_label issue_label  = new issue_label();
		issue_label.setIssueid(issueID);
		issue_label.setLabelid(label);
		int status = issue_labeldao.insert(issue_label);
		if (status==0) {
			logger.info("为"+issueID+"任务贴标签失败！");
			return false;
		}
		return true;
	}
	
	@Override
	public List<String[]> countURL(String issueId)
	{
		//根据任务ID找出该任务中所有的文件ID
    	List<StandardResult> stdResList = standardResultService.queryStdRessByIssueId(issueId);
    	if (stdResList.size()==0) {
			return null;
		}
        List<String[]> content = new ArrayList<String[]>(); //存储所有文件的有效内容
        URLTool urlTool = new URLTool();
    	for (int i=0; i < stdResList.size();i++) 
    	{
    		List<String[]> cluster = standardResultService.getStdResContentById(stdResList.get(i).getStdRid());
        	if (!cluster.isEmpty()) {
        		String[] firstline = cluster.get(0);
            	int URL = AttrUtil.findIndexOfUrl(firstline);
            	int name = AttrUtil.findIndexOfWebName(firstline);
            	List<String[]> uRList = new ArrayList<String[]>();
            	for (String[] strings : cluster) {
            		//过滤空行和为空的
            		if (strings!=null && strings.length!=1) {
            			String[] string = new String[2];
            			string[0] = strings[URL];
            			string[1] = strings[name];
            			uRList.add(string);
        			}
        		}
            	if (i!=0) {
					uRList.remove(0); //如果不是第一个文件，就把他们的属性列移除
				}
                content.addAll(uRList);
    		}
		}
    	if (content.size()==0) {
			return null;
		}
    	List<String[]> result = urlTool.statisticUrl(content);
		return result;
		
	}

}
