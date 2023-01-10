package com.fms.customerservice.model.responsemodel;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.annotation.JsonInclude;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
	public int code = HttpServletResponse.SC_OK;
	public String message = "Success";
	public Meta meta;
	public Object data;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Meta getMeta() {
		return meta;
	}

	public void setMeta(Meta meta) {
		this.meta = meta;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Response(int code, String message, Meta meta, Object data) {
		super();
		this.code = code;
		this.message = message;
		this.meta = meta;
		this.data = data;
	}

	public Response() {
		super();
	}

}
