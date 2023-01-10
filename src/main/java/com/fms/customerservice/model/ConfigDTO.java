package com.fms.customerservice.model;

public interface ConfigDTO {
	Integer getConfigId();

	Integer getCustomerId();

	Boolean getSendEmail();

	Boolean getSendSms();

	Integer getMaintenanceBefore();

	Integer getRegistryBefore();

	Integer getWarningRenewalBefore();

	String getPhone();

	String getEmail();

}
