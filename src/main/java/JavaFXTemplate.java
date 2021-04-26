import java.util.ArrayList;
import java.util.Random;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class JavaFXTemplate extends Application {
    Tiles[][] puzzle = new Tiles[4][4];
    ArrayList<int[][]> puzzleList;
    int[][] selectedPuzzle;
	EventHandler<ActionEvent> checkPosition; // to know the which tile was clicked
	PauseTransition pause = new PauseTransition(Duration.seconds(4)); // later on for when AI solves, but we need pauses in between
	PauseTransition howToPlayPause = new PauseTransition(Duration.seconds(15));
	BorderPane puzzlePane; // main scene for our puzzle
	GridPane gridpane; // this should be where the tiles go
	MenuBar menuBar;
	Menu menuGamePlay, menuOptions, howTo;
	MenuItem exit, newPuzzle, solve1, solve2, instr;
	Button showSolution, resetPuzzle, playAgain, exitPuzzle;
	Label howToPlay, moveString, moveNumber;
	int numberOfMoves = 0;
	int moveDirection = 0;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("Project 4");
		exit = new MenuItem("Exit");
		newPuzzle = new MenuItem("New Puzzle");
		solve1 = new MenuItem("Solve 1");
		solve2 = new MenuItem("Solve 2");
		instr = new MenuItem("How To Play");
		intializePuzzles();
		checkPosition = new EventHandler<ActionEvent>() {
			
			public void handle(ActionEvent event) {
				Tiles tile = (Tiles)event.getSource();
				int row = tile.getRow();
				int column = tile.getColumn();
				int num = tile.getNumber();
				int rowBlank = tile.getRowBlank();
				int columnBlank = tile.getColumnBlank();
//				System.out.println("Blank tile at (" + rowBlank + ", " + columnBlank + ")");
//				System.out.println("Selected tile at (" + row + ", " + column + ")");
				if (row == rowBlank ) { // means the clicked tile and blank tile are in the same row
					if (column > columnBlank && (column+1 == columnBlank || column-1 == columnBlank)) {
						moveDirection = -1; // move selected row down
					} else if (column < columnBlank && (column+1 == columnBlank || column-1 == columnBlank)) {
						moveDirection = 1; // move selected row up
					} else {
						moveDirection = 0;
					}
				} else if (column == columnBlank) {
					if (row > rowBlank && (row+1 == rowBlank || row-1 == rowBlank)) {
						moveDirection = -1; // move selected row down
					} else if (row < rowBlank && (row+1 == rowBlank || row-1 == rowBlank)) {
						moveDirection = 1; // move selected row up
					} else {
						moveDirection = 0;
					}
				} else {
					moveDirection = 0;
				}
				if (moveDirection != 0 ) {
					numberOfMoves++;
					moveNumber.setText(String.valueOf(numberOfMoves));
					puzzle[row][column].setEmptyTile(); // set's to empty
					puzzle[row][column].setText("");
					puzzle[rowBlank][columnBlank].updateTile(num);
				}
				if (isWinner()) {
					System.out.println("YOU WIN");
					disablePuzzle();
					puzzleList.remove(selectedPuzzle);
				}
			}
		};
		Scene scene = mainPuzzleScene();
		primaryStage.setScene(scene);
		primaryStage.show();
		instr.setOnAction(e -> {
			// allows the text to go next line and not get chopped off
			howToPlay.setWrapText(true);
			howToPlay.setText("Welcome to Puzzle 15 Game \n" 
					+ "The point of the game is to make the number go in order by swapping the empty tile, with the numbered tiles."
					+ "Click on each valid tile to swap places  with the empty tile and get it in order 0 to 15."
					);
			// set a pause for 15 seconds and then remove game play instructions
			howToPlayPause.setOnFinished(s -> {
				// set to blank screen
				howToPlay.setText("");
			});
				howToPlayPause.play();
		});
		
		exit.setOnAction(e -> {
			System.exit(0);
		});
		
		newPuzzle.setOnAction(e -> {
			gridpane.getChildren().clear();
			numberOfMoves = 0;
			moveNumber.setText(String.valueOf(numberOfMoves));
			puzzleList.remove(selectedPuzzle);
			newPuzzle(gridpane);
		});
		
		resetPuzzle.setOnAction(e -> {
			resetPuzzle();
		});
		
		Thread t = new Thread(()-> {A_IDS_A_15solver ids = new A_IDS_A_15solver();});
		t.start();

	}
	
	// for reset button
	private void resetPuzzle() {
		gridpane.getChildren().clear();
		numberOfMoves = 0;
		moveNumber.setText(String.valueOf(numberOfMoves));
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				Tiles t = new Tiles(i,j, selectedPuzzle[i][j]); // zero should be the number of that puzzle tile
				t.setMinSize(100, 100);
				t.setOnAction(checkPosition);
				puzzle[i][j] = t;
				gridpane.add(puzzle[i][j], j, i);
			}
		}
	}
	// multiple variations of the solveable puzzle
	public void intializePuzzles() {
		int[][] puzzle1 = {{2, 6, 10, 3}, {1, 4, 7, 11}, {8, 5, 9, 15}, {12, 13, 14, 0}};
		int[][] puzzle2 = {{0, 14, 13, 12}, {15, 9, 5, 8}, {11, 7, 4, 1}, {3, 10, 6, 2}};
	    int[][] puzzle3 = {{12, 1, 10, 2}, {7, 11, 4, 14}, {5, 0, 9, 15}, {8, 13, 6, 3}};
		int[][] puzzle4 = {{1, 7, 11, 6}, {8, 13, 3, 15}, {12, 9, 0, 5}, {4, 10, 2, 14}}; // to check winning part
		int[][] puzzle5 = {{6, 13, 7, 10},{8, 9, 11, 0},{15, 2, 12, 5},{14, 3, 1, 4}};
	    int[][] puzzle6 = {{13, 2, 10, 3},{1, 12, 8, 4},{5, 0, 9, 6},{15, 14, 11, 7}};
	    int[][] puzzle7 = {{9, 2, 3, 4}, {0, 14, 8, 11}, {7, 10, 6, 12}, {15, 13, 1, 5}};
		int[][] puzzle8 = {{6, 11, 8, 2}, {1, 3, 4, 12}, {14, 13, 0, 15}, {10, 5, 7, 9}};
	    int[][] puzzle9 = {{2, 14, 3, 4}, {0, 1, 7, 6}, {11, 13, 8, 12}, {5, 10, 9, 15}};
		int[][] puzzle10 = {{0, 5, 1, 4}, {9, 3, 12, 8}, {14, 13, 6, 7}, {11, 10, 2, 15}};
	    
		puzzleList = new ArrayList<int[][]>();
		puzzleList.add(puzzle1);
		puzzleList.add(puzzle2);
		puzzleList.add(puzzle3);
		puzzleList.add(puzzle4);
		puzzleList.add(puzzle5);
		puzzleList.add(puzzle6);
		puzzleList.add(puzzle7);
		puzzleList.add(puzzle8);
		puzzleList.add(puzzle9);
		puzzleList.add(puzzle10);
		
	}
	
	// choose one random puzzle variation to solve
	public int[][] getPuzzle() {
		return puzzleList.get(new Random().nextInt(puzzleList.size()));
	}
	
	// we need to have either another function to intialize the values in the buttons or do with a parameter on newPuzzle()
	void newPuzzle(GridPane gridPane) {
		selectedPuzzle = getPuzzle();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				Tiles t = new Tiles(i,j, selectedPuzzle[i][j]); // zero should be the number of that puzzle tile
				t.setMinSize(100, 100);
				t.setOnAction(checkPosition);
				puzzle[i][j] = t;
				gridPane.add(puzzle[i][j], j, i);
			}
		}
		
	}
	
	// after win, disable the puzzle so no moves can be made
	public void disablePuzzle() {
		for (int i = 0 ; i < 4; i++) {
			for (int j = 0 ; j< 4; j++) {
				puzzle[i][j].setDisable(true);
			}
		}
		
	}
	// check is the puzzle is solved
	boolean isWinner() {
		int counter = 0;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if ( puzzle[i][j].getNumber() != counter) {
					return false;
				}
				counter++;
			}
		}
		return true;
	}
	// setting up menu option 
	public void setMenuOptions () {
		menuGamePlay = new Menu( "Game Play");
		menuOptions = new Menu ("Options");
		howTo = new Menu ("Instructions");
		menuOptions.getItems().addAll(newPuzzle, exit);
		menuGamePlay.getItems().addAll(solve1, solve2);
		howTo.getItems().addAll(instr);
		menuBar.getMenus().addAll(menuGamePlay, menuOptions, howTo);
		
	}
	// create the main scene for the puzzle to be played on
	public Scene mainPuzzleScene() {
		menuBar = new MenuBar();
		setMenuOptions();
		gridpane = new GridPane();
		newPuzzle(gridpane);
		gridpane.setHgap(2);
		gridpane.setVgap(2);
		puzzlePane = new BorderPane();
		
		howToPlay = new Label("");
		howToPlay.setFont(new Font("Arial",18));
		
		VBox borderTop = new VBox(menuBar, howToPlay);
		puzzlePane.setTop(borderTop);
		
		moveString = new Label();
		moveString.setFont(new Font("Arial",18));
		moveString.setText("Move #: ");
		moveNumber = new Label();
		moveNumber.setFont(new Font("Arial",18));
		moveNumber.setText(String.valueOf(numberOfMoves));
		
		resetPuzzle = new Button("Reset");
		showSolution = new Button("Show Solution");
		showSolution.setDisable(true);
		
		HBox b = new HBox (100, resetPuzzle, showSolution);
		b.setAlignment(Pos.CENTER);
		gridpane.setAlignment(Pos.CENTER);
		HBox hB = new HBox(moveString, moveNumber);
		hB.setAlignment(Pos.CENTER);
		
		VBox vB = new VBox(20, hB, b, gridpane);
		
		vB.setAlignment(Pos.CENTER);
		
		puzzlePane.setBackground(new Background(new BackgroundFill(Color.DARKSEAGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
		
		puzzlePane.setCenter(vB);
		
		return new Scene(puzzlePane, 800, 800);
	}

}
