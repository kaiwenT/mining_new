package com.hust.mining.service;

import java.util.List;
import java.util.Map;

public interface MiningService {
    public List<List<Integer>> cluster(List<String[]> content, int converterType,int algorithmType, int granularity);

    /**
     * 根据聚类结果得到 聚类后类的信息（所有类（类信息（时间最早记录的index，类内所有记录数量）））
     * @param content 需要的聚类集合
     * @param cluster 聚类后得到下标（与conten对应）
     * @return
     */
    public List<int[]> count(List<String[]> content, List<String[]> cluster);

    public Map<String, Map<String, Map<String, Integer>>> statistic(List<String[]> content, String[] array,
            int interval);

    public Map<String, Object> getAmount(Map<String, Map<String, Map<String, Integer>>> map);

    public Map<String, Integer> calAttention(Map<String, Integer> map);

	public Map<String, Map<String, Map<String, Integer>>> statisticStdRes(List<String[]> content, int interval);
}
