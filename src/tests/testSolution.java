package tests;

import java.util.HashSet;
import java.util.Set;

import Solver.SudokuSolver;

public class testSolution {
	
	public static Integer[][] easyBoard1 = {
	        {5, 3, null, null, 7, null, null, null, null},
	        {6, null, null, 1, 9, 5, null, null, null},
	        {null, 9, 8, null, null, null, null, 6, null},
	        {8, null, null, null, 6, null, null, null, 3},
	        {4, null, null, 8, null, 3, null, null, 1},
	        {7, null, null, null, 2, null, null, null, 6},
	        {null, 6, null, null, null, null, 2, 8, null},
	        {null, null, null, 4, 1, 9, null, null, 5},
	        {null, null, null, null, 8, null, null, 7, 9}
	    };

	    public static Integer[][] easyBoard2 = {
	        {null, null, 4, 3, null, null, null, null, null},
	        {null, null, null, null, null, null, 6, null, null},
	        {7, null, null, null, null, null, null, null, null},
	        {null, null, null, null, 5, null, null, 1, null},
	        {null, 9, 3, null, null, null, null, null, null},
	        {null, null, null, null, null, null, null, 2, 6},
	        {null, null, null, 2, null, null, null, null, null},
	        {null, null, 6, null, null, null, null, null, 8},
	        {null, null, null, 9, null, null, 1, null, null}
	    };

	    public static Integer[][] mediumBoard1 = {
	        {null, null, 1, 7, null, null, null, null, 4},
	        {null, 2, null, null, 8, null, null, 9, 1},
	        {8, null, null, null, 1, null, null, null, 6},
	        {null, null, 6, null, null, 5, 8, null, null},
	        {null, null, null, null, null, null, null, null, null},
	        {null, null, 9, 3, null, null, 5, null, null},
	        {2, null, null, null, 6, null, null, null, 9},
	        {9, 7, null, null, 3, null, null, 1, null},
	        {4, null, null, null, null, 7, 2, null, null}
	    };

	    public static Integer[][] mediumBoard2 = {
	        {1, null, null, null, 7, null, 6, 8, null},
	        {null, 7, 9, null, null, null, null, null, null},
	        {null, null, 2, null, null, null, null, null, null},
	        {null, null, null, null, null, null, null, null, 9},
	        {null, null, null, null, 3, null, null, null, null},
	        {8, null, null, null, null, 5, null, null, null},
	        {null, null, null, 6, null, null, null, null, 8},
	        {null, null, null, null, null, null, 7, 2, null},
	        {null, 6, 7, null, null, 1, null, null, null}
	    };

	    public static Integer[][] hardBoard1 = {
	        {null, null, null, null, null, null, null, 8, null},
	        {null, null, null, null, null, null, null, null, null},
	        {3, null, 5, null, 7, null, null, null, null},
	        {null, null, null, null, null, 3, null, null, 5},
	        {null, null, null, null, null, null, null, 4, null},
	        {null, 6, null, null, null, null, null, null, null},
	        {7, null, null, 8, null, null, null, null, null},
	        {null, null, null, null, null, null, 7, 2, null},
	        {null, null, null, null, null, null, 1, null, null}
	    };

	    public static Integer[][] hardBoard2 = {
	        {null, 2, null, null, null, null, null, null, null},
	        {null, null, 9, null, null, 1, null, 7, null},
	        {null, null, null, null, 8, null, null, null, null},
	        {null, null, null, null, null, null, 4, null, null},
	        {null, null, null, null, null, null, null, null, 8},
	        {null, null, 7, 1, null, null, null, null, 2},
	        {null, null, null, null, null, null, 5, null, null},
	        {null, null, null, 7, null, null, null, null, null},
	        {null, null, null, null, null, null, null, 1, 3}
	    };
	    
	    public static void main(String[] args) {
	    	Integer[][][] boards = {easyBoard1, easyBoard2, mediumBoard1, mediumBoard2, hardBoard1, hardBoard2};
	    	int n = 10;
	    	double i = 1, j = 1;
	    	for (Integer[][] board : boards) {
	    		System.out.println("Level-" + (int)i + " ; board-" + j);
				
	    		Solver.SudokuSolver solver = new SudokuSolver(board);
				
	    		for (int k = 0; k < n ; k++) {
		    		if (CheckBoard(solver.solve())) {
						System.out.println("Good solution");
					}
					else {
						System.out.println("Bad solution");
					}
					System.out.println("-------------------");
	    		}
				i+=0.5;
				j = i == (int)i ? 1 : 2; 
				System.out.println("-------------------------------\n");
			}
	    	
	    }
	    
	public static boolean CheckBoard(Integer[][] board) {
		// Returns true iff the board is 9*9 integer matrix containing integers between 1-9
		// 	that describe a legal sudoku solution.
		
		if (board == null || board.length != 9 || board[0] == null || board[0].length != 9) {
			return false;
		}
		
		Set<Integer> set_1_9_R = new HashSet<>();
		Set<Integer> set_1_9_C = new HashSet<>();
		Set<Integer> set_1_9_B = new HashSet<>();
		
		for(int r = 0; r < 9; r++) {
			for (int c = 0; c < 9; c++) {
				Integer cellR = board[r][c];
				Integer cellC = board[c][r]; 
				Integer cellB = board[3*(r/3)+c/3][3*(r%3)+c%3]; 

				if (cellR == null || cellR > 9 || cellR < 1 || 
						cellC == null || cellC > 9 || cellC < 1 || 
						cellB == null || cellB > 9 || cellB < 1) {
					return false;
				}

				if (!set_1_9_R.add(cellR) || !set_1_9_C.add(cellC) || !set_1_9_B.add(cellB)) {
					return false;
				}
				
			}
			set_1_9_R.clear();
			set_1_9_C.clear();
			set_1_9_B.clear();
		}

		return true;
	}
}
