package com.fms.customerservice.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fms.module.model.BaseModel;

@Entity
@Table(name = "route_fms")
public class Route extends BaseModel {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "route_id")
	Integer routeId;

	@Column(name = "name")
	String name;

	@Column(name = "customer_id")
	Integer customerId;

	@Column(name = "deviation")
	Integer deviation;

	@Column(name = "direction")
	Integer direction;
	
	@Column(name = "route_type")
	Integer routeType;
	
	@Column(name = "points")
	String points;

	public Route() {

	}

	public Route(String name, Integer customerId, Integer deviation, Integer direction, Integer routeType, String points) {
		super();
		this.name = name;
		this.customerId = customerId;
		this.deviation = deviation;
		this.direction = direction;
		this.routeType = routeType;
		this.points = points;
	}

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

	public Integer getRouteType() {
		return routeType;
	}

	public void setRouteType(Integer routeType) {
		this.routeType = routeType;
	}

	public String getPoints() {
		return points;
	}

	public void setPoints(String points) {
		this.points = points;
	}

}
