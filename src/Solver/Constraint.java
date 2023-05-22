package Solver;

public class Constraint {
	int row;
	int col;
	ConstraintType constraintType;
	
	public Constraint(int row, int col, ConstraintType constraintType) {
		this.row = row;
		this.col = col;
		this.constraintType = constraintType;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}
	
	public ConstraintType getConstraintType() {
		return constraintType;
	}
	
	public enum ConstraintType{
		ROW,COL,BLOCK
	}
}
