package com.hust.mining.service.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hust.mining.dao.LabelDao;
import com.hust.mining.model.Label;
import com.hust.mining.service.LabelService;

@Service
public class LabelServiceImpl implements LabelService {
	
	private static final Logger logger=LoggerFactory.getLogger(LabelServiceImpl.class);

	@Autowired
	private LabelDao labeldao;
	
	@Override
	public List<Label> selectAllLable(int start, int limit) {
		List<Label> labels = labeldao.selectLabels(start, limit);
		if (labels.isEmpty()) {
			logger.info("select label is empty");
		}
		return labels;
	}
	@Override
	public long selectLabelCount(String name)
	{
		return labeldao.selectLabelCount(name);
	}
	
	@Override
	public long selectallcount() {
		
		return labeldao.selectallcount();
	}

	@Override
	public List<Label> selectLableLikeName(String labelname,int start,int limit) {
		List<Label> labels = labeldao.selectLabelLikeName(labelname,start,limit);
		if (labels.isEmpty()) {
			logger.info("labelname is not exist");
		}
		return labels;
	}
	
	@Override
	public Label selectByname(String labelname)
	{
		List<Label> labels = labeldao.selectLabelByName(labelname);
		return labels.get(0);
	}
	
	@Override
	public Label selectByID(int labelid)
	{
		return labeldao.selectByPrimaryKey(labelid);
	}

	@Override
	public boolean insertLable(String label) {
		List<Label> labels = labeldao.selectLabelByName(label);
		if (!labels.isEmpty()) {
			logger.info("label is exist");
			return false;
		}
		int status = labeldao.insertLabel(label);
		if (status==0) {
			logger.info("insert label fail");
			return false;
		}
		return true;
	}

	@Override
	public boolean deleteLabelById(int labelid) {
		int status = labeldao.deleteByPrimaryKey(labelid);
		if (status==0) {
			logger.info("delete label fail");
		}
		return true;
	}

	@Override
	public boolean updateLabel(Label label) {
		// 判断要更改的名字是否存在
		List<Label> labels = labeldao.selectLabelByName(label.getLabelname());
		if (!labels.isEmpty()) {
			logger.info("要求修改的标签名已存在");
			return false;
		}
		//判断更新是否成果
		int status = labeldao.updateByPrimaryKeySelective(label);
		//System.out.println("要修改的ID是"+label.getLabelid());
		//System.out.println("要修改的name是"+label.getLabelname());
		if (status==0) {
			logger.info("更新标签失败");
			return false;
		}
		return true;
	}

}
