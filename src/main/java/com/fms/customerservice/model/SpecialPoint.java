package com.fms.customerservice.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SpecialPoint {
	LocalDateTime currentTime;
	Integer type;
	String description;
	String statusTime;
	Double speed;
	String address;
	
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public void setCurrentTime(LocalDateTime currentTime) {
		this.currentTime = currentTime;
	}
	public LocalDateTime getCurrentTime() {
		return currentTime;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStatusTime() {
		return statusTime;
	}
	public void setStatusTime(String statusTime) {
		this.statusTime = statusTime;
	}
	public Double getSpeed() {
		return speed;
	}
	public void setSpeed(Double speed) {
		this.speed = speed;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public SpecialPoint(LocalDateTime currentTime, Integer type,String description, String statusTime, Double speed,
			String address) {
		super();
		this.type = type;
		this.currentTime = currentTime;
		this.description = description;
		this.statusTime = statusTime;
		this.speed = speed;
		this.address = address;
	}
	public SpecialPoint() {
		super();
	}
	
}
