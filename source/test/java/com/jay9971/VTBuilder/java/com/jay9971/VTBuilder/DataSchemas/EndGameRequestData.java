package com.jay9971.VTBuilder.DataSchemas;

public class EndGameRequestData {
	private String userid;
	
	private String timeElapsed;
	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getTimeElapsed() {
		return timeElapsed;
	}
	public void setTimeElapsed(String timeElapsed) {
		this.timeElapsed = timeElapsed;
	}
	@Override
	public String toString() {
		return "EndGameRequestData [userid=" + userid + ", timeElapsed=" + timeElapsed + "]";
	}
	
}
