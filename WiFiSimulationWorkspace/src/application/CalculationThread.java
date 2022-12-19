package application;

import java.io.FileInputStream;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

interface Callback {
	void callback(WritableImage img);
}

public class CalculationThread implements Runnable {
	
	static WifiEnergyComputation energyMatrixComputer;
	
	Callback c;
	
	public CalculationThread(Callback c) {
		this.c = c;
	}
	
	public void run() {
		try {
			Image image = new Image(new FileInputStream(Properties.getImagePath()));
			energyMatrixComputer = new WifiEnergyComputation(image);
			WritableImage heatMap = energyMatrixComputer.solveForEnergyMatrix();
			Platform.runLater(() -> {
				this.runCalcLater(heatMap);
			});
		}catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public void runCalcLater(WritableImage img) {
		this.c.callback(img);
	}
}
