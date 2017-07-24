package com.hust.mining.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.util.ArrayUtil;
import org.junit.runner.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hust.mining.constant.Constant.KEY;
import com.hust.mining.model.Result;
import com.hust.mining.model.params.StatisticParams;
import com.hust.mining.service.IssueService;
import com.hust.mining.service.RedisService;
import com.hust.mining.service.ResultService;
import com.hust.mining.util.ConvertUtil;
import com.hust.mining.util.ResultUtil;

import net.sf.json.JSONArray;

@RequestMapping(value = "/result")
public class ResultController {
    @Autowired
    private ResultService resultService;
    @Autowired
    private IssueService issueService;
    @Autowired
    private RedisService redisService;

    /**
     * 得到聚类结果、类簇第一个标题、时间、数量等
     * @param resultId
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/getCountResult")
    public Object getCountResult(@RequestParam(value = "resultId", required = false) String resultId,
            HttpServletRequest request) {
        String issueId = issueService.getCurrentIssueId(request);
        if (StringUtils.isEmpty(issueId)) {
            return ResultUtil.errorWithMsg("请重新选择任务");
        }
        if (StringUtils.isBlank(resultId)) {
            resultId = resultService.getCurrentResultId(request);
        }
        if (StringUtils.isBlank(resultId)) {
            return ResultUtil.errorWithMsg("不存在记录");
        }
        //System.out.println(resultId+"--REusltc-"+issueId);
        List<String[]> list = resultService.getCountResultById(resultId, issueId, request);

        if (null == list || list.size() == 0) {
            return ResultUtil.errorWithMsg("不存在记录");
        }
        redisService.setString(KEY.RESULT_ID, resultId, request);
        return ResultUtil.success(list);
    }

    /**
     * 得到某个类全部的元素的信息
     * @param clusterIndex 类的下标
     * @param resultId 聚类结果id--文件名
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/getClusterResult",method=RequestMethod.POST)
    public Object getClusterResult(@RequestParam(value = "clusterIndex", required = false) String clusterIndex,
    		@RequestParam(value = "resultId", required = false) String resultId,
            HttpServletRequest request) {
        String issueId = issueService.getCurrentIssueId(request);
        if (StringUtils.isEmpty(issueId)) {
            return ResultUtil.errorWithMsg("请重新选择任务");
        }
        if (StringUtils.isBlank(resultId)) {
            resultId = resultService.getCurrentResultId(request);
        }
        if (StringUtils.isBlank(resultId)) {
            return ResultUtil.errorWithMsg("不存在记录");
        }
        //得到类中每个元素的信息
        List<String[]> list = resultService.getClusterResultById(clusterIndex,resultId, issueId, request);
        if (null == list || list.size() == 0) {
            return ResultUtil.errorWithMsg("不存在记录");
        }
        redisService.setString(KEY.RESULT_ID, resultId, request);
        
        return ResultUtil.success(list);
    }
    
    @ResponseBody
    @RequestMapping("/deleteSets")
    public Object delSets(@RequestBody int[] sets, HttpServletRequest request) {
        String issueId = issueService.getCurrentIssueId(request);
        if (StringUtils.isEmpty(issueId)) {
            return ResultUtil.errorWithMsg("请重新选择任务");
        }
        String resultId = resultService.getCurrentResultId(request);
        if (StringUtils.isEmpty(resultId)) {
            return ResultUtil.errorWithMsg("请重新选择一条挖掘记录");
        }
        boolean result = resultService.deleteSets(sets, request);
        if (result) {
            return ResultUtil.success("删除成功");
        }
        return ResultUtil.errorWithMsg("删除失败");
    }

    /**
     * 删除类中的一些元素
     * @param sets
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/deleteClusterItems")
    public Object delClusterItems(@RequestParam(value = "clusterIndex", required = false) String clusterIndex,
    		@RequestParam(value = "ItemIdSets", required = false) int[] sets, HttpServletRequest request) {
    	
    	for(int i : sets){
    		//System.out.print(i+ " ");
    	}
    	//System.out.println("sets:----");
    	if(clusterIndex == null || Integer.valueOf(clusterIndex) < 0 || sets == null){
    		return ResultUtil.errorWithMsg("对不起，删除失败！");
    	}
        String issueId = issueService.getCurrentIssueId(request);
        if (StringUtils.isEmpty(issueId)) {
            return ResultUtil.errorWithMsg("请重新选择任务");
        }
        String resultId = resultService.getCurrentResultId(request);
        if (StringUtils.isEmpty(resultId)) {
            return ResultUtil.errorWithMsg("请重新选择一条挖掘记录");
        }
        
        boolean result = resultService.deleteClusterItems(clusterIndex, sets, request);
        if (result) {
            return ResultUtil.success("删除成功");
        }
        return ResultUtil.errorWithMsg("删除失败");
    }

    @ResponseBody
    @RequestMapping("/resetClusterItems")
    public Object resetClusterItems(@RequestBody String index, HttpServletRequest request) {
        String issueId = redisService.getString(KEY.ISSUE_ID, request);
        if (StringUtils.isEmpty(issueId)) {
            return ResultUtil.errorWithMsg("请重新选择任务");
        }
        String resultId = resultService.getCurrentResultId(request);
        if (StringUtils.isEmpty(resultId)) {
            return ResultUtil.errorWithMsg("请重新选择一条挖掘记录");
        }
        boolean result = resultService.resetCluster(index, request);
        if (result) {
            return ResultUtil.success("合并成功");
        }
        return ResultUtil.errorWithMsg("合并失败");
    }

    
    @ResponseBody
    @RequestMapping("/combineSets")
    public Object combineSets(@RequestBody int[] sets, HttpServletRequest request) {
        String issueId = redisService.getString(KEY.ISSUE_ID, request);
        if (StringUtils.isEmpty(issueId)) {
            return ResultUtil.errorWithMsg("请重新选择任务");
        }
        String resultId = resultService.getCurrentResultId(request);
        if (StringUtils.isEmpty(resultId)) {
            return ResultUtil.errorWithMsg("请重新选择一条挖掘记录");
        }
        boolean result = resultService.combineSets(sets, request);
        if (result) {
            return ResultUtil.success("合并成功");
        }
        return ResultUtil.errorWithMsg("合并失败");
    }

    @ResponseBody
    @RequestMapping("/queryResultList")
    public Object queryResultList(HttpServletRequest request) {
        String issueId = redisService.getString(KEY.ISSUE_ID, request);
        if (StringUtils.isEmpty(issueId)) {
            return ResultUtil.errorWithMsg("获取当前任务失败,请重新进入任务");
        }
        List<Result> list = resultService.queryResultsByIssueId(issueId);
        if (null == list || list.size() == 0) {
            return ResultUtil.errorWithMsg("查询失败");
        }
        return ResultUtil.success(list);
    }

    @ResponseBody
    @RequestMapping("/resetResultById")
    public Object resetResultById(@RequestParam(value = "resultId", required = true) String resultId,
            HttpServletRequest request) {
        redisService.setString(KEY.RESULT_ID, resultId, request);
        if(resultService.reset(request)){
            return ResultUtil.success("重置成功");
        }
        return ResultUtil.errorWithMsg("重置失败");
    }
    
    @ResponseBody
    @RequestMapping("/delResultById")
    public Object delResultById(@RequestParam(value = "resultId", required = true) String resultId,
            HttpServletRequest request) {
        int del = resultService.delResultById(resultId);
        if (del <= 0) {
            return ResultUtil.errorWithMsg("删除失败");
        }
        return ResultUtil.success("删除成功");
    }

    @ResponseBody
    @RequestMapping(value = "/statisticSingleSet")
    public Object statistic(@RequestBody StatisticParams params, HttpServletRequest request) {
        String resultId = resultService.getCurrentResultId(request);
        if (StringUtils.isBlank(resultId)) {
            return ResultUtil.errorWithMsg("请重新选择任务");
        }
        Map<String, Object> map = resultService.statistic(params, request);
        if (null == map || map.isEmpty()) {
            return ResultUtil.errorWithMsg("统计失败");
        }
        return ResultUtil.success(map);
    }
    
}
