package com.fms.customerservice.model;

import java.util.List;

public class SupportSMSList {
	private List<SupportSMS> smsList;
	private String type;

	public List<SupportSMS> getSupportSMSList() {
		return smsList;
	}

	public void setSupportSMSList(List<SupportSMS> smsList) {
		this.smsList = smsList;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
