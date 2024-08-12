package com.jay9971.VTBuilder.DataSchemas;

public class CreateLobbyRequestData {
	private String name;
	
	public CreateLobbyRequestData() {
		
	}
	
	public CreateLobbyRequestData(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "CreateLobbyRequestData [name=" + name + "]";
	}
	
	
}
