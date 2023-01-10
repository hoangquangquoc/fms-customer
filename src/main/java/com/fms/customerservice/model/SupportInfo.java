package com.fms.customerservice.model;

public class SupportInfo {
	private String name;
	private String phone;
	private String email;
	private String position;

	public SupportInfo(String name, String phone, String email, String position) {
		super();
		this.name = name;
		this.phone = phone;
		this.email = email;
		this.position = position;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

}
