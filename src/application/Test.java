package application;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.image.*;
import java.io.*;

import javafx.scene.image.Image;

public class Test {

	public static void main(String[] args) {
		try {
			Image image = new Image(new FileInputStream("/Users/hasundaram/Documents/WifiStrengthSimulation/sampleRoom.png"));
			WifiSignalComputerApacheCommons energyMatrixComputer = new WifiSignalComputerApacheCommons(image);
		} catch(Exception e) {
			System.out.println(e);
		}
	}

}
