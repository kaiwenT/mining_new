package com.hust.mining.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hust.mining.dao.mapper.LabelMapper;
import com.hust.mining.model.Label;
import com.hust.mining.model.LabelExample;
import com.hust.mining.model.Role;
import com.hust.mining.model.RoleExample;
import com.hust.mining.model.LabelExample.Criteria;


@Repository
public class LabelDao {
	
	@Autowired
	private LabelMapper labelmapper;
	
	//统计有多少个label
	public long selectallcount() {
		LabelExample example = new LabelExample();
		Criteria criteria = example.createCriteria();
		criteria.andLabelidIsNotNull();
		criteria.andLabelnameIsNotNull();
		return labelmapper.countByExample(example);
	}
	
	//统计与查找内容相似的数据的条数
	public long selectLabelCount(String name)
	{
		LabelExample example = new LabelExample();
		Criteria criteria = example.createCriteria();
		if (!StringUtils.isBlank(name)) 
		{
			//System.out.println("进入selectLabelCount方法");
			//System.out.println("待查找的是"+name);
			criteria.andLabelnameLike("%"+name+"%");
		}
		example.setStart(0);
        example.setLimit(0);
        //System.out.println("相似的条数为："+labelmapper.countByExample(example));
		return labelmapper.countByExample(example);
	}
	
	//分页查找
	public List<Label> selectLabels(int start,int limit)
	{
		LabelExample example = new LabelExample();
		Criteria criteria = example.createCriteria();
		criteria.andLabelidIsNotNull();
		example.setLimit(limit);
		example.setStart(start);
		List<Label> labels = labelmapper.selectByExample(example);
		return labels;
	}
	
	public List<Label> selectByExample(LabelExample example) {
		return labelmapper.selectByExample(example);
	}

	public Label selectByPrimaryKey(int LabelId) {
		return labelmapper.selectByPrimaryKey(LabelId);
	}
	//找相同名字的
	public List<Label> selectLabelByName(String labelName) {
		LabelExample example = new LabelExample();
		Criteria criteria = example.createCriteria();
		criteria.andLabelnameEqualTo(labelName);
		List<Label> labels = labelmapper.selectByExample(example);
		return labels;
	}
	
	//找类似的
	public List<Label> selectLabelLikeName(String labelName,int start,int limit) {
		LabelExample example = new LabelExample();
		Criteria criteria = example.createCriteria();
		criteria.andLabelnameLike("%"+labelName+"%");
		example.setLimit(limit);
		example.setStart(start);
		List<Label> labels = labelmapper.selectByExample(example);
		return labels;
	}

	//删除
	public int deleteByExample(LabelExample example) {
		return labelmapper.deleteByExample(example);
	}
	
	//根据id删除
	public int deleteByPrimaryKey(Integer labelid) {
		return labelmapper.deleteByPrimaryKey(labelid);
	}
	//添加
	public int insert(Label record) {
		return labelmapper.insert(record);
	}
	//根据名字添加
	public int insertLabel(String labelName) {
		Label label = new Label();
		label.setLabelname(labelName);
		return labelmapper.insert(label);
	}
	
	public int insertSelective(Label record) {
		return labelmapper.insertSelective(record);
	}
	
	public int updateByExampleSelective(Label record,LabelExample example)
	{
		return labelmapper.updateByExampleSelective(record, example);
	}
	
	public int updateByExample(Label record, LabelExample example)
	{
		return labelmapper.updateByExample(record, example);
	}
	
	public int updateByPrimaryKey(Label record)
	{
		return labelmapper.updateByPrimaryKey(record);
	}
	
	public int updateByPrimaryKeySelective(Label record)
	{
		return labelmapper.updateByPrimaryKeySelective(record);
	}
	
	

}
