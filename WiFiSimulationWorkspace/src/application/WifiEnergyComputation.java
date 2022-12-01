package application;

import javafx.util.*;
import java.io.*;
import javafx.scene.image.*;
import java.nio.ByteBuffer;
import java.util.*;
import javafx.scene.paint.Color;


public class WifiEnergyComputation {
	
	Pair<Integer, Integer> routerPosition = new Pair<>(180,300);
	double[][] imageMatrix;

	public WifiEnergyComputation(Image image) {
		double[][] test = {
				{0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
				{0.0, 2.0, 3.0, 4.0, 5.0, 0.0},
				{0.0, 2.0, 3.0, 4.0, 5.0, 0.0},
				{0.0, 2.0, 3.0, 4.0, 5.0, 0.0},
				{0.0, 0.0, 0.0, 0.0, 0.0, 0.0} };
		imageMatrix = buildImageMatrix(image);
	}

	public WritableImage solveForEnergyMatrix() {
		int dimx = (int) imageMatrix.length;
		int dimy = (int) imageMatrix[0].length;
		EnergyComputationAlgoNative solver = new EnergyComputationAlgoNative();
		double[][] e = solver.computeWifiEnergy(imageMatrix, routerPosition.getKey(), routerPosition.getValue());
		System.out.println("Solved Linear Equation and obtained energy matrix");
		return plotImage(e);
	}

	private WritableImage plotImage(double[][] e) {
		int dimx = imageMatrix.length;
		int dimy = imageMatrix[0].length;
		double[][] energyMatrix = new double[dimx][dimy];
		double maxVal = Integer.MIN_VALUE, minVal = Integer.MAX_VALUE;

		for (int i = 0; i < dimx; i++) {
			for (int j = 0; j < dimy; j++) {
				energyMatrix[i][j] = e[i][j];
				maxVal = Math.max(maxVal, energyMatrix[i][j]);
				minVal = Math.min(minVal, energyMatrix[i][j]);
			}
		}
		
		System.out.println("Building heat map: assigning color level");
		for (int i = 0; i < dimx; i++) {
			for (int j = 0; j < dimy; j++) {
				energyMatrix[i][j] = Math.round(100 * (energyMatrix[i][j] - minVal) / (maxVal - minVal));
				if (imageMatrix[i][j] == 0)
					energyMatrix[i][j] = 0;
			}
		}
		System.out.println("Max Val in the matrix: "+maxVal);
		
//		for(int i=routerPosition.getKey(); i<routerPosition.getKey()+5; i++) {
//			for(int j=routerPosition.getValue(); j<routerPosition.getValue()+5; j++) {
//				energyMatrix[i][j] = 100;
//			}
//		}
		
//		for(int i=0; i<energyMatrix.length; i++) {
//			for(int j=0; j<energyMatrix[0].length; j++) {
//				System.out.print(energyMatrix[i][j]+" ");
//			}
//			System.out.println();
//		}
		
		ArrayList<Color> colorMap = buildColorMap(Color.YELLOW, 101);
		// Create WritableImage
		WritableImage heatMap = new WritableImage(
			(int)imageMatrix[0].length,
			(int)imageMatrix.length
		);
		PixelWriter pixelWriter = heatMap.getPixelWriter();
		
		System.out.println("Building heat map: assigning pixels");
		for (int i=0; i<heatMap.getHeight(); i++) {
			for (int j=0; j<heatMap.getWidth(); j++) {
				int colorLevel = (int) (energyMatrix[i][j]);
				pixelWriter.setColor(j,i,colorMap.get(colorLevel));
			}
		}
		System.out.println("Building heat map: all done");
		return heatMap;
	}

	private double[][] buildImageMatrix(Image image) {
		int dimx = (int) image.getHeight();
		int dimy = (int) image.getWidth();
		// Obtain PixelReader
        PixelReader pixelReader = image.getPixelReader();
        System.out.println(dimx+" "+dimy);
		double plan[][] = new double[dimx][dimy];
		for(int i=0; i<dimx; i++) {
			for(int j=0; j<dimy; j++) {
				plan[i][j] = (int) (pixelReader.getColor(j,i).getRed() * 255); 
			}
		}
		return plan;
	}

	private ArrayList<Color> buildColorMap(Color color, int bands) {
		ArrayList<Color> colorBands = new ArrayList<>(bands);
		
		int red = (int) color.getRed() * 255;
		int green = (int) color.getGreen() * 255;
		int blue = (int) color.getBlue() * 255;
		
		int redStep = red / bands;
		int greenStep = green / bands;
		int blueStep = blue / bands;
		
		colorBands.add(color);
		for (int index = 0; index < bands-1; index++) {
			red -= redStep;
			green -= greenStep;
			blue -= blueStep;
			color = Color.rgb(red, green, blue);
			colorBands.add(color);
		}
		Collections.reverse(colorBands);
		System.out.println(colorBands);
		return colorBands;
	}
}
