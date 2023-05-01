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
		resetBoard();
	}
	
	private void setBoard() {
		// For each non-empty cell in the sudokuBoard we update the relevant text Field as disabled with red color
		ObservableList<Node> sudokuBlocks = getSudokuBlocks();
		for (int i = 0; i < this.sudokuBoard.length; i++) {
			for (int j = 0; j < this.sudokuBoard.length; j++) {
				if (Cell.isNotEmpty(this.sudokuBoard[i][j])) {
					int blockNumber = (i / 3) * 3 + j / 3;
					int indexInBlock = (i % 3) * 3 + (j % 3);
					TextField textField = (TextField) ((GridPane) sudokuBlocks.get(blockNumber)).getChildren().get(indexInBlock);
					textField.setDisable(true);
					textField.setStyle(cellStyleAfterSet);
					this.sudokuBoard[i][j].setDisable(true);
				}
			}
		}
	}
	
	@FXML
	void onEnter(ActionEvent event) {
		// On enter we validate the value that entered and if valid save it as Cell object in the sudokuBoard
		TextField textField = ((TextField) event.getSource());
		String textFieldContent = textField.getText();
		String textFieldId = textField.getId();  // The fx:id of the textField represents the index in the matrix
		int row = Character.getNumericValue(textFieldId.charAt(0));
		int col = Character.getNumericValue(textFieldId.charAt(1));
		validateCellNotDisable(this.sudokuBoard[row][col]);
		try {
			validateDigit(textFieldContent);
			Integer valueToInsert = Integer.valueOf(textField.getText());
			validateGameRules(row, col, valueToInsert);
			sudokuBoard[row][col] = new Cell(valueToInsert, false, textField.getStyle());
		} catch (IllegalArgumentException e) {
			textField.setText("");
			if (this.sudokuBoard[row][col] != null) {
				// Reset the value in the board if we saved it already
				this.sudokuBoard[row][col] = null;
			}
			showErrorAlert(e.getMessage());
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
				int blockNumber = (i / 3) * 3 + j / 3;
				int indexInBlock = (i % 3) * 3 + (j % 3);
				TextField textField = (TextField) ((GridPane) sudokuBlocks.get(blockNumber)).getChildren().get(indexInBlock);
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
	
	private void validateGameRules(int row, int col, Integer valueToInsert) {
		if (Cell.isNotEmpty(this.sudokuBoard[row][col]) && this.sudokuBoard[row][col].getContent().equals(valueToInsert)) {
			// In case we are updating a cell with value x to value x
			return;
		}
		validateBlock(row, col, valueToInsert);
		validateRow(row, valueToInsert);
		validateColumn(col, valueToInsert);
	}
	
	private void validateColumn(int col, Integer valueToInsert) {
		// Validate that the column of the value to insert doesn't contain the value to insert
		for (int i = 0; i < this.sudokuBoard.length; i++) {
			if (Cell.isNotEmpty(this.sudokuBoard[i][col]) && this.sudokuBoard[i][col].getContent() == valueToInsert.intValue()) {
				throw new IllegalArgumentException(String.format("Cannot put the number %d as it appears in the column", valueToInsert));
			}
		}
	}
	
	private void validateRow(int row, Integer valueToInsert) {
		// Validate that the row of the value to insert doesn't contain the value to insert
		for (int i = 0; i < this.sudokuBoard.length; i++) {
			if (Cell.isNotEmpty(this.sudokuBoard[row][i]) && this.sudokuBoard[row][i].getContent() == valueToInsert.intValue()) {
				throw new IllegalArgumentException(String.format("Cannot put the number %d as it appears in the row", valueToInsert));
			}
		}
	}
	
	private void validateBlock(int row, int col, Integer valueToInsert) {
		// Validate that the block of the value to insert doesn't contain the value to insert
		int startRow = row - (row % 3);
		int startCol = col - (col % 3);
		for (int i = startRow; i < startRow + 3; i++) {
			for (int j = startCol; j < startCol + 3; j++) {
				if (Cell.isNotEmpty(this.sudokuBoard[i][j]) && this.sudokuBoard[i][j].getContent() == valueToInsert.intValue()) {
					throw new IllegalArgumentException(String.format("Cannot put the number %d as it appears in the block", valueToInsert));
				}
			}
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

