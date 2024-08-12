package com.jay9971.VTBuilder.DataSchemas;


public class LobbyResponseData {
	private String userList;
	private String isStarted;
	private String url;

	public LobbyResponseData() {
		
	}
	
	public LobbyResponseData(String userList, String isStarted, String url) {
		this.userList = userList;
		this.isStarted = isStarted;
		this.url = url;
	}
	
	public String getUserList() {
		return userList;
	}

	public void setUserList(String userList) {
		this.userList = userList;
	}

	public String getIsStarted() {
		return isStarted;
	}

	public void setIsStarted(String isStarted) {
		this.isStarted = isStarted;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "LobbyResponseData [userList=" + userList + ", isStarted=" + isStarted + ", url=" + url + "]";
	}

	
	
	
}
