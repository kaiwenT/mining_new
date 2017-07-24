package com.hust.mining.service.impl;

import java.io.IOException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.servlet.http.HttpServletRequest;

import org.apache.tools.ant.taskdefs.optional.jlink.jlink;
import org.apache.xmlbeans.impl.xb.xsdownload.DownloadedSchemaEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hust.datamining.algorithm.cluster.Canopy;
import com.hust.datamining.algorithm.cluster.DBScan;
import com.hust.datamining.algorithm.cluster.KMeans;
import com.hust.datamining.convertor.Convertor;
import com.hust.datamining.convertor.DigitalConvertor;
import com.hust.datamining.convertor.TFIDFConvertor;
import com.hust.datamining.simcal.AcrossSimilarity;
import com.hust.datamining.simcal.CosSimilarity;
import com.hust.mining.constant.Constant.CONVERTERTYPE;
import com.hust.mining.constant.Constant.DIRECTORY;
import com.hust.mining.constant.Constant.GRANULARITY;
import com.hust.mining.constant.Constant.Index;
import com.hust.mining.constant.Constant.KEY;
import com.hust.mining.model.Issue;
import com.hust.mining.model.IssueFile;
import com.hust.mining.model.params.Condition;
import com.hust.mining.service.AlgorithmContainerService;
import com.hust.mining.service.MiningService;
import com.hust.mining.service.RedisService;
import com.hust.mining.service.SegmentService;
import com.hust.mining.util.AttrUtil;
import com.hust.mining.util.ConvertUtil;
import com.hust.mining.util.ExcelUtil;
import com.hust.mining.util.FileUtil;
import com.hust.mining.util.WeiboUtil;

@Service
public class AlgorithmContainerServiceImpl implements AlgorithmContainerService {

	 @Autowired
	 private SegmentService segmentService;
	 
	 @Autowired
	 private RedisService redisService;
	 
	 @Autowired
	 private MiningService miningService;
	 /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(MiningServiceImpl.class);
	
    /**
     * 获取测试文件内容
     * @param con
     * @param request
     * @return
     */
    
    @Override
    public int getContent(Condition con, HttpServletRequest request) {
        // TODO Auto-generated method stub
        MultipartFile file = con.getFile();
        List<String[]> list = new ArrayList<String[]>();
        InputStream is = null;
        try {
            is = file.getInputStream();
            // 此处index传入的顺序必须与constants中定义的value值保持一致
            list = ExcelUtil.read(file.getOriginalFilename(), is, 0, -1, con.getUrlIndex(), con.getTitleIndex(),
                    con.getTimeIndex());
            redisService.setObject(KEY.REDIS_CONTENT, list, request);
            List<String[]> list1= (List<String[]>) redisService.getObject(KEY.REDIS_CONTENT, request);
            return 1;
        } catch (IOException e) {
            logger.error("读取文件出现异常\t" + e.toString());
            return 0;
        }
      
    }
    /**
     * 下载聚类结果
     */
    @Override
	public List<String[]> Downloade(HttpServletRequest request) {
    	//存储原始文本的内容
        List<String[]> content= (List<String[]>) redisService.getObject(KEY.REDIS_CONTENT, request);
        //存储分类后的文本序号，每个数组为一个类
        List<String[]> cluster = (List<String[]>) redisService.getObject(KEY.REDIS_CLUSTER_RESULT, request);
		//用于存储结果
        List<String[]> list = new ArrayList<String[]>();
        for (String[] strings : cluster) {
        	for (String string : strings) {
				int i = Integer.parseInt(string);
				String[] s = content.get(i+1);
				list.add(s);
			}
			String[] s1 = {" "," " ," "};
			list.add(s1);
		}
        return list;
	}

    
    /**
     * 第一个参数为：去除属性后的文本
     * 聚类的结果
     * 属性列
     */
    @Override
    public void storeResult(List<String[]> list,List<List<Integer>> result1, String[] attrs,HttpServletRequest request) 
    {
    	List<String[]> cluster = ConvertUtil.toStringListB(result1);
		List<int[]> countResult = miningService.count(list, cluster);
		list.add(0, attrs);
		redisService.setObject(KEY.REDIS_CONTENT, list, request);
		redisService.setObject(KEY.REDIS_CLUSTER_RESULT, cluster, request);
		redisService.setObject(KEY.REDIS_COUNT_RESULT, countResult, request);
		List<String[]> list2 = getResult(request);
		
    }
    
    @Override
    public List<String[]> getResult(HttpServletRequest request) {
        // TODO Auto-generated method stub
    	List<String[]> list = new ArrayList<String[]>();
        try {
            List<String[]> content =  (List<String[]>)redisService.getObject(KEY.REDIS_CONTENT, request);
            List<int[]> count = (List<int[]>) redisService.getObject(KEY.REDIS_COUNT_RESULT, request);
            List<String[]> cluster = (List<String[]>) redisService.getObject(KEY.REDIS_CLUSTER_RESULT, request);
            //System.out.println("cluster为:");
            for (String[] strings : cluster) {
			   for (String string : strings) {
				//System.out.print(string+ "  ");
			}
			   //System.out.println();
           }
            //System.out.println("cluster完:");
            for (int[] item : count) {
                String[] old = content.get(item[Index.COUNT_ITEM_INDEX]+1);
                String[] ne = new String[old.length + 1];
                System.arraycopy(old, 0, ne, 1, old.length);
                ne[0] = item[Index.COUNT_ITEM_AMOUNT] + "";
                list.add(ne);
            }
            list.add(0, AttrUtil.findEssentialIndex(content.get(0)));
        } catch (Exception e) {
            logger.error("get count result failed:{}", e.toString());
            return null;
        }
        return list;
    }

    
    /**
     * 对带聚类的数据进行分词和向量模型的转换
     * @param list
     * @param converterType
     * @return
     */
	@Override
	public List<double[]> Converter(List<String[]> list, int converterType) {
		//对标题列进行分词。
        List<String[]> segmentList = segmentService.getSegresult(list, Index.TITLE_INDEX, 0);
        Convertor convertor = null;
        //判断选择的向量模型的类型
        if (converterType == CONVERTERTYPE.DIGITAL) {
            convertor = new DigitalConvertor();
        } else if (converterType == CONVERTERTYPE.TFIDF) {
            convertor = new TFIDFConvertor();
        }
        convertor.setList(segmentList);
        List<double[]> vectors = convertor.getVector();
    	return vectors;
	}

	 /**
     * 对聚类结构进行排序
     * @param list  带聚类文本
     * @param resultIndexSetList   未排序的聚类结果
     * @return
     */
	@Override
	public List<List<Integer>> Sort(List<String[]> list, List<List<Integer>> resultIndexSetList, int indexOfTitle, int indexOfTime) {
		Collections.sort(resultIndexSetList, new Comparator<List<Integer>>() {

            @Override
            public int compare(List<Integer> o1, List<Integer> o2) {
                // TODO Auto-generated method stub
                return o2.size() - o1.size();
            }
        });
        for (List<Integer> set : resultIndexSetList) {
            Collections.sort(set, new Comparator<Integer>() {

                @Override
                public int compare(Integer o1, Integer o2) {
                    // TODO Auto-generated method stub
                	//判断他们的标题是否相同
                    int compare = list.get(o1)[indexOfTitle].compareTo(list.get(o2)[indexOfTitle]);
                    //若不相同，使用时间进行排序。
                    if (compare == 0) {
                        compare = list.get(o1)[indexOfTime].compareTo(list.get(o2)[indexOfTime]);
                    }
                    return compare;
                }
            });
        }
        return resultIndexSetList;
	}

	 /**
     * 使用Kmeans算法
     * @param list  待聚类字符串
     * @param k     聚类中心个数
     * @param granularity    聚类粒度
     * @return
     */
	@Override
	public String UseKmeans(List<String[]> list, int k, int granularity ,HttpServletRequest request) {
		//用于存放结果
    	List<List<Integer>> resultIndexSetList = new ArrayList<List<Integer>>();
    	//移除属性行
    	String[] attrs = list.remove(0);
    	
    	int indexOfTitle = AttrUtil.findIndexOfTitle(attrs);
    	int indexOfTime = AttrUtil.findIndexOfTime(attrs);
    	//向量转换完成
        List<double[]> vectors = Converter(list, 1);
        //System.out.println("使用的是KMEANS");
        KMeans kmeans = new KMeans();
        kmeans.setVectors(vectors);
        kmeans.setIterationTimes(20);
        //相似度方式的选择
        if (granularity == GRANULARITY.AcrossSimilarity) {
        kmeans.setSimi(new AcrossSimilarity(vectors)); 
      	  //System.out.println("选择的是粗粒度AcrossSimilarity");
		
        }if(granularity == GRANULARITY.CosSimilarity){
       	  kmeans.setSimi(new CosSimilarity(vectors));
		  //System.out.println("选择的是细粒度CosSimilarity");
        }
        kmeans.setK(k);
        ExecutorService exec = Executors.newSingleThreadExecutor();
        Future<List<List<Integer>>> future1 = exec.submit(kmeans);
        try {
        	//得到聚类结果
            resultIndexSetList = future1.get();
        } catch (Exception e) {
            logger.error("error occur during clustering by canopy" + e.toString());
            return "聚类失败";
        }
        
       List<List<Integer>> result= Sort(list, resultIndexSetList,indexOfTitle,indexOfTime);
       //System.out.println("去掉前");
       for(int i = 0; i < result.size();i++)
       {
     	  List<Integer> list2 = result.get(i);
     	  for (Integer integer : list2) {
 			//System.out.print(integer+"  ");
 		} 
     	  //System.out.println();
       }
      //去除长度为0 的类簇
      int start = 0;
       
      for(int i = 0; i < result.size();i++)
      {
    	  List<Integer> list2 = result.get(i);
    	  if (list2.size()==0) {
    		start = i;
    		break;
		 } 	  
      }
  	  List<List<Integer>> result1 = new ArrayList<List<Integer>>();
  	  if (start==0) 
  	  {
  		 storeResult(list, result, attrs, request);
	  }
  	  else{
  		  
  		 for(int i = 0; i < start; i++)
  	      {
  	    	 result1.add(result.get(i));
  	      }
  		 storeResult(list, result1, attrs, request);
  		  
  	  }
  	  
      //System.out.println("去掉后");
      for(int i = 0; i < result1.size();i++)
      {
    	  List<Integer> list2 = result1.get(i);
    	  for (Integer integer : list2) {
			//System.out.print(integer+"  ");
		} 
    	  //System.out.println();
      }
      return "聚类成功";
	}

	/**
	 * 使用Canopy聚类，阈值可设置
	 */
	@Override
	public String UseCanopy(List<String[]> list, float Threshold, int granularity, HttpServletRequest request) {
		//用于存放结果
    	List<List<Integer>> resultIndexSetList = new ArrayList<List<Integer>>();
    	//移除属性行
    	String[] attrs = list.remove(0);
    	
    	int indexOfTitle = AttrUtil.findIndexOfTitle(attrs);
    	int indexOfTime = AttrUtil.findIndexOfTime(attrs);
    	//向量转换完成
        List<double[]> vectors = Converter(list, 1);
        //System.out.println("使用的是CANOPY");
   	    Canopy canopy = new Canopy();
        canopy.setVectors(vectors);
        //相似度方式的选择
        if (granularity == GRANULARITY.AcrossSimilarity) {
      	  canopy.setSimi(new AcrossSimilarity(vectors)); 
      	  //System.out.println("选择的是粗粒度AcrossSimilarity");
			
        } else if(granularity == GRANULARITY.CosSimilarity){
			  canopy.setSimi(new CosSimilarity(vectors));
			  //System.out.println("选择的是细粒度CosSimilarity");
        }
        //设置阀值
        canopy.setThreshold(Threshold);
        //开启线程池
        ExecutorService exec = Executors.newSingleThreadExecutor();
        Future<List<List<Integer>>> future = exec.submit(canopy);
        try {
        	//得到聚类结果
            resultIndexSetList = future.get();
        } catch (Exception e) {
            logger.error("error occur during clustering by canopy" + e.toString());
            return "聚类失败";
        }
        List<List<Integer>> result= Sort(list, resultIndexSetList,indexOfTitle,indexOfTime);
        //System.out.println("result:");
        storeResult(list, result, attrs, request);
        return "聚类成功";
	}

	/**
     * 使用DBScan算法
     * @param list  带聚类文本
     * @param Eps   半径
     * @param MinPts  最少的个数
     * @param granularity  粒度
     * @return
     */
	@Override
	public String UseDBScan(List<String[]> list, float Eps, int MinPts, int granularity,HttpServletRequest request) {
		//用于存放结果
    	List<List<Integer>> resultIndexSetList = new ArrayList<List<Integer>>();
    	//移除属性行
    	String[] attrs = list.remove(0);
    	
    	int indexOfTitle = AttrUtil.findIndexOfTitle(attrs);
    	int indexOfTime = AttrUtil.findIndexOfTime(attrs);
    	//向量转换完成
        List<double[]> vectors = Converter(list, 1);
        //System.out.println("使用的是DBSCAN");
   	    DBScan dbscan = new DBScan();
   	    dbscan.setVectors(vectors);
        if (granularity == GRANULARITY.AcrossSimilarity) {
       	  dbscan.setSimi(new AcrossSimilarity(vectors)); 
         	  //System.out.println("选择的是粗粒度AcrossSimilarity");
 			
           } else if(granularity == GRANULARITY.CosSimilarity){
             dbscan.setSimi(new CosSimilarity(vectors));
 			  //System.out.println("选择的是细粒度CosSimilarity");
           }
        //设置阀值
        dbscan.setMinPts(MinPts);
        dbscan.setEps(Eps);
        //开启线程池
        ExecutorService exec = Executors.newSingleThreadExecutor();
        Future<List<List<Integer>>> future = exec.submit(dbscan);
        try {
        	//得到聚类结果
            resultIndexSetList = future.get();
        } catch (Exception e) {
            logger.error("error occur during clustering by canopy" + e.toString());
            return "聚类失败";
        }
        List<List<Integer>> result= Sort(list, resultIndexSetList,indexOfTitle,indexOfTime);
        storeResult(list, result, attrs, request);
        	return "聚类成功";
	}

}
