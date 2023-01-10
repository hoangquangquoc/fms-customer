package com.fms.customerservice.model;

import java.util.List;

import org.springframework.format.annotation.NumberFormat;

public class JourneyDataResponse {
	@NumberFormat(pattern = "####.####")
	Double totalDistance;
	Integer stopCount;
	String stopTime;
	List<RawJourneyDataDTO> rawJourneyData;
	List<Trip> tripHistory;
	
	public JourneyDataResponse(Double totalDistance, Integer stopCount, String stopTime,
			List<RawJourneyDataDTO> rawJourneyData, List<Trip> tripHistory) {
		super();
		this.totalDistance = totalDistance;
		this.stopCount = stopCount;
		this.stopTime = stopTime;
		this.rawJourneyData = rawJourneyData;
		this.tripHistory = tripHistory;
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

	public String getStopTime() {
		return stopTime;
	}

	public void setStopTime(String stopTime) {
		this.stopTime = stopTime;
	}

	public List<RawJourneyDataDTO> getRawJourneyData() {
		return rawJourneyData;
	}

	public void setRawJourneyData(List<RawJourneyDataDTO> rawJourneyData) {
		this.rawJourneyData = rawJourneyData;
	}

	public JourneyDataResponse() {
		super();
	}

	public List<Trip> getTripHistory() {
		return tripHistory;
	}

	public void setTripHistory(List<Trip> tripHistory) {
		this.tripHistory = tripHistory;
	}	
}