package com.jay9971.VTBuilder;


public class ImageManager {
	private String image;
	private static final String[] imageNames = {
			"1138974.jpg",
			"1138981.jpg",
			"1138982.jpg",
			"1138983.jpg",
			"1138984.jpg",
			"1138985.jpg",
			"1138986.jpg",
			"1138987.jpg",
			"1138988.jpg",
			"1139000.jpg",
			"1139001.jpg",
			"1139002.jpg",
			"1139011.jpg",
			"1139017.jpg",
			"1139021.jpg",
			"1139024.jpg",
			"1139032.jpg"
	};
	
	public ImageManager() {
		int rand = (int)(Math.random() * 17);
		
		image = imageNames[rand];
	}
	
	public String getPath() {
		return "images/" + image;
	}
	
}
