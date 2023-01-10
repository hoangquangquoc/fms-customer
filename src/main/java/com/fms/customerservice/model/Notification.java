package com.fms.customerservice.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "notification")
public class Notification {
	public Notification() {
		super();
		// TODO Auto-generated constructor stub
	}

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "id")
	private Integer id;

	@Column(name = "subject")
	private String subject;

	@Column(name = "body")
	private String body;

	@Column(name = "type")
	private Integer type;
	
	@Column(name = "status")
	private Integer status;

	@Column(name = "is_active")
	private Boolean isActive;

	@Column(name = "created_date")
	private Date createDate;

	@Column(name = "user_id")
	private Integer userId;
	
	public Notification(Integer id, String subject, String body, Integer type, Integer status, Boolean isActive,
			Date createDate, Integer userId) {
		super();
		this.id = id;
		this.subject = subject;
		this.body = body;
		this.type = type;
		this.status = status;
		this.isActive = isActive;
		this.createDate = createDate;
		this.userId = userId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	
}
