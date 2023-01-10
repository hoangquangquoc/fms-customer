package com.fms.customerservice.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fms.module.model.BaseModel;

@Entity
@Table(name = "route_detail")
public class RouteDetail extends BaseModel {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "route_detail_id")
	private Integer routeDetailId;

	@Column(name = "route_id")
	private Integer routeId;

	@Column(name = "point_order")
	private Integer pointOrder;

	@Column(name = "longitude")
	private Double lng;

	@Column(name = "latitude")
	private Double lat;

	@Column(name = "is_marker")
	private Boolean isMarker = false;

	public Integer getRouteDetailId() {
		return routeDetailId;
	}

	public void setRouteDetailId(Integer routeDetailId) {
		this.routeDetailId = routeDetailId;
	}

	public Integer getRouteId() {
		return routeId;
	}

	public void setRouteId(Integer routeId) {
		this.routeId = routeId;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Boolean getIsMarker() {
		return isMarker;
	}

	public void setIsMarker(Boolean isMarker) {
		this.isMarker = isMarker;
	}

	public Integer getPointOrder() {
		return pointOrder;
	}

	public void setPointOrder(Integer pointOrder) {
		this.pointOrder = pointOrder;
	}

}
