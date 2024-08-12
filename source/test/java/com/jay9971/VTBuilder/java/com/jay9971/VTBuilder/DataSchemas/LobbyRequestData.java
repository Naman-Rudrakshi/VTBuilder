package com.jay9971.VTBuilder.DataSchemas;

public class LobbyRequestData {
	private String userid;
	
	public LobbyRequestData() {
		
	}
	
	public LobbyRequestData(String userid) {
		this.userid = userid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	@Override
	public String toString() {
		return "LobbyRequestData [userid=" + userid + "]";
	}
}
