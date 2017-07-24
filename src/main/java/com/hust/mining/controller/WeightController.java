package com.hust.mining.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hust.mining.model.Weight;
import com.hust.mining.model.params.WeightQueryCondition;
import com.hust.mining.service.WeightService;
import com.hust.mining.util.ResultUtil;

@Controller
@RequestMapping("/weight")
public class WeightController {

	@Autowired
	private WeightService weightService;

	@ResponseBody
	@RequestMapping("/selectAllWeight")
	public Object selectAllWeight(@RequestParam(value = "start", required = true) int start,
			@RequestParam(value = "limit", required = true) int limit) {
		List<Weight> weight = weightService.selectAllWeight(start, limit);
		if (weight.isEmpty()) {
			return ResultUtil.errorWithMsg("weight is empty");
		}
		return ResultUtil.success(weight);
	}

	@ResponseBody
	@RequestMapping("/selectByCondition")
	public Object selectByCondition(@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "weight", required = true) Integer weight,
			@RequestParam(value = "start", required = true) int start,
			@RequestParam(value = "limit", required = true) int limit) {
		WeightQueryCondition weightInfo = new WeightQueryCondition();
		weightInfo.setName(name);
		weightInfo.setWeight(weight);
		weightInfo.setStart(start);
		weightInfo.setLimit(limit);
		List<Weight> weights = weightService.selectByCondition(weightInfo);
		if (weights.isEmpty()) {
			return ResultUtil.errorWithMsg("未找到相关权重信息！");
		}
		return ResultUtil.success(weights);
	}

	@ResponseBody
	@RequestMapping("/selectWeightCount")
	public Object selectWeightCount(@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "weight", required = false) Integer weight) {
		long count = 0;
		if (null == name && null == weight) {
			count = weightService.selectCount();
		} else {
			WeightQueryCondition weightInfo = new WeightQueryCondition();
			weightInfo.setName(name);
			weightInfo.setWeight(weight);
			count = weightService.selectCountByCondition(weightInfo);
		}
		if (count <= 0) {
			return ResultUtil.errorWithMsg("未找到相关权重信息！");
		}
		return ResultUtil.success(count);
	}

	@ResponseBody
	@RequestMapping("/insertWeight")
	public Object insertWeight(@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "weight", required = true) Integer weight) {
		Weight weightInfo = new Weight();
		weightInfo.setName(name);
		weightInfo.setWeight(weight);
		boolean status = weightService.insertWeight(weightInfo);
		if (status == false) {
			return ResultUtil.errorWithMsg("insert is error(maybe weightName is exist)");
		}
		return ResultUtil.success("insert data success");
	}

	@ResponseBody
	@RequestMapping("/deleteWeight")
	public Object deleteWeight(@RequestParam(value = "weightId", required = true) int weightId) {
		boolean status = weightService.deleteWeightById(weightId);
		if (status == false) {
			return ResultUtil.errorWithMsg("delete is error");
		}
		return ResultUtil.success("delete date success");
	}

	/**
	 * 新闻媒体不能更新成已有的新闻类型
	 * 
	 * @param id
	 * @param name
	 * @param weight
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateWeight")
	public Object updateWeight(@RequestParam(value = "id", required = true) Integer id,
			@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "weight", required = true) Integer weight) {
		Weight weightInfo = new Weight();
		weightInfo.setId(id);
		weightInfo.setName(name);
		weightInfo.setWeight(weight);
		boolean status = weightService.updateWeight(weightInfo);
		if (status == false) {
			return ResultUtil.errorWithMsg("update weightName has exist or update error");
		}
		return ResultUtil.success("update date success");
	}
}
