package com.fms.customerservice.model;

public class RouteRequest {
	private Integer routeId;
	private String name;
	private Integer customerId;
	private Integer deviation;
	private Integer direction;
	private String listPoints;
	private String listMarkers;
	// loai 
	private Integer routeType = 1;
	
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

	public String getListPoints() {
		return listPoints;
	}

	public void setListPoints(String listPoints) {
		this.listPoints = listPoints;
	}

	public String getListMarkers() {
		return listMarkers;
	}

	public void setListMarkers(String listMarkers) {
		this.listMarkers = listMarkers;
	}

	public Integer getRouteId() {
		return routeId;
	}

	public void setRouteId(Integer routeId) {
		this.routeId = routeId;
	}

	public Integer getRouteType() {
		return routeType;
	}

	public void setRouteType(Integer routeType) {
		this.routeType = routeType;
	}
	
}
