package application;

import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
//import java.util.logging.Level;

import javafx.event.ActionEvent;
//import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
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
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		frequencySelector.getItems().removeAll(frequencySelector.getItems());
		frequencySelector.getItems().addAll(2.4, 5.0);
		frequencySelector.getSelectionModel().select(2.4);
		
		materialSelector.getItems().removeAll(materialSelector.getItems());
		materialSelector.getItems().addAll("Concrete", "Wood");
		materialSelector.getSelectionModel().select("Concrete");
		
		
		
//		FileChooser imageChooser = new FileChooser();
//		imageUploadButton.setOnAction(
//				new EventHandler<ActionEvent>() {
//	                @Override
//	                public void handle(ActionEvent e) {
//	                    File file = imageChooser.showOpenDialog(homeRoot.getScene().getWindow());
//	                    if (file != null) {
//	                        openFile(file);
//	                    }
//	                }
//	            });
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
				System.out.println(frequency + " " + material + imgPath);
			
			
		} catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

}
