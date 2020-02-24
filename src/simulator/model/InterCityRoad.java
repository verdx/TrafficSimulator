package simulator.model;

import simulator.exceptions.RoadCreationException;

public class InterCityRoad extends Road {

	InterCityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length,
			Weather weather) throws RoadCreationException {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}

	@Override
	void reduceTotalContamination() {
		int cond = 0;
		switch(weather) {
		case SUNNY: cond = 2; break;
		case CLOUDY: cond = 3; break;
		case RAINY: cond = 10; break;
		case WINDY: cond = 15; break;
		case STORM: cond = 20; break;
		}
		contTotal = (int) (((100.0 - cond)/100.0) * contTotal);
	}

	@Override
	void updateSpeedLimit() {
		if(contTotal > contLimit) {
			speedLimit = (int) (maxSpeed * 0.5);
		} else {
			speedLimit = maxSpeed;
		}
	}

	@Override
	int calculateVehicleSpeed(Vehicle v) {
		int resul;
		if (weather == Weather.STORM) {
			resul = (int)(speedLimit * 0.8);
		} else {
			resul = speedLimit;
		}
		return resul;
	}

}
