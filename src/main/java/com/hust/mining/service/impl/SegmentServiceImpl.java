package com.hust.mining.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.ansj.domain.Result;
import org.ansj.recognition.impl.FilterRecognition;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hust.mining.dao.StopwordDao;
import com.hust.mining.dao.mapper.StopwordMapper;
import com.hust.mining.model.Stopword;
import com.hust.mining.service.SegmentService;

@Service
public class SegmentServiceImpl implements SegmentService {
    /**
     * Logger for this class
     */

    private static final Logger logger = LoggerFactory.getLogger(SegmentServiceImpl.class);

    private static FilterRecognition filter = new FilterRecognition();

    static {
        filter.insertStopNatures("w", "u", "mq", "p", "e", "y", "o");
       
    }
  
    public static void addStopwords(List<String> list){    	
     	//System.out.println("==============="+list.size());
     	filter.insertStopWords(list);
     	logger.info("=====================添加停用词成功==========================");
    }
    
    @Autowired
    public SegmentServiceImpl(StopwordDao stopwordDao) {
		// TODO Auto-generated constructor stub
    	super();   
    	List<String> list = stopwordDao.selectAllStopword();
    	addStopwords(list);
	}
    
    public SegmentServiceImpl() {
		// TODO Auto-generated constructor stub
    	super();    	
	}
   
    @Override
    public String[] getSegresult(String str) {
        Result res;
        try {
            res = NlpAnalysis.parse(str).recognition(filter);
        } catch (Exception e) {
            // TODO Auto-generated catch block
        	logger.info(e.toString() + "\t" + str);
            //System.out.println(e.toString() + "\t" + str);
            return new String[] { "失败" };
        }
        String[] words = new String[res.size()];
        for (int i = 0; i < res.size(); i++) {
            words[i] = res.get(i).getName();
        }
        return words;
    }

    @Override
    public List<String[]> getSegresult(List<String> list, int start) {
        if (null == list) {
            throw new IllegalArgumentException();
        }
        if (start + 1 > list.size()) {
            throw new IllegalArgumentException();
        }
        List<String[]> result = new ArrayList<String[]>();
        for (int i = start; i < list.size(); i++) {
            String[] array = getSegresult(list.get(i));
            result.add(array);
        }
        return result;
    }

    @Override
    public List<List<Object>> getResult(List<String[]> list, int index, int start) {
        if (null == list) {
            throw new NullPointerException();
        }
        if (index < 0 || start + 1 > list.size()) {
            throw new IllegalArgumentException();
        }
        List<List<Object>> relist = new ArrayList<List<Object>>();
        for (int i = start; i < list.size(); i++) {
            String[] array = list.get(i);
            List<Object> templist = new ArrayList<Object>();
            for (int j = 0; j < array.length; j++) {
                if (j == index) {
                    String[] temparray = getSegresult(array[j]);
                    templist.add(temparray);
                } else {
                    templist.add(array[j]);
                }
            }
            relist.add(templist);
        }
        return relist;
    }

    @Override
    public List<String[]> getSegresult(List<String[]> list, int index, int start) {
        if (null == list) {
            throw new NullPointerException();
        }
        if (index < 0 || start + 1 > list.size()) {
            throw new IllegalArgumentException();
        }
        List<String[]> relist = new ArrayList<String[]>();
        for (int i = start; i < list.size(); i++) {
            try {
                String[] array = list.get(i);
                String[] temparray = getSegresult(array[index]);
                relist.add(temparray);
            } catch (Exception e) {
                logger.info("segment error:{}", i);
            }
        }
        return relist;
    }

    @Override
    public List<String[]> getSegresult(String[] array) {
        if (null == array) {
            throw new NullPointerException();
        }
        List<String[]> list = new ArrayList<String[]>();
        if (array.length == 0) {
            return list;
        }
        for (String str : array) {
            String[] temparray = getSegresult(str);
            list.add(temparray);
        }
        return list;
    }

}
