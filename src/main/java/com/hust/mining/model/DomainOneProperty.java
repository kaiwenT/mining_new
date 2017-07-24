package com.hust.mining.model;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class DomainOneProperty {
	private String uuid;

    private String url;

    private String name;

    private String column;

    private String type;

    private String rank;

    private String incidence;

    private Integer weight;

    private Boolean isFather;

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

	public Boolean getIsFather() {
		return isFather;
	}

	public void setIsFather(Boolean isFather) {
		this.isFather = isFather;
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
	
	public void setDomainOne(DomainOne one){
		this.setColumn(one.getColumn());
		this.setIncidence(one.getIncidence());
		this.setIsFather(one.getIsFather());
		this.setName(one.getName());
		this.setRank(one.getRank());
		this.setType(one.getType());
		this.setUpdateTime(one.getUpdateTime());
		this.setUrl(one.getUrl());
		this.setUuid(one.getUuid());
		this.setWeight(one.getWeight());
	}
	
	/**
	 * 生成对应的带扩展属性的一级域名,会生成uuid和update_time
	 * @param domain 基本域名信息
	 * @param isFather 是否含有子域名，true为有，false为无
	 */
	public void setDomain(Domain domain,Boolean isFather){
		this.setUuid(UUID.randomUUID().toString());
		this.setColumn(domain.getColumn());
		this.setIncidence(domain.getIncidence());
		this.setIsFather(isFather);
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
	
	/**
	 * 获取一级域名基本信息，
	 * @return 如果uuid属性为空返回null
	 */
	public DomainOne getDomainOne(){
		DomainOne domain = new DomainOne();
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
		domain.setIsFather(isFather);
		domain.setUpdateTime(updateTime);
		return domain;
	}
    
    
}
