package application;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

public class HomeController implements Initializable {

	@FXML Label imagePathLabel;
	@FXML Button imageUploadButton;
	@FXML ChoiceBox<Double> frequencySelector;
	@FXML ChoiceBox<String> materialSelector;
	String imgPath;
	Double frequency;
	String material;
	@FXML Button submitButton;
	@FXML Button continueButton;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		frequencySelector.getItems().removeAll(frequencySelector.getItems());
		frequencySelector.getItems().addAll(2.4);
		frequencySelector.getSelectionModel().select(2.4);
		
		materialSelector.getItems().removeAll(materialSelector.getItems());
		materialSelector.getItems().addAll("Concrete", "Wood");
		materialSelector.getSelectionModel().select("Concrete");
	}
	
	public void uploadFile(ActionEvent event) {
		try {
			FileChooser chooser = new FileChooser();
			chooser.setTitle("Choose Image");
			chooser.getExtensionFilters().addAll(
					new FileChooser.ExtensionFilter("PNG", "*.png"));
			File file = chooser.showOpenDialog(new Stage());
			
			imagePathLabel.setText(file.getAbsolutePath());	
			imgPath = file.getAbsolutePath();
			
			if(imgPath != "" || imgPath != null) {
				submitButton.setDisable(false);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public void onSubmit(ActionEvent event) {
		try {
				frequency = frequencySelector.getValue();
				material = materialSelector.getValue();
				
				Properties.setImagePath(imgPath);
				Properties.setFrequency(frequency);
				Properties.setMaterial(material);
				
				Stage newStage = (Stage) submitButton.getScene().getWindow();
				Parent routerPositionRoot = FXMLLoader.load(getClass().getClassLoader().getResource("RouterPosition.fxml"));
				Scene newScene = new Scene(routerPositionRoot);
				newScene.getStylesheets().add(HomeController.class.getResource("application.css").toExternalForm());
				newStage.setScene(newScene);
				newStage.show();
			
		} catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}
