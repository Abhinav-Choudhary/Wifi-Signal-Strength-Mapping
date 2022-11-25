package application;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class ImageHeatMap {
	
//	To produce final heat map image of the final result matrix
	public static Image convertToHeatMap(Image inputImage, int[][] pixelValues) {
		int height = (int) inputImage.getHeight();
		int width = (int) inputImage.getWidth();
		
		WritableImage outputImage = new WritableImage(width, height);
		PixelReader reader = inputImage.getPixelReader();
		PixelWriter writer = outputImage.getPixelWriter();
		
//		For each pixel in image check if it is not wall then check strength of WiFi Signal from matrix and set color to show strength
		for(int i = 0; i < height; i ++) {
			for(int j = 0; j < width; j++) {
				Color color = reader.getColor(j, i);
				int pixelColor = (int) color.getBlue();
				if(pixelColor == 1) {
					if(pixelValues[i][j] < 50) {
						writer.setColor(j, i, Color.YELLOW);
					} else if (pixelValues[i][j] >= 50 && pixelValues[i][j] < 100) {
						writer.setColor(j, i, Color.ORANGE);
					} else writer.setColor(j, i, Color.RED);
					
				} else writer.setColor(j, i, color);
			}
		}
		
		return outputImage;
	}
}
