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
		System.out.println("Hite Upload File");
		try {
			FileChooser chooser = new FileChooser();
			chooser.setTitle("Choose Image");
			File file = chooser.showOpenDialog(new Stage());
			
			imagePathLabel.setText(file.getAbsolutePath());	
		} catch (Exception e) {
			System.out.println(e);
			System.exit(0);
		}
	}
	
//	private void openFile(File file) {
//		private Desktop desktop = Desktop.getDesktop();
//        try {
//            desktop.open(file);
//        } catch (IOException ex) {
//            Logger.getLogger(
//                FileChooserSample.class.getName()).log(
//                    Level.SEVERE, null, ex
//                );
//        }
//    }

//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//
//	}

}
