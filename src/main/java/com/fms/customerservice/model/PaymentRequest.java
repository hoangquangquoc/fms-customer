package com.fms.customerservice.model;

public class PaymentRequest {
	private String transportIds;
	private String amCode;
	private int clientOs = 1;

	public int getClientOs() {
		return clientOs;
	}

	public void setClientOs(int clientOs) {
		this.clientOs = clientOs;
	}

	public String getTransportIds() {
		return transportIds;
	}

	public void setTransportIds(String transportIds) {
		this.transportIds = transportIds;
	}

	public String getAmCode() {
		return amCode;
	}

	public void setAmCode(String amCode) {
		this.amCode = amCode;
	}

}
