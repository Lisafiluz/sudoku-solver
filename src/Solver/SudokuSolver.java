package Solver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import Solver.Constraint.ConstraintType;

public class SudokuSolver {
	
	private Integer[][] puzzle;
	
	public SudokuSolver(Integer[][] puzzle) {
		this.puzzle = puzzle;
	}
	
	public Integer[][] solve() {
		//Build graph
		Set<Integer>[][] domains = new Set[getNumberOfRows()][getNumberOfColumns()];
		Queue<Constraint> constraints = new LinkedList<>();
		Set<Constraint> constraintsHelper = new HashSet<>();
		init(domains, constraints, constraintsHelper);
		AC3_Sudoku(domains, constraints, constraintsHelper);
		
		//Search
		return null;
	}
	
	private void AC3_Sudoku(Set<Integer>[][] domains, Queue<Constraint> constraints, Set<Constraint> constraintsHelper) {
		while (!constraints.isEmpty()) {
			Constraint constraint = constraints.poll();
			Set<Integer> domain = domains[constraint.getRow()][constraint.getCol()];
			List<Set<Integer>> relatedDomains = getRelatedDomains(constraint, domains);
			
			
		}
	}
	
	private List<Set<Integer>> getRelatedDomains(Constraint constraint, Set<Integer>[][] domains) {
		List<Set<Integer>> relatedDomains = new ArrayList<>();
		switch (constraint.getConstraintType()) {
			case ROW:
				for (int i = 0; i < getNumberOfColumns(); i++) {
					if (i != constraint.getCol()) {
						relatedDomains.add(domains[constraint.getRow()][i]);
					}
				}
				break;
			case COL:
				for (int i = 0; i < getNumberOfRows(); i++) {
					if (i != constraint.getRow()) {
						relatedDomains.add(domains[i][constraint.getCol()]);
					}
				}
				break;
			case BLOCK:
				int startRow = (constraint.getRow() / 3) * 3;
				int startCol = (constraint.getCol() / 3) * 3;
				for (int i = startRow; i < startRow + 3; i++) {
					for (int j = startCol; j < startCol + 3; j++) {
						if (i != constraint.getRow() && j != constraint.getCol()) {
							relatedDomains.add(domains[i][j]);
						}
					}
				}
				break;
		}
		return relatedDomains;
	}
	
	private int getNumberOfColumns() {
		return puzzle[0].length;
	}
	
	private int getNumberOfRows() {
		return puzzle.length;
	}
	
	private void init(Set<Integer>[][] domains, Queue<Constraint> constraints, Set<Constraint> constraintsHelper) {
		initDomains(domains);
		initConstrains(constraints, constraintsHelper);
	}
	
	private void initConstrains(Queue<Constraint> constraints, Set<Constraint> constraintsHelper) {
		for (int r = 0; r < getNumberOfRows(); r++) {
			for (int c = 0; c < getNumberOfColumns(); c++) {
				Constraint constraintRow = new Constraint(r, c, ConstraintType.ROW);
				Constraint constraintCol = new Constraint(r, c, ConstraintType.COL);
				Constraint constraintBlock = new Constraint(r, c, ConstraintType.BLOCK);
				constraints.add(constraintRow);
				constraints.add(constraintCol);
				constraints.add(constraintBlock);
				constraintsHelper.add(constraintRow);
				constraintsHelper.add(constraintCol);
				constraintsHelper.add(constraintBlock);
			}
		}
	}
	
	private void initDomains(Set<Integer>[][] domains) {
		for (int r = 0; r < getNumberOfRows(); r++) {
			for (int c = 0; c < getNumberOfColumns(); c++) {
				if (puzzle[r][c] != null) {
					domains[r][c] = Set.of(puzzle[r][c]);
				} else {
					domains[r][c] = SudokuSolver.getAllDomains();
				}
			}
		}
	}
	
	private static Set<Integer> getAllDomains() {
		return Set.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
	}
}
