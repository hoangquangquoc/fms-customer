package com.fms.customerservice.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import com.fms.module.model.BaseModel;
import com.fms.module.utils.ValidateUtils;

@Entity
@Table(name = "stop_point")
public class StopPoint extends BaseModel {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "stop_point_id")
	Integer stopPointId;

	@Column(name = "name")
	String name;

	@Column(name = "stop_point_type_id")
	Integer stopPointTypeId;

	@Column(name = "customer_id")
	Integer customerId;

	@Column(name = "stop_time")
	Integer stopTime;

	@Column(name = "radius")
	Integer radius;

	@Column(name = "latitude")
	Double lat;

	@Column(name = "longitude")
	Double lng;

	@Size(max = 255)
	@Column(name = "note")
	String note;

	public StopPoint trim(StopPoint stopPoint) {
		stopPoint.setName(ValidateUtils.trimValue(stopPoint.getName()));
		stopPoint.setNote(ValidateUtils.trimValue(stopPoint.getNote()));
		return stopPoint;
	}
	
	public Integer getStopPointId() {
		return stopPointId;
	}

	public void setStopPointId(Integer stopPointId) {
		this.stopPointId = stopPointId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getStopPointTypeId() {
		return stopPointTypeId;
	}

	public void setStopPointTypeId(Integer stopPointTypeId) {
		this.stopPointTypeId = stopPointTypeId;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getStopTime() {
		return stopTime;
	}

	public void setStopTime(Integer stopTime) {
		this.stopTime = stopTime;
	}

	public Integer getRadius() {
		return radius;
	}

	public void setRadius(Integer radius) {
		this.radius = radius;
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

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}
