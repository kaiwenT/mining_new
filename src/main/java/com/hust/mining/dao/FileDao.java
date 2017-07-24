package com.hust.mining.dao;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hust.mining.constant.Constant.DIRECTORY;
import com.hust.mining.dao.mapper.IssueFileMapper;
import com.hust.mining.model.IssueFile;
import com.hust.mining.model.IssueFileExample;
import com.hust.mining.model.IssueFileExample.Criteria;
import com.hust.mining.model.params.QueryFileCondition;
import com.hust.mining.util.FileUtil;

@Repository
public class FileDao {
    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(FileDao.class);

    @Autowired
    private IssueFileMapper issueFileMapper;

    /**
     * 写入文件到 upload文件夹 即存放上传的文件，并更新IssueFile数据库记录
     * @param file 文件的基本信息
     * @param content 文件内容 来源与upload文件，包含标题
     * @return
     */
    public int insert(IssueFile file, List<String[]> content) {
        String filename = DIRECTORY.FILE + file.getFileId();
        try {
            boolean success = FileUtil.write(filename, content);
            if (success) {
                return issueFileMapper.insert(file);
            }
        } catch (Exception e) {
            logger.error("write file error:{}", e.toString());
        }
        return 0;
    }

    public int deleteById(String fileId) {
        if (FileUtil.delete(DIRECTORY.FILE + fileId)) {
            return issueFileMapper.deleteByPrimaryKey(fileId);
        }
        return 0;
    }

    /**
     * 读取excel文件的内容，包含标题栏
     * @param filenames 需要读取内容的文件名（可以为单个，也可以为集合）
     * @return
     */
    public List<String[]> getFileContent(String...filenames) {
        List<String[]> content = FileUtil.readForUnificating(filenames);
        return content;
    }

    public List<IssueFile> queryFilesByIssueId(String issueId) {
        IssueFileExample example = new IssueFileExample();
        example.createCriteria().andIssueIdEqualTo(issueId);
        example.setOrderByClause("upload_time desc");
        return issueFileMapper.selectByExample(example);
    }

    public List<IssueFile> queryFilesByCondition(QueryFileCondition con) {
        IssueFileExample example = new IssueFileExample();
        Criteria criteria = example.createCriteria();
        if (!StringUtils.isEmpty(con.getIssueId())) {
            criteria.andIssueIdEqualTo(con.getIssueId());
        }
        if (null != con.getStart()) {
            criteria.andUploadTimeGreaterThanOrEqualTo(con.getStart());
        }
        if (null != con.getEnd()) {
            criteria.andUploadTimeLessThanOrEqualTo(con.getEnd());
        }
        if (null != con.getFileIds() && con.getFileIds().size() != 0) {
            criteria.andFileIdIn(con.getFileIds());
        }
        example.setOrderByClause("upload_time desc");
        return issueFileMapper.selectByExample(example);
    }

    public List<IssueFile> searchFilesByTime(String issueId, Date start, Date end) {
        IssueFileExample example = new IssueFileExample();
        Criteria criteria = example.createCriteria();
        criteria.andIssueIdEqualTo(issueId);
        criteria.andUploadTimeBetween(start, end);
        example.setOrderByClause("upload_time desc");
        return issueFileMapper.selectByExample(example);
    }
    
    public List<String[]> getContentById(String path, String name) {
		// TODO Auto-generated method stub
		return FileUtil.read(path+name);
	}
}
