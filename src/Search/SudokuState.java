package Search;

import app.SudokuGameValidator;

public class SudokuState {
	
	private Integer[][] puzzle;
	
	public SudokuState(Integer[][] puzzle) {
		this.puzzle = puzzle;
	}
	
	public static boolean isValid(Integer[][] puzzle) {
		return true;
	}
	
}
