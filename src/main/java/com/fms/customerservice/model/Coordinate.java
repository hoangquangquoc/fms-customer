package com.fms.customerservice.model;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class Coordinate {
	protected Double lat;
	protected Double lng;
	protected Integer zoom;

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

	public Integer getZoom() {
		return zoom;
	}

	public void setZoom(Integer zoom) {
		this.zoom = zoom;
	}

}
