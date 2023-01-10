package com.fms.customerservice.model;

public class VehicleWarningRoute {
	private String listTransportId;
	private Integer routeId;
	private Integer customerId;
	private String phone;
	private String email;

	public String getListTransportId() {
		return listTransportId;
	}

	public void setListTransportId(String listTransportId) {
		this.listTransportId = listTransportId;
	}

	public Integer getRouteId() {
		return routeId;
	}

	public void setRouteId(Integer routeId) {
		this.routeId = routeId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

}
