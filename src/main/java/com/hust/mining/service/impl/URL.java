package com.hust.mining.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.xmlbeans.impl.xb.xmlschema.SpaceAttribute.Space;

import com.hust.datamining.util.CommonUtils;
import com.hust.mining.util.CommonUtil;
import com.hust.mining.util.ExcelUtil;

public class URL {
	
	public static void URL(String filename)
	{
		try {
			List<String[]> teStrings =ExcelUtil.read(filename);
			
			for (String[] strings : teStrings) {
				String string = strings[0];
				strings[0] = CommonUtil.getPrefixUrl(string);
			}
			
			HashMap<String, Integer> hashMap = new HashMap<String,Integer>();
			for (String[] s : teStrings) 
			{
				if (hashMap.containsKey(s[0])) {
					hashMap.put(s[0],hashMap.get(s[0])+1);
					
				}
				else{
					hashMap.put(s[0], 1);
				}
				
			}
			
			for (String hs : hashMap.keySet()) {
				
				//System.out.println(hs+":"+hashMap.get(hs));
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	public static void main(String[] args) {
		URL("F://111.xls");
		
	}

}
