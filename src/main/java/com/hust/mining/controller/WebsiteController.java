package com.hust.mining.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.hust.mining.model.Website;
import com.hust.mining.model.params.WebsiteQueryCondition;
import com.hust.mining.service.WebsiteService;
import com.hust.mining.util.CommonUtil;
import com.hust.mining.util.ExcelUtil;
import com.hust.mining.util.ResultUtil;

@Controller
@RequestMapping("/website")
public class WebsiteController {

	@Autowired
	private WebsiteService websiteService;

	@ResponseBody
	@RequestMapping("/selectAllWebsite")
	public Object selectAllWebsite(@RequestParam(value = "start", required = true) int start,
			@RequestParam(value = "limit", required = true) int limit) {
		List<Website> website = websiteService.selectAllWebsite(start, limit);
		if (website.isEmpty()) {
			return ResultUtil.errorWithMsg("暂无网站信息被录入！");
		}
		return ResultUtil.success(website);
	}
	
	@ResponseBody
	@RequestMapping("/selectWebsiteCount")
	public Object selectWebsiteCount(@RequestParam(value = "url", required = false) String url,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "levle", required = false) String level,
			@RequestParam(value = "type", required = false) String type){
		long count = 0;
		if(null == url && null == name && null == level && null == type){
			//查询所有
			count = websiteService.selectWebsiteCount();
		}else{
			//根据条件查询
			WebsiteQueryCondition website = new WebsiteQueryCondition();
			website.setUrl(url);
			website.setName(name);
			website.setLevel(level);
			website.setType(type);
			count = websiteService.selectWebsiteByCondition(website);
		}
		if(count<=0){
		return ResultUtil.errorWithMsg("暂无网站信息被录入！");
		}
		return ResultUtil.success(count);
	}
	
	@ResponseBody
	@RequestMapping("/selectUnknowWebsiteCount")
	public Object selectUnknowWebsiteCount(@RequestParam(value = "url", required = false) String url,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "levle", required = false) String level,
			@RequestParam(value = "type", required = false) String type){
		long count = 0;
		if(null == url && null == name && null == level && null == type){
			//查询所有
			count = websiteService.selectUnknowWebsiteCount();
		}else{
			//根据条件查询
			WebsiteQueryCondition website = new WebsiteQueryCondition();
			website.setUrl(url);
			website.setName(name);
			website.setLevel(level);
			website.setType(type);
			count = websiteService.selectWebsiteByCondition(website);
		}
		if(count<=0){
		return ResultUtil.errorWithMsg("暂无网站信息被录入！");
		}
		return ResultUtil.success(count);
	}
	
	@ResponseBody
    @RequestMapping("/selectAllWebsiteUnknow")
    public Object selectAllWebsiteUnknow(@RequestParam(value = "start", required = true) int start,
            @RequestParam(value = "limit", required = true) int limit) {
        List<Website> website = websiteService.selectAllWebsiteUnknow(start, limit);
        if (website.isEmpty()) {
            return ResultUtil.errorWithMsg("暂无未知网站信息");
        }
        return ResultUtil.success(website);
    }
	
    @ResponseBody
	@RequestMapping("/importMapUrl")
	public Object importMapUrl(@RequestParam(value = "file", required = true) MultipartFile file){
    	if(file.isEmpty()){
    		return ResultUtil.errorWithMsg("file is empty");
    	}
    	try{
    		System.out.println(file.getOriginalFilename());
    		List<String[]> list = ExcelUtil.read(file.getOriginalFilename(),file.getInputStream(),1);
    		if(null == list || 0 == list.size()){
    			return ResultUtil.errorWithMsg("file is uncorrect!");
    		}
    		
    		WebsiteQueryCondition condition = new WebsiteQueryCondition();
    		String prefixUrl = null;
    		for(String[] strs:list){
    			prefixUrl = CommonUtil.getPrefixUrl(strs[0]);
    			if(prefixUrl.trim().isEmpty()){
    				continue;
    			}
    			condition.setUrl(prefixUrl);
    			Website website = new Website();
    			List<Website> list1 = websiteService.selectByCondition(condition);
    			website.setUrl(prefixUrl);
        		website.setName(strs[1]);
        		website.setLevel(strs[2]);
        		website.setType(strs[3]);
    			if(list1.isEmpty()){
            		if (!websiteService.insertWebsite(website)) {
            			return ResultUtil.errorWithMsg("insert website error");
            		}
    			}else{
    				Website web = list1.get(0);
    				website.setId(web.getId());
    				websiteService.updateWebsite(website);
    			}
    		}
    		
    		return ResultUtil.success(list);
    	}catch (Exception e){
    		
    	}
    	return ResultUtil.errorWithMsg("file preread error!");
    }

	@ResponseBody
	@RequestMapping("/insertWebsite")
	public Object insertWebsite(@RequestParam(value = "url", required = true) String url,
			@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "levle", required = true) String level,
			@RequestParam(value = "type", required = true) String type) {
		Website website = new Website();
		website.setUrl(url);
		website.setName(name);
		website.setLevel(level);
		website.setType(type);
		boolean status = websiteService.insertWebsite(website);
		if (status == false) {
			return ResultUtil.errorWithMsg("insert website error");
		}
		return ResultUtil.success("insert website success");
	}

	@ResponseBody
	@RequestMapping("/deleteWebsite")
	public Object deleteWebsiteById(@RequestParam(value = "websiteId", required = true) long websiteId) {
		boolean status = websiteService.deleteWebsiteById(websiteId);
		if (status == false) {
			return ResultUtil.errorWithMsg("delete website error ");
		}
		return ResultUtil.success("delete data success");
	}

	@ResponseBody
	@RequestMapping("/updateWebsite")
	public Object updateWebsite(@RequestParam(value = "id", required = true) long id,
			@RequestParam(value = "url", required = true) String url,
			@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "level", required = true) String level,
			@RequestParam(value = "type", required = true) String type) {
		Website website = new Website();
		website.setId(id);
		website.setUrl(url);
		website.setName(name);
		website.setLevel(level);
		website.setType(type);
		boolean status = websiteService.updateWebsite(website);
		if (status == false) {
			return ResultUtil.errorWithMsg("update website error");
		}
		return ResultUtil.success("update website success");
	}

	@ResponseBody
	@RequestMapping("/selectByCondition")
	public Object selectByCondition(@RequestParam(value = "url", required = true) String url,
			@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "levle", required = true) String level,
			@RequestParam(value = "type", required = true) String type,
			@RequestParam(value = "start", required = true) int start,
			@RequestParam(value = "limit", required = true) int limit) {
		WebsiteQueryCondition website = new WebsiteQueryCondition();
		website.setUrl(url);
		website.setName(name);
		website.setLevel(level);
		website.setType(type);
		website.setStart(start);
		website.setLimit(limit);
		List<Website> websites = websiteService.selectByCondition(website);
		if (websites.isEmpty()) {
			return ResultUtil.errorWithMsg("website is empty");
		}
		return ResultUtil.success(websites);
	}

}