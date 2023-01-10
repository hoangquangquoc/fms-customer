package com.fms.customerservice.model;

import java.util.List;

public class VehiclesAssignRoute {
	private List<TransportRoute> listVehicles;
	private Integer routeId;
	private Integer customerId;

	public List<TransportRoute> getListVehicles() {
		return listVehicles;
	}

	public void setListVehicles(List<TransportRoute> listVehicles) {
		this.listVehicles = listVehicles;
	}

	public Integer getRouteId() {
		return routeId;
	}

	public void setRouteId(Integer routeId) {
		this.routeId = routeId;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

}
