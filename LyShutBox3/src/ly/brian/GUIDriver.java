package ly.brian;

//imports
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Purpose: Simulates the game Shut the Box
 * 
 * @author B. Ly
 * @date December 17, 2024
 */
public class GUIDriver extends Application {
	// class variables
	int roundNumber = 1;
	int score = 0;
	int sum = 0;
	Die d1 = new Die();

	/**
	 * Runs the game Shut the Box
	 */
	@Override
	public void start(Stage stage) throws Exception {
		// Vertical and horizontal boxes for format
		VBox vbox = new VBox(10);
		HBox tileBox = new HBox(10);
		HBox actionBox = new HBox(10);
		HBox roundBox = new HBox(10);
		HBox scoreBox = new HBox(10);
		HBox resultBox = new HBox(10);

		// round
		Label roundCounter = new Label("Round:");
		Label roundValue = new Label(String.valueOf(roundNumber));
		Label space = new Label("   ");

		// score
		Label scoreCounter = new Label("Score:");
		Label scoreValue = new Label(String.valueOf(score));
		Label space2 = new Label("   ");

		// title
		Label title = new Label("Shut The Box");

		// tiles
		Button[] tileBtns = new Button[9];
		Tile[] tiles = new Tile[9];
		for (int i = 0; i < tileBtns.length; i++) {
			tileBtns[i] = new Button(String.valueOf(i + 1));
			tileBtns[i].setStyle("-fx-background-color: grey;");
			tiles[i] = new Tile(i + 1);
			tileBox.getChildren().add(tileBtns[i]);
		}

		// controls
		Button singleRoll = new Button("Roll Die");
		Button btnRoll = new Button("Roll Dice");
		Button lockButton = new Button("Lock In");
		Button endButton = new Button("End Round");

		// roll result
		Label result = new Label("Result:");
		Label lblValue = new Label();

		// formatting in boxes
		roundBox.getChildren().addAll(roundCounter, roundValue, space);
		scoreBox.getChildren().addAll(scoreCounter, scoreValue, space2);
		actionBox.getChildren().addAll(singleRoll, btnRoll, lockButton, endButton);
		resultBox.getChildren().addAll(result, lblValue);
		vbox.getChildren().addAll(roundBox, scoreBox, title, tileBox, actionBox, resultBox);

		// alignment
		tileBox.setAlignment(Pos.CENTER);
		scoreBox.setAlignment(Pos.BASELINE_RIGHT);
		roundBox.setAlignment(Pos.BASELINE_RIGHT);
		resultBox.setAlignment(Pos.CENTER);
		actionBox.setAlignment(Pos.CENTER);
		vbox.setAlignment(Pos.CENTER);

		// disables buttons for game setup
		lockButton.setDisable(true);
		endButton.setDisable(true);
		singleRoll.setDisable(true);
		for (Button b : tileBtns) {
			b.setDisable(true);
		}

		// action on single roll
		singleRoll.setOnAction(e -> {
			// sets roll value to a single roll
			lblValue.setText(String.valueOf(d1.roll()));

			// disables and enables relevant buttons
			singleRoll.setDisable(true);
			btnRoll.setDisable(true);
			lockButton.setDisable(false);
			endButton.setDisable(false);
			for (Button b : tileBtns) {
				b.setDisable(false);
			}
		});

		// action on a roll
		btnRoll.setOnAction(e -> {
			// sets roll value to a roll
			lblValue.setText(String.valueOf(d1.roll() + d1.roll()));

			// disables and enables relevant buttons
			singleRoll.setDisable(true);
			btnRoll.setDisable(true);
			lockButton.setDisable(false);
			endButton.setDisable(false);
			for (Button b : tileBtns) {
				b.setDisable(false);
			}
		});

		// action on pressing a tile
		for (Button b : tileBtns) {
			b.setOnAction(e -> {
				// changes tile colour and manages sum based on current color
				if (b.getStyle().equals("-fx-background-color: grey;")) {
					b.setStyle("-fx-background-color: green;");
					sum = sum + Integer.valueOf(b.getText());
				} else if (b.getStyle().equals("-fx-background-color: green;")) {
					b.setStyle("-fx-background-color: grey;");
					sum = sum - Integer.valueOf(b.getText());
				}
			});
		}

		// action on locking in choice
		lockButton.setOnAction(e -> {
			// puts down tiles if sum and roll are equal
			if (sum == Integer.valueOf(lblValue.getText())) {
				for (Button b : tileBtns) {
					if (b.getStyle() == "-fx-background-color: green;") {
						b.setStyle("-fx-background-color: black;");
					}
				}

				// disables and enables relevant buttons
				btnRoll.setDisable(false);
				lockButton.setDisable(true);
				for (Button b : tileBtns) {
					b.setDisable(true);
				}
				// enables single roll is 7,8,9 are all down
				if (tileBtns[6].getStyle() == "-fx-background-color: black;") {
					if (tileBtns[7].getStyle() == "-fx-background-color: black;") {
						if (tileBtns[8].getStyle() == "-fx-background-color: black;") {
							singleRoll.setDisable(false);
						}
					}

				}
				sum = 0;
				lblValue.setText("N/A");
			}
		});

		// action on ending round
		endButton.setOnAction(e -> {
			// increases round number
			roundNumber = roundNumber + 1;

			// increases score as a sum of all up tiles
			for (Button b : tileBtns) {
				if (b.getStyle() != "-fx-background-color: black;") {
					score += Integer.valueOf(b.getText());
				}
				else {
				}
			}

			// updates labels
			roundValue.setText(String.valueOf(roundNumber));
			scoreValue.setText(String.valueOf(score));

			// disables all buttons and displays final score if round is 5
			if (roundNumber == 6) {
				roundValue.setText(String.valueOf(5));
				lockButton.setDisable(true);
				endButton.setDisable(true);
				for (Button b : tileBtns) {
					b.setDisable(true);
				}
				scoreCounter.setText("Final Score:");
			}
			// resets for a new round
			else {
				lockButton.setDisable(true);
				endButton.setDisable(true);
				singleRoll.setDisable(true);
				for (Button b : tileBtns) {
					b.setDisable(true);
					b.setStyle("-fx-background-color: grey;");
				}
				btnRoll.setDisable(false);
				sum = 0;
			}
		});

		// sets the scene
		Scene scene = new Scene(vbox, 500, 300);
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * Main - used to run the game
	 * 
	 * @param args - thing to be launched
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
