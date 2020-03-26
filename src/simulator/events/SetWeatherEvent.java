package simulator.events;

import java.util.List;

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
		String result = "Set weather: ";
		for(Pair<String, Weather> w: ws) {
			result += "road '" + w.getFirst() + "' to '" + w.getSecond().toString() +"', "; 
		}
		return result;
	}

}
