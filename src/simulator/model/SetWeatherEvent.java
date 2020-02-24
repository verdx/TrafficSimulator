package simulator.model;

import java.util.List;

import simulator.misc.Pair;

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
	void execute(RoadMap map) {
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

}
