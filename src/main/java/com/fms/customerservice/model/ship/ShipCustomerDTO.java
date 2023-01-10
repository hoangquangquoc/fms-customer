package com.fms.customerservice.model.ship;

public interface ShipCustomerDTO {
	String getName();

	String getAddress();

	String getEmail();

	String getPhone();

	Integer getCustomerId();

	String getCreatedDate();
	
	String getTaxCode();
	
	Integer getNumberOfNonActive();
	
	Integer getNumberOfActive();
	
	Integer getNumberOfMaintainance();
}
