package simulator.model;

import java.util.LinkedList;
import java.util.List;

public class MoveAllStrategy implements DequeuingStrategy {

	@Override
	public List<Vehicle> dequeue(List<Vehicle> q) {
		List<Vehicle> result = new LinkedList<Vehicle>();
		for (Vehicle v: q) {
			result.add(v);
		}
		return result;
	}

}
