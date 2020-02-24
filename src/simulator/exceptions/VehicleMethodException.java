package simulator.exceptions;

public class VehicleMethodException extends Exception {

	public VehicleMethodException(String string) {
		super("Problem in a vehicle method: " + string);
	}

}
