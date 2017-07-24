package com.hust.mining.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hust.mining.model.Domain;
import com.hust.mining.model.DomainOne;
import com.hust.mining.model.DomainOneProperty;
import com.hust.mining.model.DomainTwo;
import com.hust.mining.model.DomainTwoProperty;
import com.hust.mining.model.params.DomainOneQueryCondition;
import com.hust.mining.model.params.DomainTwoQueryCondition;
import com.hust.mining.service.DomainService;
import com.hust.mining.util.ResultUtil;
import com.hust.mining.util.UrlUtils;

import net.sf.json.JSONObject;

@RequestMapping("/domain")
public class DomainController {
	private static final Logger logger = LoggerFactory.getLogger(DomainController.class);
	@Autowired
	private DomainService domainService;

	/**
	 * 分页查询所有一级域名一级对应的二级域名
	 * 
	 * @param start
	 * @param limit
	 * @return List<一级域名> <-----> List<List<二级域名>>
	 */
	@ResponseBody
	@RequestMapping(value = "/selectDomain", method = RequestMethod.POST)
	public Object selectDomain(@RequestParam(value = "start", required = true) int start,
			@RequestParam(value = "limit", required = true) int limit, HttpServletRequest request) {
		DomainOneQueryCondition condition = new DomainOneQueryCondition();
		condition.setStart(start);
		condition.setLimit(limit);
		List<DomainOne> one = domainService.getDomainOne(condition);
		if (one == null) {
			return ResultUtil.errorWithMsg("没有域名被找到！");
		}
		List<List<DomainTwo>> two = domainService.getDomainTwoByOne(one);

		JSONObject json = new JSONObject();
		json.put("one", one);
		json.put("two", two);
		return ResultUtil.success(json);
	}

	@ResponseBody
	@RequestMapping(value = "/selectDomainCount", method = RequestMethod.POST)
	public Object SelectDomainCount(HttpServletRequest request) {
		Long count = domainService.getDomainOneCount();
		if (null == count) {
			return ResultUtil.errorWithMsg("分页失败！");
		} else {
			return ResultUtil.success(count);
		}
	}

	/**
	 * 根据一级域名id查询一级域名以及其对应的二级域名信息
	 * 
	 * @param uuid
	 *            一级域名uuid
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/selectDomainOneById")
	public Object selectDomainOneById(@RequestParam(value = "uuid", required = true) String uuid,
			HttpServletRequest request) {
		DomainOne one = domainService.getDomainOneById(uuid);
		if (null == one) {
			return ResultUtil.errorWithMsg("未找到相关域名信息！");
		}
		DomainTwoQueryCondition condition = new DomainTwoQueryCondition();
		condition.setFatherId(uuid);
		List<DomainTwo> two = domainService.getDomainTwo(condition);
		JSONObject json = new JSONObject();
		json.put("one", one);
		json.put("two", two);
		return ResultUtil.success(json);
	}

	/**
	 * 添加域名信息，
	 * 
	 * @param url
	 *            url
	 * @param name
	 *            名称
	 * @param column
	 *            栏目
	 * @param type
	 *            类型
	 * @param rank
	 *            等级
	 * @param incidence
	 *            影响范围
	 * @param weight
	 *            权重
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/addDomain")
	public Object addDomain(@RequestParam(value = "url", required = true) String url,
			@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "column", required = true) String column,
			@RequestParam(value = "type", required = true) String type,
			@RequestParam(value = "rank", required = true) String rank,
			@RequestParam(value = "incidence", required = true) String incidence,
			@RequestParam(value = "weight", required = true) Integer weight, HttpServletRequest request) {
		if (StringUtils.isBlank(url)) {
			url = null;
		}
		if (StringUtils.isBlank(name)) {
			name = null;
		}
		if (StringUtils.isBlank(column)) {
			column = null;
		}
		if (StringUtils.isBlank(type)) {
			type = null;
		}
		if (StringUtils.isBlank(rank)) {
			rank = null;
		}
		if (StringUtils.isBlank(incidence)) {
			incidence = null;
		}
		Domain domain = new Domain();
		domain.setUrl(url);
		domain.setName(name);
		domain.setColumn(column);
		domain.setType(type);
		domain.setRank(rank);
		domain.setIncidence(incidence);
		domain.setWeight(weight);
		if (domainService.addDomain(domain))
			return ResultUtil.success("添加成功！");
		return ResultUtil.errorWithMsg("添加失败！");
	}

	/**
	 * 根据二级域名id查询二级域名以及其对应的一级域名信息
	 * 
	 * @param uuid
	 *            二级域名uuid
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/selectDomainTwoById")
	public Object selectDomainTwoById(@RequestParam(value = "uuid", required = true) String uuid,
			HttpServletRequest request) {
		DomainTwo two = domainService.getDomainTwoById(uuid);
		if (null == two) {
			return ResultUtil.errorWithMsg("未找到相关域名信息！");
		}
		DomainOne one = domainService.getDomainOneById(two.getFatherUuid());
		JSONObject json = new JSONObject();
		json.put("one", one);
		json.put("two", two);
		return ResultUtil.success(json);
	}

	/**
	 * 根据输入条件搜索满足条件的一级域名
	 * 
	 * @param url
	 * @param name
	 * @param column
	 * @param type
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/searchDomainOne")
	public Object searchDomainOne(@RequestParam(value = "url", required = true) String url,
			@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "rank", required = true) String rank,
			@RequestParam(value = "weight", required = true) String weight,
			@RequestParam(value = "start", required = true) int start,
			@RequestParam(value = "limit", required = true) int limit, HttpServletRequest request) {
		DomainOneQueryCondition condition = new DomainOneQueryCondition();
		if (StringUtils.isNotBlank(url)){
			url = UrlUtils.getDomainOne(UrlUtils.getUrl(url));
			condition.setUrl(url);
		}
		if (StringUtils.isNotBlank(name))
			condition.setName(name);
		if (StringUtils.isNotBlank(rank))
			condition.setRank(rank);
		if (StringUtils.isNotBlank(weight))
			condition.setWeight(Integer.parseInt(weight));
		condition.setStart(start);
		condition.setLimit(limit);
		List<DomainOne> list = domainService.getDomainOne(condition);
		if (null == list || list.size() == 0)
			return ResultUtil.errorWithMsg("无相关域名信息！");
		List<List<DomainTwo>> two = domainService.getDomainTwoByOne(list);
		JSONObject json = new JSONObject();
		json.put("one", list);
		json.put("two", two);
		return ResultUtil.success(json);
	}

	/**
	 * 根据输入条件搜索满足条件的一级域名的个数
	 * 
	 * @param url
	 * @param name
	 * @param column
	 * @param type
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/searchDomainOneCount")
	public Object searchDomainOneCount(@RequestParam(value = "url", required = true) String url,
			@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "rank", required = true) String rank,
			@RequestParam(value = "weight", required = true) String weight, HttpServletRequest request) {
		DomainOneQueryCondition condition = new DomainOneQueryCondition();
		if (StringUtils.isNotBlank(url))
			condition.setUrl(url);
		if (StringUtils.isNotBlank(name))
			condition.setName(name);
		if (StringUtils.isNotBlank(rank))
			condition.setRank(rank);
		if (StringUtils.isNotBlank(weight))
			condition.setWeight(Integer.parseInt(weight));
		condition.setStart(0);
		condition.setLimit(0);
		List<DomainOne> list = domainService.getDomainOne(condition);
		if (null == list || list.size() == 0)
			return ResultUtil.errorWithMsg("无相关域名信息！");
		return ResultUtil.success(list.size());
	}

	/**
	 * 根据输入条件搜索满足条件的一级域名
	 * 
	 * @param url
	 * @param name
	 * @param column
	 * @param type
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/searchDomainTwo")
	public Object searchDomainTwo(@RequestParam(value = "url", required = true) String url,
			@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "rank", required = true) String rank,
			@RequestParam(value = "weight", required = true) Integer weight,
			@RequestParam(value = "start", required = true) int start,
			@RequestParam(value = "limit", required = true) int limit, HttpServletRequest request) {
		return null;
	}

	/**
	 * 根据uuid删除一级域名
	 * 
	 * @param uuid
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteDomainOne")
	public Object deleteDomainOne(@RequestParam(value = "uuid", required = true) String uuid,
			HttpServletRequest request) {
		if (domainService.deleteDomainOneById(uuid)) {
			return ResultUtil.success("删除成功！");
		} else {
			return ResultUtil.errorWithMsg("删除失败！");
		}
	}

	/**
	 * 根据uuid删除二级域名
	 * 
	 * @param uuid
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteDomainTwo")
	public Object deleteDomainTwo(@RequestParam(value = "uuid", required = true) String uuid,
			HttpServletRequest request) {
		if (domainService.deleteDomainTwoById(uuid)) {
			return ResultUtil.success("删除成功！");
		} else {
			return ResultUtil.errorWithMsg("删除失败！");
		}
	}

	@ResponseBody
	@RequestMapping("/updateDomainOne")
	public Object updateDomainOne(@RequestParam(value = "uuid", required = true) String uuid,
			@RequestParam(value = "url", required = true) String url,
			@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "column", required = true) String column,
			@RequestParam(value = "type", required = true) String type,
			@RequestParam(value = "rank", required = true) String rank,
			@RequestParam(value = "incidence", required = true) String incidence,
			@RequestParam(value = "weight", required = true) Integer weight, HttpServletRequest request) {
		DomainOne one = new DomainOne();
		System.out.println(uuid);
		one.setUuid(uuid);
		one.setUrl(url);
		;
		one.setColumn(column);
		one.setType(type);
		one.setRank(rank);
		one.setIncidence(incidence);
		one.setWeight(weight);
		one.setUpdateTime(new Date());
		DomainOneProperty dop = new DomainOneProperty();
		dop.setDomainOne(one);
		if (domainService.updateDomainOneProperty(dop))
			return ResultUtil.success("修改成功！");
		return ResultUtil.errorWithMsg("修改失败！");
	}

	@ResponseBody
	@RequestMapping("/updateDomainTwo")
	public Object updateDomainTwo(@RequestParam(value = "uuid", required = true) String uuid,
			@RequestParam(value = "url", required = true) String url,
			@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "column", required = true) String column,
			@RequestParam(value = "type", required = true) String type,
			@RequestParam(value = "rank", required = true) String rank,
			@RequestParam(value = "incidence", required = true) String incidence,
			@RequestParam(value = "weight", required = true) Integer weight, HttpServletRequest request) {
		DomainTwo two = new DomainTwo();
		two.setUuid(uuid);
		two.setUrl(url);
		;
		two.setColumn(column);
		two.setType(type);
		two.setRank(rank);
		two.setIncidence(incidence);
		two.setWeight(weight);
		two.setUpdateTime(new Date());
		DomainTwoProperty dtp = new DomainTwoProperty();
		dtp.setDomainTwo(two);
		if (domainService.updateDomainTwoProperty(dtp))
			return ResultUtil.success("修改成功！");
		return ResultUtil.errorWithMsg("修改失败！");
	}
}
