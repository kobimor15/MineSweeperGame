package mines;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * This class represents the controller file for 'MineUI.java' , by the
 * 'MinesBoundary.fxml' file.
 * 
 * @author kobim
 *
 */
public class MinesController {

	@FXML
	private VBox mainVBox;
	@FXML
	private HBox logoHBox;
	@FXML
	private ImageView logoMine;
	@FXML
	private Label topicLabel;
	@FXML
	private HBox menuHBox;
	@FXML
	private Button resetButton;
	@FXML
	private TextField widthText;
	@FXML
	private TextField heightText;
	@FXML
	private TextField minesText;
	@FXML
	private StackPane stackPane;

	private GridPane gp;
	private Button[][] buttonsGrid; // Buttons pointer: for refreshing the board.
	private Mines m;

	/**
	 * Gets 'Height' from 'heightText' TextField as integer.
	 * 
	 * @return
	 */
	public int getHeight() {
		return Integer.valueOf(heightText.getText());
	}

	/**
	 * Gets 'Width' from 'widthText' TextField as integer.
	 * 
	 * @return
	 */
	public int getWidth() {
		return Integer.valueOf(widthText.getText());
	}

	/**
	 * Gets the number of mines from "minesTest" TextField as integer.
	 * 
	 * @return
	 */
	public int getNumMines() {
		return Integer.valueOf(minesText.getText());
	}

	/**
	 * Start or Reset game: create new game by the sizes in the TextFields.
	 * 
	 * @param event
	 */
	@FXML
	void resetBoard(ActionEvent event) {

		resetButton.setText("Reset");

		if (stackPane.getChildren().isEmpty() == false)
			stackPane.getChildren().remove(0); // If got children (not a new game), remove it.

		// Check validation of inputs:
		if (!checkInputValidation()) {
			return; // Case invalid input, show alert and wait for the user to fix the values
		}

		m = new Mines(getHeight(), getWidth(), getNumMines()); // Create new Mines by the input.
		buttonsGrid = new GridButton[getHeight()][getWidth()]; // Create new grid.
		stackPane.getChildren().add(createGrid(getHeight(), getWidth())); // Put the new grid into the StackPane built
																			// on SceneBuilder.

		RefreshBoard(); // Refresh.
		m.setShowAll(false); // Set showAll to false, for new game.
	}

	/**
	 * Refreshing the visual board after changes in the board object.
	 */
	private void RefreshBoard() {
		for (int i = 0; i < getHeight(); i++)
			for (int j = 0; j < getWidth(); j++)
				buttonsGrid[i][j].setText(m.getValueAsString(i, j));
	}

	/**
	 * Show alert in case of winning or losing a game.
	 * 
	 * @param content
	 * @param win
	 */
	private void AlertShow(String content, boolean win) {
		Alert a = new Alert(AlertType.INFORMATION);
		Image img;
		if (win)
			img = new Image("file:src/mines/gif1.gif"); // Choose GIF file.
		else
			img = new Image("file:src/mines/gif2.gif"); // Choose GIF file.
		ImageView alertImage = new ImageView(img);
		a.setGraphic(alertImage); // Insert GIF file in the alert.
		a.setTitle("Minesweeper");
		a.setHeaderText(null);
		a.setContentText(content);
		a.showAndWait();
	}

	/**
	 * Create the GridPane according to the height and width received.
	 * 
	 * @param height
	 * @param width
	 * @return
	 */
	private GridPane createGrid(int height, int width) {
		gp = new GridPane();
		gp.setAlignment(Pos.BASELINE_CENTER); // Place grid to the center.

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				Button b = new GridButton(i, j); // Create a 'GridButton' button.
				b.setMinSize(40, 40);
				b.setFont(new Font(20));
				b.setText(m.getValueAsString(i, j));
				b.setOnMouseClicked(new ButtonClicked());
				buttonsGrid[i][j] = b; // Update button's pointer.
				gp.add(b, j, i); // Add the button to the grid.
			}
		}
		return gp;
	}

	/**
	 * Checks input validation. Returns false for invalid input.
	 * 
	 * @return
	 */
	private boolean checkInputValidation() {

		String height = heightText.getText(), width = widthText.getText(), mines = minesText.getText();

		// Check validation:
		if (!height.matches("[0-9]+") || !width.matches("[0-9]+") || !mines.matches("[0-9]+")
				|| Integer.valueOf(mines) > Integer.valueOf(height) * Integer.valueOf(width)) {
			Alert al = new Alert(AlertType.INFORMATION);
			al.setTitle("Error");
			al.setHeaderText("OOPS... Invalid inputs entered.");
			al.setContentText("Choose valid numbers and click on 'Reset'");
			al.showAndWait();
			return false;
		}
		return true;
	}

	/**
	 * Inner class that represents a button on the grid, contains it's place on the
	 * grid (height and width indexes).
	 * 
	 * @author kobim
	 *
	 */
	private class GridButton extends Button {
		private int i, j;

		public GridButton(int i, int j) {
			this.i = i;
			this.j = j;
		}
	}

	/**
	 * Inner class that handles events of grid buttons click. // (Both right and
	 * left click).
	 * 
	 * @author kobim
	 *
	 */
	private class ButtonClicked implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent e) {
			if (m == null) // If the game was over.
				return;
			GridButton b = (GridButton) e.getSource(); // Get the button from grid.
			if (e.getButton() == MouseButton.SECONDARY) { // Case of right mouse button clicked.
				m.toggleFlag(b.i, b.j); // Toggle or remove flag.
				b.setText(m.getValueAsString(b.i, b.j)); // Update the text on the button.
			} else if (m.open(b.i, b.j) == false) { // Open position, and check mine case.
				m.setShowAll(true); // If mine: Show the whole board.
				RefreshBoard();
				AlertShow(" Oh no.. You Lose !!! :( ", false);
				m = null;
				return;
			} else { // Not mine case:
				b.setText(m.getValueAsString(b.i, b.j)); // Refresh the position's text.
				if (m.isDone() == true) { // If won the game.
					RefreshBoard();
					AlertShow(" WELL DONE - We got a WINNER !", true);
					m = null;
					return;
				}
			}
			RefreshBoard();
		}
	}

}
