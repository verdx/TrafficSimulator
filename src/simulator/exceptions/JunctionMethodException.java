package simulator.exceptions;

public class JunctionMethodException extends Exception {
	public JunctionMethodException(String str) {
		super("Problem with a method in class Junction: " + str);
	}
}
