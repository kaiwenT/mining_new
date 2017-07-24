package com.hust.mining.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hust.mining.constant.Constant.DomainExcelAttr;
import com.hust.mining.dao.DomainOneDao;
import com.hust.mining.dao.DomainPropertyDao;
import com.hust.mining.dao.DomainTwoDao;
import com.hust.mining.dao.ExtraPropertyDao;
import com.hust.mining.model.Domain;
import com.hust.mining.model.DomainOne;
import com.hust.mining.model.DomainOneExample;
import com.hust.mining.model.DomainOneProperty;
import com.hust.mining.model.DomainProperty;
import com.hust.mining.model.DomainTwo;
import com.hust.mining.model.DomainTwoProperty;
import com.hust.mining.model.ExtraProperty;
import com.hust.mining.model.params.DomainOneQueryCondition;
import com.hust.mining.model.params.DomainPropertyQueryCondition;
import com.hust.mining.model.params.DomainTwoQueryCondition;
import com.hust.mining.model.params.ExtraPropertyQueryCondition;
import com.hust.mining.service.DomainService;
import com.hust.mining.util.AttrUtil;
import com.hust.mining.util.ExcelUtil;
import com.hust.mining.util.UrlUtils;

@Service
public class DomainServiceImpl implements DomainService {

	private static final Logger logger = LoggerFactory.getLogger(DomainService.class);

	@Autowired
	private DomainOneDao domainOneDao;
	@Autowired
	private DomainTwoDao domainTwoDao;
	@Autowired
	private DomainPropertyDao domainPropertyDao;
	@Autowired
	private ExtraPropertyDao extraPropertyDao;

	@Override
	public List<DomainOne> getDomainOne(DomainOneQueryCondition condition) {
		// TODO Auto-generated method stub
		return domainOneDao.getDomainOneOrderByTime(condition);
	}

	// 综合查询部分
	@Override
	public List<List<DomainTwoProperty>> getDomainTwoPByOne(List<DomainOne> list) {
		// TODO Auto-generated method stub
		List<List<DomainTwoProperty>> twoPList = new ArrayList<>();
		for (DomainOne domainOne : list) {
			List<DomainTwoProperty> twoP = new ArrayList<>();
			twoP = getDomainTwoExtraProperty(domainTwoDao.getDomainTwoByFatherId(domainOne.getUuid()));
			twoPList.add(twoP);
		}
		return twoPList;
	}

	@Override
	public List<List<DomainTwo>> getDomainTwoByOne(List<DomainOne> list) {
		// TODO Auto-generated method stub
		List<List<DomainTwo>> twoPList = new ArrayList<>();
		for (DomainOne domainOne : list) {
			List<DomainTwo> twoP = new ArrayList<>();
			twoP = domainTwoDao.getDomainTwoByFatherId(domainOne.getUuid());
			twoPList.add(twoP);
		}
		return twoPList;
	}

	@Override
	public List<DomainOneProperty> getDomainOneExtraProperty(List<DomainOne> list) {
		// TODO Auto-generated method stub
		List<DomainOneProperty> onePList = new ArrayList<>();
		List<ExtraProperty> eProList = extraPropertyDao.getAllExtraProperty();
		for (DomainOne domainOne : list) {
			DomainOneProperty oneProperty = new DomainOneProperty();
			Map<String, String> map = new HashMap<>();
			for (ExtraProperty extraProperty : eProList) {
				DomainPropertyQueryCondition condition = new DomainPropertyQueryCondition();
				condition.setDomainUuid(domainOne.getUuid());
				condition.setPropertyId(extraProperty.getId());
				List<DomainProperty> dpList = domainPropertyDao.getDomainPropertyByCondition(condition);
				if (dpList.size() == 0) {
					map.put(extraProperty.getName(), null);
				} else {
					map.put(extraProperty.getName(), dpList.get(0).getPropertyValue());
				}
			}
			oneProperty.setDomainOne(domainOne);
			oneProperty.setExtraProperty(map);
			onePList.add(oneProperty);
		}
		return onePList;
	}

	@Override
	public List<DomainTwoProperty> getDomainTwoExtraProperty(List<DomainTwo> list) {
		// TODO Auto-generated method stub
		List<DomainTwoProperty> twoPList = new ArrayList<>();
		List<ExtraProperty> eProList = extraPropertyDao.getAllExtraProperty();
		for (DomainTwo domainTwo : list) {
			DomainTwoProperty twoProperty = new DomainTwoProperty();
			Map<String, String> map = new HashMap<>();
			for (ExtraProperty extraProperty : eProList) {
				DomainPropertyQueryCondition condition = new DomainPropertyQueryCondition();
				condition.setDomainUuid(domainTwo.getUuid());
				condition.setPropertyId(extraProperty.getId());
				List<DomainProperty> dpList = domainPropertyDao.getDomainPropertyByCondition(condition);
				if (dpList.size() == 0) {
					map.put(extraProperty.getName(), null);
				} else {
					map.put(extraProperty.getName(), dpList.get(0).getPropertyValue());
				}
			}
			twoProperty.setDomainTwo(domainTwo);
			twoProperty.setExtraProperty(map);
			twoPList.add(twoProperty);
		}
		return twoPList;
	}

	/*
	 * @Override public boolean addUnknowUrl(List<String> urlList) { // TODO
	 * Auto-generated method stub
	 * 
	 * return false; }
	 * 
	 * @Override public boolean addUnknowUrl(String url) { // TODO
	 * Auto-generated method stub // 获取完整域名 String domain =
	 * UrlUtils.getUrl(url); if (null == domain) { // 不是一个域名 return false; }
	 * Domain d = new Domain(); d.setUrl(domain); return addDomain(d); }
	 */

	// 综合插入部分

	@Override
	public boolean addUnknowUrlFromFile(MultipartFile file) {
		// TODO Auto-generated method stub
		try {
			InputStream input = file.getInputStream();
			String fileName = file.getOriginalFilename();
			List<String[]> content = ExcelUtil.readWithNull(fileName, input, -1, -1, null);
			if (content == null || content.size() <= 1) {
				logger.info("文件读取错误！");
				return false;
			}
			String[] attr = content.remove(0);
			int urlIndex = AttrUtil.findIndexOfUrl(attr);
			List<Domain> list = new ArrayList<>();
			for (String[] string : content) {
				Domain d = new Domain();
				d.setUrl(string[urlIndex]);
				list.add(d);
			}
			return addUnknowDomain(list);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.info(e.toString());
			return false;
		}
	}

	@Override
	public boolean addUrlFromFile(MultipartFile file) {
		// TODO Auto-generated method stub
		try {
			InputStream input = file.getInputStream();
			String fileName = file.getOriginalFilename();
			List<String[]> content = ExcelUtil.readWithNull(fileName, input, 0, -1, null);
			if (content == null || content.size() <= 1) {
				logger.info("文件读取错误！");
				return false;
			}
			String[] attr = content.get(0);
			if (attr.length < (DomainExcelAttr.WEIGHT_INDEX + 1)) {
				logger.info("文件属性类型列过少无法处理！");
				return false;
			}
			// 存放域名信息
			List<Domain> list = new ArrayList<>();
			// 构建扩展属性模版
			List<String> baseProperty = new ArrayList<>();
			for (int i = DomainExcelAttr.WEIGHT_INDEX + 1; i < attr.length; i++) {
				baseProperty.add(attr[i]);
			}
			// 无扩展属性列
			if (baseProperty.size() == 0) {
				for (int i = 1; i < content.size(); i++) {
					String[] info = content.get(i);
					Domain domain = Array2Domain(info);
					if(domain == null)
						continue;
					list.add(domain);
				}
			} else {
				// 有扩展属性列
				for (int i = 1; i < content.size(); i++) {
					String[] info = content.get(i);
					Domain domain = Array2Domain(info);
					if(domain == null)
						continue;
					// 获取扩展属性
					Map<String, String> extraProperty = new HashMap<String, String>();
					for (int j = 0; j < baseProperty.size(); j++) {
						String value = info[DomainExcelAttr.RANK_INDEX + 1 + j];
						extraProperty.put(baseProperty.get(j), StringUtils.isBlank(value) ? null : value);
					}
					domain.setExtraProperty(extraProperty);
					list.add(domain);
				}
			}
			int count = 0;
			for (Domain d : list) {
				if (addDomain(d))
					count++;
			}
			if (count > 0)
				return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.info(e.toString());
			return false;
		}

		return false;
	}

	@Override
	public boolean addUnknowDomain(Domain domain) {
		// 提取完整的域名
		String url = UrlUtils.getUrl(domain.getUrl());
		int flag = 0;
		if (null != url) {
			String one = UrlUtils.getDomainOne(url);
			String two = UrlUtils.getDomainTwo(url);
			String fatherUuid = "";
			List<DomainOne> oneList = domainOneDao.getDomainOneByUrl(one);
			// 一级域名存在就获取uuid，不存在就插入数据库并获取uuid
			if (oneList == null || oneList.size() == 0) {
				DomainOneProperty dop = new DomainOneProperty();
				dop.setDomain(domain, !(null == two));
				fatherUuid = dop.getUuid();
				if (insertDomainOneProperty(dop))
					flag++;
			} else {
				fatherUuid = oneList.get(0).getUuid();
			}
			// 是否有二级域名，如有则判断是否存在
			if (null != two) {
				List<DomainTwo> twoList = domainTwoDao.getDomainTwoByUrl(two);
				// 不存在则插入，存在不予处理
				if (twoList == null || twoList.size() == 0) {
					DomainTwoProperty dtp = new DomainTwoProperty();
					dtp.setDomain(domain, fatherUuid);
					dtp.setUrl(two);
					if (insertDomainTwoProperty(dtp)) {
						if (!oneList.get(0).getIsFather()) {
							DomainOne dm = new DomainOne();
							dm.setIsFather(true);
							dm.setUuid(fatherUuid);
							domainOneDao.updateDomainOneInfo(dm);
						}
						flag++;
					}
				}
			}
		}
		if (flag > 0)
			return true;
		return false;
	}

	@Override
	public boolean addUnknowDomain(List<Domain> domainList) {
		int count = 0;
		for (Domain domain : domainList) {
			if (addUnknowDomain(domain))
				count++;
		}
		if (count > 0)
			return true;
		return false;
	}

	@Override
	public boolean addDomain(Domain domain) {
		// TODO Auto-generated method stub
		int flag = 0;
		String url = UrlUtils.getUrl(domain.getUrl());
		if (null != url) {
			String one = UrlUtils.getDomainOne(url);
			if(null == one){
				System.out.println("-------------one==null-----------"+url);
				one = url;
			}
			String two = UrlUtils.getDomainTwo(url);
			if (null != two) {
				String fatherUuid = "";
				List<DomainOne> oneList = domainOneDao.getDomainOneByUrl(one);
				// 一级域名存在就获取uuid，不存在就插入数据库并获取uuid
				if (oneList == null || oneList.size() == 0) {
					DomainOneProperty dop = new DomainOneProperty();
					dop.setDomain(domain, !(null == two));
					dop.setUrl(one);
					fatherUuid = dop.getUuid();
					if (insertDomainOneProperty(dop))
						flag++;
				} else {
					fatherUuid = oneList.get(0).getUuid();
				}
				List<DomainTwo> twoList = domainTwoDao.getDomainTwoByUrl(two);
				DomainTwoProperty dtp = new DomainTwoProperty();				
				// 不存在则插入，存在则更新
				if (twoList == null || twoList.size() == 0) {
					dtp.setDomain(domain, fatherUuid);
					dtp.setUrl(two);
					if (insertDomainTwoProperty(dtp)) {
						if (!domainOneDao.getDomainOneByUrl(one).get(0).getIsFather()) {
							DomainOne dm = new DomainOne();
							dm.setIsFather(true);
							dm.setUuid(fatherUuid);
							domainOneDao.updateDomainOneInfo(dm);
						}
						flag++;
					} else {
						return false;
					}
				} else {
					dtp.setDomain(domain, fatherUuid);
					dtp.setUrl(two);
					dtp.setUuid(twoList.get(0).getUuid());
					if (updateDomainTwoProperty(dtp))
						flag++;
				}
			} else {
				// 不是二级域名，就一定是一级域名
				List<DomainOne> oneList = domainOneDao.getDomainOneByUrl(one);
				// 一级域名存在就获取uuid，不存在就插入数据库并获取uuid
				if (oneList == null || oneList.size() == 0) {
					DomainOneProperty dop = new DomainOneProperty();
					dop.setDomain(domain, false);
					dop.setUrl(one);
					if (insertDomainOneProperty(dop))
						flag++;
				} else {
					DomainOneProperty dop = new DomainOneProperty();
					dop.setDomain(domain, null);
					dop.setUrl(one);
					dop.setUuid(oneList.get(0).getUuid());
					if (updateDomainOneProperty(dop))
						flag++;
				}
			}
		}
		System.out.println("-------------addflag:" + flag);
		if (flag > 0)
			return true;
		return false;

	}

	/**
	 * 插入一级域名包括拓展属性
	 * 
	 * @param dop
	 *            若uuid属性为空，则返回false
	 * @return
	 */
	public boolean insertDomainOneProperty(DomainOneProperty dop) {
		// 扩展属性键值对（属性名，属性值）
		Map<String, String> ep = dop.getExtraProperty();
		// 存放域名属性关系，用于插入或更新数据库
		List<DomainProperty> dpList = new ArrayList<>();
		// 存放域名，用于插入域名记录
		DomainOne one = dop.getDomainOne();
		if (null == one) {
			return false;
		}
		// 域名插入成功继续处理属性，失败则返回false
		if (domainOneDao.insertDomain(one)) {
			if (null == ep) {
				return true;
			}
			String uuid = one.getUuid();
			return addDomainProperty(uuid, ep);
		} else
			return false;
	}

	/**
	 * 插入二级域名包括拓展属性
	 * 
	 * @param dtp
	 * @return
	 */
	public boolean insertDomainTwoProperty(DomainTwoProperty dtp) {
		// 扩展属性键值对（属性名，属性值）
		Map<String, String> ep = dtp.getExtraProperty();
		// 存放域名属性关系，用于插入或更新数据库
		List<DomainProperty> dpList = new ArrayList<>();
		// 存放域名，用于插入域名记录
		DomainTwo two = dtp.getDomainTwo();
		if (null == two) {
			return false;
		}
		// 域名插入成功继续处理属性，失败则返回false
		if (domainTwoDao.insertDomainTwo(two)) {
			if (null == ep) {
				return true;
			}
			String uuid = two.getUuid();
			return addDomainProperty(uuid, ep);
		} else
			return false;
	}

	/**
	 * 更新一级域名包括拓展属性
	 * 
	 * @param dop
	 * @return
	 */
	public boolean updateDomainOneProperty(DomainOneProperty dop) {
		// 扩展属性键值对（属性名，属性值）
		Map<String, String> ep = dop.getExtraProperty();
		// 存放域名属性关系，用于插入或更新数据库
		List<DomainProperty> dpList = new ArrayList<>();
		// 存放域名，用于插入域名记录
		DomainOne one = dop.getDomainOne();
		if (null == one) {
			return false;
		}
		// 域名插入成功继续处理属性，失败则返回false
		if (domainOneDao.updateDomainOneInfo(one)) {
			if (null == ep) {
				return true;
			}
			String uuid = one.getUuid();
			return addDomainProperty(uuid, ep);
		} else
			return false;
	}

	/**
	 * 更新二级域名包括拓展属性
	 * 
	 * @param dtp
	 * @return
	 */
	public boolean updateDomainTwoProperty(DomainTwoProperty dtp) {
		// 扩展属性键值对（属性名，属性值）
		Map<String, String> ep = dtp.getExtraProperty();
		// 存放域名属性关系，用于插入或更新数据库
		List<DomainProperty> dpList = new ArrayList<>();
		// 存放域名，用于插入域名记录
		DomainTwo two = dtp.getDomainTwo();
		if (null == two) {
			return false;
		}
		// 域名插入成功继续处理属性，失败则返回false
		if (domainTwoDao.updateDomainTwo(two)) {
			if (null == ep) {
				return true;
			}
			String uuid = two.getUuid();
			return addDomainProperty(uuid, ep);
		} else
			return false;
	}

	/**
	 * 更新指定id域名的扩展属性，若扩展属性不存在，则插入扩展属性,若关系存在则覆盖，不存在则添加
	 * 
	 * @param uuid
	 *            域名id
	 * @param map
	 *            指定域名所具有的所有扩展属性，值为null则认为其不存在
	 * @return
	 */
	public boolean addDomainProperty(String uuid, Map<String, String> map) {
		// 存放域名属性关系，用于插入或更新数据库
		List<DomainProperty> dpList = new ArrayList<>();
		/*
		 * if(null == map){ return false; }
		 */
		Set<String> key = map.keySet();
		Iterator<String> it = key.iterator();
		// 遍历键值对，获取域名属性关系
		while (it.hasNext()) {
			String property = it.next();
			String value = map.get(property);
			if (null != value) {
				ExtraPropertyQueryCondition condition = new ExtraPropertyQueryCondition();
				condition.setName(property);
				List<ExtraProperty> list = extraPropertyDao.getExtraPropertyByCondition(condition);
				// 如果扩展属性不存在则插入扩展属性
				if (null == list || list.size() == 0) {
					ExtraProperty extraP = new ExtraProperty();
					extraP.setName(property);
					extraPropertyDao.insertExtraProperty(extraP);
					list = extraPropertyDao.getExtraPropertyByCondition(condition);
				}
				try {
					int propertyId = list.get(0).getId();
					DomainProperty dp = new DomainProperty();
					dp.setDomainId(uuid);
					dp.setPropertyId(propertyId);
					dp.setPropertyValue(value);
					dpList.add(dp);
				} catch (Exception e) {
					logger.info("扩展属性" + property + "添加失败");
					continue;
				}
			}
		}
		return domainPropertyDao.insertDomainProperty(dpList);
	}

	@Override
	public boolean deleteDomainOneById(String uuid) {
		// TODO Auto-generated method stub

		return domainOneDao.delelteDomainOneById(uuid);
	}

	@Override
	public List<DomainOne> getDomainOne() {
		// TODO Auto-generated method stub
		return domainOneDao.getAllDomainOneOrderByWeight();
	}

	@Override
	public long getDomainOneCount() {
		// TODO Auto-generated method stub
		return domainOneDao.getDomainOneCount();
	}

	@Override
	public boolean deleteDomainTwoById(String uuid) {
		// TODO Auto-generated method stub
		return domainTwoDao.deleteDomainById(uuid);
	}

	@Override
	public List<DomainTwo> getDomainTwo(DomainTwoQueryCondition condition) {
		// TODO Auto-generated method stub
		return domainTwoDao.getDomainTwoByCondition(condition);
	}

	@Override
	public List<DomainTwo> getDomainTwo() {
		// TODO Auto-generated method stub
		return domainTwoDao.getAllDomainOrderByWeight();
	}

	@Override
	public long getDomainTwoCount() {
		// TODO Auto-generated method stub
		return domainTwoDao.getDomainTwoCount();
	}

	/**
	 * 按权重顺序获取所有域名信息,不包含uuid和时间信息
	 * 
	 * @return
	 */
	public List<Domain> getDomain() {
		List<DomainOneProperty> oneList = getDomainOneExtraProperty(getDomainOne());
		List<DomainTwoProperty> twoList = getDomainTwoExtraProperty(getDomainTwo());
		List<Domain> result = new ArrayList<>();
		int i = 0;
		int j = 0;
		while (i >= oneList.size() && j >= twoList.size()) {
			if (i >= oneList.size()) {
				for (; j < twoList.size(); j++) {
					result.add(twoList.get(j).getDomain());
				}
			}
			if (j >= oneList.size()) {
				for (; i < twoList.size(); i++) {
					result.add(oneList.get(i).getDomain());
				}
			}
			if (oneList.get(i).getWeight() >= twoList.get(j).getWeight()) {
				result.add(oneList.get(i++).getDomain());
			} else {
				result.add(twoList.get(j++).getDomain());
			}
		}
		return result;
	}

	@Override
	public DomainOne getDomainOneById(String uuid) {
		// TODO Auto-generated method stub
		return domainOneDao.getDomainOneById(uuid);
	}

	@Override
	public DomainOne getDomainOneByUrl(String url) {
		// TODO Auto-generated method stub
		List<DomainOne> one = domainOneDao.getDomainOneByUrl(url);
		if (null == one || one.size() == 0) {
			return null;
		}
		return one.get(0);
	}

	@Override
	public DomainTwo getDomainTwoById(String uuid) {
		// TODO Auto-generated method stub
		return domainTwoDao.getDomainTwoById(uuid);
	}
	
	private Domain Array2Domain(String[] baseInfo){
		Domain domain = new Domain();
		String url  = baseInfo[DomainExcelAttr.URL_INDEX].trim();
		if(StringUtils.isBlank(url))
			return null;
		String name = baseInfo[DomainExcelAttr.NAME_INDEX].trim();
		if(StringUtils.isBlank(name))
			name = null;
		String column = baseInfo[DomainExcelAttr.COLUMN_INDEX].trim();
		if(StringUtils.isBlank(column))
			column = null;
		String type = baseInfo[DomainExcelAttr.TYPE_INDEX].trim();
		if(StringUtils.isBlank(type))
			type = null;
		String rank = baseInfo[DomainExcelAttr.RANK_INDEX].trim();
		if(StringUtils.isBlank(rank))
			rank = null;
		String incidence = baseInfo[DomainExcelAttr.INCIDENCE_INDEX].trim();
		if(StringUtils.isBlank(incidence))
			incidence = null;
		String weight = baseInfo[DomainExcelAttr.WEIGHT_INDEX].trim();
		if(StringUtils.isBlank(weight) || !StringUtils.isNumeric(weight))
			weight="0";
		domain.setUrl(url);
		domain.setName(name);
		domain.setColumn(column);
		domain.setType(type);
		domain.setRank(rank);
		domain.setIncidence(incidence);
		domain.setWeight(Integer.parseInt(weight));
		return domain;
	}
}
