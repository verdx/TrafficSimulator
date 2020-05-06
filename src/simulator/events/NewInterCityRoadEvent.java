package simulator.events;

import org.json.JSONObject;

import simulator.exceptions.RoadCreationException;
import simulator.model.InterCityRoad;
import simulator.model.RoadMap;
import simulator.model.Weather;

public class NewInterCityRoadEvent extends NewRoadEvent {

	
	
	public NewInterCityRoadEvent(int time, String id, String srcJunc, String destJunc, int maxSpeed, int contLimit, 
			int length, Weather weather) {
		super(time, id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);	
	}

	@Override
	public void execute(RoadMap map) {
		InterCityRoad cr;
		try {
			cr = new InterCityRoad(id, map.getJunction(srcJunc), map.getJunction(destJunc), maxSpeed,
					contLimit, length, weather);
			map.addRoad(cr);
		} catch (RoadCreationException e) {
			System.out.println("Problem executing NewInterCityRoadEvent:" + e.getMessage());
		}
	}
	
	@Override
	public String toString() {
		return "New InterCity Road '" + id + "'"; 
	}
	
	@Override
	public JSONObject save() {
		JSONObject jo = new JSONObject();
		jo.put("type", "new_inter_city_road");
		
		JSONObject data = new JSONObject();
		data.put("time", _time);
		data.put("id", id);
		data.put("src", srcJunc);
		data.put("dest", destJunc);
		data.put("maxpeed", maxSpeed);
		data.put("co2Limit", contLimit);
		data.put("length", length);
		data.put("weather", weather);
		jo.put("data", data);
		return jo;
	}

}
