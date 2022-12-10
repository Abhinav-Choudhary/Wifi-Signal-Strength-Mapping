package application;

import javafx.util.*;
import javafx.scene.image.*;
import java.util.*;
import javafx.scene.paint.Color;


public class WifiEnergyComputation {
	
	Pair<Integer, Integer> routerPosition = new Pair<>(Properties.getRouterPosY(), Properties.getRouterPosX());
	double[][] imageMatrix;

	public WifiEnergyComputation(Image image) {
		imageMatrix = buildImageMatrix(image);
	}

	public WritableImage solveForEnergyMatrix() {
		EnergyComputationAlgoNative solver = new EnergyComputationAlgoNative();
		int materialType = Properties.getMaterial() == "Concrete" ? 1 : 2;
		double frequency = Properties.getFrequency();
		double[][] e = solver.computeWifiEnergy(imageMatrix, routerPosition.getKey(), routerPosition.getValue(), materialType, frequency);
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
				if (imageMatrix[i][j] < 200)
					energyMatrix[i][j] = 0;
			}
		}
		
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
		colorBands.set(0, Color.BLACK);
		return colorBands;
	}
}
