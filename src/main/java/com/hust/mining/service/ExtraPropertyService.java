package com.hust.mining.service;

import java.util.List;

import com.hust.mining.model.ExtraProperty;
import com.hust.mining.model.params.ExtraPropertyQueryCondition;

public interface ExtraPropertyService {
	/**
	 * 添加扩展属性
	 * @param extraProperty
	 * @return 添加成功返回true，失败返回false
	 */
	public boolean addExtraProperty(ExtraProperty extraProperty);
	
	/**
	 * 根据id删除扩展属性
	 * @param id 
	 * @return 删除成功返回true，失败返回false
	 */
	public boolean deleteExtraPropertyById(Integer id);
	
	/**
	 * 更新扩展属性
	 * @param extraProperty
	 * @return 更新成功返回true，失败返回false
	 */
	public boolean updateExtraProperty(ExtraProperty extraProperty);
	
	/**
	 * 按照指定条件获取扩展属性
	 * @param condition 如要分页，请设置start,limit
	 * @return
	 */
	public List<ExtraProperty> getExtraProperty(ExtraPropertyQueryCondition condition);
	
	/**
	 * 查询扩展属性的总数
	 * @return
	 */
	public long getExtraPropertyCount();
}
