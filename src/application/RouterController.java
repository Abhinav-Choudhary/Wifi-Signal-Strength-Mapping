package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
//import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;

public class RouterController implements Initializable {

	@FXML Button continueButton;
	@FXML Pane floorPlanImageContainer;
	@FXML ImageView floorPlanImage;
	double posX, posY;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
//		Setting image received from HomePage and adding to ImageViewer
		Image image = new Image(Properties.getImagePath());
		floorPlanImage.setImage(image);
		
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
				Circle marker = new Circle(posX, posY, 2, Color.RED);
				floorPlanImageContainer.getChildren().add(marker);
			} else {
				floorPlanImageContainer.getChildren().remove(1);
				Circle marker = new Circle(posX, posY, 2, Color.RED);
				floorPlanImageContainer.getChildren().add(marker);
			}
		});
		
	}
	
	public void onContinue(ActionEvent event) {
		try {
//			Setting router positions
			Properties.setRouterPosX(posX);
			Properties.setRouterPosY(posY);
			System.out.printf("\nRouter Position: %.2f , %.2f", Properties.getRouterPosX(), Properties.getRouterPosY());
			
			Stage newStage = (Stage) continueButton.getScene().getWindow();
			Parent loadingRoot = FXMLLoader.load(getClass().getClassLoader().getResource("Loading.fxml"));
			Scene newScene = new Scene(loadingRoot);
			newStage.setScene(newScene);
			
			newStage.show();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
