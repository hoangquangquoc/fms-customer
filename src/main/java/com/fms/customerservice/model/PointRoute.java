package com.fms.customerservice.model;

public class PointRoute {
	Double latitude;
	Double longitude;
	Boolean is_marker;
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Boolean getIs_marker() {
		return is_marker;
	}
	public void setIs_marker(Boolean is_marker) {
		this.is_marker = is_marker;
	}
	public PointRoute(Double latitude, Double longitude, Boolean is_marker) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.is_marker = is_marker;
	}
	public PointRoute() {
		super();
	}
	
}
