package com.hust.mining.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hust.datamining.algorithm.cluster.Canopy;
import com.hust.datamining.algorithm.cluster.DBScan;
import com.hust.datamining.algorithm.cluster.KMeans;
import com.hust.datamining.convertor.Convertor;
import com.hust.datamining.convertor.DigitalConvertor;
import com.hust.datamining.convertor.TFIDFConvertor;
import com.hust.datamining.simcal.AcrossSimilarity;
import com.hust.datamining.simcal.CosSimilarity;
import com.hust.mining.constant.Config;
import com.hust.mining.constant.Constant;
import com.hust.mining.constant.Constant.ALGORIRHMTYPE;
import com.hust.mining.constant.Constant.CONVERTERTYPE;
import com.hust.mining.constant.Constant.GRANULARITY;
import com.hust.mining.constant.Constant.Index;
import com.hust.mining.constant.Constant.KEY;
import com.hust.mining.dao.WebsiteDao;
import com.hust.mining.dao.WeightDao;
import com.hust.mining.model.Website;
import com.hust.mining.service.MiningService;
import com.hust.mining.service.SegmentService;
import com.hust.mining.util.AttrUtil;
import com.hust.mining.util.CommonUtil;
import com.hust.mining.util.ConvertUtil;

@Service
public class MiningServiceImpl implements MiningService {

    @Autowired
    private SegmentService segmentService;
    @Autowired
    private WebsiteDao websiteDao;
    @Autowired 
    private WeightDao weightDao;

    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(MiningServiceImpl.class);

    //目的是聚类。第一个参数是原始文本，第二个为向量模型的选择，第三个为算法的选择（已经写死了，为canopy）。
    @Override
    public List<List<Integer>> cluster(List<String[]> list, int converterType, int algorithmType,int granularity) {
        // TODO 进行聚类
    	//用于存放结果
    	List<List<Integer>> resultIndexSetList = new ArrayList<List<Integer>>();
    	//移除属性行
    	String[] attrs = list.remove(0);
    	int indexOfTitle = AttrUtil.findIndexOfTitle(attrs);
    	int indexOfTime = AttrUtil.findIndexOfTime(attrs);
        List<String[]> segmentList = segmentService.getSegresult(list,indexOfTitle, 0);
        Convertor convertor = null;
        //判断选择的向量模型的类型
        if (converterType == CONVERTERTYPE.DIGITAL) {
            convertor = new DigitalConvertor();
        } else if (converterType == CONVERTERTYPE.TFIDF) {
            convertor = new TFIDFConvertor();
        }
        convertor.setList(segmentList);
        List<double[]> vectors = convertor.getVector();
        //向量转换完成
        
        //如果选择的是canopy算法
        if (algorithmType == ALGORIRHMTYPE.CANOPY) 
        {
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
             canopy.setThreshold(Config.SIMILARITYTHRESHOLD);
             //开启线程池
             ExecutorService exec = Executors.newSingleThreadExecutor();
             Future<List<List<Integer>>> future = exec.submit(canopy);
             try {
             	//得到聚类结果
                 resultIndexSetList = future.get();
             } catch (Exception e) {
                 logger.error("error occur during clustering by canopy" + e.toString());
                 return null;
             }
		}
        else if (algorithmType == ALGORIRHMTYPE.KMEANS) 
        {
        	 //System.out.println("使用的是KMEANS");
        	 Canopy canopy = new Canopy();
             canopy.setVectors(vectors);
             //相似度方式的选择
           	 canopy.setSimi(new AcrossSimilarity(vectors)); 
             //设置阀值
             canopy.setThreshold(Config.SIMILARITYTHRESHOLD);
             //开启线程池
             ExecutorService exec = Executors.newSingleThreadExecutor();
             Future<List<List<Integer>>> future = exec.submit(canopy);
             try {
             	//得到聚类结果
                 resultIndexSetList = future.get();
                 int k = resultIndexSetList.size();
                 KMeans kmeans = new KMeans();
                 kmeans.setVectors(vectors);
                 kmeans.setIterationTimes(20);
               //相似度方式的选择
                 if (granularity == GRANULARITY.AcrossSimilarity) {
                	 kmeans.setSimi(new AcrossSimilarity(vectors)); 
               	  //System.out.println("选择的是粗粒度AcrossSimilarity");
       			
                 } else if(granularity == GRANULARITY.CosSimilarity){
                	 kmeans.setSimi(new CosSimilarity(vectors));
       			  //System.out.println("选择的是细粒度CosSimilarity");
                 }
                 kmeans.setK(k);
                 
                 ExecutorService exec1 = Executors.newSingleThreadExecutor();
                 Future<List<List<Integer>>> future1 = exec.submit(kmeans);
                 try {
                 	//得到聚类结果
                     resultIndexSetList = future1.get();
                 } catch (Exception e) {
                     logger.error("error occur during clustering by canopy" + e.toString());
                     return null;
                 }
                 
             } catch (Exception e) {
                 logger.error("error occur during clustering by canopy" + e.toString());
                 return null;
             }
		}
        else if (algorithmType == ALGORIRHMTYPE.DBSCAN) 
        {
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
             dbscan.setMinPts(Config.MinPts);
             dbscan.setEps(Config.Eps);
             //开启线程池
             ExecutorService exec = Executors.newSingleThreadExecutor();
             Future<List<List<Integer>>> future = exec.submit(dbscan);
             try {
             	//得到聚类结果
                 resultIndexSetList = future.get();
             } catch (Exception e) {
                 logger.error("error occur during clustering by canopy" + e.toString());
                 return null;
             }
		}
        else {
			logger.error("没有选择任何算法");
			return null;
		}
       
        //重载排序的方法，按照降序。类中数量多的排在前面。
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

    //用于得出orig_count的数据
    @Override
    public List<int[]> count(List<String[]> content, List<String[]> cluster) {
        // TODO Auto-generated method stub
        List<int[]> clusterInt = ConvertUtil.toIntList(cluster); //变成 [2,5,45,89]为一个类簇
        List<int[]> reList = new ArrayList<int[]>();
        for (int i = 0; i < clusterInt.size(); i++) {
            int[] tmpList = clusterInt.get(i); //取第i条数组
            int origIndex = -1;
            String origTime = "9999-12-12 23:59:59";
            for (int j = 0; j < tmpList.length; j++) {
                String[] row = content.get(tmpList[j]); //取它的真实内容
                try {
                    if (origTime.compareTo(row[Index.TIME_INDEX]) > 0) {
                        origTime = row[Index.TIME_INDEX];
                        origIndex = tmpList[j];
                    }
                } catch (Exception e) {
                    logger.error("sth error when count:{}", tmpList[j]);
                }
            }
            if (origIndex == -1) {
                origIndex = tmpList[0];
            }
            int[] item = new int[2];
            item[Index.COUNT_ITEM_INDEX] = origIndex;
            item[Index.COUNT_ITEM_AMOUNT] = tmpList.length;
            reList.add(item);
        }
//        //按照递减进行排序，如果数量相同，按照时间进行排序
//        Collections.sort(reList, new Comparator<int[]>() {
//
//            @Override
//            public int compare(int[] o1, int[] o2) {
//                // TODO Auto-generated method stub
//                int compare = o2[Index.COUNT_ITEM_AMOUNT] - o1[Index.COUNT_ITEM_AMOUNT];
//                if (compare == 0) {
//                    String time1 = content.get(o1[Index.COUNT_ITEM_INDEX])[Index.TIME_INDEX];
//                    String time2 = content.get(o2[Index.COUNT_ITEM_INDEX])[Index.TIME_INDEX];
//                    compare = time1.compareTo(time2);
//                }
//                return compare;
//            }
//        });
        return reList;
    }

    @Override
    public Map<String, Integer> calAttention(Map<String, Integer> map) {
        // TODO Auto-generated method stub
        Map<String, Integer> attention = new HashMap<String, Integer>();
        if (null == map) {
            return attention;
        }
        for (Entry<String, Integer> entry : map.entrySet()) {
            int weight = weightDao.queryWeightByName(entry.getKey().toString());
            int atten = weight * entry.getValue();
            attention.put(entry.getKey(), atten);
        }
        return attention;
    }

    @Override
    public Map<String, Map<String, Map<String, Integer>>> statistic(List<String[]> content, String[] array,
            int interval) {
        // TODO Auto-generated method stub
        Map<String, Map<String, Map<String, Integer>>> map =
                new TreeMap<String, Map<String, Map<String, Integer>>>(new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        return o1.compareTo(o2);
                    }
                });
        //属性行
        String[] attrs = content.remove(0);
        int indexOfUrl = AttrUtil.findIndexOfUrl(attrs);
        int indexOfTime = AttrUtil.findIndexOfTime(attrs);
        
        if (null == array) {
            array = new String[content.size()];
            for (int i = 0; i < content.size(); i++) {
                array[i] = i + "";
            }
        }
        int[] list = ConvertUtil.toIntArray(array);
        for (int item : list) {
            String[] row = content.get(item);
            if (CommonUtil.isEmptyArray(row)) {
                continue;
            }
            Website website = websiteDao.queryByUrl(CommonUtil.getPrefixUrl(row[indexOfUrl]));
            String level = website.getLevel();
            String type = website.getType();
            String timeKey = CommonUtil.getTimeKey(row[indexOfTime], interval);
            Map<String, Map<String, Integer>> timeMap = map.get(timeKey);
            if (timeMap == null) {
                timeMap = new HashMap<String, Map<String, Integer>>();
                Map<String, Integer> typeMap = new HashMap<String, Integer>();
                Map<String, Integer> levelMap = new HashMap<String, Integer>();
                typeMap.put(type, 1);
                levelMap.put(level, 1);
                timeMap.put(Constant.MEDIA_EN, levelMap);
                timeMap.put(Constant.INFOTYPE_EN, typeMap);
                map.put(timeKey, timeMap);
            } else {
                Map<String, Integer> typeMap = timeMap.get(Constant.INFOTYPE_EN);
                if (null == typeMap) {
                    typeMap = new HashMap<String, Integer>();
                    typeMap.put(type, 1);
                } else {
                    if (typeMap.get(type) == null) {
                        typeMap.put(type, 1);
                    } else {
                        typeMap.put(type, typeMap.get(type) + 1);
                    }
                }

                Map<String, Integer> levelMap = timeMap.get(Constant.MEDIA_EN);
                if (null == levelMap) {
                    levelMap = new HashMap<String, Integer>();
                    levelMap.put(level, 1);
                } else {
                    if (levelMap.get(level) == null) {
                        levelMap.put(level, 1);
                    } else {
                        levelMap.put(level, levelMap.get(level) + 1);
                    }
                }
                timeMap.put(Constant.MEDIA_EN, levelMap);
                timeMap.put(Constant.INFOTYPE_EN, typeMap);
                map.put(timeKey, timeMap);
            }
        }
        for (String time : map.keySet()) {
            Map<String, Map<String, Integer>> timeMap = map.get(time);
            Map<String, Integer> mediaAttention = calAttention(timeMap.get(Constant.MEDIA_EN));
            Map<String, Integer> netizenAttention = calAttention(timeMap.get(Constant.INFOTYPE_EN));
            timeMap.put(Constant.NETIZENATTENTION_EN, netizenAttention);
            timeMap.put(Constant.MEDIAATTENTION_EN, mediaAttention);
        }
        return map;
    }

    @Override
    public Map<String, Object> getAmount(Map<String, Map<String, Map<String, Integer>>> map) {
        // TODO Auto-generated method stub
        if (map == null) {
            return null;
        }
        Map<String, Integer> typeAmountMap = new HashMap<String, Integer>();
        for (Map<String, Map<String, Integer>> values : map.values()) {
            Map<String, Integer> typeMap = values.get(Constant.INFOTYPE_EN);
            for (Entry<String, Integer> entry : typeMap.entrySet()) {
                Integer oldValue = typeAmountMap.get(entry.getKey());
                if (null == oldValue) {
                    oldValue = 0;
                }
                typeAmountMap.put(entry.getKey(), entry.getValue() + oldValue);
            }
        }
        Map<String, Integer> mediaAmountMap = new HashMap<String, Integer>();
        for (Map<String, Map<String, Integer>> values : map.values()) {
            Map<String, Integer> mediaMap = values.get(Constant.MEDIA_EN);
            for (Entry<String, Integer> entry : mediaMap.entrySet()) {
                Integer oldValue = mediaAmountMap.get(entry.getKey());
                if (null == oldValue) {
                    oldValue = 0;
                }
                mediaAmountMap.put(entry.getKey(), entry.getValue() + oldValue);
            }
        }
        Map<String, Object> reMap = new HashMap<>();
        reMap.put(KEY.MINING_AMOUNT_MEDIA, mediaAmountMap);
        reMap.put(KEY.MINING_AMOUNT_TYPE, typeAmountMap);
        return reMap;
    }

    /**
     * 统计准数据某个类各个维度信息
     * content 类的所有记录
     */
    @Override
	public Map<String, Map<String, Map<String, Integer>>> statisticStdRes(List<String[]> content, int interval) {
		Map<String, Map<String, Map<String, Integer>>> map =
                new TreeMap<String, Map<String, Map<String, Integer>>>(new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        return o1.compareTo(o2);
                    }
                });
        //属性行
        String[] attrs = content.remove(0);
        int indexOfUrl = AttrUtil.findIndexOfUrl(attrs);
        int indexOfTime = AttrUtil.findIndexOfTime(attrs);
        
        for (String[] row : content) {
           
            if (CommonUtil.isEmptyArray(row)) {
                continue;
            }
            Website website = websiteDao.queryByUrl(CommonUtil.getPrefixUrl(row[indexOfUrl]));
            String level = website.getLevel();
            String type = website.getType();
            String timeKey = CommonUtil.getTimeKey(row[indexOfTime], interval);
            Map<String, Map<String, Integer>> timeMap = map.get(timeKey);
            if (timeMap == null) {
                timeMap = new HashMap<String, Map<String, Integer>>();
                Map<String, Integer> typeMap = new HashMap<String, Integer>();
                Map<String, Integer> levelMap = new HashMap<String, Integer>();
                typeMap.put(type, 1);
                levelMap.put(level, 1);
                timeMap.put(Constant.MEDIA_EN, levelMap);
                timeMap.put(Constant.INFOTYPE_EN, typeMap);
                map.put(timeKey, timeMap);
            } else {
                Map<String, Integer> typeMap = timeMap.get(Constant.INFOTYPE_EN);
                if (null == typeMap) {
                    typeMap = new HashMap<String, Integer>();
                    typeMap.put(type, 1);
                } else {
                    if (typeMap.get(type) == null) {
                        typeMap.put(type, 1);
                    } else {
                        typeMap.put(type, typeMap.get(type) + 1);
                    }
                }

                Map<String, Integer> levelMap = timeMap.get(Constant.MEDIA_EN);
                if (null == levelMap) {
                    levelMap = new HashMap<String, Integer>();
                    levelMap.put(level, 1);
                } else {
                    if (levelMap.get(level) == null) {
                        levelMap.put(level, 1);
                    } else {
                        levelMap.put(level, levelMap.get(level) + 1);
                    }
                }
                timeMap.put(Constant.MEDIA_EN, levelMap);
                timeMap.put(Constant.INFOTYPE_EN, typeMap);
                map.put(timeKey, timeMap);
            }
        }
        for (String time : map.keySet()) {
            Map<String, Map<String, Integer>> timeMap = map.get(time);
            Map<String, Integer> mediaAttention = calAttention(timeMap.get(Constant.MEDIA_EN));
            Map<String, Integer> netizenAttention = calAttention(timeMap.get(Constant.INFOTYPE_EN));
            timeMap.put(Constant.NETIZENATTENTION_EN, netizenAttention);
            timeMap.put(Constant.MEDIAATTENTION_EN, mediaAttention);
        }
        return map;
	}

}
