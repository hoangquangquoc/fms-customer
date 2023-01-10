package com.fms.customerservice.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;

@Entity
@Table(name = "feedback", schema = "common")
public class Feedback implements Cloneable, Diffable<Feedback> {
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	@Column(name = "feedback_id")
	private Integer feedbackId;

	@Column(name = "user_id")
	private Integer userId;

	@Column(name = "rating")
	private Integer rating;

	@Column(name = "content")
	private String content;

	@Column(name = "is_active")
	private Integer isActive;

	@Column(name = "created_date")
	private LocalDateTime createdDate;
	
	@Override
	public DiffResult diff(Feedback obj) {
		// TODO Auto-generated method stub
		return null;
	}

	public Feedback() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getFeedbackId() {
		return feedbackId;
	}

	public void setFeedbackId(Integer feedbackId) {
		this.feedbackId = feedbackId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public Feedback(Integer userId, Integer rating, String content, Integer isActive,
			LocalDateTime createdDate) {
		super();
		this.userId = userId;
		this.rating = rating;
		this.content = content;
		this.isActive = isActive;
		this.createdDate = createdDate;
	}
	
}
