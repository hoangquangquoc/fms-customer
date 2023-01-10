package com.fms.customerservice.model.responsemodel;

public class Meta {
	public int page;
	public int pageSize;
	public int totalPages;
	public long totalElements;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(int totalElements) {
		this.totalElements = totalElements;
	}

	public Meta(int page, int pageSize, int totalPages, long totalElements) {
		super();
		this.page = page;
		this.pageSize = pageSize;
		this.totalPages = totalPages;
		this.totalElements = totalElements;
	}
}
