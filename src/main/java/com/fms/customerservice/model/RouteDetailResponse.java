package com.fms.customerservice.model;

import java.util.List;

public class RouteDetailResponse {
	private Integer routeId;
	private String name;
	private Integer customerId;
	private Integer deviation;
	private Integer direction;
	private Integer routeType;
	private List<PointDTO> listPoints;

	public Integer getRouteId() {
		return routeId;
	}

	public void setRouteId(Integer routeId) {
		this.routeId = routeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getDeviation() {
		return deviation;
	}

	public void setDeviation(Integer deviation) {
		this.deviation = deviation;
	}

	public Integer getDirection() {
		return direction;
	}

	public void setDirection(Integer direction) {
		this.direction = direction;
	}

	public List<PointDTO> getListPoints() {
		return listPoints;
	}

	public void setListPoints(List<PointDTO> listPoints) {
		this.listPoints = listPoints;
	}

	public Integer getRouteType() {
		return routeType;
	}

	public void setRouteType(Integer routeType) {
		this.routeType = routeType;
	}

}
