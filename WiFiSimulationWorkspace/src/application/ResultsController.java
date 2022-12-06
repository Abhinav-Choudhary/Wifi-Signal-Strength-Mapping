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

public class ResultsController implements Initializable {

	@FXML ImageView resultImage;
	@FXML Button saveButton;
	@FXML Button doneButton;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
//		resultImage.setImage(new Image(Properties.getImagePath()));
		resultImage.setImage(Properties.getFinalImage());
	}
	
	public void onDone(ActionEvent event) {
		try {
			System.out.printf("%nResults page --- ImagePath: %s%n Frequency: %.2f%n Material: %s", Properties.getImagePath(), Properties.getFrequency(), Properties.getMaterial());
			
			Stage newStage = (Stage) doneButton.getScene().getWindow();
			Parent goBackRoot = FXMLLoader.load(getClass().getClassLoader().getResource("GoBack.fxml"));
			Scene newScene = new Scene(goBackRoot);
			newStage.setScene(newScene);
			newStage.show();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public void onSave(ActionEvent event) {
		System.out.printf("%nSave Image.%n");
	}

}
