package simulator.model;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONObject;

public class MoveFirstStrategy implements DequeuingStrategy {

	@Override
	public List<Vehicle> dequeue(List<Vehicle> q) {
		List<Vehicle> result = new LinkedList<Vehicle>();
		if (!q.isEmpty())
			result.add(q.get(0));
		return result;
	}

	@Override
	public JSONObject save() {
		JSONObject jo = new JSONObject();
		jo.put("type", "move_first_dqs");
		JSONObject data  = new JSONObject();
		jo.put("data", data);
		return jo;
	}

}
