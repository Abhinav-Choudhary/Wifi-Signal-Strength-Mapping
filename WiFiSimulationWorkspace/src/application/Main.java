package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.*;


public class Main extends Application {
	static WifiEnergyComputation energyMatrixComputer;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent homeRoot = FXMLLoader.load(getClass().getClassLoader().getResource("Home.fxml"));
			Scene scene = new Scene(homeRoot);
            scene.getStylesheets().add(Main.class.getResource("application.css").toExternalForm());
			primaryStage.setTitle("Wifi Simulator CSYE6200");
            primaryStage.getIcons().add(new Image("icon.png"));
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
