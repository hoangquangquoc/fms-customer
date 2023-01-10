package com.fms.customerservice.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Trip {
	String fromAddress;
	String toAddress;
	LocalDateTime startTime;
	LocalDateTime endTime;
	String totalTime;
	Double totalDistance;
	Integer stopCount;
	List<InfoPoint> stopTimes = new ArrayList<InfoPoint>();
	Integer parkCount;
	List<InfoPoint> parkTimes = new ArrayList<InfoPoint>();
	Integer lostGPRSCount;
	List<InfoPoint> lostGPRSTimes = new ArrayList<InfoPoint>();
	Double speedAverage;
	List<SpecialPoint> specialPoints = new ArrayList<SpecialPoint>();
	
	public String getFromAddress() {
		return fromAddress;
	}
	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}
	public String getToAddress() {
		return toAddress;
	}
	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}
	public LocalDateTime getStartTime() {
		return startTime;
	}
	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}
	public LocalDateTime getEndTime() {
		return endTime;
	}
	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}
	public String getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}
	public Double getTotalDistance() {
		return totalDistance;
	}
	public void setTotalDistance(Double totalDistance) {
		this.totalDistance = totalDistance;
	}
	public Integer getStopCount() {
		return stopCount;
	}
	public void setStopCount(Integer stopCount) {
		this.stopCount = stopCount;
	}
	public List<InfoPoint> getStopTimes() {
		return stopTimes;
	}
	public void setStopTimes(List<InfoPoint> stopTimes) {
		this.stopTimes = stopTimes;
	}
	public Integer getParkCount() {
		return parkCount;
	}
	public void setParkCount(Integer parkCount) {
		this.parkCount = parkCount;
	}
	public List<InfoPoint> getParkTimes() {
		return parkTimes;
	}
	public void setParkTimes(List<InfoPoint> parkTimes) {
		this.parkTimes = parkTimes;
	}
	public Integer getLostGPRSCount() {
		return lostGPRSCount;
	}
	public void setLostGPRSCount(Integer lostGPRSCount) {
		this.lostGPRSCount = lostGPRSCount;
	}
	public List<InfoPoint> getLostGPRSTimes() {
		return lostGPRSTimes;
	}
	public void setLostGPRSTimes(List<InfoPoint> lostGPRSTimes) {
		this.lostGPRSTimes = lostGPRSTimes;
	}
	public Double getSpeedAverage() {
		return speedAverage;
	}
	public void setSpeedAverage(Double speedAverage) {
		this.speedAverage = speedAverage;
	}
	
	public List<SpecialPoint> getSpecialPoints() {
		return specialPoints;
	}
	public void setSpecialPoints(List<SpecialPoint> specialPoints) {
		this.specialPoints = specialPoints;
	}
	public Trip(String fromAddress,String toAddress, LocalDateTime startTime, LocalDateTime endTime, String totalTime, Double totalDistance,
			Integer stopCount, List<InfoPoint> stopTimes, Integer parkCount, List<InfoPoint> parkTimes,
			Integer lostGPRSCount, List<InfoPoint> lostGPRSTimes, Double speedAverage, List<SpecialPoint> specialPoints) {
		super();
		this.fromAddress = fromAddress;
		this.toAddress = toAddress;
		this.startTime = startTime;
		this.endTime = endTime;
		this.totalTime = totalTime;
		this.totalDistance = totalDistance;
		this.stopCount = stopCount;
		this.stopTimes = stopTimes;
		this.parkCount = parkCount;
		this.parkTimes = parkTimes;
		this.lostGPRSCount = lostGPRSCount;
		this.lostGPRSTimes = lostGPRSTimes;
		this.speedAverage = speedAverage;
		this.specialPoints = specialPoints;
	}
	public Trip() {
		super();
	}
	
}
