package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.image.*;
import javafx.scene.layout.VBox;
import java.io.*;


public class Main extends Application {
	static WifiEnergyComputation energyMatrixComputer;
	
	@Override
	public void start(Stage primaryStage) {
		// Create Image and ImageView objects
		 ImageView imageView = new ImageView();
		 // The next three lines scale the image to fit into 400x750
//		 imageView.setFitWidth(400);
//		 imageView.setFitHeight(750);
		 imageView.setPreserveRatio(true);
		 // Display image on screen using a Vertical Box layout
		 VBox root = new VBox();
		 root.getChildren().add(imageView);
		 Scene scene = new Scene(root, 600, 600);
		 primaryStage.setTitle("Heat map");
		try {
			Image image = new Image(new FileInputStream("/Users/hasundaram/Documents/WifiStrengthSimulation/sampleRoom.png"));
			energyMatrixComputer = new WifiEnergyComputation(image);
			WritableImage heatMap = energyMatrixComputer.solveForEnergyMatrix();
			imageView.setImage(heatMap);
		} catch(Exception e) {
			System.out.println(e);
		}
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
