package application;

public class Properties {
	private static String imagePath, material;
	private static double frequency, routerPosX, routerPosY;
	
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
	
	public static void setImagePath(String imgPath) {
		Properties.imagePath = imgPath;
	}
	
	public static void setFrequency(double frequency) {
		Properties.frequency = frequency;
	}
	
	public static void setMaterial(String material) {
		Properties.material = material;
	}

	public static double getRouterPosX() {
		return routerPosX;
	}

	public static void setRouterPosX(double routerPosX) {
		Properties.routerPosX = routerPosX;
	}

	public static double getRouterPosY() {
		return routerPosY;
	}

	public static void setRouterPosY(double routerPosY) {
		Properties.routerPosY = routerPosY;
	}
}
