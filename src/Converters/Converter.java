package Converters;

import app.Cell;

public class Converter {
	
	public static Integer[][] convert(Cell[][] board) {
		Integer[][] res = new Integer[9][9];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				if (board[i][j] != null) {
					res[i][j] = board[i][j].getContent();
				}
			}
		}
		return res;
	}
	
}
