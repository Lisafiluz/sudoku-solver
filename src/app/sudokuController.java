package app;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class sudokuController {
	
	private final static String cellStyleAfterSet = "-fx-text-inner-color: #ff002c;";
	private Cell[][] sudokuBoard;
	
	@FXML
	private Button setButton;
	
	@FXML
	private Button solveButton;
	
	@FXML
	private GridPane mainGrid;
	
	@FXML
	void initialize() {
		sudokuBoard = new Cell[9][9];
	}
	
	@FXML
	void setPressed() {
		setButton.setDisable(true);
		setBoard();
	}
	
	@FXML
	void clearPressed() {
		setButton.setDisable(false);
		solveButton.setDisable(false);
		resetBoard();
	}
	
	@FXML
	public void solvePressed() {
		solveButton.setDisable(true);
		// call the solver
	}
	@FXML
	public void rufflePressed() {
		resetBoard();
		Integer[][] easyBoard = {{null, null, null, 2, 6, null, 7, null, 1},
														 {6, 8, null, null, 7, null, null, 9, null},
														 {1, 9, null, null, null, 4, 5, null, null},
														 {8, 2, null, 1, null, null, null, 4, 0},
														 {null, null, 4, 6, null, 2, 9, null, null},
														 {null, 5, null, null, null, 3, null, 2, 8},
														 {null, null, 9, 3, null, null, null, 7, 4},
														 {null, 4, null, null, 5, null, null, 3, 6},
														 {7, null, 3, null, 1, 8, null, null, null}};
		fillBoardWithPuzzle(easyBoard);
	}
	
	private void fillBoardWithPuzzle(Integer[][] puzzle) {
		for (int i = 0; i < puzzle.length; i ++) {
			for (int j = 0; j < puzzle.length; j ++) {
				if (puzzle[i][j] != null) {
					Integer valueToInsert = puzzle[i][j];
					TextField textField = getTextFieldCell(i, j);
					sudokuBoard[i][j] = new Cell(valueToInsert, true, textField.getStyle());
					textField.setText(String.valueOf(valueToInsert));
					textField.setDisable(true);
					textField.setStyle(cellStyleAfterSet);
				}
			}
		}
	}
	
	private TextField getTextFieldCell(int row, int column) {
		ObservableList<Node> sudokuBlocks = getSudokuBlocks();
		int blockNumber = (row / 3) * 3 + column / 3;
		int indexInBlock = (row % 3) * 3 + (column % 3);
		return (TextField) ((GridPane) sudokuBlocks.get(blockNumber)).getChildren().get(indexInBlock);
	}
	
	@FXML
	void onEnter(ActionEvent event) {
		// On enter we validate the value that entered and if valid save it as Cell object in the sudokuBoard
		TextField textField = ((TextField) event.getSource());
		String textFieldContent = textField.getText();
		String textFieldId = textField.getId();  // The fx:id of the textField represents the index in the matrix
		int row = Character.getNumericValue(textFieldId.charAt(0));
		int col = Character.getNumericValue(textFieldId.charAt(1));
		try {
			SudokuGameValidator.validateCellNotDisable(this.sudokuBoard[row][col]);
			SudokuGameValidator.validateDigit(textFieldContent);
			Integer valueToInsert = Integer.valueOf(textField.getText());
			SudokuGameValidator.validateGameRules(this.sudokuBoard, row, col, valueToInsert);
			sudokuBoard[row][col] = new Cell(valueToInsert, false, textField.getStyle());
		} catch (IllegalArgumentException e) {
			resetCellContent(textField, row, col);
			showErrorAlert(e.getMessage());
		} catch (BusinessException e) {
			showErrorAlert(e.getMessage());
		}
	}
	
	private void resetCellContent(TextField textField, int row, int col) {
		textField.setText("");
		if (this.sudokuBoard[row][col] != null) {
			// Reset the value in the board if we saved it already
			this.sudokuBoard[row][col] = null;
		}
	}
	
	private void setBoard() {
		// For each non-empty cell in the sudokuBoard we update the relevant text Field as disabled with red color
		ObservableList<Node> sudokuBlocks = getSudokuBlocks();
		for (int i = 0; i < this.sudokuBoard.length; i++) {
			for (int j = 0; j < this.sudokuBoard.length; j++) {
				if (Cell.isNotEmpty(this.sudokuBoard[i][j])) {
					TextField textField = getTextFieldCell(i, j);
					textField.setDisable(true);
					textField.setStyle(cellStyleAfterSet);
					this.sudokuBoard[i][j].setDisable(true);
				}
			}
		}
	}
	
	private void showErrorAlert(String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR, message);
		alert.show();
	}
	
	private void resetBoard() {
		// Reset all board and return all the cells to the original states
		ObservableList<Node> sudokuBlocks = getSudokuBlocks();
		for (int i = 0; i < this.sudokuBoard.length; i++) {
			for (int j = 0; j < this.sudokuBoard.length; j++) {
				TextField textField = getTextFieldCell(i, j);
				textField.setText("");
				if (Cell.isNotEmpty(this.sudokuBoard[i][j])) {
					textField.setDisable(false);
					textField.setStyle(this.sudokuBoard[i][j].getSourceStyle());
				}
				this.sudokuBoard[i][j] = null;
			}
		}
	}
	
	private ObservableList<Node> getSudokuBlocks() {
		return mainGrid.getChildren();
	}
	
	private void validateCellNotDisable(Cell cell) {
		if (Cell.isNotEmpty(cell) && cell.isDisable()) {
			showErrorAlert("Cannot edit this cell as it is disabled");
		}
	}
	
	private void validateDigit(String digit) {
		if (digit == null || digit.length() != 1) {
			throw new IllegalArgumentException("Not a digit");
		}
		char digitCh = digit.charAt(0);
		if (digitCh == '0') {
			throw new IllegalArgumentException("Only digits between 1 to 9 are valid");
		}
		if (!Character.isDigit(digitCh)) {
			throw new IllegalArgumentException("Not a digit");
		}
	}
}

