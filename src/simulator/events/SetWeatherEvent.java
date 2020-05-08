package simulator.events;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.RoadMap;
import simulator.model.Weather;

public class SetWeatherEvent extends Event {
	
	private List<Pair<String, Weather>> ws;

	public SetWeatherEvent(int time, List<Pair<String, Weather>> ws) throws Exception {
		super(time);
		if (ws == null) {
			throw new Exception("Problem creating SetWeatherEvent:The argument ws is null.");
		} else {
			this.ws = ws;
		}
	}

	@Override
	public void execute(RoadMap map) {
		try {
		for(Pair<String, Weather> w: ws) {
			if(map.getRoad(w.getFirst()) == null) {
				throw new Exception("Problem executing SetWeatherEvent: Road " + w.getFirst() + " does not exist");
			} else {
				map.getRoad(w.getFirst()).setWeather(w.getSecond());
			}
		}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}
	
	@Override
	public String toString() {
		String result = "Change Weather: [";
		for(Pair<String, Weather> w: ws) {
			result += "(" + w.getFirst() + ", " + w.getSecond().toString() +"), "; 
		}
		result += "]";
		return result;
	}
	
	@Override
	public JSONObject save() {
		JSONObject jo = new JSONObject();
		jo.put("type", "set_weather");
		
		JSONObject data = new JSONObject();
		data.put("time", _time);
		
		JSONArray info = new JSONArray();
		for(Pair<String, Weather> p: ws) {
			JSONObject temp = new JSONObject();
			temp.put("road", p.getFirst());
			temp.put("weather", p.getSecond());
			info.put(temp);
		}
		data.put("info", info);

		jo.put("data", data);
		
		return jo;
	}

}
