package com.fms.customerservice.model;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.NumberFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fms.module.utils.Constants;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection="raw_journey_data")
public class RawJourneyDataDTO {
	@NumberFormat(pattern = "####.####")
	Double totalDistance;
	Integer stopCount;
	String stopTime;
	@Field(value="gps_speed")
	Double gpsSpeed;
	@Field(value="gps_direction")
	Double gpsDirection;
	@Field(value="gps_create_date")
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	LocalDateTime currentTime;
	@Field(value="gps_state")
	@JsonProperty(access = Access.WRITE_ONLY)
	Integer gpsState;
	@Field(value="car_motor_state")
	@JsonProperty(access = Access.WRITE_ONLY)
	Integer carMotorState;
	Integer status;
	String parkTime;
	String lostGPRSTime;
	String lostGPSTime;
	String address = Constants.DEFAULT_ADDRESS;
	@Field(value="latitude")
	Double lat;
	@Field(value="longitude")
	Double lng;
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
	public Double getGpsSpeed() {
		return gpsSpeed;
	}
	public void setGpsSpeed(Double gpsSpeed) {
		this.gpsSpeed = gpsSpeed;
	}
	public Double getGpsDirection() {
		return gpsDirection;
	}
	public void setGpsDirection(Double gpsDirection) {
		this.gpsDirection = gpsDirection;
	}
	public LocalDateTime getCurrentTime() {
		return currentTime;
	}
	public void setCurrentTime(LocalDateTime currentTime) {
		this.currentTime = currentTime;
	}
	public Integer getGpsState() {
		return gpsState;
	}
	public void setGpsState(Integer gpsState) {
		this.gpsState = gpsState;
	}
	public Integer getCarMotorState() {
		return carMotorState;
	}
	public void setCarMotorState(Integer carMotorState) {
		this.carMotorState = carMotorState;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
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
	
	public String getLostGPSTime() {
		return lostGPSTime;
	}
	public void setLostGPSTime(String lostGPSTime) {
		this.lostGPSTime = lostGPSTime;
	}
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
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
	public RawJourneyDataDTO(Double totalDistance, Integer stopCount, String stopTime, Double gpsSpeed,
			Double gpsDirection, LocalDateTime currentTime, Integer gpsState, Integer carMotorState, Integer status,
			String parkTime, String lostGPRSTime, String lostGPSTime, String address, Double lat, Double lng) {
		super();
		this.totalDistance = totalDistance;
		this.stopCount = stopCount;
		this.stopTime = stopTime;
		this.gpsSpeed = gpsSpeed;
		this.gpsDirection = gpsDirection;
		this.currentTime = currentTime;
		this.gpsState = gpsState;
		this.carMotorState = carMotorState;
		this.status = status;
		this.parkTime = parkTime;
		this.lostGPRSTime = lostGPRSTime;
		this.lostGPSTime = lostGPSTime;
		this.address = address;
		this.lat = lat;
		this.lng = lng;
	}
	public RawJourneyDataDTO() {
		super();
	}
}