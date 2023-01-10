package com.fms.customerservice.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fms.module.model.BaseModel;

@Entity
@Table(name = "stop_point_type")
public class StopPointType extends BaseModel {
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Id
	@Column(name = "stop_point_type_id")
	Integer stopPointTypeId;

	@Column(name = "name")
	String name;

	@Column(name = "icon")
	String icon;

	public Integer getStopPointTypeId() {
		return stopPointTypeId;
	}

	public void setStopPointTypeId(Integer stopPointTypeId) {
		this.stopPointTypeId = stopPointTypeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

}
