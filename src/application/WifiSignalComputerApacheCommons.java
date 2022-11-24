package application;

import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.complex.ComplexField;
import org.apache.commons.math3.linear.Array2DRowFieldMatrix;
import org.apache.commons.math3.linear.FieldLUDecomposition;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.Field;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.paint.Color;
import javafx.util.Pair;

public class WifiSignalComputerApacheCommons {
	static final double π = 3.14;
	Pair<Integer, Integer> routerPosition = new Pair<>(180, 90);

	double[][] imageMatrix;
	// Refraction index for concrete 2.55 - 0.01im. The imaginary part conveys the
	// absorption.
	Complex η_concrete = new Complex(2.55, 0.01);
	// Refraction index for air 1.0
	Complex η_air = new Complex(1.0, 0);
	// For a 2.5 GHz signal, wavelength is ~ 12cm
	double λ = 0.12;
	double k = (2 * π) / λ;
	double δ = 0.03;

	public WifiSignalComputerApacheCommons(Image image) {
		imageMatrix = buildImageMatrix(image);
	}

	public WritableImage solveForEnergyMatrix() {
		int dimx = (int) imageMatrix.length;
		int dimy = (int) imageMatrix[0].length;

		Array2DRowFieldMatrix<Complex> plan = new Array2DRowFieldMatrix<Complex>(ComplexField.getInstance(), dimx, dimy);
		
		Complex airConstant = η_air.pow(-1).multiply(k).pow(2);
		Complex concreteConstant = η_concrete.pow(-1).multiply(k).pow(2);

		for (int i = 0; i < imageMatrix.length; i++) {
			for (int j = 0; j < imageMatrix[0].length; j++) {
				if (imageMatrix[i][j] != 0)
					plan.setEntry(i, j, airConstant);
				else
					plan.setEntry(i, j, concreteConstant);
			}
		}
		
		ArrayRealVector xs = new ArrayRealVector(5 * dimx * dimy);
		ArrayRealVector ys = new ArrayRealVector(5 * dimx * dimy);
		SparseFieldVector<Complex> vs = new SparseFieldVector<>(ComplexField.getInstance(), 5 * dimx * dimy);

		int i = 0;
		for (int x = 0; x < dimx; x++) {
			for (int y = 0; y < dimy; y++) {
				int xm = (x + dimx - 2) % dimx + 1;
				int xp = x % dimx + 1;
				int ym = (y + dimy - 2) % dimy + 1;
				int yp = y % dimy + 1;

				xs.setEntry(i, calculateLinearIndex(x, y));
				ys.setEntry(i, calculateLinearIndex(x, y));
				vs.setEntry(i, plan.getEntry(x, y).subtract(2 * Math.pow(δ, -2)));
				i += 1;

				xs.setEntry(i, calculateLinearIndex(x, y));
				ys.setEntry(i, calculateLinearIndex(xp, y));
				vs.setEntry(i, new Complex(Math.pow(δ, -2), 0.0));
				i += 1;

				xs.setEntry(i, calculateLinearIndex(x, y));
				ys.setEntry(i, calculateLinearIndex(xm, y));
				vs.setEntry(i, new Complex(Math.pow(δ, -2), 0.0));
				i += 1;

				xs.setEntry(i, calculateLinearIndex(x, y));
				ys.setEntry(i, calculateLinearIndex(x, yp));
				vs.setEntry(i, new Complex(Math.pow(δ, -2), 0.0));
				i += 1;

				xs.setEntry(i, calculateLinearIndex(x, y));
				ys.setEntry(i, calculateLinearIndex(x, ym));
				vs.setEntry(i, new Complex(Math.pow(δ, -2), 0.0));
				i += 1;
			}
			System.out.println("Processing");
		}
		System.out.println("Processing Done");
		int fsize = dimx * dimy;

		SparseFieldMatrix<Complex> s = new SparseFieldMatrix<>(ComplexField.getInstance(), fsize, fsize);
		SparseFieldMatrix<Complex> f = new SparseFieldMatrix<>(ComplexField.getInstance(), fsize, 1);


		// Create sparse matrix
		for (int x = 0; x < fsize; x++) {
			s.setEntry((int)xs.getEntry(x), (int)ys.getEntry(x), vs.getEntry(x));
			System.out.println(xs.getEntry(x)+" "+ys.getEntry(x)+" "+vs.getEntry(x));
		}

		// Create router position vector
		for (int x = routerPosition.getKey(); x < routerPosition.getKey() + 4; x++) {
			for (int y = routerPosition.getValue(); y < routerPosition.getValue() + 4; y++) {
				f.setEntry(x * dimx + y, 0, new Complex(1.0, 0.0));
			}
		}
		
		System.out.println(f);
		System.out.println("Done Major chunk");
		
		System.out.println("Solving Linear Matrix equation");
		FieldDecompositionSolver<Complex> solver = new FieldLUDecomposition<Complex>(s).getSolver();
		SparseFieldMatrix<Complex> e = (SparseFieldMatrix<Complex>) solver.solve(f);
		System.out.println("Solved Linear Matrix equation");
		return plotImage(e);
	}

	private WritableImage plotImage(SparseFieldMatrix<Complex> e) {
		int dimx = imageMatrix.length;
		int dimy = imageMatrix[0].length;
		double[][] energyMatrix = new double[dimx][dimy];
		int k = 0;
		double maxVal = 0, minVal = Integer.MAX_VALUE;
		// Reshaping energy matrix and taking square of amplitude
		for (int i = 0; i < dimx; i++) {
			for (int j = 0; j < dimy; j++) {
				System.out.print(e.getEntry(k,0)+" ");
				energyMatrix[i][j] = e.getEntry(k,0).getReal() * e.getEntry(k,0).getReal();
				maxVal = Math.max(maxVal, energyMatrix[i][j]);
				minVal = Math.min(minVal, energyMatrix[i][j]);
				k++;
			}
		}
		
		try {
			PrintWriter writer = new PrintWriter("matrix.txt");
			for (int i = 0; i < dimx; i++) {
				for (int j = 0; j < dimy; j++) {
					writer.print(energyMatrix[i][j]+" ");
				}
				writer.println();
			}
			writer.close();
		} catch(Exception error) {
			System.out.println(error);
		}
		
		System.out.println("Building heat map: assigning color level");
		for (int i = 0; i < dimx; i++) {
			for (int j = 0; j < dimy; j++) {
				energyMatrix[i][j] = Math.round(100 * (energyMatrix[i][j] - minVal) / (maxVal - minVal));
				if (imageMatrix[i][j] == 0)
					energyMatrix[i][j] = 0;
			}
		}
		ArrayList<Color> colorMap = buildColorMap(Color.RED, 101);
		// Create WritableImage
		WritableImage heatMap = new WritableImage(
			(int)imageMatrix[0].length,
			(int)imageMatrix.length
		);
		PixelWriter pixelWriter = heatMap.getPixelWriter();
		
		System.out.println("Building heat map: assigning pixels");
		for (int i=0; i<heatMap.getHeight(); i++) {
			for (int j=0; j<heatMap.getWidth(); j++) {
				int colorLevel = (int) energyMatrix[i][j];
				pixelWriter.setColor(j,i,colorMap.get(colorLevel));
			}
		}
		System.out.println("Building heat map: all done");
		return heatMap;
	}

	private long calculateLinearIndex(int x, int y) {
		int dimx = imageMatrix.length;
		return dimx * x + y;
	}

	private double[][] buildImageMatrix(Image image) {
		int dimx = (int) image.getHeight();
		int dimy = (int) image.getWidth();

		int width = (int) image.getWidth();
		int height = (int) image.getHeight();
		byte[] buffer = new byte[dimx * dimy * 4];
		PixelReader reader = image.getPixelReader();
		WritablePixelFormat<ByteBuffer> format = WritablePixelFormat.getByteBgraInstance();
		reader.getPixels(0, 0, width, height, format, buffer, 0, width * 4);
		double plan[][] = new double[dimx][dimy];
		
		for (int i = 0; i < buffer.length; i += 4) {
			plan[i / (dimy * 4)][(i % (dimy * 4)) / 4] = buffer[i];
		}
		return plan;
	}

	private ArrayList<Color> buildColorMap(Color color, int bands) {
		ArrayList<Color> colorBands = new ArrayList<>(bands);
		for (int index = 0; index < bands; index++) {
			colorBands.add(color);
			int red = (int) Math.round(Math.max(0, color.getRed() - color.getRed() / bands));
			int green = (int) Math.round(Math.max(0, color.getGreen() - color.getGreen() / bands));
			int blue = (int) Math.round(Math.max(0, color.getBlue() - color.getBlue() / bands));
			color = Color.rgb(red, green, blue);
		}
		return colorBands;
	}
}
