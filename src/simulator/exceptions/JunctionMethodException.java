package simulator.exceptions;

public class JunctionMethodException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JunctionMethodException(String str) {
		super("Problem with a method in class Junction: " + str);
	}
}
