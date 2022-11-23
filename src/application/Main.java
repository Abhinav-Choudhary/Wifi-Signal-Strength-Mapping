package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent homeRoot = FXMLLoader.load(getClass().getClassLoader().getResource("Home.fxml"));
			Parent routerPositionRoot = FXMLLoader.load(getClass().getClassLoader().getResource("RouterPosition.fxml"));
			Parent loadingRoot = FXMLLoader.load(getClass().getClassLoader().getResource("Loading.fxml"));
			Parent resultsRoot = FXMLLoader.load(getClass().getClassLoader().getResource("Results.fxml"));
			Parent goBackRoot = FXMLLoader.load(getClass().getClassLoader().getResource("GoBack.fxml"));
			Scene scene = new Scene(homeRoot);
			primaryStage.setTitle("Wifi Simulator CSYE6200");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
