package com.fms.customerservice.model;

import java.util.List;

public class OrderPackageRequest {
	private Integer customerId;
	private List<OrderPackageDto> orderPackages;
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	public List<OrderPackageDto> getOrderPackages() {
		return orderPackages;
	}
	public void setOrderPackages(List<OrderPackageDto> orderPackages) {
		this.orderPackages = orderPackages;
	}
	public OrderPackageRequest(Integer customerId, List<OrderPackageDto> orderPackages) {
		super();
		this.customerId = customerId;
		this.orderPackages = orderPackages;
	}
	public OrderPackageRequest() {
		super();
	}
	
}
