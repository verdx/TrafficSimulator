package simulator.factories;

import org.json.JSONException;
import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewInterCityRoadEvent;
import simulator.model.Weather;

public class NewInterCityRoadEventBuilder extends Builder<Event>{

	public NewInterCityRoadEventBuilder() {
		super("new_inter_city_road");
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		Event cre = null;
		try {
			int time = data.getInt("time");
			String id = data.getString("id");
			String src = data.getString("src");
			String dest = data.getString("dest");
			int length = data.getInt("length");
			int co2limit = data.getInt("co2limit");
			int maxSpeed = data.getInt("maxspeed");
			String weather_str = data.getString("weather");
			Weather weather = Weather.valueOf(weather_str.toUpperCase());
			
			cre = new NewInterCityRoadEvent(time, id, src, dest, maxSpeed, co2limit, length, weather);
		} catch (JSONException e) {
			System.out.println("Problem parsing JSONObject for NewInterCityRoadEvent" + e.getMessage());
		}
		return cre;
	}
	
	

}