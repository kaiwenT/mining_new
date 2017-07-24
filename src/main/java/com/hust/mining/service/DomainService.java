package com.hust.mining.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.hust.mining.model.Domain;
import com.hust.mining.model.DomainOne;
import com.hust.mining.model.DomainOneProperty;
import com.hust.mining.model.DomainTwo;
import com.hust.mining.model.DomainTwoProperty;
import com.hust.mining.model.params.DomainOneQueryCondition;
import com.hust.mining.model.params.DomainTwoQueryCondition;

public interface DomainService {
	//一级域名start
	
	/**
	 * 根据uuid删除一级域名
	 * @param uuid
	 * @return 删除成功返回true，失败返回false
	 */
	boolean deleteDomainOneById(String uuid);

	/**
	 * 根据指定条件查询一级域名,为null则查询所有
	 * @param condition 如需分页，请在condition中设置start,limit
	 * @return 
	 */
	List<DomainOne> getDomainOne(DomainOneQueryCondition condition);
/*	*//**
	 * 分页查找所有已知的域名，即name不为null
	 * @param start
	 * @param limit
	 * @return
	 *//*
	List<DomainOne> getKnowDomainOne(int start,int limit);
	*//**
	 * 查询所有已知域名的数量
	 * @return
	 *//*
	long getKnowDomainOneCount();
	*//**
	 * 分页查找所有未知域名信息，即name为null
	 * @param start
	 * @param limit
	 * @return
	 *//*
	List<DomainOne> getUnknowDomainOne(int start,int limit);
	*//**
	 * 查询所有未知域名的数量
	 * @return
	 *//*
	long getUnknowDomainOneCount();*/
	/**
	 * 按权重大小排序获取所有一级域名，权重相同时按时间先后排序
	 * @return
	 */
	List<DomainOne> getDomainOne();
	/**
	 * 获取所有一级域名数
	 * @return
	 */
	long getDomainOneCount();
	//一级域名end
	
	/**
	 * 二级域名start
	 */
	/**
	 * 根据id删除二级域名
	 * @param uuid
	 * @return 删除成功返回true，失败返回false
	 */
	boolean deleteDomainTwoById(String uuid);
	/**
	 * 根据条件查询二级域名，null则查询所有
	 * @param condition
	 * @return
	 */
	List<DomainTwo> getDomainTwo(DomainTwoQueryCondition condition);
	/**
	 * 按权重大小排序获取权重不为0的所有二级域名
	 * @return
	 */
	List<DomainTwo> getDomainTwo();
	/**
	 * 查询所有二级域名的个数
	 * @return
	 */
	long getDomainTwoCount();
	/**
	 * 二级域名end
	 */
	/**
	 * 域名综合start
	 */
		
	/**
	 * 根据一级域名查询对应的二级域名
	 * @return 返回的二级域名顺序与一级域名一一对应 List< one --> List<two>>
	 */
	List<List<DomainTwo>> getDomainTwoByOne(List<DomainOne> list);
	/**
	 * 根据一级域名查询对应的带扩展属性的二级域名
	 * @return 返回的二级域名顺序与一级域名一一对应 List< one --> List<two>>
	 */
	List<List<DomainTwoProperty>> getDomainTwoPByOne(List<DomainOne> list);
	
	/**
	 * 根据一级域名获取带扩展属性的一级域名
	 * @param list
	 * @return
	 */
	List<DomainOneProperty> getDomainOneExtraProperty(List<DomainOne> list);
	/**
	 * 根据二级域名获取带扩展属性的二级域名
	 * @param list
	 * @return
	 */
	List<DomainTwoProperty> getDomainTwoExtraProperty(List<DomainTwo> list);
	/**
	 * 域名综合end
	 */
	
	//对应controller的业务逻辑处理
/*	*//**
	 * 将给定的未知网址集合做清洗分级后插入数据库，对已经存在的域名不做处理
	 * @param urlList
	 * @return 添加失败返回false
	 *//*
	boolean addUnknowUrl(List<String> urlList);
	*//**
	 * 将给定的未知网址做清洗域名分级后插入数据库
	 * @param url
	 * @return 若url已经存在则返回false
	 *//*
	boolean addUnknowUrl(String url);*/
	
	/**
	 * 处理来自于泛数据的域名信息
	 * 先清洗出完整域名，在做域名分级处理
	 * 若存在则不做处理返回false
	 * @param domian 未知来源的域名信息，必须包含url属性
	 * @return 对数据库影响行数大于0返回true
	 */
	boolean addUnknowDomain(Domain domain);
	
	/**
	 * 处理来自于泛数据的域名信息
	 * 从上往下的处理流程
	 * 先清洗出完整域名，在做域名分级处理
	 * @param domianList 未知来源的域名信息，必须包含url属性，其他属性可以不设置
	 * @return 对数据库影响行数大于0返回true
	 */
	boolean addUnknowDomain(List<Domain> domainList);
	
	/**
	 * 处理来源于人工修改后的excel文件的域名信息
	 * 从下往上的处理流程
	 * 先判断域名等级，在处理其父级域名信息
	 * @param domain 人工处理后的域名信息，必须包含url属性，同时其他属性基本健全
	 * @return 对数据库影响行数大于0返回true
	 */
	boolean addDomain(Domain domain);
	/**
	 * 从泛数据文件中读取url，将其中的未知url添加到数据库，只添加url，其他基本属性均设为null
	 * @param file
	 * @return
	 */
	boolean addUnknowUrlFromFile(MultipartFile file);
	
	/**
	 * 从url表中读取url基本信息和扩展信息，并做分级处理后添加到数据库中，已有的url信息直接覆盖
	 * @param file
	 * @return 若文件格式不正确读取错误或添加失败返回false
	 */
	boolean addUrlFromFile(MultipartFile file);
	
	boolean addDomainProperty(String uuid,Map<String,String> map);
	
	boolean updateDomainTwoProperty(DomainTwoProperty dtp);
	
	boolean updateDomainOneProperty(DomainOneProperty dop);
	
	boolean insertDomainTwoProperty(DomainTwoProperty dtp);
	
	boolean insertDomainOneProperty(DomainOneProperty dop);
	/**
	 * 按权重顺序获取所有域名信息,不包含uuid和时间信息
	 * 
	 * @return
	 */
	List<Domain> getDomain();
	
	DomainOne getDomainOneById(String uuid);
	
	DomainOne getDomainOneByUrl(String url);
	
	DomainTwo getDomainTwoById(String uuid);
	
}
