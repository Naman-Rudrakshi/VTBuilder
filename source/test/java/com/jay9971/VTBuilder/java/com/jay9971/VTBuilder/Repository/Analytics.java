package com.jay9971.VTBuilder.Repository;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Analytics {
	private int numOfPlayers;
	
	private String timeElapsed;
	
	private String teamScore;
	
	private int ro;
	
	private int postPlayers;
	
	private int aiStatus;
	
	@Id
	private long id;
	
	public Analytics() {
		
	}
	
	public Analytics(long id) {
		this.id = id;
		
	}
	
	public Analytics(long id, int numOfPlayers, String timeElapsed, String teamScore) {
		this.id = id;
		this.numOfPlayers = numOfPlayers;
		this.timeElapsed = timeElapsed;
		this.teamScore = teamScore;
	}

	
	public int getAiStatus() {
		return aiStatus;
	}

	public void setAiStatus(int aiStatus) {
		this.aiStatus = aiStatus;
	}

	public int getRo() {
		return ro;
	}

	public void setRo(int ro) {
		this.ro = ro;
	}

	public int getPostPlayers() {
		return postPlayers;
	}

	public void setPostPlayers(int postPlayers) {
		this.postPlayers = postPlayers;
	}

	public int getNumOfPlayers() {
		return numOfPlayers;
	}

	public void setNumOfPlayers(int numOfPlayers) {
		this.numOfPlayers = numOfPlayers;
	}

	public String getTimeElapsed() {
		return timeElapsed;
	}

	public void setTimeElapsed(String timeElapsed) {
		this.timeElapsed = timeElapsed;
	}

	public String getTeamScore() {
		return teamScore;
	}

	public void setTeamScore(String teamScore) {
		this.teamScore = teamScore;
	}


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Analytics [numOfPlayers=" + numOfPlayers + ", timeElapsed=" + timeElapsed + ", teamScore=" + teamScore
				+ ", id=" + id + "]";
	}

	
	
	
}
