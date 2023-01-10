package com.fms.customerservice.model;

import java.math.BigDecimal;

public class StopPointDTO {
	private String name;
	private String managerName;
	private Integer stopPointId;
	private String stopPointTypeName;
	private Integer stopPointTypeId;
	private BigDecimal lat;
	private BigDecimal lng;
	private String icon;

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Integer getStopPointTypeId() {
		return stopPointTypeId;
	}

	public void setStopPointTypeId(Integer stopPointTypeId) {
		this.stopPointTypeId = stopPointTypeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public Integer getStopPointId() {
		return stopPointId;
	}

	public void setStopPointId(Integer stopPointId) {
		this.stopPointId = stopPointId;
	}

	public String getStopPointTypeName() {
		return stopPointTypeName;
	}

	public void setStopPointTypeName(String stopPointTypeName) {
		this.stopPointTypeName = stopPointTypeName;
	}

	public BigDecimal getLat() {
		return lat;
	}

	public void setLat(BigDecimal lat) {
		this.lat = lat;
	}

	public BigDecimal getLng() {
		return lng;
	}

	public void setLng(BigDecimal lng) {
		this.lng = lng;
	}

}
