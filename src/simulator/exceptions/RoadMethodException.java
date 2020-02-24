package simulator.exceptions;

public class RoadMethodException extends Exception {

	public RoadMethodException(String string) {
		super("Problem in a road method: " + string);
	}
}
