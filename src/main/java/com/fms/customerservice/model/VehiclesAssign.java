package com.fms.customerservice.model;

import java.util.List;

public class VehiclesAssign {
	private Integer userId;
	private List<Integer> listVehicles;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public List<Integer> getListVehicles() {
		return listVehicles;
	}

	public void setListVehicles(List<Integer> listVehicles) {
		this.listVehicles = listVehicles;
	}

}
