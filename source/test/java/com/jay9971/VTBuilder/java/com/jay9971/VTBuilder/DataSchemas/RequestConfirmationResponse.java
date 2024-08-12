package com.jay9971.VTBuilder.DataSchemas;

public class RequestConfirmationResponse {
	private String status;
	
	public RequestConfirmationResponse() {
		
	}
	
	public RequestConfirmationResponse(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
