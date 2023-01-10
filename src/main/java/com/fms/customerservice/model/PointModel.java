package com.fms.customerservice.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PointModel {
	private Double lat;
	private Double lng;
	private Integer isMarker;
	private double deviation;
	private LocalDateTime time;

	public PointModel() {

	}

	public PointModel(Double lat, Double lng, double deviation) {
		super();
		this.lat = lat;
		this.lng = lng;
		this.deviation = deviation;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public Integer getIsMarker() {
		return isMarker;
	}

	public void setIsMarker(Integer isMarker) {
		this.isMarker = isMarker;
	}

	public double getDeviation() {
		return deviation;
	}

	public void setDeviation(double deviation) {
		this.deviation = deviation;
	}

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}

}
