package simulator.exceptions;

public class VehicleMethodException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public VehicleMethodException(String string) {
		super("Problem in a vehicle method: " + string);
	}

}
