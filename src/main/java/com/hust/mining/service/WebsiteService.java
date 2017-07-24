package com.hust.mining.service;

import java.util.List;

import com.hust.mining.model.Website;
import com.hust.mining.model.params.WebsiteQueryCondition;

public interface WebsiteService {

	Website queryByUrl(String url);
	
	List<Website> selectAllWebsite(int start, int limit);

	List<Website> selectByCondition(WebsiteQueryCondition website);
	
	List<Website> selectAllWebsiteUnknow(int start, int limit);
	
	long selectWebsiteCount();
	
	long selectUnknowWebsiteCount();
	
	long selectWebsiteByCondition(WebsiteQueryCondition website);

	List<String[]> exportKnownUrlService();
	
	List<String[]> exportUnKnownUrlService();
	
	boolean deleteWebsiteById(long id);

	boolean updateWebsite(Website website);

	boolean insertWebsite(Website website);
}
