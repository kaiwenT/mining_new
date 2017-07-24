package com.hust.mining.dao;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hust.mining.constant.Constant.DIRECTORY;
import com.hust.mining.constant.Constant.FONT;
import com.hust.mining.constant.Constant.Index;
import com.hust.mining.dao.mapper.CoreResultMapper;
import com.hust.mining.dao.mapper.StandardResultMapper;
import com.hust.mining.model.CoreResult;
import com.hust.mining.model.CoreResultExample;
import com.hust.mining.model.CoreResultExample.Criteria;
import com.hust.mining.model.StandardResult;
import com.hust.mining.model.params.CoreResultQueryCondition;
import com.hust.mining.urltags.crawler;
import com.hust.mining.util.AttrUtil;
import com.hust.mining.util.ConvertUtil;
import com.hust.mining.util.FileUtil;
import com.hust.mining.util.WordUtil;
import com.hust.mining.util.WordUtil.Env;
import com.hust.summary.Summary;

@Repository
public class CoreResultDao {
	private static final Logger logger = LoggerFactory.getLogger(CoreResultDao.class);

	@Autowired
	private CoreResultMapper coreResultMapper;
	@Autowired
	private StandardResultMapper standardResultMapper;

	// 读取可以优化、读取指定列。等谈凯改完result对result也进行优化
	public int insert(CoreResultQueryCondition con, CoreResult coreResult) {
		List<String[]> countResult = FileUtil.read(DIRECTORY.STDRES_COUNT + con.getStdResId());
		StandardResult standardResult = standardResultMapper.selectByPrimaryKey(con.getStdResId());
		List<String[]> content = FileUtil.read(DIRECTORY.CONTENT + standardResult.getContentName());
		List<int[]> count = ConvertUtil.toIntList(countResult);
		List<String[]> staticsInfo = new ArrayList<String[]>();
		for (int[] item : count) {
			String[] old = content.get(item[Index.COUNT_ITEM_INDEX] + 1);
			String[] ne = new String[old.length + 1];
			System.arraycopy(old, 0, ne, 1, old.length);
			ne[0] = item[Index.COUNT_ITEM_AMOUNT] + "";
			staticsInfo.add(ne);
		}
		// title、url、time
		staticsInfo.add(0, AttrUtil.findEssentialIndex(content.get(0)));

		List<String[]> cluster = FileUtil.read(DIRECTORY.STDRES_CLUSTER + con.getStdResId());
		List<int[]> clusterCount = ConvertUtil.toIntList(cluster);
		generateWordCoreReport(DIRECTORY.CORERES + coreResult.getCoreRid(), con.getCoreResName(), con.getStdResId(),
				staticsInfo, content, clusterCount);

		return coreResultMapper.insert(coreResult);
	}

	// 插入核心数据
	public int insertCore(CoreResultQueryCondition con, String contentName, CoreResult coreResult) {
		try {
			List<List<String[]>> allContent = FileUtil.readwithNullRow(DIRECTORY.STDRES_CONTENT + contentName);
			// title、url、time
			generateWordResReport(DIRECTORY.CORERES + coreResult.getCoreRid(), con.getCoreResName(), con.getStdResId(),
					allContent);
		} catch (Exception e) {
			e.printStackTrace();
		}


		return coreResultMapper.insert(coreResult);
	}

	public int update(CoreResult coreResult) {
		return coreResultMapper.updateByPrimaryKeySelective(coreResult);
	}

	public List<CoreResult> queryCoreRessByIssueId(String issueId) {
		CoreResultExample example = new CoreResultExample();
		Criteria criteria = example.createCriteria();
		criteria.andIssueIdEqualTo(issueId);
		example.setOrderByClause("create_time desc");
		return coreResultMapper.selectByExample(example);
	}

	public CoreResult queryCoreResById(String coreResId) {
		return coreResultMapper.selectByPrimaryKey(coreResId);
	}

	public int deleteById(String coreResId) {
		FileUtil.delete(DIRECTORY.CORERES + coreResId);
		return coreResultMapper.deleteByPrimaryKey(coreResId);
	}

	/**
	 * 产生核心报告txt版本
	 * 
	 * @param reportName
	 * @param stdResId
	 * @param staticsInfo
	 * @param content
	 *            Conten内容
	 * @param clusterCount
	 *            聚类结果（一行为一个类（每个数字为content内容的index+1））
	 * @return
	 */
	public List<StringBuilder> generateTxtCoreReport(String reportName, String stdResId, List<String[]> staticsInfo,
			List<String[]> content, List<int[]> clusterCount) {
		List<StringBuilder> sbList = new ArrayList<StringBuilder>();
		try {
			StandardResult standardResult = standardResultMapper.selectByPrimaryKey(stdResId);
			String dateCount = standardResult.getDateCount();
			String sourceCount = standardResult.getSourceCount();

			// 信息总数
			int totalInfo = 0;
			// 信息平均数
			int avgInfo = 0;
			// 信息峰值日期
			String maxDate = "";
			// 信息峰值
			int maxCount = 0;
			// 信息峰值占比
			float pOfMaxCount = 0.0f;
			String[] dateCountArray = dateCount.split(",");
			// 第一天的日期
			String firstDateCount = dateCountArray[0].split("=")[0];
			// 最后一天的日期
			String lastDateCount = dateCountArray[dateCountArray.length - 1].split("=")[0];
			for (String eachCount : dateCountArray) {
				String[] curCount = eachCount.split("=");
				int count = Integer.parseInt(curCount[1]);
				if (maxCount < count) {
					maxCount = count;
					maxDate = curCount[0];
				}
				totalInfo += count;
			}
			avgInfo = totalInfo / dateCountArray.length;
			pOfMaxCount = Math.round((float) maxCount / totalInfo * 100);
			// title、count、url
			List<String[]> maxDayInfoCount = dayInfoCount(content, clusterCount, maxDate);
			List<String[]> msgTypeCount = calcSourceCount(sourceCount);
			// 一周概况
			StringBuilder summaryOfWeek = new StringBuilder("舆情参阅\n\n一周概况:\n\t本周互联网相关舆情信息更新量 " + totalInfo + " 条（日均 "
					+ avgInfo + " 条）与上周xxx条（日均xx条）相比，信息量...。未发现“正/负”面舆情。\n\n");
			sbList.add(summaryOfWeek);

			// 舆情聚焦
			StringBuilder focusOfPubSentiment = new StringBuilder("舆情聚焦:\n");
			String[] essentialIndexs = staticsInfo.get(0);
			for (int i = 1; i < staticsInfo.size(); i++) {
				String[] currentInfo = staticsInfo.get(i);
				focusOfPubSentiment.append("\t" + i + "." + currentInfo[Integer.parseInt(essentialIndexs[0]) + 1]
						+ "\n\t\t该事件出现次数：" + currentInfo[0] + "条。\n\t\t该新闻的网址为： "
						+ currentInfo[Integer.parseInt(essentialIndexs[1]) + 1] + "\n");
			}
			focusOfPubSentiment.append("行业舆情——（未知）\n\n");
			sbList.add(focusOfPubSentiment);

			// 监测信息量日分布情况
			StringBuilder dailyDistributionOfPubSentiment = new StringBuilder("监测信息量日分布情况:\n");
			dailyDistributionOfPubSentiment
					.append("\t" + firstDateCount + " 至 " + lastDateCount + "，通过四川电信互联网舆情信息服务平台监测数据显示，本时间段内与" + "“"
							+ reportName + "”相关互联网信息 " + totalInfo + " 条，未发现“正/负”面舆情。");
			dailyDistributionOfPubSentiment.append("信息具体情况如下：\n\t监测数据显示，" + reportName + firstDateCount + " 至 "
					+ lastDateCount + "相关信息总量 " + totalInfo + " 条，平均每日信息量为 " + avgInfo + " 条。其中，" + maxDate
					+ "当天的相关信息量是本周最大峰值，相关信息共有 " + maxCount + " 条，占一周信息量的 " + pOfMaxCount + "% ，主要为");
			for (int i = 0; i < maxDayInfoCount.size() && i < 3; i++) {
				String[] infoCount = maxDayInfoCount.get(i);
				dailyDistributionOfPubSentiment
						.append("《" + infoCount[0] + "》（" + infoCount[1] + "条，网址：" + infoCount[2] + "）");
			}
			dailyDistributionOfPubSentiment.append("（只罗列出这一天的排名前三条信息。），相关转载传播。\n\t本周信息主要为" + msgTypeCount.get(0)[0]
					+ "信息，占所有信息的比例共为" + msgTypeCount.get(0)[1] + "%；" + "其次" + msgTypeCount.get(1)[0] + "信息，占比共为"
					+ msgTypeCount.get(1)[1] + "%；" + msgTypeCount.get(2)[0] + "信息，占比共为" + msgTypeCount.get(2)[1]
					+ "%。\n\n");
			sbList.add(dailyDistributionOfPubSentiment);

			// 舆情聚焦(摘要部分，陈杰)
			StringBuilder summaryOfFocus = new StringBuilder("摘要部分待续");
			sbList.add(summaryOfFocus);

		} catch (Exception e) {
			logger.error("产生核心报告出错!{}", e.toString());
			return null;
		}
		return sbList;
	}

	/**
	 * 产生核心报告word版本
	 * 
	 * @param filename
	 * @param reportName
	 * @param stdResId
	 * @param staticsInfo
	 * @param content
	 *            Content内容
	 * @param clusterCount
	 *            聚类结果（一行为一个类（每个数字为content内容的index+1））
	 */
	public void generateWordCoreReport(String filename, String reportName, String stdResId, List<String[]> staticsInfo,
			List<String[]> content, List<int[]> clusterCount) {
		List<StringBuilder> sbList = new ArrayList<StringBuilder>();
		String[] attrs = content.get(0);
		try {
			WordUtil wu = new WordUtil();
			wu.addParaText("(四川省戒毒管理局专供)", new Env().bold(true).fontType(FONT.KAITI));
			wu.addParaText("舆 情 参 阅", new Env().fontSize(52).bold(true).fontType(FONT.XINSONGTI).fontColor(FONT.GREEN)
					.alignment("center"));

			Env titleEnv = new Env().fontSize(22).bold(true).fontType(FONT.SONGTI);
			Env mainEnv = new Env().fontType(FONT.FANGSONG);
			Env mainBEnv = new Env(mainEnv).bold(true).fontType(FONT.SONGTI);
			Env mainRBEnv = new Env(mainBEnv).fontColor(FONT.RED);
			Env mainSEnv = new Env(mainEnv).fontSize(10);

			StandardResult standardResult = standardResultMapper.selectByPrimaryKey(stdResId);
			String dateCount = standardResult.getDateCount();
			String sourceCount = standardResult.getSourceCount();

			// 信息总数
			int totalInfo = 0;
			// 信息平均数
			int avgInfo = 0;
			// 信息峰值日期
			String maxDate = "";
			// 信息峰值
			int maxCount = 0;
			// 信息峰值占比
			float pOfMaxCount = 0.0f;
			String[] dateCountArray = dateCount.split(",");
			// 第一天的日期
			String firstDateCount = dateCountArray[0].split("=")[0];
			// 最后一天的日期
			String lastDateCount = dateCountArray[dateCountArray.length - 1].split("=")[0];
			for (String eachCount : dateCountArray) {
				String[] curCount = eachCount.split("=");
				int count = Integer.parseInt(curCount[1]);
				if (maxCount < count) {
					maxCount = count;
					maxDate = curCount[0];
				}
				totalInfo += count;
			}
			avgInfo = totalInfo / dateCountArray.length;
			pOfMaxCount = Math.round((float) maxCount / totalInfo * 100);
			// title、count、url
			List<String[]> maxDayInfoCount = dayInfoCount(content, clusterCount, maxDate);
			List<String[]> msgTypeCount = calcSourceCount(sourceCount);
			// 一周概况
			wu.addParaText("一周概况", titleEnv);
			wu.addParaText("本周互联网相关舆情信息更新量 " + totalInfo + " 条（日均 " + avgInfo + " 条）与上周xxx条（日均xx条）相比，信息量...。", mainEnv);
			wu.appendParaText("未发现“正/负”面舆情。", mainBEnv);

			wu.setBreak();

			// 舆情聚焦
			wu.addParaText("舆情聚焦", titleEnv);
			String[] essentialIndexs = staticsInfo.get(0);
			for (int i = 1; i < staticsInfo.size(); i++) {
				String[] currentInfo = staticsInfo.get(i);
				wu.addParaText(i + ". " + currentInfo[Integer.parseInt(essentialIndexs[0]) + 1], mainBEnv);
				wu.addParaText(currentInfo[0] + "条;" + currentInfo[Integer.parseInt(essentialIndexs[1]) + 1], mainSEnv);
			}
			wu.addParaText("行业舆情", titleEnv);

			wu.setBreak();

			// 监测信息量日分布情况
			wu.addParaText("监测信息量日分布情况", titleEnv);
			wu.addParaText(firstDateCount + " 至 " + lastDateCount + "，通过四川电信互联网舆情信息服务平台监测数据显示，本时间段内与" + "“" + reportName
					+ "”相关互联网信息 " + totalInfo + " 条，", mainEnv);
			wu.appendParaText("未发现“正/负”面舆情。", mainBEnv);
			wu.addParaText("信息具体情况如下：", mainEnv);
			wu.addParaText("监测数据显示，" + reportName + firstDateCount + " 至 " + lastDateCount + "相关信息总量 " + totalInfo
					+ " 条，平均每日信息量为 " + avgInfo + " 条。其中，" + maxDate + "当天的相关信息量是本周最大峰值，相关信息共有 " + maxCount
					+ " 条，占一周信息量的 ", mainEnv);
			wu.appendParaText(pOfMaxCount + "%", mainRBEnv);
			wu.appendParaText("，主要为", mainEnv);
			for (int i = 0; i < maxDayInfoCount.size() && i < 3; i++) {
				String[] infoCount = maxDayInfoCount.get(i);
				wu.appendParaText("《" + infoCount[0] + "》", mainBEnv);
				wu.addParaText("（" + infoCount[1] + "条;" + infoCount[2] + "）", mainSEnv);
			}
			wu.addParaText("，相关转载传播。", mainEnv);
			wu.addParaText("本周信息主要为" + msgTypeCount.get(0)[0] + "信息，占所有信息的比例共为", mainEnv);
			wu.appendParaText(msgTypeCount.get(0)[1] + "%", mainRBEnv);
			wu.appendParaText("；其次" + msgTypeCount.get(1)[0] + "信息，占比共为", mainEnv);
			wu.appendParaText(msgTypeCount.get(1)[1] + "%", mainRBEnv);
			wu.appendParaText("；" + msgTypeCount.get(2)[0] + "信息，占比共为", mainEnv);
			wu.appendParaText(msgTypeCount.get(2)[1] + "%", mainRBEnv);
			wu.appendParaText("。", mainEnv);

			wu.setBreak();

			// 舆情聚焦(摘要部分，陈杰)
			wu.addParaText("舆情聚焦", titleEnv);
			// List<标题，摘要内容，发布媒体>
			List<String[]> summary = new ArrayList<>();
//			summary = generateSummary(attrs, content, clusterCount);
			int num = 1;
			for (String[] strings : summary) {
				wu.addParaText((num++) + ". " + strings[0], mainBEnv);
				wu.addParaText("        " + strings[1] + strings[2], mainEnv);
			}

			wu.setPageBreak();

			Env env1 = new Env().fontType(FONT.KAITI);
			Env env2 = new Env(env1).alignment("right");
			Env env3 = new Env(env1).fontSize(16).bold(true);
			wu.addParaText("（责任编辑：汪静远）", env2);
			wu.addParaText("四川电信互联网舆情信息服务中心 ", env3);
			wu.addParaText("声明：", env3);
			wu.appendParaText(
					"以上舆情信息仅供参考，用户对于舆情信息所反映出的问题或状况的处理，应综合其他信息加以判断和利用，仅凭以上舆情信息做出判断、进行决策等处理措施造成不利后果及损失的，我单位不承担任何责任。",
					env1);

			wu.write(filename);
		} catch (Exception e) {
			logger.error("产生核心报告出错!{}", e.toString());
		}
	}

	// 新版本的生成结果数据
	public void generateWordResReport(String filename, String reportName, String stdResId,
			List<List<String[]>> allContent) {
		String[] essentialIndexs = AttrUtil.findEssentialIndex(allContent.get(0).get(0));
		String[] attrs = allContent.get(0).remove(0);
		try {
			WordUtil wu = new WordUtil();
			wu.addParaText("(四川省戒毒管理局专供)", new Env().bold(true).fontType(FONT.KAITI));
			wu.addParaText("舆 情 参 阅", new Env().fontSize(52).bold(true).fontType(FONT.XINSONGTI).fontColor(FONT.GREEN)
					.alignment("center"));

			Env titleEnv = new Env().fontSize(22).bold(true).fontType(FONT.SONGTI);
			Env mainEnv = new Env().fontType(FONT.FANGSONG);
			Env mainBEnv = new Env(mainEnv).bold(true).fontType(FONT.SONGTI);
			Env mainRBEnv = new Env(mainBEnv).fontColor(FONT.RED);
			Env mainSEnv = new Env(mainEnv).fontSize(10);

			StandardResult standardResult = standardResultMapper.selectByPrimaryKey(stdResId);
			String dateCount = standardResult.getDateCount();
			String sourceCount = standardResult.getSourceCount();

			// 信息总数
			int totalInfo = 0;
			// 信息平均数
			int avgInfo = 0;
			// 信息峰值日期
			String maxDate = "";
			// 信息峰值
			int maxCount = 0;
			// 信息峰值占比
			float pOfMaxCount = 0.0f;
			String[] dateCountArray = dateCount.split(",");
			// 第一天的日期
			String firstDateCount = dateCountArray[0].split("=")[0];
			// 最后一天的日期
			String lastDateCount = dateCountArray[dateCountArray.length - 1].split("=")[0];
			for (String eachCount : dateCountArray) {
				String[] curCount = eachCount.split("=");
				int count = Integer.parseInt(curCount[1]);
				if (maxCount < count) {
					maxCount = count;
					maxDate = curCount[0];
				}
				totalInfo += count;
			}
			avgInfo = totalInfo / dateCountArray.length;
			pOfMaxCount = Math.round((float) maxCount / totalInfo * 100);
			// title、count、url
			List<String[]> maxDayInfoCount = dayInfoCount_new(allContent,essentialIndexs,maxDate);
			List<String[]> msgTypeCount = calcSourceCount(sourceCount);
			// 一周概况
			wu.addParaText("一周概况", titleEnv);
			wu.addParaText("本周互联网相关舆情信息更新量 " + totalInfo + " 条（日均 " + avgInfo + " 条）与上周xxx条（日均xx条）相比，信息量...。", mainEnv);
			wu.appendParaText("未发现“正/负”面舆情。", mainBEnv);

			wu.setBreak();

			// 舆情聚焦
			wu.addParaText("舆情聚焦", titleEnv);
			//String[] essentialIndexs = staticsInfo.get(0);
			for (int i = 1; i < allContent.size(); i++) {
				String[] currentInfo = allContent.get(i).get(0);
				wu.addParaText(i + ". " + currentInfo[Integer.parseInt(essentialIndexs[0]) + 1], mainBEnv);
				wu.addParaText(currentInfo[0] + "条;" + currentInfo[Integer.parseInt(essentialIndexs[1]) + 1], mainSEnv);
			}
			wu.addParaText("行业舆情", titleEnv);

			wu.setBreak();

			// 监测信息量日分布情况
			wu.addParaText("监测信息量日分布情况", titleEnv);
			wu.addParaText(firstDateCount + " 至 " + lastDateCount + "，通过四川电信互联网舆情信息服务平台监测数据显示，本时间段内与" + "“" + reportName
					+ "”相关互联网信息 " + totalInfo + " 条，", mainEnv);
			wu.appendParaText("未发现“正/负”面舆情。", mainBEnv);
			wu.addParaText("信息具体情况如下：", mainEnv);
			wu.addParaText("监测数据显示，" + reportName + firstDateCount + " 至 " + lastDateCount + "相关信息总量 " + totalInfo
					+ " 条，平均每日信息量为 " + avgInfo + " 条。其中，" + maxDate + "当天的相关信息量是本周最大峰值，相关信息共有 " + maxCount
					+ " 条，占一周信息量的 ", mainEnv);
			wu.appendParaText(pOfMaxCount + "%", mainRBEnv);
			wu.appendParaText("，主要为", mainEnv);
			for (int i = 0; i < maxDayInfoCount.size() && i < 3; i++) {
				String[] infoCount = maxDayInfoCount.get(i);
				wu.appendParaText("《" + infoCount[0] + "》", mainBEnv);
				wu.addParaText("（" + infoCount[1] + "条;" + infoCount[2] + "）", mainSEnv);
			}
			wu.addParaText("，相关转载传播。", mainEnv);
			wu.addParaText("本周信息主要为" + msgTypeCount.get(0)[0] + "信息，占所有信息的比例共为", mainEnv);
			wu.appendParaText(msgTypeCount.get(0)[1] + "%", mainRBEnv);
			wu.appendParaText("；其次" + msgTypeCount.get(1)[0] + "信息，占比共为", mainEnv);
			wu.appendParaText(msgTypeCount.get(1)[1] + "%", mainRBEnv);
			wu.appendParaText("；" + msgTypeCount.get(2)[0] + "信息，占比共为", mainEnv);
			wu.appendParaText(msgTypeCount.get(2)[1] + "%", mainRBEnv);
			wu.appendParaText("。", mainEnv);

			wu.setBreak();

			// 舆情聚焦(摘要部分，陈杰)
			wu.addParaText("舆情聚焦", titleEnv);
			// List<标题，摘要内容，发布媒体>
			List<String[]> summary = new ArrayList<>();
			summary = generateSummary(attrs, allContent);
			int num = 1;
			for (String[] strings : summary) {
				wu.addParaText((num++) + ". " + strings[0], mainBEnv);
				wu.addParaText("        " + strings[1] + strings[2], mainEnv);
			}

			wu.setPageBreak();

			Env env1 = new Env().fontType(FONT.KAITI);
			Env env2 = new Env(env1).alignment("right");
			Env env3 = new Env(env1).fontSize(16).bold(true);
			wu.addParaText("（责任编辑：汪静远）", env2);
			wu.addParaText("四川电信互联网舆情信息服务中心 ", env3);
			wu.addParaText("声明：", env3);
			wu.appendParaText(
					"以上舆情信息仅供参考，用户对于舆情信息所反映出的问题或状况的处理，应综合其他信息加以判断和利用，仅凭以上舆情信息做出判断、进行决策等处理措施造成不利后果及损失的，我单位不承担任何责任。",
					env1);

			wu.write(filename);
		} catch (Exception e) {
			logger.error("产生核心报告出错!{}", e.toString());
		}
	}

	/**
	 * 统计某一天新闻出现的数量
	 */
	public List<String[]> dayInfoCount(List<String[]> content, List<int[]> clusterCount, String pointDate) {
		List<String[]> list = new ArrayList<String[]>();
		try {

			// title、url、time
			String[] essentialIndex = AttrUtil.findEssentialIndex(content.remove(0));
			int titleIndex = Integer.parseInt(essentialIndex[0]);
			int urlIndex = Integer.parseInt(essentialIndex[1]);
			int timeIndex = Integer.parseInt(essentialIndex[2]);

			for (int[] subClusterCount : clusterCount) {
				int count = 0;
				for (int index : subClusterCount) {
					String[] subContent = content.get(index);
					String date = subContent[timeIndex].trim().substring(0, 10);
					if (date.equals(pointDate)) {
						count++;
					}
				}
				String[] firstContent = content.get(subClusterCount[0]);
				String[] info = new String[3];
				info[0] = firstContent[titleIndex];
				info[1] = String.valueOf(count);
				info[2] = firstContent[urlIndex];
				list.add(info);
			}

			Collections.sort(list, new Comparator<String[]>() {
				@Override
				public int compare(String[] o1, String[] o2) {
					return Integer.parseInt(o2[1]) - Integer.parseInt(o1[1]);
				}
			});
		} catch (Exception e) {
			logger.error("统计某一天新闻出现的数量 {}", e.toString());
		}
		return list;
	}
	
	// 结果数据的统计 new
	public List<String[]> dayInfoCount_new(List<List<String[]>> allContent,String[] essentialIndex, String pointDate) {
		List<String[]> list = new ArrayList<String[]>();
		pointDate = pointDate.trim();
		try {
			
			// title、url、time
			//String[] essentialIndex = AttrUtil.findEssentialIndex(content.remove(0));
			int titleIndex = Integer.parseInt(essentialIndex[0]);
			int urlIndex = Integer.parseInt(essentialIndex[1]);
			int timeIndex = Integer.parseInt(essentialIndex[2]);
			
			for(int i=0; i<allContent.size(); i++){
				int count = 0;
				
				List<String[]> subList = allContent.get(i);
				for(int j=0; j<subList.size(); j++){
					String date = subList.get(j)[timeIndex].trim().substring(0, 10);
					if (date.equals(pointDate)) {
						count++;
					}
				}
				String[] info = new String[3];
				String[] firstNews = subList.get(0);
				info[0] = firstNews[titleIndex];
				info[1] = String.valueOf(count);
				info[2] = firstNews[urlIndex];
				list.add(info);
			}
			
			Collections.sort(list, new Comparator<String[]>() {
				@Override
				public int compare(String[] o1, String[] o2) {
					return Integer.parseInt(o2[1]) - Integer.parseInt(o1[1]);
				}
			});
		} catch (Exception e) {
			logger.error("统计某一天新闻出现的数量 {}", e.toString());
		}
		return list;
	}

	/**
	 * 用于统计信息类型占比（新闻、报纸类；微博、微信类；论坛、问道、博客等类。）
	 */
	private List<String[]> calcSourceCount(String sourceCount) {
		List<String[]> res = new ArrayList<String[]>();
		try {
			int total = 0;
			int xb = 0;
			int ww = 0;
			int lwb = 0;

			for (String strs : sourceCount.split(",")) {
				String[] kv = strs.split("=");
				int count = Integer.valueOf(kv[1]);
				String msgType = kv[0].trim();
				if (Pattern.matches("新闻|报纸", msgType)) {
					xb += count;
				} else if (Pattern.matches("微博|微信", msgType)) {
					ww += count;
				} else {
					lwb += count;
				}
				total += count;
			}

			res.add(new String[] { "新闻、报纸类", String.valueOf(Math.round((float) xb / total * 100)) });
			res.add(new String[] { "微博、微信类", String.valueOf(Math.round((float) ww / total * 100)) });
			res.add(new String[] { "论坛、问道、博客等类", String.valueOf(Math.round((float) lwb / total * 100)) });

			Collections.sort(res, new Comparator<String[]>() {
				@Override
				public int compare(String[] o1, String[] o2) {
					// System.out.println(Integer.parseInt(o1[1]) + "\t" +
					// Integer.parseInt(o2[1]));
					return Integer.parseInt(o2[1]) - Integer.parseInt(o1[1]);
				}
			});
		} catch (Exception e) {
			logger.error("统计信息类型占比出错！ {}", e.toString());
		}
		return res;
	}

	/**
	 * 通过读取content的内容获取url爬取新闻，然后摘要，并统计每个类的发布机构
	 * 
	 * @param content
	 *            Conten内容
	 * @param clusterCount
	 *            聚类结果（一行为一个类（每个数字为content内容的index+1））
	 * @return 返回摘要（List<标题，摘要，发布机构>）
	 */
//	private List<String[]> generateSummary(String[] attrs, List<String[]> content, List<int[]> clusterCount) {
//		List<String[]> summary = new ArrayList<>();
//		for (String string : attrs) {
//			// System.out.print(string + " ");
//		}
//		// System.out.println();
//		int titleIndex = AttrUtil.findIndexOfTitle(attrs);
//		int urlIndex = AttrUtil.findIndexOfUrl(attrs);
//		int organizationIndex = AttrUtil.findIndexOfSth(attrs, "网站|媒体名称");
//		for (int[] index : clusterCount) {
//			String title = null;
//			List<String> sentence = null;
//			Set<String> organization = new HashSet<>();
//			// 是否找到了要摘要的文章
//			boolean flag = false;
//			// 找到可以爬的sentence
//			for (int i : index) {
//				if (!flag) {
//					sentence = crawler.getSummary(content.get(i)[urlIndex]);
//					if (null != sentence) {
//						flag = true;
//						title = content.get(i)[titleIndex];
//						sentence.add(0, title);
//						Summary s = new Summary(sentence);
//						s.summary();
//						sentence = s.getSummary(null);
//					}
//				}
//				organization.add(content.get(i)[organizationIndex]);
//			}
//			String str_organization = "（";
//			if (organization.size() == 0) {
//				str_organization = "（四川戒毒所）";
//			} else {
//				for (String string : organization) {
//					str_organization += string + "、";
//				}
//				str_organization = str_organization.substring(0, str_organization.length() - 1) + "）";
//			}
//			String str_sentence = "";
//			if (flag) {
//				for (String string : sentence) {
//					str_sentence += string + "。";
//				}
//			} else {
//				title = content.get(index[0])[titleIndex];
//				str_sentence = "由于该类新闻没有合适的网站来获取新闻内容，故无法获得摘要。该类新闻的第一条新闻来源于：" + content.get(index[0])[urlIndex];
//			}
//			summary.add(new String[] { title, str_sentence, str_organization });
//		}
//		return summary;
//	}
	
	private List<String[]> generateSummary(String[] attrs, List<List<String[]>> allContent) {
		List<String[]> summary = new ArrayList<>();
	/*	for (String string : attrs) {
			// System.out.print(string + " ");
		}*/
		// System.out.println();
		int titleIndex = AttrUtil.findIndexOfTitle(attrs);
		int urlIndex = AttrUtil.findIndexOfUrl(attrs);
		int organizationIndex = AttrUtil.findIndexOfSth(attrs, "网站|媒体名称");
		for (List<String[]> content : allContent) {
			String title = null;
			List<String> sentence = null;
			Set<String> organization = new HashSet<>();
			// 是否找到了要摘要的文章
			boolean flag = false;
			// 找到可以爬的sentence
			for (String[] str : content) {
				if (!flag) {
					sentence = crawler.getSummary(str[urlIndex]);
					if (null != sentence) {
						flag = true;
						title = str[titleIndex];
						sentence.add(0, title);
						Summary s = new Summary(sentence);
						s.summary();
						sentence = s.getSummary(null);
					}
				}
				organization.add(str[organizationIndex]);
			}
			String str_organization = "（";
			if (organization.size() == 0) {
				str_organization = "（四川戒毒所）";
			} else {
				for (String string : organization) {
					str_organization += string + "、";
				}
				str_organization = str_organization.substring(0, str_organization.length() - 1) + "）";
			}
			String str_sentence = "";
			if (flag) {
				for (String string : sentence) {
					str_sentence += string + "。";
				}
			} else {
				title = content.get(0)[titleIndex];
				str_sentence = "由于该类新闻没有合适的网站来获取新闻内容，故无法获得摘要。该类新闻的第一条新闻来源于：" + content.get(0)[urlIndex];
			}
			summary.add(new String[] { title, str_sentence, str_organization });
		}
		return summary;
	}

	/**
	 * 导出文件
	 */
	public boolean export(String coreResId, OutputStream outputStream) {
		return FileUtil.write(DIRECTORY.CORERES + coreResId, outputStream);
	}
}
