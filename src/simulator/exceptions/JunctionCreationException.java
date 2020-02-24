package simulator.exceptions;

public class JunctionCreationException extends Exception {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public JunctionCreationException(String str) {
		super("Problem creating a Junction: " + str);
	}
}
