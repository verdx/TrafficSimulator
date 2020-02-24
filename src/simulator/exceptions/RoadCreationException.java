package simulator.exceptions;

public class RoadCreationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RoadCreationException(String string) {
		super("Problem creating a road: " + string);
	}

}
