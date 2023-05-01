package app;

public class SudokuGameValidator {
	
	public static void validateGameRules(Cell[][] sudokuBoard, int row, int col, Integer valueToInsert) {
		if (Cell.isNotEmpty(sudokuBoard[row][col]) && sudokuBoard[row][col].getContent().equals(valueToInsert)) {
			// In case we are updating a cell with value x to value x
			return;
		}
		validateBlock(sudokuBoard, row, col, valueToInsert);
		validateRow(sudokuBoard, row, valueToInsert);
		validateColumn(sudokuBoard, col, valueToInsert);
	}
	private static void validateColumn(Cell[][] sudokuBoard, int col, Integer valueToInsert) {
		// Validate that the column of the value to insert doesn't contain the value to insert
		for (int i = 0; i < sudokuBoard.length; i++) {
			if (Cell.isNotEmpty(sudokuBoard[i][col]) && sudokuBoard[i][col].getContent() == valueToInsert.intValue()) {
				throw new IllegalArgumentException(String.format("Cannot put the number %d as it appears in the column", valueToInsert));
			}
		}
	}
	
	private static void validateRow(Cell[][] sudokuBoard, int row, Integer valueToInsert) {
		// Validate that the row of the value to insert doesn't contain the value to insert
		for (int i = 0; i < sudokuBoard.length; i++) {
			if (Cell.isNotEmpty(sudokuBoard[row][i]) && sudokuBoard[row][i].getContent() == valueToInsert.intValue()) {
				throw new IllegalArgumentException(String.format("Cannot put the number %d as it appears in the row", valueToInsert));
			}
		}
	}
	
	private static void validateBlock(Cell[][] sudokuBoard, int row, int col, Integer valueToInsert) {
		// Validate that the block of the value to insert doesn't contain the value to insert
		int startRow = row - (row % 3);
		int startCol = col - (col % 3);
		for (int i = startRow; i < startRow + 3; i++) {
			for (int j = startCol; j < startCol + 3; j++) {
				if (Cell.isNotEmpty(sudokuBoard[i][j]) && sudokuBoard[i][j].getContent() == valueToInsert.intValue()) {
					throw new IllegalArgumentException(String.format("Cannot put the number %d as it appears in the block", valueToInsert));
				}
			}
		}
	}
	
	public static void validateCellNotDisable(Cell cell) throws BusinessException {
		if (Cell.isNotEmpty(cell) && cell.isDisable()) {
			throw new BusinessException("Cannot edit this cell as it is disabled");
		}
	}
	
	public static void validateDigit(String digit) {
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
