package simulator.model;

import java.util.List;

import org.json.JSONObject;

public class MostCrowdedStrategy implements LightSwitchingStrategy {

	private int timeSlot;
	
	public MostCrowdedStrategy(int timeSlot) {
		this.timeSlot = timeSlot;
	}
	
	@Override
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime,
			int currTime) {
		if (roads.size() == 0) {
			return -1;
		} else if (currGreen == -1) {
			int max = 0;
			int index = -1;
			for (int i = 0; i < qs.size(); i++) {
				if(qs.get(i).size() > max) {
					max = qs.get(i).size();
					index = i;
				}
			}
			return index;
		} else if (currTime - lastSwitchingTime < timeSlot) {
			return currGreen;
		} else {
			int max = 0;
			int index = -1;
			int size = qs.size();
			for (int i = (currGreen + 1)%size; i == currGreen; i = (i + 1) % size) {
				if (qs.get(i).size() > max) {
					max = qs.get(i).size();
					index = i;
				}
			}
			if (qs.get(currGreen).size() > max) {
				return currGreen;
			} else {
				return index;
			}
		}
	}

	@Override
	public JSONObject save() {
		JSONObject jo = new JSONObject();
		jo.put("type", "most_crowded_lss");
		JSONObject data = new JSONObject();
		data.put("timeslot", timeSlot);
		jo.put("data", data);
		return jo;
	}

}
