package com.fms.customerservice.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class InfoPoint {
	Integer status;
	LocalDateTime currentTime;
	String address;
	String stopTime;
	String parkTime;
	String lostGPRSTime;
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public LocalDateTime getCurrentTime() {
		return currentTime;
	}
	public void setCurrentTime(LocalDateTime currentTime) {
		this.currentTime = currentTime;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getStopTime() {
		return stopTime;
	}
	public void setStopTime(String stopTime) {
		this.stopTime = stopTime;
	}
	public String getParkTime() {
		return parkTime;
	}
	public void setParkTime(String parkTime) {
		this.parkTime = parkTime;
	}
	public String getLostGPRSTime() {
		return lostGPRSTime;
	}
	public void setLostGPRSTime(String lostGPRSTime) {
		this.lostGPRSTime = lostGPRSTime;
	}
	public InfoPoint(Integer status, LocalDateTime currentTime, String address, String stopTime, String parkTime,
			String lostGPRSTime) {
		super();
		this.status = status;
		this.currentTime = currentTime;
		this.address = address;
		this.stopTime = stopTime;
		this.parkTime = parkTime;
		this.lostGPRSTime = lostGPRSTime;
	}
	public InfoPoint(RawJourneyDataDTO data) {
		super();
		this.status = data.getStatus();
		this.currentTime = data.getCurrentTime();
		this.address = data.getAddress();
		this.stopTime = data.stopTime;
		this.parkTime = data.parkTime;
		this.lostGPRSTime = data.lostGPRSTime;
	}
	public InfoPoint() {
		super();
	}
}
