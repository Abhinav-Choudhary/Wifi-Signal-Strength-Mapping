package application;

import java.io.FileInputStream;
import java.net.URL;
import java.util.ResourceBundle;

//import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;

public class LoadingController implements Initializable {

	@FXML ImageView progressImage;
	@FXML ProgressBar progressBar;
	static WifiEnergyComputation energyMatrixComputer;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		progressImage.setImage(new Image(Properties.getImagePath()));
		progressBar.setProgress(0);
		try {
			Image image = new Image(new FileInputStream(Properties.getImagePath()));
			energyMatrixComputer = new WifiEnergyComputation(image);
//			progressBar.setProgress(50);
			WritableImage heatMap = energyMatrixComputer.solveForEnergyMatrix();
//			imageView.setImage(heatMap);
			Properties.setFinalImage(heatMap);
//			progressBar.setProgress(100);
			
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public void onLoadComplete() {
		try {
			System.out.printf("%nLoading page --- ImagePath: %s%n Frequency: %.2f%n Material: %s", Properties.getImagePath(), Properties.getFrequency(), Properties.getMaterial());
			
			Stage newStage = (Stage) progressBar.getScene().getWindow();
			Parent resultsRoot = FXMLLoader.load(getClass().getClassLoader().getResource("Results.fxml"));
			Scene newScene = new Scene(resultsRoot);
			newStage.setScene(newScene);
			newStage.show();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
