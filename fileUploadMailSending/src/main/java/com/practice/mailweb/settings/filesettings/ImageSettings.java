package com.practice.mailweb.settings.filesettings;

public class ImageSettings {
	
	private static int maxUploadSizeInKb = 500 * 1024;
	private static int imageHeight = 128;
	
	public static int getMaxLoadSizeInKb() {
		return maxUploadSizeInKb;
	}
	
	public static void setMaxLoadSizeInKb(int sizeInKb) {
		maxUploadSizeInKb = sizeInKb * 1024;
	}
	
	public static int getImageHeight() {
		return imageHeight;
	}
	
	public static void setImageHeight(int height) {
		imageHeight = height;
	}

}
