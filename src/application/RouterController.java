package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

public class RouterController implements Initializable {

	@FXML ImageView floorPlanImage;
	@FXML Button continueButton;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		floorPlanImage.setImage(new Image(Properties.getImagePath()));
	}
	
	public void onContinue(ActionEvent event) {
		try {
			System.out.printf("%nRouter Position page --- ImagePath: %s%n Frequency: %.2f%n Material: %s", Properties.getImagePath(), Properties.getFrequency(), Properties.getMaterial());
			
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
