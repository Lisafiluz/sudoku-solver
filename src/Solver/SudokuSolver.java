package Solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import Solver.Constraint.ConstraintType;
import javafx.util.Pair;

public class SudokuSolver {
	
	private Integer[][] puzzle;
	
	public SudokuSolver(Integer[][] puzzle) {
		this.puzzle = puzzle;
	}
	
	public Integer[][] solve() {
		// Build graph
		Set<Integer>[][] domains = new HashSet[getNumberOfRows()][getNumberOfColumns()];
		Queue<Constraint> constraints = new LinkedList<>();
		Set<Constraint> constraintsHelper = new HashSet<>();
		init(domains, constraints, constraintsHelper);
		
		// AC3
		AC3_Sudoku(domains, constraints, constraintsHelper);
		
		// Search
		
		return FindSolution(domains);
	}
	
	private Integer[][] FindSolution(Set<Integer>[][] domains) {
		// returns solution of the soduko board and null if there is no solution
		Integer[][] solution = new Integer[9][9];
		
		List<Pair<Integer, Integer>> sortedDomains = SortDomains(domains);
		
		boolean hasSolution = SearchRecursive(domains, sortedDomains, 0, solution);
		if (hasSolution) {
			return solution;
		}
		return null;
	}
	
	private List<Pair<Integer, Integer>> SortDomains(Set<Integer>[][] domains) {
		List<Pair<Set<Integer>, Pair<Integer, Integer>>> lst = new ArrayList<>();
		
		for (int r = 0; r < getNumberOfRows(); r++) {
			for (int c = 0; c < getNumberOfColumns(); c++) {
				lst.add(new Pair<>(domains[r][c], new Pair<>(r, c)));
			}
		}
		
		lst.sort(Comparator.comparingInt(p -> p.getKey().size()));
		
		return lst.stream().map(Pair::getValue).collect(Collectors.toList());
	}
	
	private boolean SearchRecursive(Set<Integer>[][] domains, List<Pair<Integer, Integer>> sortedDomains,
																	int index, Integer[][] solution) {
		if (index == sortedDomains.size()) {
			return true;
		}
		
		Pair<Integer, Integer> p = sortedDomains.get(index);
		int row = p.getKey();
		int col = p.getValue();
		Set<Integer> domain = domains[row][col];
		
		if (domain.isEmpty()) {
			return false;
		}
		
		for (Integer n : domain) {
			solution[row][col] = n;
			List<Pair<Integer, Integer>> restrictedNDomains = RestrictN(domains, n, row, col);
			
			// resorting pairs from index+1
			
			if (!SearchRecursive(domains, sortedDomains, index + 1, solution)) {
				solution[row][col] = 0;
				RevertN(domains, n, restrictedNDomains);
			} else {
				return true;
			}
			
		}
		
		return false;
	}
	
	private void RevertN(Set<Integer>[][] domains, Integer n, List<Pair<Integer, Integer>> restrictedNDomains) {
		for (Pair<Integer, Integer> pair : restrictedNDomains) {
			domains[pair.getKey()][pair.getValue()].add(n);
		}
	}
	
	private List<Pair<Integer, Integer>> RestrictN(Set<Integer>[][] domains, Integer n, int row, int col) {
		List<Pair<Integer, Integer>> restrictedDomains = new ArrayList<>();
		
		for (int c = 0; c < getNumberOfColumns(); c++) {
			if (c != col) {
				if (domains[row][c].remove(n)) {
					restrictedDomains.add(new Pair<>(row, c));
				}
			}
		}
		
		for (int r = 0; r < getNumberOfRows(); r++) {
			if (r != row) {
				if (domains[r][col].remove(n)) {
					restrictedDomains.add(new Pair<>(r, col));
				}
			}
		}
		
		int startRow = (row / 3) * 3;
		int startCol = (col / 3) * 3;
		for (int i = startRow; i < startRow + 3; i++) {
			for (int j = startCol; j < startCol + 3; j++) {
				if (i != row && j != col) {
					if (domains[i][j].remove(n)) {
						restrictedDomains.add(new Pair<>(i, j));
					}
				}
			}
		}
		return restrictedDomains;
	}
	
	private void AC3_Sudoku(Set<Integer>[][] domains, Queue<Constraint> constraintsQ, Set<Constraint> constraintsHelper) {
		while (!constraintsQ.isEmpty()) {
			Constraint constraint = constraintsQ.poll();
			constraintsHelper.remove(constraint);
			Set<Integer> domain = domains[constraint.getRow()][constraint.getCol()];
			List<Set<Integer>> relatedDomains = getRelatedDomains(constraint, domains);
			if (!arcConsistency(domain, relatedDomains)) {
				// arcConsistency update the domain if needed
				// If the constraint is not consistent - update constraints Queue
				updateConstraints(constraint, constraintsQ, constraintsHelper);
			}
			
		}
	}
	
	private void updateConstraints(Constraint constraint, Queue<Constraint> constraintsQ,
																 Set<Constraint> constraintsHelper) {
		ConstraintType[] conTypes = { ConstraintType.ROW, ConstraintType.COL, ConstraintType.BLOCK };
		for (ConstraintType constraintType : conTypes) {
			if (constraintType != constraint.getConstraintType()) {
				AddAllConstraints(constraintType, constraintsQ, constraintsHelper, constraint);
			}
		}
		
	}
	
	private void AddAllConstraints(ConstraintType constraintType, Queue<Constraint> constraintsQ,
																 Set<Constraint> constraintsHelper, Constraint constraint) {
		switch (constraintType) {
			case ROW:
				for (int i = 0; i < getNumberOfColumns(); i++) {
					if (i != constraint.getCol()) {
						Constraint c = new Constraint(constraint.getRow(), i, constraintType);
						if (!constraintsHelper.contains(c)) {
							constraintsHelper.add(c);
							constraintsQ.add(c);
						}
					}
				}
				break;
			case COL:
				for (int i = 0; i < getNumberOfRows(); i++) {
					if (i != constraint.getRow()) {
						Constraint c = new Constraint(i, constraint.getCol(), constraintType);
						if (!constraintsHelper.contains(c)) {
							constraintsHelper.add(c);
							constraintsQ.add(c);
						}
					}
				}
				break;
			case BLOCK:
				int startRow = (constraint.getRow() / 3) * 3;
				int startCol = (constraint.getCol() / 3) * 3;
				for (int i = startRow; i < startRow + 3; i++) {
					for (int j = startCol; j < startCol + 3; j++) {
						if (i != constraint.getRow() && j != constraint.getCol()) {
							Constraint c = new Constraint(i, j, constraintType);
							if (!constraintsHelper.contains(c)) {
								constraintsHelper.add(c);
								constraintsQ.add(c);
							}
						}
					}
				}
				break;
		}
		
	}
	
	private boolean arcConsistency(Set<Integer> domain, List<Set<Integer>> relatedDomains) {
		boolean con = true;
		Set<Integer> domainsToRemove = new HashSet<>();
		for (Integer n : domain) {
			int[] one_to_nine = new int[10];
			one_to_nine[n] = 1;
			if (!check_nRecursive(one_to_nine, relatedDomains, 0)) {
				domainsToRemove.add(n);
				//				domain.remove(n);
				con = false;
			}
		}
		domain.removeAll(domainsToRemove);
		return con;
	}
	
	private boolean check_nRecursive(int[] one_to_nine, List<Set<Integer>> relatedDomains, int i) {
		if (i == relatedDomains.size()) {
			return true;
		}
		
		for (Integer p : relatedDomains.get(i)) {
			if (one_to_nine[p] == 0) {
				one_to_nine[p] = 1;
				if (!check_nRecursive(one_to_nine, relatedDomains, i + 1)) {
					one_to_nine[p] = 0;
				} else {
					return true;
				}
			}
		}
		return false;
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
					domains[r][c] = new HashSet<>(Collections.singletonList(puzzle[r][c]));
				} else {
					domains[r][c] = SudokuSolver.getAllDomains();
				}
			}
		}
	}
	
	private static Set<Integer> getAllDomains() {
		Integer[] arr = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		return new HashSet<>(Arrays.asList(arr));
	}
}
