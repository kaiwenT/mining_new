package com.hust.mining.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hust.mining.constant.Constant.KEY;
import com.hust.mining.model.CoreResult;
import com.hust.mining.model.Issue;
import com.hust.mining.service.CoreResultService;
import com.hust.mining.service.IssueService;
import com.hust.mining.service.RedisService;
import com.hust.mining.util.ResultUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/coreResult")
public class CoreResultController {
	private static final Logger logger = LoggerFactory.getLogger(CoreResultController.class);
	@Autowired
	private IssueService issueService;
	@Autowired
	private CoreResultService coreResultService;
	@Autowired
	private RedisService redisService;

	@ResponseBody
	@RequestMapping(value = "/queryCoreResults")
	public Object queryStandardResults(@RequestParam(value = "issueId", required = false) String issueId,
			HttpServletRequest request) {
		Issue issue = issueService.queryIssueById(issueId);
		if (issue == null) {
			return ResultUtil.errorWithMsg("查询任务文件失败");
		}
		List<CoreResult> coreResList = coreResultService.queryCoreRessByIssueId(issueId);
		redisService.setString(KEY.ISSUE_ID, issueId, request);
		JSONObject json = new JSONObject();
		json.put("issue", issue);
		json.put("coreResList", coreResList);
		return ResultUtil.success(json);
	}

	@ResponseBody
	@RequestMapping(value = "/download", method = RequestMethod.POST)
	public void download(@RequestParam(value = "coreResId", required = true) String coreResId,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		CoreResult coreResult = coreResultService.queryCoreResById(coreResId);
		if (null == coreResult) {
			response.sendError(404, "请重新生成准数据。");
			logger.error("coreResult为空");
			return;
		}
		OutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
			response.setCharacterEncoding("utf-8");
			response.setContentType("multipart/form-data");
			String coreResName = new String(coreResult.getResName().getBytes(), "ISO8859-1");
			response.setHeader("Content-Disposition", "attachment;filename=" + coreResName + ".docx");
			if (!coreResultService.export(coreResId, outputStream)) {
				throw new Exception();
			}
		} catch (Exception e) {
			logger.error("结果数据 导出失败\t" + e.toString());
		} finally {
			try {
				outputStream.close();
			} catch (Exception e) {
				logger.error("导出结果数据时，关闭outputstream失败");
				return;
			}
		}
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public Object delete(@RequestParam(value = "coreResId", required = false) String coreResId,
			HttpServletRequest request) {
		String issueId = issueService.getCurrentIssueId(request);
		if (coreResultService.deleteById(coreResId) <= 0) {
			return ResultUtil.errorWithMsg("删除失败！");
		}
		JSONObject json = new JSONObject();
		json.put("issueId", issueId);
		return ResultUtil.success(json);
	}
}
