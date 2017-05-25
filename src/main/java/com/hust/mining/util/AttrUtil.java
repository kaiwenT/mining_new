package com.hust.mining.util;

import java.util.List;
import java.util.regex.Pattern;

public class AttrUtil {

	public static String[] findEssentialIndex(String[] attrs){
		String indexOfTitle = String.valueOf(findIndexOfTitle(attrs));
		String indexOfUrl = String.valueOf(findIndexOfUrl(attrs));
		String indexOfTime = String.valueOf(findIndexOfTime(attrs));
		
		return new String[]{indexOfTitle,indexOfUrl,indexOfTime};
	}
	
	public static int findIndexOfTitle(String[] attrs){
		for(int i=0; i<attrs.length; i++){
			if(Pattern.matches("标题|内容", attrs[i])){
				return i;
			}
		}
		
		return -1;
	}
	
	public static int findIndexOfUrl(String[] attrs){
		for(int i=0; i<attrs.length; i++){
			if(Pattern.matches("链接|网址|[Uu][Rr][Ll]", attrs[i])){
				return i;
			}
		}
		
		return -1;
	}
	
	public static int findIndexOfTime(String[] attrs){
		for(int i=0; i<attrs.length; i++){
			if(Pattern.matches("发布时间|发贴时间|时间", attrs[i])){
				return i;
			}
		}
		
		return -1;
	}
	
	public static int findIndexOfUrl(List<String> attrs){
		for(int i=0; i<attrs.size(); i++){
			if(Pattern.matches("链接|网址|[Uu][Rr][Ll]", attrs.get(i))){
				return i;
			}
		}
		
		return -1;
	}
	
	public static void main(String[] args) {
		System.out.println(AttrUtil.findIndexOfTime(new String[]{
				"a","时间","sadf"
		}));
	}
}
