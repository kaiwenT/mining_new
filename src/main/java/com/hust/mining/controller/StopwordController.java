package com.hust.mining.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hust.mining.model.Stopword;
import com.hust.mining.service.StopwordService;
import com.hust.mining.util.ResultUtil;

@Controller
@RequestMapping("/stopword")
public class StopwordController {
	@Autowired
	private StopwordService stopwordService;

	@ResponseBody
	@RequestMapping(value = "/selectAllStopword", method = RequestMethod.POST)
	public Object selectAllStopword(@RequestParam(value = "start", required = true) int start,
			@RequestParam(value = "limit", required = true) int limit, HttpServletRequest request) {
		List<Stopword> list = new ArrayList<Stopword>();
		list = stopwordService.selectAllStopwordInfor(start, limit);
		if (null == list || 0 == list.size()) {
			return ResultUtil.errorWithMsg("select stopword empty");
		}
		return ResultUtil.success(list);
	}

	@ResponseBody
	@RequestMapping("/selectByCondition")
	public Object selectByCondition(@RequestParam(value = "word", required = true) String word,
			@RequestParam(value = "start", required = true) int start,
			@RequestParam(value = "limit", required = true) int limit, HttpServletRequest request) {
		List<Stopword> list = stopwordService.selectStopwordInforByWord(word, start, limit);
		if (null == list || 0 == list.size()) {
			return ResultUtil.errorWithMsg("无相关停用词！");
		}
		return ResultUtil.success(list);
	}
	@ResponseBody
	@RequestMapping("/selectStopwordCount")
	public Object selectStopwordCount(@RequestParam(value = "word",required = false)  String word){
		long count = 0;
		if(null == word){
			count = stopwordService.selectCount();
		}else{
			count = stopwordService.selectCountWord(word);
		}
		if(count <= 0){
			return ResultUtil.errorWithMsg("无停用词");
		}
		return ResultUtil.success(count);
	}

	@ResponseBody
	@RequestMapping("deleteStopwordById")
	public Object deleteStopwordById(@RequestParam(value = "stopwordId", required = true) Integer id,
			HttpServletRequest request) {
		if (!stopwordService.delStopwordById(id)) {
			return ResultUtil.errorWithMsg("delete stopword error");
		}
		return ResultUtil.success("delete stopword success");
	}

	@ResponseBody
	@RequestMapping("insertStopwords")
	public Object insertStopwords(@RequestParam(value = "words", required = true) String[] words,
			HttpServletRequest request) {
		List<Stopword> list = new ArrayList<Stopword>();
		String creator = stopwordService.getCurrentUser(request);

		for (String word : words) {
			if ("" == word) {
				continue;
			}
			Stopword stopword = new Stopword();
			stopword.setCreateTime(new Date());
			stopword.setCreator(creator);
			stopword.setWord(word);
			list.add(stopword);
		}
		if (!stopwordService.insertStopwords(list)) {
			return ResultUtil.errorWithMsg("插入停用词失败，可能是该停用词已存在！");
		}
		return ResultUtil.success("insert stopword success");
	}
}
