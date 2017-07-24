package com.hust.mining.urltags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.map.HashedMap;

import com.hust.mining.util.CommonUtil;
import com.hust.mining.util.ExcelUtil;

public class URLTool {
	
	public static void main(String[] args) {
		//测试
		
	}
	/**
	 * 统计准数据文件url中出现次数最多的网站
	 * @param fileContent 包含两个内容，第一个是URL,第二个是网站名
	 */
	public List<String[]> statisticUrl(List<String[]> fileContent){
		Map<String,Integer> urlNumberMap = new HashedMap();
		Map<String,String> urlNameMap = new HashedMap();
		//去除属性行
		fileContent.remove(0);
		if(fileContent.isEmpty()){
			return null;
		}
		for(String [] row : fileContent){
			String url = row[0].trim();
			String urlName = row[1].trim();
			if(!url.equals("")){
				url = CommonUtil.getPrefixUrl(url); //去掉前缀
				countUrl(url,urlNumberMap);
				if(!urlNameMap.containsKey(url)){
					urlNameMap.put(url, urlName);
				}
			}
		}
		//System.out.println("排序后-----");
		//System.out.println("网站url\t网站名称\t数量");
		return sort(urlNumberMap,urlNameMap);
	}

	public void countUrl(String url, Map<String, Integer> urlNumberMap) {
		// TODO Auto-generated method stub
		if(urlNumberMap.keySet().contains(url)){
			urlNumberMap.replace(url, urlNumberMap.get(url)+1);
		}else{
			urlNumberMap.put(url, 1);
		}
		
	}
	//对url按数量进行排序
	public List<String[]> sort( Map<String, Integer> urlNumberMap, Map<String, String> urlNameMap){
		List<String[]> list = new ArrayList<String[]>();
		List<Map.Entry<String, Integer>> urls = new ArrayList<>(urlNumberMap.entrySet());
		Collections.sort(urls, new Comparator<Map.Entry<String, Integer>>() {

			@Override
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
				// TODO Auto-generated method stub
				return o2.getValue() - o1.getValue();
			}
		});
		
		for (Entry<String, Integer> entry : urls) {
			//System.out.println(String.format("%-50s", entry.getKey())+
			//		String.format("%-25s", urlNameMap.get(entry.getKey()))+
			//		String.format("%-5s", entry.getValue()));
			String[] string = new String[3];
			string[0] = entry.getKey();
			string[1] = urlNameMap.get(entry.getKey());
			string[2] = entry.getValue().toString();
			list.add(string);
		}
		return list;
	}
}
