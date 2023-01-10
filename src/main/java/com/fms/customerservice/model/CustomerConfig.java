package com.fms.customerservice.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fms.module.utils.ValidateUtils;

@Entity
@Table(name = "customer_config")
public class CustomerConfig {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "config_id")
	private int configId;

	@Column(name = "customer_id")
	private Integer customerId;

	@Column(name = "phone")
	private String phone;

	@Column(name = "email")
	@Length(max = 50)
	private String email;

	@Column(name = "maintenance_before")
	@Range(min = 0, max = 100)
	private Integer maintenanceBefore;

	@Column(name = "registry_before")
	@Range(min = 0, max = 100)
	private Integer registryBefore;

	@Column(name = "warning_renewal_before")
	@Range(min = 0, max = 100)
	private Integer warningRenewalBefore;

	@Column(name = "send_email")
	private Boolean sendEmail;

	@Column(name = "send_sms")
	private Boolean sendSms;

	@JsonProperty(access = Access.WRITE_ONLY)
	@Field(value = "created_by")
	protected String createdBy;

	@JsonProperty(access = Access.WRITE_ONLY)
	@Field(value = "modified_by")
	protected String modifiedBy;

	@JsonProperty(access = Access.WRITE_ONLY)
	@Field(value = "created_date")
	protected LocalDateTime createdDate;

	@JsonProperty(access = Access.WRITE_ONLY)
	@Field(value = "modified_date")
	protected LocalDateTime modifiedDate;

	public CustomerConfig() {

	}

	public CustomerConfig trim(CustomerConfig config) {
		if (!ValidateUtils.isNullOrEmpty(config.getPhone())) {
			config.setPhone(config.getPhone().trim());
		}
		if (!ValidateUtils.isNullOrEmpty(config.getEmail())) {
			config.setEmail(config.getEmail().trim());
		}

		return config;
	}

	public CustomerConfig(Integer customerId, Integer maintenanceBefore, Integer registryBefore,
			Integer warningRenewalBefore) {
		super();
		this.customerId = customerId;
		this.maintenanceBefore = maintenanceBefore;
		this.registryBefore = registryBefore;
		this.warningRenewalBefore = warningRenewalBefore;
	}

	public Integer getConfigId() {
		return configId;
	}

	public void setConfigId(Integer configId) {
		this.configId = configId;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
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

	public Integer getMaintenanceBefore() {
		return maintenanceBefore;
	}

	public void setMaintenanceBefore(Integer maintenanceBefore) {
		this.maintenanceBefore = maintenanceBefore;
	}

	public Integer getRegistryBefore() {
		return registryBefore;
	}

	public void setRegistryBefore(Integer registryBefore) {
		this.registryBefore = registryBefore;
	}

	public Boolean getSendEmail() {
		return sendEmail;
	}

	public void setSendEmail(Boolean sendEmail) {
		this.sendEmail = sendEmail;
	}

	public Boolean getSendSms() {
		return sendSms;
	}

	public void setSendSms(Boolean sendSms) {
		this.sendSms = sendSms;
	}

	public void setConfigId(int configId) {
		this.configId = configId;
	}

	public Integer getWarningRenewalBefore() {
		return warningRenewalBefore;
	}

	public void setWarningRenewalBefore(Integer warningRenewalBefore) {
		this.warningRenewalBefore = warningRenewalBefore;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDateTime getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(LocalDateTime modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

}
