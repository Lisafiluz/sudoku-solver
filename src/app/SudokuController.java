package app;

import Converters.Converter;
import Solver.SudokuSolver;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class SudokuController {
	
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
		SudokuSolver sudokuSolver = new SudokuSolver(Converter.convert(sudokuBoard));
		long time0 = System.currentTimeMillis();
		Integer[][] solution = sudokuSolver.solve();
		System.out.println(String.format("Execution time: %sms", System.currentTimeMillis() - time0));
		//visualizeSolution(solution);
		for (int i = 0; i < solution.length; i++) {
			for (int j = 0; j < solution[0].length; j++) {
				if (sudokuBoard[i][j]== null) {
					TextField textField = getTextFieldCell(i, j);
					textField.setText(solution[i][j].toString());
//					sudokuBoard[i][j] = new Cell(solution[i][j], false, null);
				}
				System.out.print(solution[i][j] + " ");
			}
			System.out.println();
		}
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
		
		Integer[][] medBoard = {
				{null, 2, 3, null, null, null, 7, 8, null},
				{null, null, null, 2, null, 7, null, null, null},
				{null, null, null, 3, null, 1, null, null, 6},
				{null, null, null, null, null, 3, 1, 7, null},
				{4, null, null, 7, null, 6, null, null, 5},
				{null, 5, 7, 9, null, null, null, null, null},
				{2, null, null, 1, null, 9, null, null, null},
				{null, null, null, 6, null, 2, null, null, null},
				{null, 3, 6, null, null, null, 4, 1, null}
		};
		Integer[][] medBoardSolution = {
				{5, 2, 3, 6, 4, 9, 7, 8, 1},
				{1, 6, 9, 2, 8, 7, 5, 4, 3},
				{7, 8, 4, 3, 5, 1, 2, 9, 6},
				{6, 9, 8, 5, 2, 3, 1, 7, 4},
				{4, 1, 2, 7, 9, 6, 8, 3, 5},
				{3, 5, 7, 9, 1, 4, 6, 2, 8},
				{2, 7, 5, 1, 6, 9, 3, 5, 4},
				{9, 4, 1, 6, 3, 2, 5, 6, 7},
				{8, 3, 6, 4, 7, 5, 4, 1, 9}
		};
		Integer[][] hardBoardSolution = {
				{5, 6, 1, 7, 4, 2, 8, 9, 3},
				{9, 4, 8, 1, 3, 6, 9, 5, 7},
				{2, 3, 7, 9, 5, 8, 1, 6, 4},
				{6, 9, 5, 3, 8, 1, 7, 4, 2},
				{8, 1, 7, 2, 9, 4, 6, 3, 5},
				{3, 2, 4, 6, 7, 5, 2, 8, 9},
				{4, 5, 2, 8, 1, 3, 5, 7, 6},
				{7, 8, 6, 5, 2, 9, 4, 1, 3},
				{1, 7, 3, 4, 6, 7, 9, 2, 8}
		};
		Integer[][] hardBoard = {
				{null, null, null, 7, null, 2, null, null, null},
				{null, null, null, 1, null, null, 9, null, 7},
				{2, null, null, null, null, null, 1, null, 4},
				{null, null, 5, null, null, null, 7, null, null},
				{null, null, 7, null, null, null, 6, null, null},
				{null, null, 4, null, null, null, 2, null, null},
				{4, null, 2, null, null, null, null, null, 6},
				{7, null, 6, null, null, 9, null, null, null},
				{null, null, null, 4, null, 7, null, null, null}
		};
		
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
}

