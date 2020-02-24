package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetWeatherEvent;
import simulator.model.Weather;

public class SetWeatherEventBuilder extends Builder<Event>{

	SetWeatherEventBuilder() {
		super("set_weather");
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		Event swe = null;
		try {
			int time = data.getInt("time");
			JSONArray info_list = data.getJSONArray("info");
			List<Pair<String, Weather>> ws = new ArrayList<Pair<String, Weather>>();
			for(int i = 0; i<info_list.length();i++) {
				String road = info_list.getJSONObject(i).getString("road");
				Weather weather = Weather.valueOf(info_list.getJSONObject(i).getString("weather").toUpperCase());
				ws.add(new Pair<String, Weather>(road, weather));
			}
			
			swe = new SetWeatherEvent(time, ws);
		} catch (Exception e) {
			System.out.println("Problem parsing JSONObject for SetWeatherEvent" + e.getMessage());
		}
		return swe;
	}

}
