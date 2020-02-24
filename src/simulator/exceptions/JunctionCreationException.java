package simulator.exceptions;

public class JunctionCreationException extends Exception {

	public JunctionCreationException(String str) {
		super("Problem creating a Junction: " + str);
	}
}
