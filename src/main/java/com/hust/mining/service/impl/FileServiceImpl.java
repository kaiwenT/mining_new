package com.hust.mining.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hust.mining.dao.FileDao;
import com.hust.mining.model.Issue;
import com.hust.mining.model.IssueFile;
import com.hust.mining.model.params.Condition;
import com.hust.mining.service.FileService;
import com.hust.mining.service.IssueService;
import com.hust.mining.service.UserService;
import com.hust.mining.util.ExcelUtil;

@Service
public class FileServiceImpl implements FileService {
    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Autowired
    private FileDao fileDao;

    @Autowired
    private UserService userService;
    @Autowired
    private IssueService issueService;

    @Override
    public int insert(Condition con, HttpServletRequest request) {
        // TODO Auto-generated method stub
        MultipartFile file = con.getFile();
        List<String[]> list = new ArrayList<String[]>();
        InputStream is = null;
        try {
            // 此处index传入的顺序必须与constants中定义的value值保持一致
            list = ExcelUtil.readAll(file.getOriginalFilename(), file.getInputStream(), con.getUrlIndex());
        } catch (IOException e) {
            logger.error("读取文件出现异常\t" + e.toString());
            return 0;
        }
//        if(con.getSourceType().equals("微博")){
//            WeiboUtil.filter(list);
//        }
        String user = userService.getCurrentUser(request);
        String issueId = issueService.getCurrentIssueId(request);
        Issue issue = new Issue();
        issue.setIssueId(issueId);
        issueService.updateIssueInfo(issue, request);
        IssueFile issueFile = new IssueFile();
        issueFile.setFileId(UUID.randomUUID().toString());
        issueFile.setFileName(file.getOriginalFilename());
        issueFile.setCreator(user);
        issueFile.setIssueId(issueId);
        issueFile.setLineNumber(list.size());
        issueFile.setUploadTime(new Date());
        issueFile.setSize((int) (file.getSize() / 1024));
        issueFile.setSourceType(con.getSourceType());
        return fileDao.insert(issueFile, list);
    }

    @Override
    public int deleteById(String fileId) {
        // TODO Auto-generated method stub
        return fileDao.deleteById(fileId);
    }

	@Override
	public List<String[]> getContentById(String path, String name) {
		// TODO Auto-generated method stub
		return fileDao.getContentById(path, name);
	}
    
    @Override
    public List<IssueFile> queryFilesByIssueId(String issueId) {
        // TODO Auto-generated method stub
        return fileDao.queryFilesByIssueId(issueId);
    }

    @Override
    public List<IssueFile> searchFilesByTime(String issueId, Date start, Date end) {
        // TODO Auto-generated method stub
        return fileDao.searchFilesByTime(issueId, start, end);
    }
}
