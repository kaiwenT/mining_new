package com.hust.mining.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hust.mining.model.IssueFile;
import com.hust.mining.model.params.Condition;

public interface FileService {
	/**
	 * 读取上传文件（upload），并写入存储文件到upload文件夹中，并更新数据库添加文件信息
	 * @param con 文件内容，以及标题下标位置信息
	 * @param request 请求信息，包含redis中当前用户名，和redis中当前任务ID
	 * @return
	 */
	int insert(Condition con, HttpServletRequest request);

	int deleteById(String fileId);

	List<String[]> getContentById(String path, String name);

	List<IssueFile> queryFilesByIssueId(String issueId);

	List<IssueFile> searchFilesByTime(String issueId, Date start, Date end);

}
