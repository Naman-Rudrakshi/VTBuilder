package com.jay9971.VTBuilder.DataSchemas;

import java.util.Arrays;

public class AudioDataReceiveRequest {
	private String userid;
	private byte[] audio;
	
	public AudioDataReceiveRequest (String userid, byte[] audio) {
		this.userid = userid;
		this.audio = audio;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public byte[] getAudio() {
		return audio;
	}

	public void setAudio(byte[] audio) {
		this.audio = audio;
	}

	@Override
	public String toString() {
		return "AudioDataReceiveRequest [userid=" + userid + ", audio=" + Arrays.toString(audio) + "]";
	}
	
	
}
