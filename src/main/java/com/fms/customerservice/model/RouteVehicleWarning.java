package com.fms.customerservice.model;

import java.util.List;

public class RouteVehicleWarning {
	private List<TransportDTO> listVehicles;
	private Integer routeId;
	private String emailWarning;
	private String phoneWarning;
	private Integer customerId;

	public List<TransportDTO> getListVehicles() {
		return listVehicles;
	}

	public void setListVehicles(List<TransportDTO> listVehicles) {
		this.listVehicles = listVehicles;
	}

	public Integer getRouteId() {
		return routeId;
	}

	public void setRouteId(Integer routeId) {
		this.routeId = routeId;
	}

	public String getEmailWarning() {
		return emailWarning;
	}

	public void setEmailWarning(String emailWarning) {
		this.emailWarning = emailWarning;
	}

	public String getPhoneWarning() {
		return phoneWarning;
	}

	public void setPhoneWarning(String phoneWarning) {
		this.phoneWarning = phoneWarning;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

}
