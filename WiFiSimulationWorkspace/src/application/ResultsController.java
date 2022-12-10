package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

public class ResultsController implements Initializable {

	@FXML ImageView resultImage;
	@FXML Button saveButton;
	@FXML Button doneButton;
	Image exportImage;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		resultImage.setImage(Properties.getFinalImage());
	}
	
	public void onDone(ActionEvent event) {
		try {
			Stage newStage = (Stage) doneButton.getScene().getWindow();
			Parent goBackRoot = FXMLLoader.load(getClass().getClassLoader().getResource("GoBack.fxml"));
			Scene newScene = new Scene(goBackRoot);
			newScene.getStylesheets().add(ResultsController.class.getResource("application.css").toExternalForm());
			newStage.setScene(newScene);
			newStage.show();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public void onSave(ActionEvent event) {
		System.out.printf("%nSave Image.%n");
		try {
			FileChooser chooser = new FileChooser();
			chooser.setTitle("Save Image");
			chooser.getExtensionFilters().addAll(
					new FileChooser.ExtensionFilter("PNG", "*.png"));
			File file = chooser.showSaveDialog(new Stage());
			ImageIO.write(SwingFXUtils.fromFXImage(resultImage.getImage(), null), "PNG", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
