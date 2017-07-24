package com.hust.mining.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.util.ArrayUtil;
import org.apache.tools.ant.types.resources.selectors.Compare;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.sql.visitor.functions.If;
import com.hust.mining.constant.Constant;
import com.hust.mining.constant.Constant.CONVERTERTYPE;
import com.hust.mining.constant.Constant.DIRECTORY;
import com.hust.mining.constant.Constant.Index;
import com.hust.mining.constant.Constant.KEY;
import com.hust.mining.dao.FileDao;
import com.hust.mining.dao.IssueDao;
import com.hust.mining.dao.ResultDao;
import com.hust.mining.model.CoreResult;
import com.hust.mining.model.Issue;
import com.hust.mining.model.IssueFile;
import com.hust.mining.model.Result;
import com.hust.mining.model.ResultWithContent;
import com.hust.mining.model.StandardResult;
import com.hust.mining.model.User;
import com.hust.mining.model.params.CoreResultQueryCondition;
import com.hust.mining.model.params.IssueQueryCondition;
import com.hust.mining.model.params.QueryFileCondition;
import com.hust.mining.model.params.StandardResultQueryCondition;
import com.hust.mining.service.CoreResultService;
import com.hust.mining.service.IssueService;
import com.hust.mining.service.MiningService;
import com.hust.mining.service.RedisService;
import com.hust.mining.service.ResultService;
import com.hust.mining.service.StandardResultService;
import com.hust.mining.service.UserService;
import com.hust.mining.util.AttrUtil;
import com.hust.mining.util.ConvertUtil;
import com.hust.mining.util.FileUtil;

@Service
public class IssueServiceImpl implements IssueService {
	@Autowired
	private IssueDao issueDao;
	@Autowired
	private FileDao fileDao;
	@Autowired
	private UserService userService;
	@Autowired
	private MiningService miningService;
	@Autowired
	private StandardResultService standardResultService;
	@Autowired
	private CoreResultService coreResultService;
	@Autowired
	private ResultService resultService;
	@Autowired
	private ResultDao resultDao;
	@Autowired
	private RedisService redisService;

	// 创建任务
	@Override
	public int createIssue(String issueName, String issueType, HttpServletRequest request) {
		// TODO Auto-generated method stub
		String user = userService.getCurrentUser(request);
		Issue issue = new Issue();
		issue.setIssueId(UUID.randomUUID().toString());
		issue.setIssueName(issueName);
		issue.setIssueType(issueType);
		issue.setCreator(user);
		issue.setCreateTime(new Date());
		issue.setLastOperator(user);
		issue.setLastUpdateTime(issue.getCreateTime());
		int insert = issueDao.insert(issue);
		if (insert > 0) {
			redisService.setString(KEY.ISSUE_ID, issue.getIssueId(), request);
		}
		// System.out.println(insert);
		return insert;
	}

	@Override
	public int createIssueWithLink(String linkedIssueId, String issueType, String stdResId,
			HttpServletRequest request) {
		String target = null;
		String replacement = null;
		if (issueType.equals(Constant.ISSUETYPE_STANDARD)) {
			target = Constant.ISSUE_EXTENSIVE;
			replacement = Constant.ISSUE_STANDARD;
		} else if (issueType.equals(Constant.ISSUETYPE_CORE)) {
			target = Constant.ISSUE_STANDARD;
			replacement = Constant.ISSUE_CORE;
		} else {
			return 0;
		}

		Issue linkedIssue = this.queryIssueById(linkedIssueId);
		String issueName = linkedIssue.getIssueName().replace(target, replacement);
		Issue issue = null;
		if (StringUtils.isBlank(linkedIssue.getIssueHold())) {
			String user = userService.getCurrentUser(request);
			issue = new Issue();
			issue.setIssueId(UUID.randomUUID().toString());
			issue.setIssueName(issueName);
			issue.setIssueType(issueType);
			issue.setIssueBelongTo(linkedIssueId);
			issue.setCreator(user);
			issue.setCreateTime(new Date());
			issue.setLastOperator(user);
			issue.setLastUpdateTime(issue.getCreateTime());
			issueDao.insert(issue);
			// 更新linkedIssue信息：添加IssueHold
			linkedIssue.setIssueHold(issue.getIssueId());
			// 不更新对应泛数据的last operate time。如果需要，则改为 issueService.
			issueDao.updateIssueInfo(linkedIssue);
		} else {
			issue = queryIssueById(linkedIssue.getIssueHold());
			// 主要为了更新插入新的standardResult此时的issue last operate time。
			this.updateIssueInfo(issue, request);
		}

		if (issueType.equals(Constant.ISSUETYPE_STANDARD)) {
			return insertStdRes(issue, request);
		} else {
			return insertCoreRes(issue, stdResId, request);
		}
	}

	@Override
	public String createCoreIssue(String linkedIssueId, String stdResId, HttpServletRequest request) {
		Issue linkedIssue = this.queryIssueById(linkedIssueId);
		String issueName = linkedIssue.getIssueName().replace(Constant.ISSUE_STANDARD, Constant.ISSUE_CORE);
		Issue issue = null;
		if (StringUtils.isBlank(linkedIssue.getIssueHold())) {
			String user = userService.getCurrentUser(request);
			issue = new Issue();
			issue.setIssueId(UUID.randomUUID().toString());
			issue.setIssueName(issueName);
			issue.setIssueType(Constant.ISSUETYPE_CORE);
			issue.setIssueBelongTo(linkedIssueId);
			issue.setCreator(user);
			issue.setCreateTime(new Date());
			issue.setLastOperator(user);
			issue.setLastUpdateTime(issue.getCreateTime());
			issueDao.insert(issue);
			// 更新linkedIssue信息：添加IssueHold
			linkedIssue.setIssueHold(issue.getIssueId());
			// 不更新对应泛数据的last operate time。如果需要，则改为 issueService.
			issueDao.updateIssueInfo(linkedIssue);
		} else {
			issue = queryIssueById(linkedIssue.getIssueHold());
			// 主要为了更新插入新的standardResult此时的issue last operate time。
			this.updateIssueInfo(issue, request);
		}

		return insertCore(issue, stdResId, request);
	}

	// 核心任务结果数据
	private String insertCore(Issue issue, String stdResId, HttpServletRequest request) {
		StandardResult standardResult = standardResultService.queryStdResById(stdResId);
		if (StringUtils.isBlank(standardResult.getDateCount())) {
			List<String[]> cluster = standardResultService.getStdResContentById(stdResId);
			if (cluster == null) {
				return null;
			}
			String dateCount = standardResultService.getDateCount(cluster);
			String srcCount = standardResultService.getSourceCount(cluster);
			standardResult.setDateCount(dateCount);
			standardResult.setSourceCount(srcCount);
			standardResultService.updateByPrimaryKey(standardResult);
		}

		CoreResultQueryCondition coreResultQueryCondition = new CoreResultQueryCondition();
		coreResultQueryCondition.setIssueId(issue.getIssueId());

		List<CoreResult> list = coreResultService.queryCoreRessByIssueId(issue.getIssueId());
		if (!list.isEmpty()) {
			coreResultQueryCondition.setCoreResId(list.get(0).getCoreRid());
			int update = coreResultService.update(coreResultQueryCondition, request);

			return list.get(0).getCoreRid();
		} else {
			// 给改核心数据结果取名
			coreResultQueryCondition.setCoreResName(standardResult.getResName());
			coreResultQueryCondition.setCoreResName(issue.getIssueName());
			// 这个属性不在core_result字段里。但是为了生成核心数据需要
			coreResultQueryCondition.setStdResId(stdResId);
			return coreResultService.insertCore(coreResultQueryCondition,standardResult.getContentName(), request);
		}
	}

	private int insertStdRes(Issue issue, HttpServletRequest request) {
		StandardResultQueryCondition standardResultQueryCondition = new StandardResultQueryCondition();
		standardResultQueryCondition.setIssueId(issue.getIssueId());
		// 给该准数据结果取名，等谈凯修改完result再改名
		standardResultQueryCondition.setStdResName(issue.getIssueName());
		String currentResultId = resultService.getCurrentResultId(request);
		if (StringUtils.isBlank(currentResultId)) {
			return -1;
		}
		// setResId为设置standard_result的content_name属性所用
		standardResultQueryCondition.setResId(currentResultId);
		int insert = standardResultService.insert(standardResultQueryCondition, request);
		if (insert > 0) {
			redisService.setString(KEY.ISSUE_ID, issue.getIssueId(), request);
		}
		return insert;
	}

	private int insertCoreRes(Issue issue, String stdResId, HttpServletRequest request) {
		StandardResult standardResult = standardResultService.queryStdResById(stdResId);
		if (StringUtils.isBlank(standardResult.getDateCount())) {
			List<String[]> cluster = standardResultService.getStdResContentById(stdResId);
			if (cluster == null) {
				return 0;
			}
			String dateCount = standardResultService.getDateCount(cluster);
			String srcCount = standardResultService.getSourceCount(cluster);
			standardResult.setDateCount(dateCount);
			standardResult.setSourceCount(srcCount);
			standardResultService.updateByPrimaryKey(standardResult);
		}

		CoreResultQueryCondition coreResultQueryCondition = new CoreResultQueryCondition();
		coreResultQueryCondition.setIssueId(issue.getIssueId());
		// 给改核心数据结果取名
		coreResultQueryCondition.setCoreResName(standardResult.getResName());
		coreResultQueryCondition.setCoreResName(issue.getIssueName());
		// 这个属性不在core_result字段里。但是为了生成核心数据需要
		coreResultQueryCondition.setStdResId(stdResId);
		int insert = coreResultService.insert(coreResultQueryCondition, request);
		if (insert > 0) {
			redisService.setString(KEY.ISSUE_ID, issue.getIssueId(), request);
		}
		return insert;
	}

	@Override
	public String getCurrentIssueId(HttpServletRequest request) {
		// TODO Auto-generated method stub
		String obj = redisService.getString(KEY.ISSUE_ID, request);
		if (null == obj) {
			return StringUtils.EMPTY;
		}
		return obj;
	}

	@Override
	public List<Issue> queryIssue(IssueQueryCondition con) {
		// TODO Auto-generated method stub
		List<Issue> list = issueDao.queryIssue(con);
		// System.out.println(list.size() + "service");
		return list;
	}

	@Override
	public Issue queryIssueById(String uuid) {
		// TODO Auto-generated method stub
		return issueDao.selectById(uuid);
	}

	@Override
	public int updateIssueInfo(Issue issue, HttpServletRequest request) {
		// TODO Auto-generated method stub
		String user = userService.getCurrentUser(request);
		issue.setLastOperator(user);
		issue.setLastUpdateTime(new Date());
		return issueDao.updateIssueInfo(issue);
	}

	@Override
	public int deleteIssueById(String issueId, String issueType, HttpServletRequest request) {
		String user = userService.getCurrentUser(request);

		Issue issue = queryIssueById(issueId);
		if (issueType.equals(Constant.ISSUETYPE_EXTENSIVE)) {
			deleteExtensiveIssue(issueId, issue.getIssueHold());
		} else if (issueType.equals(Constant.ISSUETYPE_STANDARD)) {
			deleteStandardIssue(issueId, issue.getIssueBelongTo(), issue.getIssueHold());
		} else {
			deleteCoreIssue(issueId, issue.getIssueBelongTo());
		}

		return issueDao.deleteIssueById(issueId, user);
	}

	@Override
	public int deleteIssueAllById(String issueId, HttpServletRequest request) {
		String user = userService.getCurrentUser(request);
		
		int del1 = 1,del2 = 1, del3 = 1;
		
		Issue issue = queryIssueById(issueId);
		deleteExtensiveIssue(issueId, issue.getIssueHold());
		del1 = issueDao.deleteIssueById(issueId, user);
		
		if(!StringUtils.isBlank(issue.getIssueHold())){
			issue = queryIssueById(issue.getIssueHold());
			deleteStandardIssueAnd(issue.getIssueId(), issue.getIssueBelongTo(), issue.getIssueHold());
			del2 = issueDao.deleteIssueById(issue.getIssueId(), user);
			if(!StringUtils.isBlank(issue.getIssueHold())){
				issue = queryIssueById(issue.getIssueHold());
				deleteCoreIssue(issue.getIssueId(), issue.getIssueBelongTo());
				del3 = issueDao.deleteIssueById(issue.getIssueId(), user);
			}
		}
		
		if(del1 > 0 && del2 > 0 && del2 > 0){
			return 1;
		}
		
		return 0;
	}

	private void deleteExtensiveIssue(String issueId, String holdIssueId) {
		List<Result> resList = resultService.queryResultsByIssueId(issueId);

		// 若该泛数据不存在对应的准数据则返回false
		boolean holdExists = !StringUtils.isBlank(holdIssueId);

		List<String> delList = new ArrayList<String>();
		for (Result result : resList) {
			if (holdExists) {
				// 保留content,若该泛数据对应的准数据还需要的话则不删除
				delList.add(result.getRid());
			} else {
				FileUtil.delete(DIRECTORY.CONTENT + result.getRid());
			}
			FileUtil.delete(DIRECTORY.MODIFY_CLUSTER + result.getRid());
			FileUtil.delete(DIRECTORY.MODIFY_COUNT + result.getRid());
			FileUtil.delete(DIRECTORY.ORIG_CLUSTER + result.getRid());
			FileUtil.delete(DIRECTORY.ORIG_COUNT + result.getRid());
		}
		// 删除上传文件 upload
		List<IssueFile> files = fileDao.queryFilesByIssueId(issueId);
		for (IssueFile file : files) {
			FileUtil.delete(DIRECTORY.FILE + file.getFileId());
		}

		if (holdExists) {
			List<StandardResult> stdResList = standardResultService.queryStdRessByIssueId(holdIssueId);
			Set<String> remainSet = new HashSet<String>();
			for (StandardResult result : stdResList) {
				remainSet.add(result.getContentName());
			}

			for (String del : delList) {
				if (!remainSet.contains(del)) {
					FileUtil.delete(DIRECTORY.CONTENT + del);
				}
			}

			// 更新与之相连的准数据的issue_belong_to值
			Issue holdIssue = queryIssueById(holdIssueId);
			holdIssue.setIssueBelongTo(StringUtils.EMPTY);
			issueDao.updateIssueInfo(holdIssue);
		}
	}
	
	//删除standardIssue并且删除 文件系统内的 standard_content
	private void deleteStandardIssueAnd(String issueId, String belongToIssueId, String holdIssueId) {
		List<StandardResult> stdResList = standardResultService.queryStdRessByIssueId(issueId);

		boolean belongToExists = !StringUtils.isBlank(belongToIssueId);
		List<String> delList = new ArrayList<String>();
		for (StandardResult standardResult : stdResList) {
			if (belongToExists) {
				delList.add(standardResult.getContentName());
			} else {
				FileUtil.delete(DIRECTORY.CONTENT + standardResult.getContentName());
			}
			FileUtil.delete(DIRECTORY.STDRES_CONTENT + standardResult.getContentName());
			FileUtil.delete(DIRECTORY.STDRES_CLUSTER + standardResult.getStdRid());
			FileUtil.delete(DIRECTORY.STDRES_COUNT + standardResult.getStdRid());
		}

		if (belongToExists) {
			List<Result> resList = resultService.queryResultsByIssueId(belongToIssueId);
			Set<String> remainSet = new HashSet<String>();
			for (Result result : resList) {
				remainSet.add(result.getRid());
			}

			for (String del : delList) {
				if (!remainSet.contains(del)) {
					FileUtil.delete(DIRECTORY.CONTENT + del);
				}
			}

			// 更新与之相连的准数据的issue_hold值
			Issue belongToIssue = queryIssueById(belongToIssueId);
			belongToIssue.setIssueHold(StringUtils.EMPTY);
			issueDao.updateIssueInfo(belongToIssue);
		}

		if (!StringUtils.isBlank(holdIssueId)) {
			Issue holdIssue = queryIssueById(holdIssueId);
			holdIssue.setIssueBelongTo(StringUtils.EMPTY);
			issueDao.updateIssueInfo(holdIssue);
		}
	}

	private void deleteStandardIssue(String issueId, String belongToIssueId, String holdIssueId) {
		List<StandardResult> stdResList = standardResultService.queryStdRessByIssueId(issueId);

		boolean belongToExists = !StringUtils.isBlank(belongToIssueId);
		List<String> delList = new ArrayList<String>();
		for (StandardResult standardResult : stdResList) {
			if (belongToExists) {
				delList.add(standardResult.getContentName());
			} else {
				FileUtil.delete(DIRECTORY.CONTENT + standardResult.getContentName());
			}
			FileUtil.delete(DIRECTORY.STDRES_CLUSTER + standardResult.getStdRid());
			FileUtil.delete(DIRECTORY.STDRES_COUNT + standardResult.getStdRid());
		}

		if (belongToExists) {
			List<Result> resList = resultService.queryResultsByIssueId(belongToIssueId);
			Set<String> remainSet = new HashSet<String>();
			for (Result result : resList) {
				remainSet.add(result.getRid());
			}

			for (String del : delList) {
				if (!remainSet.contains(del)) {
					FileUtil.delete(DIRECTORY.CONTENT + del);
				}
			}

			// 更新与之相连的准数据的issue_hold值
			Issue belongToIssue = queryIssueById(belongToIssueId);
			belongToIssue.setIssueHold(StringUtils.EMPTY);
			issueDao.updateIssueInfo(belongToIssue);
		}

		if (!StringUtils.isBlank(holdIssueId)) {
			Issue holdIssue = queryIssueById(holdIssueId);
			holdIssue.setIssueBelongTo(StringUtils.EMPTY);
			issueDao.updateIssueInfo(holdIssue);
		}
	}

	private void deleteCoreIssue(String issueId, String belongToIssueId) {
		if (!StringUtils.isBlank(belongToIssueId)) {
			Issue belongToIssue = queryIssueById(belongToIssueId);
			if (belongToIssue != null) {
				belongToIssue.setIssueHold(StringUtils.EMPTY);
				issueDao.updateIssueInfo(belongToIssue);
			}

			// 删除该核心issue下的所有核心数据
			List<CoreResult> coreResList = coreResultService.queryCoreRessByIssueId(issueId);
			for (CoreResult coreResult : coreResList) {
				FileUtil.delete(DIRECTORY.CORERES + coreResult.getCoreRid());
			}
		}
	}

	// 把某个时间段的文件聚类
	@SuppressWarnings("unchecked")
	@Override
	public List<String[]> miningByTime(Date start, Date end, HttpServletRequest request) {
		// TODO Auto-generated method stub
		String issueId = redisService.getString(KEY.ISSUE_ID, request);
		QueryFileCondition con = new QueryFileCondition();
		con.setIssueId(issueId);
		con.setStart(start);
		con.setEnd(end);
		List<IssueFile> files = fileDao.queryFilesByCondition(con); // 待聚类文件list
		String[] filenames = new String[files.size()];
		for (int i = 0; i < files.size(); i++) {
			filenames[i] = DIRECTORY.FILE + files.get(i).getFileId();
		}
		List<String[]> content = fileDao.getFileContent(filenames);
		if (null == content) {
			return null;
		}
		String user = userService.getCurrentUser(request);
		List<User> users = userService.selectSingleUserInfo(user, request);
		User user2 = users.get(0);
		int granularity = user2.getGranularity();
		int algorithmType = user2.getAlgorithm();
		// 选择了0-1模型
		Map<String, Object> res = mining(content, CONVERTERTYPE.DIGITAL, algorithmType, granularity);
		if (null == res) {
			return null;
		}
		// 开始插入数据库

		content = (List<String[]>) res.get("content");
		List<int[]> count = (List<int[]>) res.get("countResult");
		List<List<Integer>> cluster = (List<List<Integer>>) res.get("clusterResult");

		Result result = new Result();
		result.setRid(UUID.randomUUID().toString());
		result.setIssueId(issueId);
		result.setCreator(user);
		result.setCreateTime(new Date());
		ResultWithContent rc = new ResultWithContent();
		rc.setResult(result);
		rc.setContent(content);
		rc.setOrigCluster(ConvertUtil.toStringListB(cluster));
		rc.setOrigCount(ConvertUtil.toStringList(count));
		int update = resultDao.insert(rc);
		if (update <= 0) {
			return null;
		}
		// 插入数据库完成

		// 开始更新issue状态
		redisService.setString(KEY.RESULT_ID, result.getRid(), request);
		Issue issue = new Issue();
		issue.setIssueId(issueId);
		issue.setLastOperator(user);
		issue.setLastUpdateTime(new Date());
		issueDao.updateIssueInfo(issue);
		// 状态更新完成

		// 开始根据统计结果映射源数据
		List<String[]> list = new ArrayList<String[]>();
		for (int[] array : count) {
			String[] old = content.get(array[Index.COUNT_ITEM_INDEX]);
			String[] row = new String[old.length + 1];
			System.arraycopy(old, 0, row, 1, old.length);
			row[0] = array[Index.COUNT_ITEM_AMOUNT] + "";
			list.add(row);
		}
		// 映射完成
		redisService.setObject(KEY.REDIS_CONTENT, content, request);
		redisService.setObject(KEY.REDIS_CLUSTER_RESULT, rc.getOrigCluster(), request);
		redisService.setObject(KEY.REDIS_COUNT_RESULT, rc.getOrigCount(), request);
		return list;
	}

	// 多文件去重并聚类
	@SuppressWarnings("unchecked")
	@Override
	public List<String[]> miningByFileIds(List<String> fileIds, HttpServletRequest request) {
		// TODO Auto-generated method stub
		String user = userService.getCurrentUser(request);
		String issueId = redisService.getString(KEY.ISSUE_ID, request);
		QueryFileCondition con = new QueryFileCondition();
		con.setIssueId(issueId);
		con.setFileIds(fileIds);
		List<IssueFile> files = fileDao.queryFilesByCondition(con);
		// 获取upload文件夹下的需要聚类的文件名
		String[] filenames = new String[files.size()];
		for (int i = 0; i < files.size(); i++) {
			filenames[i] = DIRECTORY.FILE + files.get(i).getFileId();
		}
		// 读取需要聚类文件的内容，已把标题栏做合并处理
		List<String[]> contentWithAttr = fileDao.getFileContent(filenames);

		if (null == contentWithAttr) {
			return null;
		}
		// 开始去重
		String[] attrs = contentWithAttr.get(0);
		int indexOfUrl = AttrUtil.findIndexOfUrl(attrs);
		List<String> exitUrls = new ArrayList<String>();
		List<String[]> filteredContent = new ArrayList<String[]>();
		for (int i = 1; i < contentWithAttr.size(); i++) {
			String[] row = contentWithAttr.get(i);
			if (exitUrls.indexOf(row[indexOfUrl]) == -1) {
				filteredContent.add(row);
				exitUrls.add(row[indexOfUrl]);
			}
		}
		filteredContent.add(0, attrs);

		List<User> users = userService.selectSingleUserInfo(user, request);
		User user2 = users.get(0);
		int granularity = user2.getGranularity();
		int algorithmType = user2.getAlgorithm();
		// 去重结束

		Map<String, Object> res = mining(filteredContent, CONVERTERTYPE.DIGITAL, algorithmType, granularity);
		if (null == res) {
			return null;
		}
		// 开始插入数据库
		contentWithAttr = (List<String[]>) res.get("content");
		List<int[]> count = (List<int[]>) res.get("countResult");
		List<List<Integer>> cluster = (List<List<Integer>>) res.get("clusterResult");
		String comment = "";
		for (int i = 0; i < files.size(); i++) {
			if (i == 0) {
				comment = files.get(i).getFileName();
			} else {
				comment += "\t" + files.get(i).getFileName();
			}
		}
		Result result = new Result();
		result.setIssueId(issueId);
		result.setCreator(user);
		// 寻找数据库中同一个人创建的任务是否有过聚类记录
		List<Result> resultslist = resultDao.selectByissueIdAndUser(issueId, user);
		// 如果有记录，则rid不变；否则，生成新的
		if (resultslist == null) {
			result.setRid(UUID.randomUUID().toString());
		} else {
			result.setRid(resultslist.get(0).getRid());
		}
		result.setCreateTime(new Date());
		result.setComment(comment);
		// 以上是数据库的记录，以下是聚类的结果
		ResultWithContent rc = new ResultWithContent();
		rc.setResult(result);
		rc.setContent(contentWithAttr);
		rc.setOrigCluster(ConvertUtil.toStringListB(cluster));
		rc.setOrigCount(ConvertUtil.toStringList(count));
		int operateResult = 0;
		if (resultslist == null) { // 没有查找到记录
			System.out.println("*********数据中没有");
			operateResult = resultDao.insert(rc);
		} else {
			System.out.println("***************有");
			operateResult = resultDao.update(rc);
		}
		if (operateResult <= 0) {
			return null;
		}
		// 插入数据库完成

		// 开始更新issue状态
		redisService.setString(KEY.RESULT_ID, result.getRid(), request);
		Issue issue = new Issue();
		issue.setIssueId(issueId);
		issue.setLastOperator(user);
		issue.setLastUpdateTime(new Date());
		issueDao.updateIssueInfo(issue);
		// 状态更新完成

		// 开始根据统计结果映射源数据
		List<String[]> list = new ArrayList<String[]>();
		for (int[] array : count) {
			String[] old = contentWithAttr.get(array[Index.COUNT_ITEM_INDEX]);
			String[] row = new String[old.length + 1];
			System.arraycopy(old, 0, row, 1, old.length);
			row[0] = array[Index.COUNT_ITEM_AMOUNT] + "";
			list.add(row);
		}
		// 映射完成
		redisService.setObject(KEY.REDIS_CONTENT, contentWithAttr, request);
		redisService.setObject(KEY.REDIS_CLUSTER_RESULT, rc.getOrigCluster(), request);
		redisService.setObject(KEY.REDIS_COUNT_RESULT, rc.getOrigCount(), request);
		list.add(0, AttrUtil.findEssentialIndex(attrs));
		return list;
	}

	// 标明 向量的装换方式，和算法。
	/**
	 * 选择算法和向量模型，对给定集合按标题排序后进行聚类
	 * 
	 * @param content
	 *            需要聚类的集合（包含属性）
	 * @param converterType
	 *            向量模型 （0-1或TF-IDF）
	 * @param algorithmType
	 *            算法（canopy、kmeans等等）
	 * @param granularity
	 *            精度（粗or细）
	 * @return 返回
	 *         排序后的集合，聚类的结果（List<String[]>所有类（类内记录下标，用空格隔开）），类的信息（List<int[]>类内最早的一条记录index，类记录个数）
	 */
	private Map<String, Object> mining(List<String[]> content, int converterType, int algorithmType, int granularity) {
		if (content == null || content.size() == 0) {
			return null;
		}
		// 将属性存储起来，因为下面cluster会删除属性
		String[] attrs = content.get(0);
		// 对聚类内容按标题排序
		int titleIndex = AttrUtil.findIndexOfTitle(attrs);
		content = resortContent(content, titleIndex);
		for (String[] string : content) {
			// System.out.println(string[titleIndex]);
		}
		// 聚类
		List<List<Integer>> clusterResult = miningService.cluster(content, converterType, algorithmType, granularity);
		for (List<Integer> list : clusterResult) {
			for (Integer integer : list) {
				// System.out.print(integer + " ");
			}
			// System.out.println();
		}
		// 每个String[]都是某个类簇的数据ID的集合。
		List<String[]> cluster = ConvertUtil.toStringListB(clusterResult);
		List<int[]> countResult = miningService.count(content, cluster);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("clusterResult", clusterResult);
		result.put("countResult", countResult);
		content.add(0, attrs);
		result.put("content", content);
		return result;
	}

	@Override
	public String queryLinkedIssue(String issueId, String issueType) {
		Issue issue = queryIssueById(issueId);
		if (issueType.equals(Constant.ISSUETYPE_EXTENSIVE)) {
			// 如果issue为核心数据
			if (issue.getIssueType().equals(Constant.ISSUETYPE_CORE)) {
				// 将issue变为该核心数据对应的准数据
				issue = queryIssueById(issue.getIssueBelongTo());
				if (issue == null) {
					return null;
				}
			}
			return issue.getIssueBelongTo();
		} else if (issueType.equals(Constant.ISSUETYPE_STANDARD)) {
			if (issue.getIssueType().equals(Constant.ISSUETYPE_EXTENSIVE)) {
				return issue.getIssueHold();
			} else {
				return issue.getIssueBelongTo();
			}
		} else {
			// 如果issue为泛数据
			if (issue.getIssueType().equals(Constant.ISSUETYPE_EXTENSIVE)) {
				// 将issue变为该泛数据对应的准数据
				issue = queryIssueById(issue.getIssueHold());
				if (issue == null) {
					return null;
				}
			}
			return issue.getIssueHold();
		}
	}

	@Override
	public long queryIssueCount(IssueQueryCondition con) {
		return issueDao.queryIssueCount(con);
	}

	/**
	 * 对以去重好的待聚类集合按照标题进行重排序
	 * 
	 * @param filteredContent
	 *            去重后的待聚类集合(不带标题)
	 * @param titleIndex
	 *            去重后的待聚类集合中的标题下标
	 * @return
	 */
	private List<String[]> resortContent(List<String[]> filteredContent, int titleIndex) {
		List<String[]> newContent = new ArrayList<String[]>();
		newContent.addAll(filteredContent);
		newContent.remove(0);
		// 排序
		Collections.sort(newContent, new Comparator<String[]>() {
			public int compare(String[] o1, String[] o2) {
				return (o1[titleIndex]).compareTo(o2[titleIndex]);
			}
		});
		newContent.add(0, filteredContent.get(0));
		return newContent;
	}

}
