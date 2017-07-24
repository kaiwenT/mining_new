package com.hust.mining.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hust.mining.model.SourceType;
import com.hust.mining.model.params.SourceTypeQueryCondition;
import com.hust.mining.model.params.WeightQueryCondition;
import com.hust.mining.service.SourceTypeService;
import com.hust.mining.util.ResultUtil;

@Controller
@RequestMapping("/sourceType")
public class SourceTypeContrller {

	@Autowired
	private SourceTypeService sourceTypeService;

	@ResponseBody
	@RequestMapping(value = "/selectAllSourceType")
	public Object selectAllSourceType(@RequestParam(value = "start", required = true) int start,
			@RequestParam(value = "limit", required = true) int limit) {
		List<SourceType> sourceType = sourceTypeService.selectSourceType(start, limit);
		if (null == sourceType || sourceType.isEmpty()) {
			return ResultUtil.errorWithMsg("sourceType is empty");
		}
		return ResultUtil.success(sourceType);
	}

	@ResponseBody
	@RequestMapping("/selectSourceTypeCount")
	public Object selectWeightCount(@RequestParam(value = "name", required = false) String name) {
		long count = 0;
		if(null == name){
			count = sourceTypeService.selectSourceTypeCount();
		}else{
			count = sourceTypeService.selectSourceTypeCountByName(name);
		}
		if (count <= 0) {
			return ResultUtil.errorWithMsg("未找到相关类型信息！");
		}
		return ResultUtil.success(count);
	}
	
	@ResponseBody
	@RequestMapping(value = "/selectSourceTypeByName")
	public Object selectSourceTypeByName(@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "start", required = true) int start,
			@RequestParam(value = "limit", required = true) int limit) {
		//System.out.println(name);
		SourceTypeQueryCondition sourceType = new SourceTypeQueryCondition();
		sourceType.setName(name);
		sourceType.setStart(start);
		sourceType.setLimit(limit);
		List<SourceType> sourceTypes = sourceTypeService.selectSourceTypeByName(sourceType);
		if (sourceTypes.isEmpty()) {
			return ResultUtil.errorWithMsg("The name is not exist");
		}
		return ResultUtil.success(sourceTypes);
	}

	@ResponseBody
	@RequestMapping(value = "/deleteSourceTypeById")
	public Object deleteSourceTypeById(@RequestParam(value = "id", required = true) int id) {
		int status = sourceTypeService.deleteSourceTypeById(id);
		if (status == 0) {
			return ResultUtil.errorWithMsg("delete sourcetype is error");
		}
		return ResultUtil.success("delete data success");
	}

	@ResponseBody
	@RequestMapping(value = "/insertSourceType")
	public Object insertSourceType(@RequestParam(value = "name", required = true) String name) {
		int status = sourceTypeService.insertSourceType(name);
		if (status == 0) {
			return ResultUtil.errorWithMsg("insert sourceType error");
		}
		return ResultUtil.success("insert data success");
	}

	@ResponseBody
	@RequestMapping(value = "/updateSourceType")
	public Object updateSourceType(@RequestParam(value = "id", required = true) int id,
			@RequestParam(value = "name", required = true) String name) {
		SourceType sourceType = new SourceType();
		sourceType.setId(id);
		sourceType.setName(name);
		int status = sourceTypeService.updateSourceType(sourceType);
		if (status == 0) {
			return ResultUtil.errorWithMsg("update sourceType error");
		}
		return ResultUtil.success("update data success");
	}

}
