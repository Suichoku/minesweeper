package minesweeper;

public class CustomException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ExType type;
	
	public CustomException(String msg, ExType e) {
		
        super(msg);
        type = e;
    }
	
	public ExType getType() {
		
		return this.type;
	}
}
