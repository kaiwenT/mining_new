package com.hust.mining.service.impl;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.hust.mining.constant.Constant.DIRECTORY;
import com.hust.mining.constant.Constant.Index;
import com.hust.mining.constant.Constant.KEY;
import com.hust.mining.dao.IssueDao;
import com.hust.mining.dao.ResultDao;
import com.hust.mining.dao.WebsiteDao;
import com.hust.mining.model.Issue;
import com.hust.mining.model.Result;
import com.hust.mining.model.ResultWithContent;
import com.hust.mining.model.Website;
import com.hust.mining.model.params.StatisticParams;
import com.hust.mining.service.IssueService;
import com.hust.mining.service.MiningService;
import com.hust.mining.service.RedisService;
import com.hust.mining.service.ResultService;
import com.hust.mining.service.UserService;
import com.hust.mining.util.AttrUtil;
import com.hust.mining.util.CommonUtil;
import com.hust.mining.util.ConvertUtil;
import com.hust.mining.util.TimeUtil;

@Service
public class ResultServiceImpl implements ResultService {
    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(ResultServiceImpl.class);

    @Autowired
    private ResultDao resultDao;
    @Autowired
    private IssueDao issueDao;
    @Autowired
    private WebsiteDao websiteDao;
    @Autowired
    private MiningService miningService;
    @Autowired
    private UserService userService;
    @Autowired
    private IssueService issueService;
    @Autowired
    private RedisService redisService;

    @Override
    public String getCurrentResultId(HttpServletRequest request) {
        String result = redisService.getString(KEY.RESULT_ID, request);
        if (result == null) {
            return StringUtils.EMPTY;
        }
        return result;
    }

    @Override
    public List<String[]> getCountResultById(String resultId, String issueId, HttpServletRequest request) {
        // TODO Auto-generated method stub
        List<String[]> modiCount = resultDao.getResultConentById(resultId, issueId, DIRECTORY.MODIFY_COUNT);
        List<String[]> list = new ArrayList<String[]>();
        try {
            List<String[]> content = resultDao.getResultConentById(resultId, issueId, DIRECTORY.CONTENT);
            List<int[]> count = ConvertUtil.toIntList(modiCount);
            List<String[]> cluster = resultDao.getResultConentById(resultId, issueId, DIRECTORY.MODIFY_CLUSTER);
            redisService.setObject(KEY.REDIS_CLUSTER_RESULT, cluster, request);
            redisService.setObject(KEY.REDIS_CONTENT, content, request);
            redisService.setObject(KEY.REDIS_COUNT_RESULT, modiCount, request);
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

    @SuppressWarnings("unchecked")
    @Override
    public boolean deleteSets(int[] sets, HttpServletRequest request) {
        // TODO Auto-generated method stub
        String resultId = redisService.getString(KEY.RESULT_ID, request);
        String issueId = issueService.getCurrentIssueId(request);
        try {
            // 从redis获取数据
            List<String[]> count = (List<String[]>) redisService.getObject(KEY.REDIS_COUNT_RESULT, request);
            List<String[]> cluster = (List<String[]>) redisService.getObject(KEY.REDIS_CLUSTER_RESULT, request);
            // 删除集合
            Arrays.sort(sets);
            for (int i = sets.length - 1; i >= 0; i--) {
                cluster.remove(sets[i]);
                count.remove(sets[i]);
            }
            // 更新redis数据
            redisService.setObject(KEY.REDIS_CLUSTER_RESULT, cluster, request);
            redisService.setObject(KEY.REDIS_COUNT_RESULT, count, request);
            // 写回数据库
            Result result = new Result();
            result.setRid(resultId);
            result.setIssueId(issueId);
            ResultWithContent rc = new ResultWithContent();
            rc.setResult(result);
            rc.setModiCluster(cluster);
            rc.setModiCount(count);
            int update = resultDao.updateResult(rc);
            if (update <= 0) {
                return false;
            }
            String user = userService.getCurrentUser(request);
            Issue issue = new Issue();
            issue.setIssueId(issueId);
            issue.setLastOperator(user);
            issue.setLastUpdateTime(new Date());
            issueDao.updateIssueInfo(issue);
        } catch (Exception e) {
            logger.error("sth failed when delete sets:{}" + e.toString());
            return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean combineSets(int[] sets, HttpServletRequest request) {
        // TODO Auto-generated method stub
        String resultId = redisService.getString(KEY.RESULT_ID, request);
        String issueId = issueService.getCurrentIssueId(request);
        try {
            // 从redis获取数据
            List<String[]> content = (List<String[]>) redisService.getObject(KEY.REDIS_CONTENT, request);
            List<String[]> cluster = (List<String[]>) redisService.getObject(KEY.REDIS_CLUSTER_RESULT, request);
            // 合并集合
            String[] newrow = cluster.get(sets[0]);
            for (int i = 1; i < sets.length; i++) {
                newrow = (String[]) ArrayUtils.addAll(newrow, cluster.get(sets[i]));
            }
            Arrays.sort(sets);
            for (int i = sets.length - 1; i >= 0; i--) {
                cluster.remove(sets[i]);
            }
            cluster.add(newrow);
            Collections.sort(cluster, new Comparator<String[]>() {

                @Override
                public int compare(String[] o1, String[] o2) {
                    // TODO Auto-generated method stub
                    return o2.length - o1.length;
                }
            });
            // TODO:重新统计
            List<int[]> count = miningService.count(content, cluster);
            // 更新数据库
            Result result = new Result();
            result.setRid(resultId);
            result.setIssueId(issueId);
            ResultWithContent rc = new ResultWithContent();
            rc.setResult(result);
            rc.setModiCluster(cluster);
            rc.setModiCount(ConvertUtil.toStringList(count));
            int update = resultDao.updateResult(rc);
            if (update <= 0) {
                return false;
            }
            String user = userService.getCurrentUser(request);
            Issue issue = new Issue();
            issue.setIssueId(issueId);
            issue.setLastOperator(user);
            issue.setLastUpdateTime(new Date());
            issueDao.updateIssueInfo(issue);
            // 更新redis数据
            redisService.setObject(KEY.REDIS_CLUSTER_RESULT, cluster, request);
            redisService.setObject(KEY.REDIS_COUNT_RESULT, count, request);
        } catch (Exception e) {
            logger.error("sth failed when combine sets:{}" + e.toString());
        }
        return true;
    }

    @Override
    public boolean reset(HttpServletRequest request) {
        // TODO Auto-generated method stub
        String resultId = redisService.getString(KEY.RESULT_ID, request);
        String issueId = issueService.getCurrentIssueId(request);
        // 从数据库获取数据
        List<String[]> origCluster = resultDao.getResultConentById(resultId, issueId, DIRECTORY.ORIG_CLUSTER);
        List<String[]> origCount = resultDao.getResultConentById(resultId, issueId, DIRECTORY.ORIG_COUNT);
        // 用原始数据覆盖修改后数据
        Result result = new Result();
        result.setRid(resultId);
        result.setIssueId(issueId);
        ResultWithContent rc = new ResultWithContent();
        rc.setResult(result);
        rc.setModiCluster(origCluster);
        rc.setModiCount(origCount);
        int update = resultDao.updateResult(rc);
        if (update <= 0) {
            return false;
        }
        String user = userService.getCurrentUser(request);
        Issue issue = new Issue();
        issue.setIssueId(issueId);
        issue.setLastOperator(user);
        issue.setLastUpdateTime(new Date());
        issueDao.updateIssueInfo(issue);
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> statistic(StatisticParams params, HttpServletRequest request) {
        // TODO Auto-generated method stub
        try {
            List<String[]> content = (List<String[]>) redisService.getObject(KEY.REDIS_CONTENT, request);
            List<String[]> cluster = (List<String[]>) redisService.getObject(KEY.REDIS_CLUSTER_RESULT, request);
            String[] set = cluster.get(params.getCurrentSet());
            Map<String, Map<String, Map<String, Integer>>> timeMap =
                    miningService.statistic(content, set, params.getInterval());
            Map<String, Object> reMap = miningService.getAmount(timeMap);
            Map<String, Integer> levelMap = (Map<String, Integer>) reMap.get(KEY.MINING_AMOUNT_MEDIA);
            Map<String, Integer> typeMap = (Map<String, Integer>) reMap.get(KEY.MINING_AMOUNT_TYPE);
            Map<String, Object> map = Maps.newHashMap();
            map.put("time", timeMap);
            Map<String, Object> countMap = Maps.newHashMap();
            countMap.put("type", typeMap);
            countMap.put("level", levelMap);
            map.put("count", countMap);
            return map;
        } catch (Exception e) {
            logger.error("exception occur when statistic:{}", e.toString());
        }
        return null;
    }

    @Override
    public int delResultById(String resultId) {
        // TODO Auto-generated method stub
        return resultDao.delResultById(resultId);
    }

    @Override
    public List<Result> queryResultsByIssueId(String issueId) {
        // TODO Auto-generated method stub
        return resultDao.queryResultsByIssueId(issueId);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String[]> exportService(String issueId, String resultId, HttpServletRequest request) {
        // TODO Auto-generated method stub
        try {
            List<String[]> content = (List<String[]>) redisService.getObject(KEY.REDIS_CONTENT, request);
            List<String[]> cluster = new ArrayList<String[]>();
            List<int[]> clusterIndex =
                    ConvertUtil.toIntList((List<String[]>) redisService.getObject(KEY.REDIS_CLUSTER_RESULT, request));
            //保存第一行属性
            String[] attrs = content.get(0);
            content.remove(0);
            for (int[] set : clusterIndex) {
                for (int index : set) {
                    String[] row = content.get(index);
                    cluster.add(row);
                }
                cluster.add(new String[1]);
            }
            //统计类的数量，目前不需要，注释先。
//            List<String[]> count = new ArrayList<String[]>();
//            List<int[]> countResult =
//                    ConvertUtil.toIntList((List<String[]>) redisService.getObject(KEY.REDIS_COUNT_RESULT, request));
//            for (int[] row : countResult) {
//                String[] oldRow = content.get(row[Index.COUNT_ITEM_INDEX]);
//                String[] nRow = new String[oldRow.length + 1];
//                System.arraycopy(oldRow, 0, nRow, 1, oldRow.length);
//                nRow[0] = row[Index.COUNT_ITEM_AMOUNT] + "";
//                count.add(nRow);
//            }
//            Map<String, List<String[]>> map = Maps.newHashMap();
            cluster.add(0, attrs);
            return cluster;
        } catch (Exception e) {
            logger.error("exception occur when get export result:{}", e.toString());
            return null;
        }
    }

    @Override
    public String exportAbstract(List<String[]> count) {
        // TODO Auto-generated method stub
        if (null == count || count.size() == 0) {
            return null;
        }    
        String[] firstData = count.get(0);
        Website firstSite = websiteDao.queryByUrl(CommonUtil.getPrefixUrl(firstData[1]));
        String line = "本次信息挖掘结果中，总共涉及 " + count.size() + " 个话题。\n\n";
        line += "排名前五的话题信息分别是：\n";
        for (int i = 0; i < count.size() && i < 5; i++) {
            String[] data = count.get(i);
            Website website = websiteDao.queryByUrl(CommonUtil.getPrefixUrl(data[1]));
            line += "\t话题名称：" + data[2] + "\n";
            line += "\t信息数量：" + data[0] + "\n";
            line += "\t最早发布时间：" + data[3] + "\n";
            line += "\t最早发布网站：" + website.getName() + "\n";
            line += "\t信息类型为:" + website.getLevel() + "\n\n\n";
        }
        
        return line;
    }

	@Override
	public List<String[]> getClusterResultById( String clusterIndex, String resultId, String issueId, HttpServletRequest request) {
        List<String[]> list = new ArrayList<String[]>();
        String[] indexList = null;
        try {
            List<String[]> content = resultDao.getResultConentById(resultId, issueId, DIRECTORY.CONTENT);
            List<String[]> cluster = resultDao.getResultConentById(resultId, issueId, DIRECTORY.MODIFY_CLUSTER);
        	//clusterIndex是类的Index
            if(Integer.valueOf(clusterIndex) >= cluster.size()){
            	return null;
            }
            indexList = cluster.get(Integer.valueOf(clusterIndex));
            if(indexList == null || indexList.length == 0){
            	return null;
            }
            //---
            for(String index : indexList){            	
                		list.add(content.get(Integer.parseInt(index)+1));
            }
            list.add(0, AttrUtil.findEssentialIndex(content.get(0)));
        } catch (Exception e) {
            logger.error("get count result failed:{}", e.toString());
            e.printStackTrace();
            return null;
        }
        
        return list;
	}

	/**
	 * 删除类中元素
	 * sets id集合
	 */
	@Override
	public boolean deleteClusterItems(String clusterIndex, int[] sets, HttpServletRequest request) {
		String resultId = redisService.getString(KEY.RESULT_ID, request);
        String issueId = issueService.getCurrentIssueId(request);
        try {
            // 从redis获取数据
            List<String[]> count = (List<String[]>) redisService.getObject(KEY.REDIS_COUNT_RESULT, request);
            List<String[]> cluster = (List<String[]>) redisService.getObject(KEY.REDIS_CLUSTER_RESULT, request);
            
            String[] items = cluster.get(Integer.valueOf(clusterIndex));
            String[] clusterCount = count.get(Integer.valueOf(clusterIndex));
            String[] newItems = null;
            //该类元素个数和要删除的个数相同，则直接删除这个类
            if(items.length == sets.length){
            	return deleteSets(new int[]{Integer.valueOf(clusterIndex)}, request);
            }
            //排序、升序
            Arrays.sort(sets);
                        
            //
            if(items != null && items.length != 0 ){

            	//数组转List
            	List<String> clusterList = new ArrayList<String>();
            	for(String s : items){
            		clusterList.add(s);
            	}
            	// 删除集合中指定的一些元素
            	for(int i = sets.length - 1 ; i >= 0 ; i--){
            		clusterList.remove(sets[i]);
            	}
            	//新的类集合
            	newItems = clusterList.toArray(new String[clusterList.size()]);
            	cluster.set(Integer.valueOf(clusterIndex), newItems);
            	
            }
            if(newItems != null && newItems.length != 0){
            	//更新类第一条记录
            	clusterCount[1] = String.valueOf(newItems.length);
            	clusterCount[0] = newItems[0];
            }else if(newItems.length == 0){
            	count.remove(clusterCount);
            }
            // 更新redis数据
            redisService.setObject(KEY.REDIS_CLUSTER_RESULT, cluster, request);
            redisService.setObject(KEY.REDIS_COUNT_RESULT, count, request);
            // 写回数据库
            Result result = new Result();
            result.setRid(resultId);
            result.setIssueId(issueId);
            //System.out.println("shanchu"+resultId+"--"+issueId);
            ResultWithContent rc = new ResultWithContent();
            rc.setResult(result);
            rc.setModiCluster(cluster);
            rc.setModiCount(count);
            int update = resultDao.updateResult(rc);
            if (update <= 0) {
                return false;
            }
            String user = userService.getCurrentUser(request);
            Issue issue = new Issue();
            issue.setIssueId(issueId);
            issue.setLastOperator(user);
            issue.setLastUpdateTime(new Date());
            issueDao.updateIssueInfo(issue);
        } catch (Exception e) {
            logger.error("sth failed when delete sets:{}" + e.toString());
            e.printStackTrace();
            return false;
        }
        return true;
	}

	/**
	 * 重置某个类中元素
	 */
	@Override
	public boolean resetCluster(String index, HttpServletRequest request) {
		// TODO Auto-generated method stub
		int idx = Integer.valueOf(index);
		//要重置的元素在初始聚类结果中的下标
		int origIndex = -1;
		String resultId = redisService.getString(KEY.RESULT_ID, request);
        String issueId = issueService.getCurrentIssueId(request);
        // 从文件系统获取原数据
        List<String[]> origCluster = resultDao.getResultConentById(resultId, issueId, DIRECTORY.ORIG_CLUSTER);
        List<String[]> origCount = resultDao.getResultConentById(resultId, issueId, DIRECTORY.ORIG_COUNT);
    
        // 从文件系统获取修改后的数据
        List<String[]> modiCluster = resultDao.getResultConentById(resultId, issueId, DIRECTORY.MODIFY_CLUSTER);
        List<String[]> modiCount = resultDao.getResultConentById(resultId, issueId, DIRECTORY.MODIFY_COUNT);
       
        if(idx > origCluster.size() || idx < 0){
        	return false;
        }
        //判断类簇序号有没有变化
        String[] items = modiCluster.get(idx);
        loop1:for(int i = 0 ; i < origCluster.size() ; i++){
        	String[] cluster = origCluster.get(i);
        	for(String s1 : items){
        		for(String s2 : cluster){
        			if(s1.equals(s2)){
        				origIndex = i;
        				break loop1;
        			}
        		}
        	}
        }
        if(origIndex == -1){
        	return false;
        }
        //重置被修改的类簇的数据
        modiCluster.set(idx, origCluster.get(origIndex));
        modiCount.set(idx, origCount.get(origIndex));
        
        // 用原始数据覆盖修改后数据
        Result result = new Result();
        result.setRid(resultId);
        result.setIssueId(issueId);
        ResultWithContent rc = new ResultWithContent();
        rc.setResult(result);
        rc.setModiCluster(modiCluster);
        rc.setModiCount(modiCount);
        int update = resultDao.updateResult(rc);
        if (update <= 0) {
            return false;
        }
        String user = userService.getCurrentUser(request);
        Issue issue = new Issue();
        issue.setIssueId(issueId);
        issue.setLastOperator(user);
        issue.setLastUpdateTime(new Date());
        issueDao.updateIssueInfo(issue);
        return true;
	}

	@Override
	public List<Integer> getMarked(List<String[]> cluster) {
		// 返回待标记的id集合
		if(cluster == null || cluster.isEmpty()){
			return null;
		}
		List<Integer> set = new ArrayList<>();
		String[] attrs = cluster.remove(0);		
		
        List<String[]> items = new ArrayList<>();
        for(String[] item : cluster){
        	if(CommonUtil.isEmptyArray(item)){
        		if(items == null){
        			continue;
        		}if(items.size() == 1){
        			set.add(0);
        			continue;
        		}
        		int index = getMarkedIndex(items,attrs);
        		if(index >= 0 ){
        			set.add(index);        			
        		}
        		items = new ArrayList<>();
        	}else{
        		items.add(item);
        	}
        }
        
		return set;
	}

	private int getMarkedIndex(List<String[]> items, String[] attrs) {
		// 返回待标记的id
		int indexOfUrl = AttrUtil.findIndexOfUrl(attrs);
        int indexOfTime = AttrUtil.findIndexOfTime(attrs);
        int indexOfType = AttrUtil.findIndexOfWebName(attrs);
        //最早的记录下标
        int earliest = 0;
		for(int i = 0 ; i < items.size() ; i++){
			String[] item = items.get(i);
			
			if(TimeUtil.compare_date(item[indexOfTime], items.get(earliest)[indexOfTime]) < 0){
				earliest = i;
			}
			String type = "";
			if(item.length >= indexOfUrl){
				//获取url前缀，并在数据库中查询该URL类型
				 type = websiteDao.queryTypeByUrl(CommonUtil.getPrefixUrl(item[indexOfUrl]));
			}
			
			if(StringUtils.isEmpty(type)){
				;
			}
			if(type.equals("报纸")){
				return i;
			}
			if(item[indexOfType] != null && item[indexOfType].equals("报纸")){
				return i;
			}
		}
		return earliest;
	}

}
