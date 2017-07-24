package com.hust.mining.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hust.mining.constant.Constant.KEY;
import com.hust.mining.model.Issue;
import com.hust.mining.model.Label;
import com.hust.mining.model.StandardResult;
import com.hust.mining.model.params.IssueQueryCondition;
import com.hust.mining.service.IssueService;
import com.hust.mining.service.LabelService;
import com.hust.mining.service.RedisService;
import com.hust.mining.service.StandardResultService;
import com.hust.mining.service.UserService;
import com.hust.mining.service.issue_labelService;
import com.hust.mining.urltags.URLTool;
import com.hust.mining.util.AttrUtil;
import com.hust.mining.util.ResultUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/issue")
public class IssueController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(IssueController.class);

	@Autowired
	private UserService userService;
	@Autowired
	private IssueService issueService;
	@Autowired
	private RedisService redisService;
	@Autowired
	private issue_labelService issue_labelservice;
	@Autowired
	private LabelService labelservice;
	@Autowired
	private StandardResultService standardResultService;

	/**
	 * 创建任务
	 * 
	 * @param issueName
	 *            创建任务的名字
	 * @param issueType
	 *            创建任务的类型（泛数据，准数据，核心数据）
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public Object createIssue(@RequestParam(value = "issueName", required = true) String issueName,
			@RequestParam(value = "issueType", required = true) String issueType, HttpServletRequest request) {
		if (issueService.createIssue(issueName, issueType, request) == 0) {
			logger.info("create issue fail");
			return ResultUtil.errorWithMsg("创建任务失败");
		}
		return ResultUtil.success("创建任务成功");
	}

	@ResponseBody
	@RequestMapping("/delete")
	public Object deleteIssue(@RequestParam(value = "issueId", required = true) String issueId,
			@RequestParam(value = "issueType", required = true) String issueType, HttpServletRequest request) {
		if (issueService.deleteIssueById(issueId, issueType, request) > 0) {
			return ResultUtil.success("删除任务成功");
		}
		return ResultUtil.errorWithMsg("删除任务失败");
	}

	// 删除任务里的所有类型任务以及数据
	@ResponseBody
	@RequestMapping("/deleteAll")
	public Object deleteIssueAll(@RequestParam(value = "issueId", required = true) String issueId,
			HttpServletRequest request) {
		if (issueService.deleteIssueAllById(issueId, request) > 0) {
			return ResultUtil.success("删除任务成功");
		}
		return ResultUtil.errorWithMsg("删除任务失败");
	}

	@ResponseBody
	@RequestMapping("/createIssueWithLink")
	public Object createIssueWithLink(@RequestParam(value = "issueType", required = true) String issueType,
			@RequestParam(value = "stdResId", required = false) String stdResId, HttpServletRequest request) {
		String linkedIssueId = redisService.getString(KEY.ISSUE_ID, request);
		if (StringUtils.isEmpty(linkedIssueId)) {
			return ResultUtil.errorWithMsg("请重新选择任务");
		}
		int exists = issueService.createIssueWithLink(linkedIssueId, issueType, stdResId, request);
		if (exists == 0) {
			logger.error("create Issue with link fail.");
			return ResultUtil.errorWithMsg("创建准数据失败.");
		} else if (exists == -1) {
			logger.error("current Result Id is null.");
			return ResultUtil.errorWithMsg("result为空，请点击“重置”。");
		}
		return ResultUtil.success("创建任务成功");
	}

	@ResponseBody
	@RequestMapping("/createCore")
	public Object createCore(@RequestParam(value = "linkedIssueId", required = true) String linkedIssueId,
			@RequestParam(value = "stdResId", required = false) String stdResId, HttpServletRequest request) {
		if (StringUtils.isEmpty(linkedIssueId)) {
			return ResultUtil.errorWithMsg("准数据不存在，请生成准数据。");
		}
		String coreResId = issueService.createCoreIssue(linkedIssueId, stdResId, request);
		JSONObject json = new JSONObject();
		json.put("coreResId", coreResId);
		return ResultUtil.success(json);
	}

	@ResponseBody
	@RequestMapping("/queryLinkedIssue")
	public Object queryLinkedIssue(@RequestParam(value = "issueType", required = true) String issueType,
			HttpServletRequest request) {
		String issueId = redisService.getString(KEY.ISSUE_ID, request);

		String linkedIssueId = issueService.queryLinkedIssue(issueId, issueType);
		if (StringUtils.isBlank(linkedIssueId)) {
			return ResultUtil.errorWithMsg("该任务不存在" + issueType);
		}
		JSONObject result = new JSONObject();
		result.put("issueId", linkedIssueId);
		redisService.setString(KEY.ISSUE_ID, linkedIssueId, request);
		return ResultUtil.success(result);
	}

	@ResponseBody
	@RequestMapping("/queryOwnIssue")
	public Object queryOwnIssue(@RequestBody IssueQueryCondition con, HttpServletRequest request) {
		String user = userService.getCurrentUser(request);
		con.setUser(user);
		// System.out.println(con.getIssueId());
		// System.out.println(con.getIssueName());
		// System.out.println(con.getIssueType());
		// System.out.println(con.getIssueHold()+"***************");
		if (null == con.getIssueType()) {
			return ResultUtil.successWithoutMsg();
		}
		// System.out.println(con.getPageNo());
		// System.out.println(con.getPageSize());
		// System.out.println(con.getUser());
		// System.out.println("CreateEndTime" + con.getCreateEndTime());
		// System.out.println("CreateStartTime" + con.getCreateStartTime());
		// System.out.println("LastUpdateEndTime" + con.getLastUpdateEndTime());
		// System.out.println("LastUpdateStartTime" +
		// con.getLastUpdateStartTime());
		List<Issue> list = issueService.queryIssue(con);
		if (null == list || 0 == list.size()) {
			return ResultUtil.errorWithMsg("没有任务被创建！");
		}
		long count = list.size();
		JSONObject result = new JSONObject();
		long pageTotal = count % 10 == 0 ? (count / 10) : (count / 10 + 1);
		result.put("pageTotal", pageTotal);
		result.put("list", list);
		return ResultUtil.success(result);
	}

	@ResponseBody
	@RequestMapping("/queryAllIssue")
	public Object queryAllIssue(@RequestBody IssueQueryCondition con, HttpServletRequest request) {
		List<Issue> list = issueService.queryIssue(con);
		if (null == list || 0 == list.size()) {
			return ResultUtil.errorWithMsg("没有任务被创建！");
		}
		long count = list.size();
		JSONObject result = new JSONObject();
		long pageTotal = count % 10 == 0 ? (count / 10) : (count / 10 + 1);
		result.put("pageTotal", pageTotal);
		result.put("list", list);
		return ResultUtil.success(result);
	}

	@ResponseBody
	@RequestMapping("/queryOwnIssueCount")
	public Object queryOwnIssueCount(@RequestBody IssueQueryCondition con, HttpServletRequest request) {
		long count = 0;
		count = issueService.queryIssueCount(con);
		if (count <= 0) {
			return ResultUtil.errorWithMsg("没有任务被创建！");
		}
		// System.out.println(count+"-----------------------------");
		return ResultUtil.success(count);
	}

	@ResponseBody
	@RequestMapping("/queryAllIssueCount")
	public Object queryAllIssueCount(@RequestBody IssueQueryCondition con, HttpServletRequest request) {
		long count = 0;
		count = issueService.queryIssueCount(con);
		if (count <= 0) {
			return ResultUtil.errorWithMsg("没有任务被创建！");
		}
		return ResultUtil.success(count);
	}

	@ResponseBody
	@RequestMapping("/miningByTime")
	public Object miningByTime(@RequestParam(value = "startTime", required = true) Date startTime,
			@RequestParam(value = "endTime", required = true) Date endTime, HttpServletRequest request) {
		String issueId = redisService.getString(KEY.ISSUE_ID, request);
		if (StringUtils.isEmpty(issueId)) {
			return ResultUtil.errorWithMsg("请重新选择任务");
		}
		List<String[]> count = issueService.miningByTime(startTime, endTime, request);
		if (count == null) {
			return ResultUtil.unknowError();
		}
		return ResultUtil.success(count);
	}

	@ResponseBody
	@RequestMapping("/miningByFile")
	public Object miningByFileIds(@RequestBody List<String> fileIds, HttpServletRequest request) {

		// System.out.println("-------------");
		// System.out.println("-------------"+fileIds.size());
		for (String string : fileIds) {
			// System.out.println("-------------"+string);
		}
		String issueId = redisService.getString(KEY.ISSUE_ID, request);
		if (StringUtils.isEmpty(issueId)) {
			return ResultUtil.errorWithMsg("请重新选择任务");
		}
		List<String[]> count = issueService.miningByFileIds(fileIds, request);
		if (count == null) {
			return ResultUtil.unknowError();
		}
		return ResultUtil.success(count);
	}

	/**
	 * 单文件聚类
	 * 
	 * @param fileId
	 *            聚类文件id
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/miningSingleFile")
	public Object miningSingleFile(@RequestParam(value = "fileId", required = true) String fileId,
			HttpServletRequest request) {
		String issueId = redisService.getString(KEY.ISSUE_ID, request);
		if (StringUtils.isEmpty(issueId)) {
			return ResultUtil.errorWithMsg("请重新选择任务");
		}
		List<String> fileIds = new ArrayList<String>();
		fileIds.add(fileId);
		// System.out.println("hahahah");
		List<String[]> count = issueService.miningByFileIds(fileIds, request);

		if (count == null) {
			return ResultUtil.unknowError();
		}
		return ResultUtil.successWithoutMsg();
	}

	@InitBinder
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		CustomDateEditor editor = new CustomDateEditor(df, false);
		binder.registerCustomEditor(Date.class, editor);
	}

	//// 以下全部都是为任务贴标签
	@ResponseBody
	@RequestMapping(value = "/SetLabelForStandardResult")
	public Object SetLabelForStandardResult(@RequestParam(value = "issueId", required = true) String issueId,
			@RequestParam(value = "labelid", required = true) Integer labelid) {
		if (labelid.equals(null)) {
			return ResultUtil.errorWithMsg("没有选中任何标签！");
		}
		boolean status = issue_labelservice.attachlabels(issueId, labelid);
		if (status == false) {
			return ResultUtil.errorWithMsg("贴标签失败！");
		}
		return ResultUtil.success("贴标签成功！");
	}

	/**
	 * 查找当前的任务，没有哪些标签
	 */
	@ResponseBody
	@RequestMapping(value = "/findLabelNotInStandardResult")
	public Object findLabelNotInStandardResult(@RequestParam(value = "issueId", required = true) String issueId) {
		if (StringUtils.isBlank(issueId)) {
			return ResultUtil.errorWithMsg("没有选中任何准数据！");
		}
		// 当前任务已有的标签
		List<Label> exitLabel = new ArrayList<Label>();
		exitLabel = issue_labelservice.selectLabelsForStandResult(issueId);
		// System.out.println("已有的标签长度："+exitLabel.size());
		// 全部标签
		List<Label> allLabel = new ArrayList<Label>();
		allLabel = labelservice.selectAllLable(0, 0);
		// System.out.println("全部标签长度："+allLabel.size());
		// 没有的标签
		List<Label> list = new ArrayList<Label>();
		for (int i = 0; i < allLabel.size(); i++) {
			int status = 0;// 0表示不存在相同的
			for (int j = 0; j < exitLabel.size(); j++) {
				String string1 = allLabel.get(i).getLabelname();
				String string2 = exitLabel.get(j).getLabelname();
				if (string1.equals(string2)) {
					status = 1;
				}
			}
			if (status != 1) {
				Label label = new Label();
				label = allLabel.get(i);
				list.add(label);
			}
		}
		// System.out.println("没有的标签长度："+list.size());
		return ResultUtil.success(list);
	}

	/**
	 * 查看某个准数据被打贴了什么标签
	 * 
	 * @param stdResId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/selectLabelsForStandResult")
	public Object selectLabelsForStandResult(@RequestParam(value = "issueId", required = true) String issueId) {
		if (StringUtils.isBlank(issueId)) {
			return ResultUtil.errorWithMsg("没有选中任何准数据！");
		}
		List<Label> list = issue_labelservice.selectLabelsForStandResult(issueId);
		// System.out.println("已有的标签是：");
		for (Label label : list) {
			// System.out.println(label.getLabelname());
		}
		return ResultUtil.success(list);
	}

	/**
	 * 根据标签查找那些准数据包含此标签
	 * 
	 * @param labelname
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/selectStandResultsBylabel")
	public Object selectIssuesBylabel(@RequestParam(value = "labelname", required = true) String labelname) {
		// 根据labelname找到labelid
		Label label = labelservice.selectByname(labelname);
		if (StringUtils.isBlank(labelname)) {
			return ResultUtil.errorWithMsg("没有准数据包含此标签！");
		}
		// 根据labelId返回任务的id列表
		List<String> list = issue_labelservice.selectIssuesBylabel(label.getLabelid());
		return ResultUtil.success(list);
	}

	@ResponseBody
	@RequestMapping(value = "/deleteLabelOfStandard")
	public Object deleteLabelOfStandard(@RequestParam(value = "issueId", required = true) String issueId,
			@RequestParam(value = "labelid", required = true) int labelid) {

		if (StringUtils.isBlank(issueId)) {
			return ResultUtil.errorWithMsg("请选择准数据任务！");
		}
		boolean status = issue_labelservice.delete(issueId, labelid);
		if (status == false) {
			return ResultUtil.errorWithMsg("移除标签失败！");
		}
		return ResultUtil.success("移除标签成功！");
	}

	@ResponseBody
	@RequestMapping(value = "/countURL")
	public Object countURL(@RequestParam(value = "issueId", required = true) String issueId) {
		if (StringUtils.isBlank(issueId)) {
			return ResultUtil.errorWithMsg("请选择准数据任务！");
		}
		List<String[]> list = issue_labelservice.countURL(issueId);
		List<String[]> finalresult = new ArrayList<String[]>();
		if (list == null) {
			return ResultUtil.errorWithMsg("该任务下没有文件！");
		}
		if (list.size() < 5) {
			return ResultUtil.success(list);
		} else {
			// 只需输出5个
			for (int i = 0; i < 5; i++) {
				finalresult.add(list.get(i));
			}
			return ResultUtil.success(finalresult);
		}
	}
}
