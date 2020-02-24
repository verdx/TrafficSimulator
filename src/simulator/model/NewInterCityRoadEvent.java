package simulator.model;

import simulator.exceptions.RoadCreationException;

public class NewInterCityRoadEvent extends NewRoadEvent {

	
	
	public NewInterCityRoadEvent(int time, String id, String srcJunc, String destJunc, int maxSpeed, int contLimit, 
			int length, Weather weather) {
		super(time, id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);	
	}

	@Override
	void execute(RoadMap map) {
		InterCityRoad cr;
		try {
			cr = new InterCityRoad(id, map.getJunction(srcJunc), map.getJunction(destJunc), maxSpeed,
					contLimit, length, weather);
			map.addRoad(cr);
		} catch (RoadCreationException e) {
			System.out.println("Problem executing NewInterCityRoadEvent:" + e.getMessage());
		}
	}

}
