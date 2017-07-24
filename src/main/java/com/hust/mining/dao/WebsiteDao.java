package com.hust.mining.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hust.mining.dao.mapper.WebsiteMapper;
import com.hust.mining.model.Website;
import com.hust.mining.model.WebsiteExample;
import com.hust.mining.model.WebsiteExample.Criteria;
import com.hust.mining.model.params.WebsiteQueryCondition;

@Repository
public class WebsiteDao {

    @Autowired
    private WebsiteMapper websiteMapper;

    public String queryLevelByUrl(String url) {
        WebsiteExample example = new WebsiteExample();
        example.createCriteria().andUrlEqualTo(url);
        return websiteMapper.selectByExample(example).get(0).getLevel();
    }

    public String queryTypeByUrl(String url) {
        WebsiteExample example = new WebsiteExample();
        example.createCriteria().andUrlEqualTo(url);
        List<Website> list = websiteMapper.selectByExample(example);
        if(list == null || list.isEmpty()){
        	return "";
        }
        return list.get(0).getType();
    }

    public Website queryByUrl(String url) {
        WebsiteExample example = new WebsiteExample();
        example.createCriteria().andUrlEqualTo(url);
        List<Website> list = websiteMapper.selectByExample(example);
        if (null == list || list.size() == 0) {
            Website web = new Website();
            web.setLevel("其他");
            web.setName("其他");
            web.setType("其他");
            web.setUrl(url);
            websiteMapper.insert(web);
            return web;
        }
        return list.get(0);
    }

    public List<Website> selecAlltWebsite(int start, int limit) {
        WebsiteExample example = new WebsiteExample();
        Criteria criteria = example.createCriteria();
        criteria.andIdIsNotNull();
        criteria.andNameNotEqualTo("其他");
        example.setStart(start);
        example.setLimit(limit);
        List<Website> website = websiteMapper.selectByExample(example);
        return website;
    }
    
    public long selectWebsiteCount(){
    	WebsiteExample example = new WebsiteExample();
        Criteria criteria = example.createCriteria();
        criteria.andIdIsNotNull();
        criteria.andNameNotEqualTo("其他");
    	return websiteMapper.countByExample(example);
    }
    
    public long selectUnknowWebsiteCount(){
    	WebsiteExample example = new WebsiteExample();
        Criteria criteria = example.createCriteria();
        criteria.andIdIsNotNull();
        criteria.andNameEqualTo("其他");
    	return websiteMapper.countByExample(example);
    }
    
    public long selectWebsiteCount(WebsiteQueryCondition webSite){
    	WebsiteExample example = new WebsiteExample();
        Criteria criteria = example.createCriteria();
        if (!StringUtils.isBlank(webSite.getUrl())) {
            criteria.andUrlLike(webSite.getUrl());
        }
        if (!StringUtils.isBlank(webSite.getLevel())) {
            criteria.andLevelLike(webSite.getLevel());
        }
        if (!StringUtils.isBlank(webSite.getName())) {
            criteria.andNameLike("%"+webSite.getName()+"%");
        }
        if (!StringUtils.isBlank(webSite.getType())) {
            criteria.andTypeLike(webSite.getType());
        }
        example.setStart(0);
        example.setLimit(0);
    	return websiteMapper.countByExample(example);
    }

    public List<Website> selectAllWebsiteUnknow(int start, int limit) {
        WebsiteExample example = new WebsiteExample();
        Criteria criteria = example.createCriteria();
        criteria.andIdIsNotNull();
        criteria.andNameEqualTo("其他");
        example.setStart(start);
        example.setLimit(limit);
        List<Website> website = websiteMapper.selectByExample(example);
        return website;
    }

    public int insertWebsite(Website werbsite) {
        return websiteMapper.insert(werbsite);
    }

    public List<Website> selectByCondition(WebsiteQueryCondition webSite) {
        WebsiteExample example = new WebsiteExample();
        Criteria criteria = example.createCriteria();
        if (!StringUtils.isBlank(webSite.getUrl())) {
            criteria.andUrlLike(webSite.getUrl());
        }
        if (!StringUtils.isBlank(webSite.getLevel())) {
            criteria.andLevelLike(webSite.getLevel());
        }
        if (!StringUtils.isBlank(webSite.getName())) {
            criteria.andNameLike("%"+webSite.getName()+"%");
        }
        if (!StringUtils.isBlank(webSite.getType())) {
            criteria.andTypeLike(webSite.getType());
        }
        if (webSite.getStart() != 0) {
            example.setStart(webSite.getStart());
        }
        if (webSite.getLimit() != 0) {
            example.setLimit(webSite.getLimit());
        }
        List<Website> websites = websiteMapper.selectByExample(example);
        return websites;
    }

    public int deleteWebsiteById(long id) {
        WebsiteExample example = new WebsiteExample();
        Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(id);
        return websiteMapper.deleteByExample(example);
    }

    public int updateWebsite(Website website) {
        return websiteMapper.updateByPrimaryKeySelective(website);
    }
    
    public int updateWebsiteInfo(Website website){
    	WebsiteExample example = new WebsiteExample();
		Criteria criteria = example.createCriteria();
		criteria.andUrlEqualTo(website.getUrl());
    	return websiteMapper.updateByExampleSelective(website, example);
    }
}
