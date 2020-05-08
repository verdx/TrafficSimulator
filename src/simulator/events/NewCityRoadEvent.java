package simulator.events;

import org.json.JSONObject;

import simulator.exceptions.RoadCreationException;
import simulator.model.CityRoad;
import simulator.model.RoadMap;
import simulator.model.Weather;

public class NewCityRoadEvent extends NewRoadEvent {

	
	public NewCityRoadEvent(int time, String id, String srcJunc, String destJunc, int maxSpeed, int contLimit, 
			int length, Weather weather) {
		super(time, id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}

	@Override
	public void execute(RoadMap map) {
		CityRoad cr;
		try {
			cr = new CityRoad(id, map.getJunction(srcJunc), map.getJunction(destJunc), maxSpeed,
					contLimit, length, weather);
			map.addRoad(cr);
		} catch (RoadCreationException e) {
			System.out.println("Problem executing NewCityRoadEvent: " + e.getMessage());
		}
	}
	
	@Override
	public String toString() {
		return "New City Road '" + id + "'"; 
	}

	@Override
	public JSONObject save() {
		JSONObject jo = new JSONObject();
		jo.put("type", "new_city_road");
		
		JSONObject data = new JSONObject();
		data.put("time", _time);
		data.put("id", id);
		data.put("src", srcJunc);
		data.put("dest", destJunc);
		data.put("maxspeed", maxSpeed);
		data.put("co2limit", contLimit);
		data.put("length", length);
		data.put("weather", weather);
		jo.put("data", data);
		return jo;
	}
}
