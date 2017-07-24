package com.hust.mining.dao;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.common.io.Files;
import com.hust.mining.constant.Constant.DIRECTORY;
import com.hust.mining.dao.mapper.StandardResultMapper;
import com.hust.mining.dao.mapper.WebsiteMapper;
import com.hust.mining.model.StandardResult;
import com.hust.mining.model.StandardResultExample;
import com.hust.mining.model.Website;
import com.hust.mining.model.WebsiteExample;
import com.hust.mining.model.WebsiteExample.Criteria;
import com.hust.mining.model.params.StandardResultQueryCondition;
import com.hust.mining.util.AttrUtil;
import com.hust.mining.util.CommonUtil;
import com.hust.mining.util.FileUtil;

@Repository
public class StandardResultDao {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(StandardResultDao.class);

	@Autowired
	private StandardResultMapper standardResultMapper;
	@Autowired
	private WebsiteMapper websiteMapper;

	public int insert(StandardResultQueryCondition con, StandardResult standardResult) {
		String from = DIRECTORY.MODIFY_CLUSTER + con.getResId();
		String to = DIRECTORY.STDRES_CLUSTER + standardResult.getStdRid();
		try {
			Files.copy(new File(from), new File(to));
		} catch (IOException e) {
			logger.error("create standard result cluster from {} failed", from);
			return 0;
		}
		from = DIRECTORY.MODIFY_COUNT + con.getResId();
		to = DIRECTORY.STDRES_COUNT + standardResult.getStdRid();
		try {
			Files.copy(new File(from), new File(to));
		} catch (IOException e) {
			logger.error("create standard result cluster from {} failed", from);
			return 0;
		}

		return standardResultMapper.insert(standardResult);
	}

	public int insert(StandardResult standardResult) {		
		return standardResultMapper.insert(standardResult);
	}
	
	public int deleteById(String stdResId) {
		if (!FileUtil.delete(DIRECTORY.STDRES_CLUSTER + stdResId)
				|| !FileUtil.delete(DIRECTORY.STDRES_COUNT + stdResId)) {
			logger.error("delete standard cluster or count fail.");
		}
		return standardResultMapper.deleteByPrimaryKey(stdResId);
	}

	public StandardResult queryStdResById(String stdResId) {
		return standardResultMapper.selectByPrimaryKey(stdResId);
	}

	public List<StandardResult> queryStdRessByIssueId(String issueId) {
		StandardResultExample example = new StandardResultExample();
		example.createCriteria().andIssueIdEqualTo(issueId);
		example.setOrderByClause("create_time desc");
		return standardResultMapper.selectByExample(example);
	}

	public int updateByPrimaryKey(StandardResult record) {
		return standardResultMapper.updateByPrimaryKey(record);
	}

	public List<String[]> getContentById(String path, String name) {
		return FileUtil.read(path + name);
	}

	/**
	 * 统计准数据里的日期数量
	 * 
	 * @param cluster
	 *            准数据
	 * @return
	 */
	@SuppressWarnings("unused")
	public String dateCount(List<String[]> cluster) {
		int indexOfTime = AttrUtil.findIndexOfTime(cluster.get(0));
		// 按照key值升序排列
		TreeMap<String, Integer> map = new TreeMap<String, Integer>(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		});

		for (int i = 1; i < cluster.size(); i++) {
			try{
				String[] tmp = cluster.get(i);
				if (CommonUtil.isEmptyArray(tmp) || StringUtils.isBlank(tmp[indexOfTime])) {
					continue;
				}
				String currentTime = tmp[indexOfTime].trim().substring(0, 10);
				Integer oldValue = map.get(currentTime);
				if (oldValue != null) {
					map.replace(currentTime, oldValue, oldValue + 1);
				} else {
					map.put(currentTime, 1);
				}
			}catch(Exception e){
				logger.error("当前列 "+i+" 时间为空：{}",e.toString());
				continue;
			}
		}
		String res = map.toString();
		return res.substring(1, res.length() - 1);
	}

	/**
	 * 统计准数据里的来源数量
	 * 
	 * @param cluster
	 *            准数据
	 * @return
	 */
	@SuppressWarnings("unused")
	public String sourceCount(List<String[]> cluster) {
		// 按照key值升序排列
		TreeMap<String, Integer> map = new TreeMap<String, Integer>(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		});

		int indexOfSource = AttrUtil.findIndexOfSth(cluster.get(0), "来源");
		int indexOfType = AttrUtil.findIndexOfSth(cluster.get(0), "资源类型");
		int indexOfUrl = AttrUtil.findIndexOfUrl(cluster.get(0));
		int i = 1;
		try {
			for (i = 1; i < cluster.size(); i++) {
				String[] tmp = cluster.get(i);
				if (CommonUtil.isEmptyArray(tmp)) {
					continue;
				}
				String currentSource = "其他";
				if (indexOfSource != -1 && !StringUtils.isBlank(tmp[indexOfSource])) {
					currentSource = tmp[indexOfSource];
				} else if (indexOfType != -1 && !StringUtils.isBlank(tmp[indexOfType])) {
					currentSource = tmp[indexOfType];
				} else if (indexOfUrl != -1) {
					WebsiteExample example = new WebsiteExample();
					Criteria criteria = example.createCriteria();
					criteria.andUrlEqualTo(CommonUtil.getPrefixUrl(tmp[indexOfUrl]));
					List<Website> list = websiteMapper.selectByExample(example);
					if (list.size() != 0) {
						currentSource = list.get(0).getType();
					}
				}
				if (currentSource.length() > 2) {
					if (currentSource.equals("新浪微博")) {
						currentSource = "微博";
					} else {
						currentSource = "其他";
					}
				} else if(currentSource.equals("资讯")){
					currentSource = "新闻";
				}

				Integer oldValue = map.get(currentSource);
				if (oldValue != null) {
					map.replace(currentSource, oldValue, oldValue + 1);
				} else {
					map.put(currentSource, 1);
				}
			}
		} catch (Exception e) {
			logger.error("统计准数据里的来源数量出错！");
		}

		String res = map.toString();
		return res.substring(1, res.length() - 1);
	}

}
