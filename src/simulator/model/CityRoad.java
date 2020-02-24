package simulator.model;

import simulator.exceptions.RoadCreationException;

public class CityRoad extends Road {

	CityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length,
			Weather weather) throws RoadCreationException {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}

	@Override
	void reduceTotalContamination() {
		int cond;
		switch(weather) {
		case WINDY:
		case STORM:
			cond = 10;
			break;
		default:
			cond = 2;
			break;
		}
		if(contTotal - cond > 0) {
			contTotal -= cond;
		} else {
			contTotal = 0;
		}
	}

	@Override
	void updateSpeedLimit() {}

	@Override
	int calculateVehicleSpeed(Vehicle v) {
		int resul = (int) Math.ceil((((11.0 - v.getContClass())/11.0)*speedLimit));
		return resul;
	}

}
