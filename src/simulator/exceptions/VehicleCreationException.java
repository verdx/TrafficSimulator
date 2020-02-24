package simulator.exceptions;

public class VehicleCreationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public VehicleCreationException(String string) {
		super("Problem creating vehicle: " + string);
	}

}
