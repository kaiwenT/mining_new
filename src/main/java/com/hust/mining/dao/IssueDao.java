package com.hust.mining.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hust.mining.dao.mapper.IssueMapper;
import com.hust.mining.model.Issue;
import com.hust.mining.model.IssueExample;
import com.hust.mining.model.IssueExample.Criteria;
import com.hust.mining.model.params.IssueQueryCondition;

@Repository
public class IssueDao {

    @Autowired
    private IssueMapper issueMapper;

    public int insert(Issue issueInfo) {
        return issueMapper.insert(issueInfo);
    }

    public int insertSelective(Issue issueInfo) {
        return issueMapper.insertSelective(issueInfo);
    }

    public Issue selectById(String issueId) {
        return issueMapper.selectByPrimaryKey(issueId);
    }

    public int updateIssueInfo(Issue issue) {
        if (StringUtils.isBlank(issue.getIssueId())) {
            return 0;
        }
        String uuid = issue.getIssueId();
        IssueExample example = new IssueExample();
        example.createCriteria().andIssueIdEqualTo(uuid);
        return issueMapper.updateByExampleSelective(issue, example);
    }

    public List<Issue> queryIssue(IssueQueryCondition con) {
        IssueExample example = new IssueExample();
        Criteria criteria = example.createCriteria();
        if (!StringUtils.isBlank(con.getIssueId())) {
            criteria.andIssueIdEqualTo(con.getIssueId());
        }
        if (!StringUtils.isBlank(con.getUser())) {
            criteria.andCreatorEqualTo(con.getUser());
        }
        if (!StringUtils.isBlank(con.getIssueName())) {
          //  criteria.andIssueNameEqualTo(con.getIssueName());
        	criteria.andIssueNameLike("%"+con.getIssueName()+"%");
        }
        if (!StringUtils.isBlank(con.getIssueType())) {
        	criteria.andIssueTypeEqualTo(con.getIssueType());
        }
        if (null != con.getCreateStartTime()) {
            criteria.andCreateTimeGreaterThanOrEqualTo(con.getCreateStartTime());
        }
        if (null != con.getCreateEndTime()) {
            criteria.andCreateTimeLessThanOrEqualTo(con.getCreateEndTime());
        }
        if (null != con.getLastUpdateStartTime()) {
            criteria.andLastUpdateTimeGreaterThanOrEqualTo(con.getLastUpdateStartTime());
        }
        if (null != con.getLastUpdateEndTime()) {
            criteria.andLastUpdateTimeLessThanOrEqualTo(con.getLastUpdateEndTime());
        }
        example.setOrderByClause("last_update_time desc");
        example.setStart((con.getPageNo() - 1) * con.getPageSize());
        example.setLimit(con.getPageSize());
        return issueMapper.selectByExample(example);
    }

    public long queryIssueCount(IssueQueryCondition con){
    	IssueExample example = new IssueExample();
        Criteria criteria = example.createCriteria();
        if (!StringUtils.isBlank(con.getIssueId())) {
            criteria.andIssueIdEqualTo(con.getIssueId());
        }
        if (!StringUtils.isBlank(con.getUser())) {
            criteria.andCreatorEqualTo(con.getUser());
        }
        if (!StringUtils.isBlank(con.getIssueName())) {
            criteria.andIssueNameLike("%"+con.getIssueName()+"%");
        }
        if (!StringUtils.isBlank(con.getIssueType())) {
        	criteria.andIssueTypeEqualTo(con.getIssueType());
        }
        if (null != con.getCreateStartTime()) {
            criteria.andCreateTimeGreaterThanOrEqualTo(con.getCreateStartTime());
        }
        if (null != con.getCreateEndTime()) {
            criteria.andCreateTimeLessThanOrEqualTo(con.getCreateEndTime());
        }
        if (null != con.getLastUpdateStartTime()) {
            criteria.andLastUpdateTimeGreaterThanOrEqualTo(con.getLastUpdateStartTime());
        }
        if (null != con.getLastUpdateEndTime()) {
            criteria.andLastUpdateTimeLessThanOrEqualTo(con.getLastUpdateEndTime());
        }
    	return issueMapper.countByExample(example);
    }
    
    public int deleteIssueById(String issueId, String user) {
        IssueExample example = new IssueExample();
        Criteria cri = example.createCriteria();
        //System.out.println("删除的数据为"+issueId);
        cri.andIssueIdEqualTo(issueId);
        //cri.andCreatorEqualTo(user);
        return issueMapper.deleteByExample(example);
    }
}
