package simulator.exceptions;

public class RoadCreationException extends Exception {

	public RoadCreationException(String string) {
		super("Problem creating a road: " + string);
	}

}
