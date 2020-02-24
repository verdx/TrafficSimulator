package simulator.exceptions;

public class VehicleCreationException extends Exception {

	public VehicleCreationException(String string) {
		super("Problem creating vehicle: " + string);
	}

}
