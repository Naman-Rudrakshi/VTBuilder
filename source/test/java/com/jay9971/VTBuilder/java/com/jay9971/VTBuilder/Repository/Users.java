package com.jay9971.VTBuilder.Repository;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Users {
	
	private String name;
	
	private String squares;
	
	private long lobby;
	
	private double playerAccuracy;
	
	private int numPlacements;
	
	private String selfRating;
	
	private String teamRating;
	
	private String audio;
	
	private String finalSquares;
	
	@Id
	private long id;
	
	public Users() {
		
	}
	
	public Users(long id, String name, String squares, long lobby) {
		this.id = id;
		this.name = name;
		this.squares = squares;
		this.lobby = lobby;
	}
	
	
	
	public String getFinalSquares() {
		return finalSquares;
	}

	public void setFinalSquares(String finalSquares) {
		this.finalSquares = finalSquares;
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

	public double getPlayerAccuracy() {
		return playerAccuracy;
	}

	public void setPlayerAccuracy(double playerAccuracy) {
		this.playerAccuracy = playerAccuracy;
	}

	public int getNumPlacements() {
		return numPlacements;
	}

	public void setNumPlacements(int numPlacements) {
		this.numPlacements = numPlacements;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSquares() {
		return squares;
	}

	public void setSquares(String squares) {
		this.squares = squares;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getLobby() {
		return lobby;
	}

	public void setLobby(long lobby) {
		this.lobby = lobby;
	}
	
	public String getAudio() {
		return audio;
	}

	public void setAudio(String audio) {
		this.audio = audio;
	}

	@Override
	public String toString() {
		return "Users [name=" + name + ", squares=" + squares + ", lobby=" + lobby + ", playerAccuracy="
				+ playerAccuracy + ", numPlacements=" + numPlacements + ", selfRating=" + selfRating + ", teamRating="
				+ teamRating + ", id=" + id + "]";
	}



	

	
	
	


	
	
}
