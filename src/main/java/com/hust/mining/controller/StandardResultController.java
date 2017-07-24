package com.hust.mining.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpRequest;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.hust.mining.constant.Constant;
import com.hust.mining.constant.Constant.KEY;
import com.hust.mining.dao.StandardResultDao;
import com.hust.mining.model.Issue;
import com.hust.mining.model.Label;
import com.hust.mining.model.StandardResult;
import com.hust.mining.model.Website;
import com.hust.mining.model.params.StatisticParams;
import com.hust.mining.service.IssueService;
import com.hust.mining.service.LabelService;
import com.hust.mining.service.RedisService;
import com.hust.mining.service.ResultService;
import com.hust.mining.service.StandardResultService;
import com.hust.mining.service.StandardResult_labelService;
import com.hust.mining.urltags.URLTool;
import com.hust.mining.util.AttrUtil;
import com.hust.mining.util.ConvertUtil;
import com.hust.mining.util.ExcelUtil;
import com.hust.mining.util.FileUtil;
import com.hust.mining.util.ResultUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/standardResult")
public class StandardResultController {
    private static final Logger logger = LoggerFactory.getLogger(StandardResultController.class);
    @Autowired
    private IssueService issueService;
    @Autowired
    private StandardResultService standardResultService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private StandardResult_labelService stand_label;
    @Autowired
    private LabelService labelservice;
    @Autowired
	private ResultService resultService;
    
    @ResponseBody
    @RequestMapping(value = "/queryStandardResults")
    public Object queryStandardResults(@RequestParam(value = "issueId", required = false) String issueId,
            HttpServletRequest request){
    	Issue issue = issueService.queryIssueById(issueId);
    	if (issue == null) {
            return ResultUtil.errorWithMsg("查询任务文件失败");
        }
    	List<StandardResult> stdResList = standardResultService.queryStdRessByIssueId(issueId);
    	redisService.setString(KEY.ISSUE_ID, issueId, request);
    	JSONObject json = new JSONObject();
        json.put("issue", issue);
        json.put("stdResList", stdResList);
        return ResultUtil.success(json);
    }
    
    @ResponseBody
    @RequestMapping(value = "/queryIssueName")
    public Object queryIssueName(@RequestParam(value = "issueId", required = false) String issueId,
            HttpServletRequest request){
    	if(StringUtils.isEmpty(issueId)){
    		issueId = issueService.getCurrentIssueId(request);
    	}
    	
    	String stdIssueId = issueService.queryLinkedIssue(issueId, Constant.ISSUETYPE_STANDARD);
    	
    	Issue issue = issueService.queryIssueById(stdIssueId);
    	if (issue == null) {
            return ResultUtil.errorWithMsg("查询任务名称失败");
        }
    	List<StandardResult> stdResList = standardResultService.queryStdRessByIssueId(stdIssueId);
    	redisService.setString(KEY.ISSUE_ID, issueId, request);
    	JSONObject json = new JSONObject();
        json.put("issue", issue);
        json.put("stdRes", stdResList != null && !stdResList.isEmpty() ?stdResList.get(0):"");
        return ResultUtil.success(json);
    }
    /**
     * //从上传的准数据文件生成准数据 
     * @param file
     * @return
     */
    @ResponseBody
	@RequestMapping("/createResWithFile")
	public Object createResWithFile(@RequestParam(value = "file", required = true) MultipartFile file,HttpServletRequest request) {
    	if (issueService.getCurrentIssueId(request) == null) {
			return ResultUtil.errorWithMsg("请选择或者创建一个任务");
		}
		// 数组之间必须是一一对应关系
		if (file.isEmpty()) {
			logger.info(file.getName() + "is empty");
			return ResultUtil.errorWithMsg("文件为空");
		}
		try {
			//System.out.println(file.getOriginalFilename());
			List<String[]> list = ExcelUtil.readWithNullRow(file.getOriginalFilename(), file.getInputStream(), 0, -1, null);
			for(String t :list.get(list.size()-1)){
				System.out.print(t+"\t");
			}
			if (null == list || 0 == list.size()) {
				return ResultUtil.errorWithMsg("准数据文件内容为空!");
			}
			String resid = standardResultService.createStandResult(list,request);
			StandardResult stdRes = standardResultService.queryStdResById(resid);
			String issueid = "";
			if(stdRes != null){
				issueid = stdRes.getIssueId();
			}
			JSONObject json = new JSONObject();
			if(resid != null && !resid.equals("") && issueid != ""){
				json.put("stdResId", resid);
				json.put("stdIssueId", issueid);
				return ResultUtil.success(json);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtil.errorWithMsg("生成准数据失败!");
		}
		return ResultUtil.errorWithMsg("生成准数据失败!");
	}
    /**
     * //从已有的泛数据生成准数据 
     * @param file
     * @return
     */
    @ResponseBody
	@RequestMapping("/createResWithoutFile")
	public Object createResWithFile(HttpServletRequest request) {
    	  String issueId = issueService.getCurrentIssueId(request);
          if (StringUtils.isBlank(issueId)) {
              logger.info("从session中无法s获得任务的任务id");
              return ResultUtil.errorWithMsg("未找到当前处理事件，请先创建或者选择某一事件");
          }
          String resultId = resultService.getCurrentResultId(request);
          if (StringUtils.isBlank(resultId)) {
              logger.info("从session中无法s获得记录的记录id");
              return ResultUtil.errorWithMsg("未找到当前处理记录，请先创建或者选择某一记录");
          }
         
          try {
              List<String[]> list = resultService.exportService(issueId, resultId, request);
         
			if (null == list || 0 == list.size()) {
				return ResultUtil.errorWithMsg("请重新选择泛数据!");
			}
			String resid = standardResultService.createStandResult(list,request);
			StandardResult stdRes = standardResultService.queryStdResById(resid);
			String issueid = "";
			if(stdRes != null){
				issueid = stdRes.getIssueId();
			}
			JSONObject json = new JSONObject();
			if(resid != null && !resid.equals("") && issueid != ""){
				json.put("stdResId", resid);
				json.put("stdIssueId", issueid);
				return ResultUtil.success(json);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResultUtil.errorWithMsg("生成准数据失败!");
	}
    
    /**
     * 得到准数据聚类结果：类簇第一个标题、时间、数量
     * @param resultId
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/getCountResult")
    public Object getCountResult(@RequestParam(value = "resultId", required = false) String resultId,
            HttpServletRequest request) {
        
        if (StringUtils.isBlank(resultId)) {
            resultId = resultService.getCurrentResultId(request);
        }
        if (StringUtils.isBlank(resultId)) {
            return ResultUtil.errorWithMsg("不存在记录");
        }
        //System.out.println(resultId+"--REusltc-"+issueId);
        List<String[]> list = standardResultService.getCountResultById(resultId, request);
        
        if (null == list || list.size() == 0) {
            return ResultUtil.errorWithMsg("不存在记录");
        }
        redisService.setString(KEY.STD_RESULT_ID, resultId, request);
        return ResultUtil.success(list);
    }

    /**
     * 对准数据一个类统计出图
     * @param params
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/statisticSingleSet")
    public Object statistic(@RequestBody StatisticParams params, HttpServletRequest request) {
        String stdResId = redisService.getString(KEY.STD_RESULT_ID, request);
        if (StringUtils.isBlank(stdResId)) {
            return ResultUtil.errorWithMsg("请重新选择准数据任务");
        }
        Map<String, Object> map = standardResultService.statistic(stdResId,params, request);
        if (null == map || map.isEmpty()) {
            return ResultUtil.errorWithMsg("统计失败");
        }
        return ResultUtil.success(map);
    }
    
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/download", method = RequestMethod.POST)
    public void download(@RequestParam(value = "stdResId", required = true) String stdResId,HttpServletRequest request, HttpServletResponse response) throws IOException {
    	String issueId = issueService.getCurrentIssueId(request);
        if (StringUtils.isBlank(issueId)) {
            response.sendError(404, "未找到当前处理事件，请先创建或者选择某一事件");
            logger.error("issueId为空");
            return;
        }
        StandardResult standardResult = standardResultService.queryStdResById(stdResId);
        
        if (null == standardResult) {
            response.sendError(404, "未找到当前处理记录，请先创建或者选择某一记录");
            logger.error("standardResult为空");
            return;
        }
        OutputStream outputStream = null;
        try {
            List<String[]> cluster = standardResultService.getStdResContentById(stdResId);
            if (cluster == null) {
                return;
            }
            
            String dateCount = "";
            String srcCount = "";
            
            if(StringUtils.isBlank(standardResult.getDateCount())){
            	dateCount = standardResultService.getDateCount(cluster);
            	srcCount = standardResultService.getSourceCount(cluster);
            	standardResult.setDateCount(dateCount);
            	standardResult.setSourceCount(srcCount);
            	standardResultService.updateByPrimaryKey(standardResult);
            } else {
            	dateCount = standardResult.getDateCount();
            	srcCount = standardResult.getSourceCount();
            }
            
            List<String[]> dateCountList = FileUtil.calcPOfCount(ConvertUtil.strConvertToList(dateCount, "日期"));
            List<String[]> srcCountList = FileUtil.calcPOfCount(ConvertUtil.strConvertToList(srcCount, "来源"));
            
            outputStream = response.getOutputStream();
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            String stdResName = new String(standardResult.getResName().getBytes(),"ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename="+stdResName+".xls");
            HSSFWorkbook workbook = ExcelUtil.exportToExcel(cluster,dateCountList,srcCountList);
            workbook.write(outputStream);
        } catch (Exception e) {
            logger.info("excel 导出失败\t" + e.toString());
        } finally {
            try {
                outputStream.close();
            } catch (Exception e) {
                logger.info("导出excel时，关闭outputstream失败");
                return;
            }
        }
    }
   
    @ResponseBody
    @RequestMapping(value = "/delete")
    public Object delete(@RequestParam(value = "stdResId", required = false) String stdResId,
            HttpServletRequest request){
    	String issueId = issueService.getCurrentIssueId(request);
    	if(standardResultService.deleteById(stdResId) <= 0){
    		return ResultUtil.errorWithMsg("删除失败！");
    	}
    	JSONObject json = new JSONObject();
        json.put("issueId", issueId);
        return ResultUtil.success(json);
    }
    
    ////以下全是关于为准数据贴标签的函数
    /**
     * 为准数据贴标签
     */
    @ResponseBody
    @RequestMapping(value="/SetLabelForStandardResult")
    public Object SetLabelForStandardResult(@RequestParam(value="stdResId",required=true) String stdResId,
    		@RequestParam(value="labelid",required=true) Integer labelid)
    {
    	if (labelid.equals(null)) {
			return ResultUtil.errorWithMsg("没有选中任何标签！");
		}
    	boolean status = stand_label.attachlabels(stdResId, labelid);
    	if (status==false) {
    		return ResultUtil.errorWithMsg("贴标签失败！");
		}
    	return ResultUtil.success("贴标签成功！");
    	
    }
    
    /**
     * 查找当前的任务，没有哪些标签
     */
    @ResponseBody
    @RequestMapping(value="/findLabelNotInStandardResult")
    public Object findLabelNotInStandardResult(@RequestParam(value="stdResId",required=true) String stdResId)
    {
    	if (StringUtils.isBlank(stdResId)) {
    		return ResultUtil.errorWithMsg("没有选中任何准数据！");
		}
    	//当前任务已有的标签
		List<Label> exitLabel = new ArrayList<Label>();
		exitLabel = stand_label.selectLabelsForStandResult(stdResId);
		//System.out.println("已有的标签长度："+exitLabel.size());
		//全部标签
		List<Label> allLabel = new ArrayList<Label>();
		allLabel = labelservice.selectAllLable(0, 0);
		//System.out.println("全部标签长度："+allLabel.size());
		//没有的标签
		List<Label> list = new ArrayList<Label>();
		for(int i = 0; i < allLabel.size();i++)
		{
			int status = 0;//0表示不存在相同的
			for(int j = 0; j < exitLabel.size(); j++)
			{
				String string1 = allLabel.get(i).getLabelname();
				String string2 = exitLabel.get(j).getLabelname();
				if (string1.equals(string2)) {
					status = 1;
				}
			}
			if (status!=1) {
				Label label = new Label();
				label = allLabel.get(i);
				list.add(label);
			}
		}
		//System.out.println("没有的标签长度："+list.size());
    	return ResultUtil.success(list);
    	
    }
    
    /**
     * 查看某个准数据被打贴了什么标签
     * @param stdResId
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/selectLabelsForStandResult")
    public Object selectLabelsForStandResult(@RequestParam(value="stdResId",required=true)String stdResId)
    {
    	if (StringUtils.isBlank(stdResId)) {
    		return ResultUtil.errorWithMsg("没有选中任何准数据！");
		}
    	List<Label> list = stand_label.selectLabelsForStandResult(stdResId);
    	//System.out.println("已有的标签是：");
    	for (Label label : list) {
			//System.out.println(label.getLabelname());
		}
    	return ResultUtil.success(list);
    }
    
    /**
     * 根据标签查找那些准数据包含此标签
     * @param labelname
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/selectStandResultsBylabel")
    public Object selectStandResultsBylabel(@RequestParam(value="labelname",required=true)String labelname)
    {
    	//根据labelname找到labelid
    	Label label = labelservice.selectByname(labelname);
    	if (StringUtils.isBlank(labelname)) {
    		return ResultUtil.errorWithMsg("没有准数据包含此标签！");
		}
    	//根据labelId返回任务的id列表
    	List<String> list = stand_label.selectStandResultsBylabel(label.getLabelid());
    	return ResultUtil.success(list);
    }
    
    @ResponseBody
    @RequestMapping(value="/deleteLabelOfStandard")
    public Object deleteLabelOfStandard(@RequestParam(value="stdResId",required=true)String staRtId,
    		@RequestParam(value="labelid",required=true)int labelid)
    {
    	
    	if (StringUtils.isBlank(staRtId)) {
    		return ResultUtil.errorWithMsg("请选择准数据！");
		}
    	boolean status = stand_label.delete(staRtId, labelid);
    	if (status==false) {
    		return ResultUtil.errorWithMsg("移除标签失败！");
		}
    	return ResultUtil.success("移除标签成功！");
    }
    
   
    
    @ResponseBody
    @RequestMapping(value="/countURL")
	public Object countURL(@RequestParam(value="stdResId",required=true)String staRtId)
	{
    	//获取当前准数据的内容
    	List<String[]> cluster = standardResultService.getStdResContentById(staRtId);
    	if (cluster.isEmpty()) {
    		return ResultUtil.errorWithMsg("当前准数据为空！");
		}
    	String[] firstline = cluster.get(0);
    	int URL = AttrUtil.findIndexOfUrl(firstline);
    	int name = AttrUtil.findIndexOfWebName(firstline);
    	List<String[]> uRList = new ArrayList<String[]>();
    	for (String[] strings : cluster) {
    		//过滤空行和为空的
    		if (strings!=null && strings.length!=1) {
    			String[] string = new String[2];
    			string[0] = strings[URL];
    			string[1] = strings[name];
    			uRList.add(string);
			}
		}
    	URLTool urlTool = new URLTool();
    	List<String[]> result = urlTool.statisticUrl(uRList);
    	if (result.isEmpty()) {
    		return ResultUtil.errorWithMsg("统计网站失败！");
		}
    	List<String[]> finalresult = new ArrayList<String[]>();
    	if (result.size()<5) {
    		return ResultUtil.success(result);
		}
    	else {
    		//只需输出5个
        	for(int i = 0; i < 5; i++)
        	{
        		finalresult.add(result.get(i));
        	}
        	return ResultUtil.success(finalresult);
		}
	}
}
