package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class GoBackController {

	@FXML Button changeRouterButton;
	@FXML Button startOverButton;
	@FXML Button exitButton;
	
	public void onChangeRouter(ActionEvent event) {
		try {
			
			Stage newStage = (Stage) changeRouterButton.getScene().getWindow();
			Parent routerPositionRoot = FXMLLoader.load(getClass().getClassLoader().getResource("RouterPosition.fxml"));
			Scene newScene = new Scene(routerPositionRoot);
			newScene.getStylesheets().add(GoBackController.class.getResource("application.css").toExternalForm());
			newStage.setScene(newScene);
			newStage.show();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public void onStartOver(ActionEvent event) {
		try {
			
			Stage newStage = (Stage) changeRouterButton.getScene().getWindow();
			Parent homeRoot = FXMLLoader.load(getClass().getClassLoader().getResource("Home.fxml"));
			Scene newScene = new Scene(homeRoot);
			newStage.setScene(newScene);
			newStage.show();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public void onExit() {
		System.exit(0);
	}

}
