package com.jay9971.VTBuilder.DataSchemas;

public class PostGameResponseData {
	private String finalOccupiedList;
	private String teamAccuracy;
	private String analytics;
	
	public PostGameResponseData() {
		
	}
	
	public PostGameResponseData(String dat, String teamAcc, String ana) {
		finalOccupiedList = dat;
		teamAccuracy = teamAcc;
		analytics = ana;
	}

	public String getFinalOccupiedList() {
		return finalOccupiedList;
	}

	public void setFinalOccupiedList(String finalOccupiedList) {
		this.finalOccupiedList = finalOccupiedList;
	}
	
	

	public String getTeamAccuracy() {
		return teamAccuracy;
	}

	public void setTeamAccuracy(String teamAccuracy) {
		this.teamAccuracy = teamAccuracy;
	}

	public String getAnalytics() {
		return analytics;
	}

	public void setAnalytics(String analytics) {
		this.analytics = analytics;
	}

	@Override
	public String toString() {
		return "PostGameResponseData [finalOccupiedList=" + finalOccupiedList + ", teamAccuracy=" + teamAccuracy
				+ ", analytics=" + analytics + "]";
	}
	
}
