package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
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
				Image routerImage = new Image(routerImagePath);
		        double centerX, centerY;
		        centerX = posX - 15;
		        centerY = posY - 15;
		        Rectangle rect = new Rectangle(centerX, centerY, 30, 30);
		        rect.setFill(new ImagePattern(routerImage));
		        floorPlanImageContainer.getChildren().add(rect);
		        
			} else {
				floorPlanImageContainer.getChildren().remove(1);
				Image routerImage = new Image(routerImagePath);
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
	}
}
