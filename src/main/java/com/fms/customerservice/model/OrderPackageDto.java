package com.fms.customerservice.model;

public class OrderPackageDto {
    private Integer packageId;
    private String packageName;
    private Integer quantity;
	public Integer getPackageId() {
		return packageId;
	}
	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public OrderPackageDto(Integer packageId, String packageName, Integer quantity) {
		super();
		this.packageId = packageId;
		this.packageName = packageName;
		this.quantity = quantity;
	}
	public OrderPackageDto() {
		super();
	}

}
    
