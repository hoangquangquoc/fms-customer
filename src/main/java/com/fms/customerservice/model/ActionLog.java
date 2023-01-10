package com.fms.customerservice.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "action_log")
public class ActionLog {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "action_log_id")
	private Integer actionLogId;

	@Column(name = "modified_by")
	private String modifiedBy;

	@Column(name = "created_date")
	private LocalDateTime createdDate;

	@Column(name = "action_name")
	private String actionName;

	@Column(name = "action_type")
	private Integer actionType;

	@Column(name = "function_name")
	private String functionName;

	@Column(name = "function_type")
	private Integer functionType;

	@Column(name = "content")
	private String content;

	@Column(name = "ip_address")
	private String ipAddress;

	@Column(name = "old_value")
	private String oldValue;

	@Column(name = "new_value")
	private String newValue;

	@Column(name = "affected_id")
	private Integer affectedId;

	@Column(name = "affected_name")
	private String affectedName;

	@Column(name = "duration")
	private long duration;

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

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public Integer getActionType() {
		return actionType;
	}

	public void setActionType(Integer actionType) {
		this.actionType = actionType;
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public Integer getFunctionType() {
		return functionType;
	}

	public void setFunctionType(Integer functionType) {
		this.functionType = functionType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	public Integer getAffectedId() {
		return affectedId;
	}

	public void setAffectedId(Integer affectedId) {
		this.affectedId = affectedId;
	}

	public String getAffectedName() {
		return affectedName;
	}

	public void setAffectedName(String affectedName) {
		this.affectedName = affectedName;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

}
