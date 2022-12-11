package application;

import javafx.scene.image.WritableImage;

public class Properties {
	private static String imagePath, material;
	private static double frequency;
	private static int routerPosX, routerPosY;
	private static WritableImage finalImage;
	
	public Properties( ) {
		Properties.imagePath = "";
		Properties.frequency = 0.0;
		Properties.material = "";
	}
	
	public static String getImagePath() {
		return Properties.imagePath;
	}
	
	public static double getFrequency() {
		return Properties.frequency;
	}
	
	public static String getMaterial() {
		return Properties.material;
	}
	
	public static WritableImage getFinalImage() {
		return Properties.finalImage;	
	}
	
	public static void setImagePath(String imgPath) {
		Properties.imagePath = imgPath;
	}
	
	public static void setFrequency(double frequency) {
		Properties.frequency = frequency;
	}
	
	public static void setMaterial(String material) {
		Properties.material = material;
	}
	
	public static void setFinalImage(WritableImage processedImage) {
		Properties.finalImage = processedImage;
	}

	public static int getRouterPosX() {
		return routerPosX;
	}

	public static void setRouterPosX(int routerPosX) {
		Properties.routerPosX = routerPosX;
	}

	public static int getRouterPosY() {
		return routerPosY;
	}

	public static void setRouterPosY(int routerPosY) {
		Properties.routerPosY = routerPosY;
	}
}
