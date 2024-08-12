package com.jay9971.VTBuilder.DataSchemas;

public class NewLobbyRequest {
	private String userid;
	private String code;
	
	public NewLobbyRequest() {}
	
	public NewLobbyRequest(String us, String c) {
		this.userid = us;
		code = c;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	
}
