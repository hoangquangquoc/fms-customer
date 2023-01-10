package com.fms.customerservice.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fms.module.model.BaseModel;

@Entity
@Table(name = "user_transport", schema = "customer")
public class UserTranport extends BaseModel{
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Id
	@Column(name = "user_transport_id")
	private Integer userTranportId;

	@Column(name = "user_id")
	private Integer userId;

	@Column(name = "transport_id")
	private Integer tranportId;

	public Integer getUserTranportId() {
		return userTranportId;
	}

	public void setUserTranportId(Integer userTranportId) {
		this.userTranportId = userTranportId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getTranportId() {
		return tranportId;
	}

	public void setTranportId(Integer tranportId) {
		this.tranportId = tranportId;
	}

	public UserTranport(Integer userId, Integer tranportId) {
		super();
		this.userId = userId;
		this.tranportId = tranportId;
	}

	public UserTranport() {
		super();
	}
	

}
