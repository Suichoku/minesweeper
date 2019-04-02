package minesweeper;

/**
 * Makes custom exceptions possible for program
 * 
 * @author Tuomas Rautanen
 *
 */
public class CustomException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ExType type; // type of exception
	
	/**
	 * Class for custom made exceptions used by program
	 * 
	 * @param msg exception message
	 * @param e type of exception
	 */
	public CustomException(String msg, ExType e) {
		
        super(msg);
        type = e;
    }
	
	public ExType getType() {
		
		return this.type;
	}
}
