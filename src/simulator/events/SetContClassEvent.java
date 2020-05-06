package simulator.events;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.RoadMap;

public class SetContClassEvent extends Event {
	
	private List<Pair<String, Integer>> cs;

	public SetContClassEvent(int time, List<Pair<String, Integer>> cs) throws Exception {
		super(time);
		if (cs == null) {
			throw new Exception("Problem creating SetContaminationClass:The argument cs is null.");
		} else {
			this.cs = cs;
		}
	}

	@Override
	public void execute(RoadMap map) {
		try {
		for(Pair<String, Integer> w: cs) {
			if(map.getVehicle(w.getFirst()) == null) {
				throw new Exception("Problem executing SetContaminationClass: Vehicle " + w.getFirst() + " does not exist");
			} else {
				map.getVehicle(w.getFirst()).setContaminationClass(w.getSecond());
			}
		}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}
	
	@Override
	public String toString() {
		String result = "Change CO2 Class: [";
		for(Pair<String, Integer> w: cs) {
			result += "(" + w.getFirst() + ", " + w.getSecond() +"), "; 
		}
		result += "]";
		return result;
	}

	@Override
	public JSONObject save() {
		JSONObject jo = new JSONObject();
		jo.put("type", "set_cont_class");
		
		JSONObject data = new JSONObject();
		data.put("time", _time);
		
		JSONArray info = new JSONArray();
		for(Pair<String, Integer> p: cs) {
			JSONObject temp = new JSONObject();
			temp.put("vehicle", p.getFirst());
			temp.put("class", p.getSecond());
		}
		data.put("info", info);

		jo.put("data", data);
		
		return jo;
	}

}
