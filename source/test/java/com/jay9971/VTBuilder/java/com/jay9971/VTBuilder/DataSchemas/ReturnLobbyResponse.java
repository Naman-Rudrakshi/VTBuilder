package com.jay9971.VTBuilder.DataSchemas;

public class ReturnLobbyResponse {
	
	private String name;
	private String lobbyCode;
	
	public ReturnLobbyResponse() {
		
	}
	
	public ReturnLobbyResponse(String name, String code) {
		this.name = name;
		this.lobbyCode = code;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLobbyCode() {
		return lobbyCode;
	}
	public void setLobbyCode(String lobbyCode) {
		this.lobbyCode = lobbyCode;
	}

	@Override
	public String toString() {
		return "ReturnLobbyResponse [name=" + name + ", lobbyCode=" + lobbyCode + "]";
	}
	
	
	
	
}
