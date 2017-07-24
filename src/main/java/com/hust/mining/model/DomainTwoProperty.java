package com.hust.mining.model;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class DomainTwoProperty {
    private String uuid;

    private String url;

    private String name;

    private String column;

    private String type;

    private String rank;

    private String incidence;

    private Integer weight;

    private String fatherUuid;

    private Date updateTime;
    
    private Map<String,String> extraProperty;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getIncidence() {
		return incidence;
	}

	public void setIncidence(String incidence) {
		this.incidence = incidence;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public String getFatherUuid() {
		return fatherUuid;
	}

	public void setFatherUuid(String fatherUuid) {
		this.fatherUuid = fatherUuid;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Map<String, String> getExtraProperty() {
		return extraProperty;
	}

	public void setExtraProperty(Map<String, String> extraProperty) {
		this.extraProperty = extraProperty;
	}

	public void setDomainTwo(DomainTwo domainTwo) {
		// TODO Auto-generated method stub
		this.setColumn(domainTwo.getColumn());
		this.setIncidence(domainTwo.getIncidence());
		this.setFatherUuid(domainTwo.getFatherUuid());
		this.setName(domainTwo.getName());
		this.setRank(domainTwo.getRank());
		this.setType(domainTwo.getType());
		this.setUpdateTime(domainTwo.getUpdateTime());
		this.setUrl(domainTwo.getUrl());
		this.setUuid(domainTwo.getUuid());
		this.setWeight(domainTwo.getWeight());
	}
	
	/**
	 * 生成对应的带扩展属性的二级域名
	 * @param domain 基本域名信息
	 * @param fatherUuid 父级id
	 */
	public void setDomain(Domain domain,String fatherUuid){
		this.setUuid(UUID.randomUUID().toString());
		this.setColumn(domain.getColumn());
		this.setIncidence(domain.getIncidence());
		this.setFatherUuid(fatherUuid);
		this.setName(domain.getName());
		this.setRank(domain.getRank());
		this.setType(domain.getType());
		this.setUrl(domain.getUrl());	
		this.setWeight(domain.getWeight());
		this.setUpdateTime(new Date());
	}
	/**
	 * 获取与之对应的域名信息不包括uuid和更新时间
	 * @return
	 */
	public Domain getDomain(){
		Domain domain = new Domain();
		domain.setUrl(url);
		domain.setName(name);
		domain.setColumn(column);
		domain.setType(type);
		domain.setRank(rank);
		domain.setIncidence(incidence);
		domain.setWeight(weight);
		domain.setExtraProperty(extraProperty);
		return domain;
	}

	public DomainTwo getDomainTwo() {
		// TODO Auto-generated method stub
		DomainTwo domain = new DomainTwo();
		if(uuid == null){
			return null;
		}
		domain.setUuid(uuid);
		domain.setUrl(url);
		domain.setName(name);
		domain.setColumn(column);
		domain.setType(type);
		domain.setRank(rank);
		domain.setIncidence(incidence);
		domain.setWeight(weight);
		domain.setFatherUuid(fatherUuid);
		domain.setUpdateTime(updateTime);
		return domain;
	}
    
}
