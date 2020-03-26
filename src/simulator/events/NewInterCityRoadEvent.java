package simulator.events;

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

}
