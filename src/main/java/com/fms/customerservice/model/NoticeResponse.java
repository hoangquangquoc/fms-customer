package com.fms.customerservice.model;

import java.util.List;

public class NoticeResponse {
	private List<NoticeDTO> listNotices;
	private Long countNew;

	public List<NoticeDTO> getListNotices() {
		return listNotices;
	}

	public void setListNotices(List<NoticeDTO> listNotices) {
		this.listNotices = listNotices;
	}

	public Long getCountNew() {
		return countNew;
	}

	public void setCountNew(Long countNew) {
		this.countNew = countNew;
	}

}
