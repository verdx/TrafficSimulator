package simulator.model;

import java.util.List;

import org.json.JSONObject;

public class RoundRobinStrategy implements LightSwitchingStrategy {

	private int timeSlot;
	
	public RoundRobinStrategy(int timeSlot) {
		this.timeSlot = timeSlot;
	}
	
	@Override
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime,
			int currTime) {
		if (roads.size() == 0) {
			return -1;
		} else if (currGreen == -1) {
			return 0;
		} else if (currTime-lastSwitchingTime < timeSlot) {
			return currGreen;
		} else {
			return ((currGreen + 1)%roads.size());
		}
	}

	@Override
	public JSONObject save() {
		JSONObject jo = new JSONObject();
		jo.put("type", "round_robin_lss");
		JSONObject data = new JSONObject();
		data.put("timeslot", timeSlot);
		jo.put("data", data);
		return jo;
	}

}
