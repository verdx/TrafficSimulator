package simulator.exceptions;

public class RoadMethodException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RoadMethodException(String string) {
		super("Problem in a road method: " + string);
	}
}
