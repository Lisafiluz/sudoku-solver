package app;

/*
 * The Cell class represents a cell in a table or a grid.
 * It stores the content of the cell as an Integer, whether the cell is disabled or not as a boolean, and the source style of the cell as a String.
 * It provides methods to access and modify these properties, and a static utility method to check whether a given cell is not empty.
 * */
public class Cell {
	
	private Integer content;
	private boolean isDisable;
	private String sourceStyle;
	
	public Cell(Integer content, boolean isDisable, String sourceStyle) {
		this.content = content;
		this.isDisable = isDisable;
		this.sourceStyle = sourceStyle;
	}
	
	public void setDisable(boolean disable) {
		isDisable = disable;
	}
	
	public Integer getContent() {
		return content;
	}
	
	public boolean isDisable() {
		return isDisable;
	}
	
	public String getSourceStyle() {
		return sourceStyle;
	}
	
	public static boolean isNotEmpty(Cell c) {
		return c != null && c.getContent() != null;
	}
}
