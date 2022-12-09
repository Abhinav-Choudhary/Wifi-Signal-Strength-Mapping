package application;

import java.io.FileInputStream;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.ImagePattern;
//import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.control.ProgressIndicator;

public class RouterController implements Initializable, Callback {
	
	static WifiEnergyComputation energyMatrixComputer;

	@FXML Button continueButton;
	@FXML Pane floorPlanImageContainer;
	@FXML ImageView floorPlanImage;
	double posX, posY;
	String routerImagePath = "router.png";

	@FXML ProgressIndicator routerLoading;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
//		Setting image received from HomePage and adding to ImageViewer
		Image image = new Image(Properties.getImagePath());
		floorPlanImage.setImage(image);
		routerLoading.setVisible(false);
		
//		Setting up ImageContainer and ImageViewer Height and Width
		floorPlanImageContainer.setPrefHeight(image.getHeight());
		floorPlanImageContainer.setPrefWidth(image.getWidth());
		floorPlanImage.setFitHeight(image.getHeight());
		floorPlanImage.setFitWidth(image.getWidth());
		
//		To align the ImageViewer to center in X axis
		floorPlanImageContainer.setTranslateX(100 - image.getWidth()/2);
		
//		To calculate the mouse click event with respect to the node size
		floorPlanImage.setPickOnBounds(true);
		
//		Handling mouse click event
		floorPlanImage.setOnMouseClicked(e -> {
			posX = Math.round(e.getX());
			posY = Math.round(e.getY());
			if(floorPlanImageContainer.getChildren().size() < 2) {
//				Circle marker = new Circle(posX, posY, 20);
				Image routerImage = new Image(routerImagePath);
//				marker.setFill(new ImagePattern(routerImage));
//		        floorPlanImageContainer.getChildren().add(marker);
		        double centerX, centerY;
		        centerX = posX - 15;
		        centerY = posY - 15;
		        Rectangle rect = new Rectangle(centerX, centerY, 30, 30);
		        rect.setFill(new ImagePattern(routerImage));
		        floorPlanImageContainer.getChildren().add(rect);
		        
			} else {
				floorPlanImageContainer.getChildren().remove(1);
//				Circle marker = new Circle(posX, posY, 20);
				Image routerImage = new Image(routerImagePath);
//				marker.setFill(new ImagePattern(routerImage));
//		        floorPlanImageContainer.getChildren().add(marker);
				double centerX, centerY;
		        centerX = posX - 15;
		        centerY = posY - 15;
		        Rectangle rect = new Rectangle(centerX, centerY, 30, 30);
		        rect.setFill(new ImagePattern(routerImage));
		        floorPlanImageContainer.getChildren().add(rect);
			}
		});
		
	}
	
	public void callback(WritableImage img) {
//		pauseToGoToNextStage.play();
		try {
			Properties.setFinalImage(img);
			Stage newStage = (Stage) continueButton.getScene().getWindow();
			Parent loadingRoot = FXMLLoader.load(getClass().getClassLoader().getResource("Results.fxml"));
			Scene newScene = new Scene(loadingRoot);
            newScene.getStylesheets().add(RouterController.class.getResource("application.css").toExternalForm());
			newStage.setScene(newScene);
			
			newStage.show();	
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}
	
	public void onContinue(ActionEvent event) {
		continueButton.setDisable(true);
		routerLoading.setVisible(true);
		
		Properties.setRouterPosX((int) posX);
		Properties.setRouterPosY((int) posY);
		
		Thread calcThread = new Thread(new CalculationThread(this));
		calcThread.start();
		
		PauseTransition pauseForCalculation = new PauseTransition(
		        Duration.seconds(1)
		);
		PauseTransition pauseToGoToNextStage = new PauseTransition(
		        Duration.seconds(1)
		);
		pauseForCalculation.setOnFinished(e -> {
			try {
//				Properties.setRouterPosX((int) posX);
//				Properties.setRouterPosY((int) posY);
//				Calculation
//				Image image = new Image(new FileInputStream(Properties.getImagePath()));
//				System.out.println(Properties.getRouterPosX() + " " + Properties.getRouterPosY() );
//				energyMatrixComputer = new WifiEnergyComputation(image);
//				System.out.println(energyMatrixComputer.routerPosition.getKey() + " " + energyMatrixComputer.routerPosition.getValue());
//				WritableImage heatMap = energyMatrixComputer.solveForEnergyMatrix();
//				Properties.setFinalImage(heatMap);
				
//				Thread calcThread = new Thread(new CalculationThread(this));
//				calcThread.start();
				
//				pauseToGoToNextStage.play();
				
			} catch (Exception ex) {
				System.out.println(ex);
			}
		});
		
		pauseToGoToNextStage.setOnFinished(e -> {
			try {
				Stage newStage = (Stage) continueButton.getScene().getWindow();
				Parent loadingRoot = FXMLLoader.load(getClass().getClassLoader().getResource("Results.fxml"));
				Scene newScene = new Scene(loadingRoot);
				newStage.setScene(newScene);
				
				newStage.show();	
			} catch (Exception ex) {
				System.out.println(ex);
			}
		});
		
//		pauseForCalculation.play();
		
	}
}
