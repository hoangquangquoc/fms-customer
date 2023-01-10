package com.fms.customerservice.model;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fms.module.model.BaseModel;

@Entity
@Table(name = "route_transport_fms")
public class RouteTransport extends BaseModel {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "route_transport_id")
	private Integer routeTransportId;

	@Column(name = "route_id")
	private Integer routeId;

	@Column(name = "transport_id")
	private Integer transportId;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_date")
	protected LocalDateTime createdDate;

	@Column(name = "from_date")
	protected Date fromDate;

	@Column(name = "to_date")
	protected Date toDate;

	public Integer getRouteTransportId() {
		return routeTransportId;
	}

	public void setRouteTransportId(Integer routeTransportId) {
		this.routeTransportId = routeTransportId;
	}

	public Integer getRouteId() {
		return routeId;
	}

	public void setRouteId(Integer routeId) {
		this.routeId = routeId;
	}

	public Integer getTransportId() {
		return transportId;
	}

	public void setTransportId(Integer transportId) {
		this.transportId = transportId;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
}
