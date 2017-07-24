package com.hust.mining.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.hust.mining.constant.Constant.KEY;
import com.hust.mining.model.params.Condition;
import com.hust.mining.service.AlgorithmContainerService;
import com.hust.mining.service.FileService;
import com.hust.mining.service.IssueService;
import com.hust.mining.service.RedisService;
import com.hust.mining.service.ResultService;
import com.hust.mining.service.UserService;
import com.hust.mining.service.WebsiteService;
import com.hust.mining.util.ExcelUtil;
import com.hust.mining.util.ResultUtil;

@Controller
@RequestMapping("/AlgorithmContainer")
public class AlgorithmContainerController {
	
	 /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private AlgorithmContainerService algorithmContainerService;
    @Autowired
    private IssueService issueService;
    @Autowired
    private ResultService resultService;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisService redisService;
	/**
	 *  上传测试算法效果的文档
	 * @param file
	 * @param titleIndex
	 * @param timeIndex
	 * @param urlIndex
	 * @param sourceType
	 * @param request
	 * @return
	 */
	@ResponseBody
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public Object upload(@RequestParam(value = "file", required = true) MultipartFile file,
            @RequestParam(value = "titleIndex", required = true) int titleIndex,
            @RequestParam(value = "timeIndex", required = true) int timeIndex,
            @RequestParam(value = "urlIndex", required = true) int urlIndex,
            @RequestParam(value = "sourceType", required = true) String sourceType, HttpServletRequest request)
	{
        // 数组之间必须是一一对应关系
        if (file.isEmpty()) {
            logger.info(file.getName() + "is empty");
            return ResultUtil.errorWithMsg("文件为空");
        }
        Condition condition = new Condition();
        condition.setFile(file);
        condition.setTimeIndex(timeIndex);
        condition.setUrlIndex(urlIndex);
        condition.setTitleIndex(titleIndex);
        condition.setSourceType(sourceType);
        if (algorithmContainerService.getContent(condition, request) == 0) {
            logger.info("file insert failed");
            return ResultUtil.errorWithMsg("读取文件内容失败");
        }
        return ResultUtil.success("读取文件内容成功");
    }
	
	@ResponseBody
	@RequestMapping("/downloadResult")
	public void downloadResult(HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		try {
			List<String[]> list = algorithmContainerService.Downloade(request);
//			System.out.println("输出结果：");
			for (String[] strings : list) {
				for (String string : strings) {
//					System.out.print(string+"  ");
				}
//				System.out.println();
			}
			//System.out.println("输出完");
			if (list == null) {
				response.sendError(404, "导出错误");
				return;
			}
			outputStream = response.getOutputStream();
			response.setCharacterEncoding("utf-8");
			response.setContentType("multipart/form-data");
			response.setHeader("Content-Disposition", "attachment;fileName=ClusterResult.xls");
			HSSFWorkbook workbook = ExcelUtil.exportToExcel(list);
			workbook.write(outputStream);
		} catch (Exception e) {
			logger.info("excel 下载失败\t" + e.toString());
		} finally {
			try {
				outputStream.close();
			} catch (Exception e) {
				logger.info("下载excel时，关闭outputstream失败");
			}
		}
		
	}
	
	@ResponseBody
    @RequestMapping("/getCountResult")
    public Object getCountResult(HttpServletRequest request) {
       
        List<String[]> list = algorithmContainerService.getResult(request);
        if (null == list || list.size() == 0) {
            return ResultUtil.errorWithMsg("不存在记录");
        }
        return ResultUtil.success(list);
    }
	
	@ResponseBody
    @RequestMapping(value = "/ClusterByKmeans", method = RequestMethod.POST)
	public Object ClusterByKmeans(@RequestParam(value = "k_value", required = true) int k_value,
    		@RequestParam(value = "granularity", required = true) int granularity, HttpServletRequest request)
	{
		//待聚类文本
		List<String[]> list= (List<String[]>) redisService.getObject(KEY.REDIS_CONTENT, request);
		if (k_value < 0 || k_value > list.size()) 
		{
			 return ResultUtil.errorWithMsg("K超出其取值范围: 1~"+list.size());
		}
		else
		{
			String result = algorithmContainerService.UseKmeans(list, k_value, granularity,request);
			if (result==null) {
				 return ResultUtil.errorWithMsg("kmeans聚类失败");
				
			}
			return ResultUtil.success(result);
		}
		
	}
	
	@ResponseBody
    @RequestMapping(value = "/ClusterByCanopy", method = RequestMethod.POST)
	public Object ClusterByCanopy(@RequestParam(value = "Threshold", required = true) float Threshold,
    		@RequestParam(value = "granularity", required = true) int granularity, HttpServletRequest request)
	{
		//待聚类文本
		List<String[]> list= (List<String[]>) redisService.getObject(KEY.REDIS_CONTENT, request);
		if (Threshold < 0 || Threshold > 1) 
		{
			 return ResultUtil.errorWithMsg("阈值超出其取值范围: 0 ~ 1");
		}
		else
		{
			String result = algorithmContainerService.UseCanopy(list, Threshold, granularity,request);
			if (result==null) {
				 return ResultUtil.errorWithMsg("Canopy聚类失败");
			}
			return ResultUtil.success(result);
		}
		
	}
	
	@ResponseBody
    @RequestMapping(value = "/ClusterByDBScan", method = RequestMethod.POST)
	public Object ClusterByDBScan(@RequestParam(value = "Eps", required = true) float Eps,
    		@RequestParam(value = "granularity", required = true) int granularity, 
    		@RequestParam(value = "MinPts", required = true) int MinPts,HttpServletRequest request)
	{
		//待聚类文本
		List<String[]> list= (List<String[]>) redisService.getObject(KEY.REDIS_CONTENT, request);
		if (Eps < 0 || Eps > 1 ) 
		{
			 return ResultUtil.errorWithMsg("半径超出其取值范围: 0 ~ 1");
		}
		if (MinPts < 1 ) 
		{
			 return ResultUtil.errorWithMsg("最小数目应该大于1");
		}
		else
		{
			String result = algorithmContainerService.UseDBScan(list, Eps, MinPts, granularity, request);
			if (result==null) {
				 return ResultUtil.errorWithMsg("DBScan聚类失败");
			}
			return ResultUtil.success(result);
		}
	}
}
