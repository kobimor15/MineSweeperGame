package mines;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This class represents the MineSweeper's game user interface.
 * 
 * ---update--- class name changed from "MineFX" to "MineUI".
 * 
 * @author kobim
 *
 */
public class MinesUI extends Application {

	/**
	 * Loads the 'fxml' file and shows the window.
	 */
	@Override
	public void start(Stage primaryStage) {
		VBox vbox;
		try {
			FXMLLoader loader = new FXMLLoader(); // The class that reads the XML file and loads it to our class.
			loader.setLocation(getClass().getResource("MinesBoundary.fxml")); // FXML file location.
			vbox = loader.load(); // Load the data on the FXML file into this VBox variable.
			Scene scene = new Scene(vbox); // Create the scene by the VBox variable.
			primaryStage.setScene(scene); // Set the scene to the stage.
			primaryStage.show(); // Show the stage.

		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	/**
	 * Starts the window (the game).
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

}