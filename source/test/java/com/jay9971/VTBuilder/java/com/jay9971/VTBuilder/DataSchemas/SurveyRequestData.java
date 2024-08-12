package com.jay9971.VTBuilder.DataSchemas;

public class SurveyRequestData {
	private String userid;
	private String selfRating;
	private String teamRating;
	
	public SurveyRequestData() {
		
	}
	
	public SurveyRequestData(String userid, String selfRating, String team) {
		this.userid = userid;
		this.selfRating = selfRating;
		this.teamRating = team;
	}


	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getSelfRating() {
		return selfRating;
	}

	public void setSelfRating(String selfRating) {
		this.selfRating = selfRating;
	}

	public String getTeamRating() {
		return teamRating;
	}

	public void setTeamRating(String teamRating) {
		this.teamRating = teamRating;
	}

	@Override
	public String toString() {
		return "SurveyRequestData [userid=" + userid + ", selfRating=" + selfRating + ", teamRating=" + teamRating
				+ "]";
	}
	
	
}
