package com.hust.mining.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hust.mining.constant.Constant.DIRECTORY;
import com.hust.mining.dao.mapper.ResultMapper;
import com.hust.mining.model.Result;
import com.hust.mining.model.ResultExample;
import com.hust.mining.model.ResultExample.Criteria;
import com.hust.mining.model.ResultWithContent;
import com.hust.mining.util.FileUtil;

@Repository
public class ResultDao {

    @Autowired
    private ResultMapper resultMapper;

    public List<String[]> getResultConentById(String resultId, String issueId, String path) 
    {
        ResultExample example = new ResultExample();
        Criteria cri = example.createCriteria();
        cri.andRidEqualTo(resultId);
        cri.andIssueIdEqualTo(issueId);
        List<Result> list = resultMapper.selectByExample(example);
        if (null == list || list.size() == 0) {
            return null;
        }
        List<String[]> clusterResult = FileUtil.read(path + resultId);
        return clusterResult;
    }

    public List<Result> selectByissueIdAndUser(String issueid, String user)
    {
    	ResultExample example = new ResultExample();
    	Criteria cri = example.createCriteria();
    	cri.andIssueIdEqualTo(issueid);
    	cri.andCreatorEqualTo(user);
		List<Result> list = resultMapper.selectByExample(example);
		if (null==list || list.size()==0) {
			return null;
		}
		return list;
    }

    public int delResultById(String resultId) {
//        FileUtil.delete(DIRECTORY.CONTENT + resultId);
        FileUtil.delete(DIRECTORY.MODIFY_CLUSTER + resultId);
        FileUtil.delete(DIRECTORY.MODIFY_COUNT + resultId);
        FileUtil.delete(DIRECTORY.ORIG_CLUSTER + resultId);
        FileUtil.delete(DIRECTORY.ORIG_COUNT + resultId);
        int del = resultMapper.deleteByPrimaryKey(resultId);
        return del;
    }

    public int updateResult(ResultWithContent rc) {
        String name = rc.getResult().getRid();
        boolean b1 = FileUtil.write(DIRECTORY.MODIFY_CLUSTER + name, rc.getModiCluster());
        boolean b2 = FileUtil.write(DIRECTORY.MODIFY_COUNT + name, rc.getModiCount());
        if (b1 && b2) {
            Result result = rc.getResult();
            return resultMapper.updateByPrimaryKeySelective(result);
        }
        return 0;
    }
    
  //同一人创建的任务中，只允许存在一条记录。若检测到记录的存在，则对原数据库记录更新，以及为相关文件重新填充内容
    public int update(ResultWithContent rc) {
        String name = rc.getResult().getRid();
        String contentpath = DIRECTORY.CONTENT + name;
        boolean b1 = FileUtil.write(contentpath, rc.getContent());
        String clusterpath = DIRECTORY.ORIG_CLUSTER + name;
        boolean b2 = FileUtil.write(clusterpath, rc.getOrigCluster());
        String countpath = DIRECTORY.ORIG_COUNT + name;
        boolean b3 = FileUtil.write(countpath, rc.getOrigCount());
        String modicluster = DIRECTORY.MODIFY_CLUSTER + name;
        boolean b4 = FileUtil.write(modicluster, rc.getOrigCluster());
        String modicount = DIRECTORY.MODIFY_COUNT + name;
        boolean b5 = FileUtil.write(modicount, rc.getOrigCount());
        if (b1 && b2 && b3 && b4 && b5) {
        	Result result = rc.getResult();
            return resultMapper.updateByPrimaryKeySelective(result);
        }
        return 0;
    }
    
    public int insert(ResultWithContent rc) {
        String name = rc.getResult().getRid();
        String contentpath = DIRECTORY.CONTENT + name;
        boolean b1 = FileUtil.write(contentpath, rc.getContent());
        String clusterpath = DIRECTORY.ORIG_CLUSTER + name;
        boolean b2 = FileUtil.write(clusterpath, rc.getOrigCluster());
        String countpath = DIRECTORY.ORIG_COUNT + name;
        boolean b3 = FileUtil.write(countpath, rc.getOrigCount());
        String modicluster = DIRECTORY.MODIFY_CLUSTER + name;
        boolean b4 = FileUtil.write(modicluster, rc.getOrigCluster());
        String modicount = DIRECTORY.MODIFY_COUNT + name;
        boolean b5 = FileUtil.write(modicount, rc.getOrigCount());
        if (b1 && b2 && b3 && b4 && b5) {
            return resultMapper.insert(rc.getResult());
        }
        return 0;
    }
    
    

    public List<Result> queryResultsByIssueId(String issueId) {
        ResultExample example = new ResultExample();
        example.createCriteria().andIssueIdEqualTo(issueId);
        example.setOrderByClause("create_time desc");
        return resultMapper.selectByExample(example);
    }
}
