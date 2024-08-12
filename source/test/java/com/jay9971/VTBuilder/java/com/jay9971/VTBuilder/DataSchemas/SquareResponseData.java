package com.jay9971.VTBuilder.DataSchemas;


public class SquareResponseData {
	private String occupiedList;
	private String gameStatus;
	private String userList;
	
	public SquareResponseData() {
		
	}
	
	public SquareResponseData(String square, String game, String playerList) {
		this.occupiedList = square;
		this.gameStatus = game;
		this.userList = playerList;
	}

	public String getOccupiedList() {
		return occupiedList;
	}

	public void setOccupiedList(String occupiedList) {
		this.occupiedList = occupiedList;
	}

	public String getGameStatus() {
		return gameStatus;
	}

	public void setGameStatus(String gameStatus) {
		this.gameStatus = gameStatus;
	}

	public String getUserList() {
		return userList;
	}

	public void setUserList(String userList) {
		this.userList = userList;
	}

	@Override
	public String toString() {
		return "SquareResponseData [occupiedList=" + occupiedList + ", gameStatus=" + gameStatus + ", userList="
				+ userList + "]";
	}

	

	
	
	
}
