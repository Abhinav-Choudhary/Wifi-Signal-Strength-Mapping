package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

public class HomeController implements Initializable {

	@FXML Label imagePathLabel;
	@FXML Button imageUploadButton;
	@FXML ChoiceBox<String> frequencySelector;
	@FXML ChoiceBox<String> materialSelector;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		frequencySelector.getItems().removeAll(frequencySelector.getItems());
		frequencySelector.getItems().addAll("2.4Ghz", "5Ghz");
		frequencySelector.getSelectionModel().select("2.4Ghz");
		
		materialSelector.getItems().removeAll(materialSelector.getItems());
		materialSelector.getItems().addAll("Concrete", "Wood");
		materialSelector.getSelectionModel().select("Concrete");
	}

//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//
//	}

}
