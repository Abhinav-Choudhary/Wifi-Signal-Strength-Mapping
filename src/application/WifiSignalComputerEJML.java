package application;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.ejml.data.Complex_F64;


import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.paint.Color;
import javafx.util.Pair;


public class WifiSignalComputerEJML {
	static final double π = 3.14;
	Pair<Integer, Integer> routerPosition = new Pair<>(180, 90);

	double[][] imageMatrix;
	// Refraction index for concrete 2.55 - 0.01im. The imaginary part conveys the
	// absorption.
	Complex_F64 η_concrete = new Complex_F64(2.55, 0.01);
	// Refraction index for air 1.0
	Complex_F64 η_air = new Complex_F64(1.0, 0);
	// For a 2.5 GHz signal, wavelength is ~ 12cm
	double λ = 0.12;
	double k = (2 * π) / λ;
	double δ = 0.03;

	public WifiSignalComputerEJML(Image image) {
		imageMatrix = buildImageMatrix(image);
	}

	public WritableImage solveForEnergyMatrix() {
		int dimx = (int) imageMatrix.length;
		int dimy = (int) imageMatrix[0].length;

		ComplexMatrix.DenseReceiver plan = ComplexMatrix.FACTORY.rows(imageMatrix).copy();
		ComplexNumber airConstant = η_air.invert().multiply(k).power(2);
		ComplexNumber concreteConstant = η_concrete.invert().multiply(k).power(2);

		for (int i = 0; i < imageMatrix.length; i++) {
			for (int j = 0; j < imageMatrix[0].length; j++) {
				if (imageMatrix[i][j] != 0)
					plan.set(i, j, airConstant);
				else
					plan.set(i, j, concreteConstant);
			}
		}

		SparseArray<Double> xs = SparseArray.factory(ArrayR064.FACTORY).make(5 * dimx * dimy);
		SparseArray<Double> ys = SparseArray.factory(ArrayR064.FACTORY).make(5 * dimx * dimy);
		SparseArray<ComplexNumber> vs = SparseArray.factory(ArrayC128.FACTORY).make(5 * dimx * dimy);

		long i = 1;
		for (int x = 0; x < dimx; x++) {
			for (int y = 0; y < dimy; y++) {
				int xm = (x + dimx - 2) % dimx + 1;
				int xp = x % dimx + 1;
				int ym = (y + dimy - 2) % dimy + 1;
				int yp = y % dimy + 1;

				xs.set(i, calculateLinearIndex(x, y));
				ys.set(i, calculateLinearIndex(x, y));
				vs.set(i, plan.get(x, y).subtract(2 * Math.pow(δ, -2)));
				i += 1;

				xs.set(i, calculateLinearIndex(x, y));
				ys.set(i, calculateLinearIndex(xp, y));
				vs.set(i, Math.pow(δ, -2));
				i += 1;

				xs.set(i, calculateLinearIndex(x, y));
				ys.set(i, calculateLinearIndex(xm, y));
				vs.set(i, Math.pow(δ, -2));
				i += 1;

				xs.set(i, calculateLinearIndex(x, y));
				ys.set(i, calculateLinearIndex(x, yp));
				vs.set(i, Math.pow(δ, -2));
				i += 1;

				xs.set(i, calculateLinearIndex(x, y));
				ys.set(i, calculateLinearIndex(x, ym));
				vs.set(i, Math.pow(δ, -2));
				i += 1;
			}
			System.out.println("Processing");
		}
		long fsize = dimx * dimy;

		SparseStore<ComplexNumber> s = SparseStore.COMPLEX.make(fsize, fsize);
		SparseStore<ComplexNumber> f = SparseStore.COMPLEX.make(fsize, 1);

		System.out.println(f.countColumns()+" "+f.countRows());
		// Create sparse matrix
		for (int x = 0; x < fsize; x++) {
			s.set(xs.get(x).longValue(), ys.get(x).longValue(), vs.get(x));
			System.out.println(xs.get(x)+" "+ys.get(x)+" "+vs.get(x));
		}

		// Create router position vector
		for (int x = routerPosition.getKey(); x < routerPosition.getKey() + 4; x++) {
			for (int y = routerPosition.getValue(); y < routerPosition.getValue() + 4; y++) {
				f.set(x * dimx + y, 0, 1.0);
			}
		}
		System.out.println(f);
		System.out.println("Done Major chunk");
		
		
        ConjugateGradientSolver solver = new ConjugateGradientSolver();
		try {
			System.out.println("Solving ConjugateGradientSolver");
			solver.configurator().debug(BasicLogger.DEBUG).iterations(1);
	        BasicLogger.debug("ConjugateGradientSolver");
			MatrixStore<Double> e = solver.solve(s, f);
			System.out.println("Solved ConjugateGradientSolver and obtained: "+e);
			return plotImage(e);
		} catch (Exception e) {
			System.out.println("ConjugateGradientSolver exception: " + e);
		}
		return new WritableImage(
			(int)imageMatrix[0].length,
			(int)imageMatrix.length
		);
	}

	private WritableImage plotImage(MatrixStore<Double> e) {
		int dimx = imageMatrix.length;
		int dimy = imageMatrix[0].length;
		double[][] energyMatrix = new double[dimx][dimy];
		int k = 0;
		double maxVal = 0, minVal = Integer.MAX_VALUE;
		// Reshaping energy matrix and taking square of amplitude
		for (int i = 0; i < dimx; i++) {
			for (int j = 0; j < dimy; j++) {
				System.out.print(e.get(k)+" ");
				energyMatrix[i][j] = e.get(k) * e.get(k);
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
		int dimy = imageMatrix[0].length;
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
