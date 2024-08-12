package com.jay9971.VTBuilder.DataSchemas;

public class PostGameUrlData {
	private String postGamePath;
	
	public PostGameUrlData(String path) {
		postGamePath = path;
	}

	public String getPostGamePath() {
		return postGamePath;
	}

	public void setPostGamePath(String postGamePath) {
		this.postGamePath = postGamePath;
	}

	
	@Override
	public String toString() {
		return "PostGameUrlData [postGamePath=" + postGamePath + "]";
	}
	
	
}
