package simulator.model;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONObject;

public class MoveAllStrategy implements DequeuingStrategy {

	@Override
	public List<Vehicle> dequeue(List<Vehicle> q) {
		List<Vehicle> result = new LinkedList<Vehicle>();
		for (Vehicle v: q) {
			result.add(v);
		}
		return result;
	}
	
	@Override
	public JSONObject save() {
		JSONObject jo = new JSONObject();
		jo.put("type", "most_all_dqs");
		JSONObject data  = new JSONObject();
		jo.put("data", data);
		return jo;
	}

}
