package com.miu.ea.goodpeople.web.rest;

public class StatusUpdateReq {
    String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public StatusUpdateReq(String status) {
		super();
		this.status = status;
	}

	public StatusUpdateReq() {
		super();
	}
    
    
}
