package simulator.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.exceptions.RoadCreationException;
import simulator.exceptions.RoadMethodException;
import simulator.exceptions.VehicleMethodException;
import simulator.misc.SortedArrayList;

public abstract class Road extends SimulatedObject {
	
	protected Junction destJunc, srcJunc;
	protected int length;
	protected int maxSpeed;
	protected int speedLimit;
	protected int contLimit;
	protected Weather weather;
	protected int contTotal;
	protected List<Vehicle> vehicles;
	protected Map<String, Vehicle> vehiclesMap;
	
	
	
	

	Road(String id, Junction srcJunc, Junction destJunc, int maxSpeed,
			int contLimit, int length, Weather weather) throws RoadCreationException {
		super(id);
		if(maxSpeed < 0) {
			throw new RoadCreationException("Maximum speed must be positive");
		} else if(contLimit < 0) {
			throw new RoadCreationException("Contamination limit must be positive");
		} else if(length < 0) {
			throw new RoadCreationException("Length must be positive");
		} else if(srcJunc == null || destJunc == null) {
			throw new RoadCreationException("Source and destination junctions can't be null");
		} else if(weather == null) {
			throw new RoadCreationException("Weather cannot be null");
		} else {
			this.srcJunc = srcJunc;
			this.destJunc = destJunc;
			try {
				srcJunc.addOutGoingRoad(this);
				destJunc.addIncomingRoad(this);
			} catch (JunctionMethodException e) {
				throw new RoadCreationException("Problem inserting as outgoing road: " + e.getMessage());
			}
			this.maxSpeed = maxSpeed;
			speedLimit = maxSpeed;
			this.contLimit = contLimit;
			this.length = length;
			this.weather = weather;
			contTotal = 0;
			vehicles = new SortedArrayList<Vehicle>(new VehicleComparator());
			vehiclesMap = new HashMap<String, Vehicle>();
		}
	}


	@Override
	void advance(int time) throws Exception {
		reduceTotalContamination();
		updateSpeedLimit();
		for(Vehicle v: vehicles) {
			try {
				v.setSpeed(calculateVehicleSpeed(v));
				v.advance(time);
			} catch (VehicleMethodException e) {
				throw new Exception("Problem changing vehicles speed: " + e.toString());
			}
		}
		vehicles.sort(new VehicleComparator());
	}

	@Override
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		jo.put("id", _id);
		jo.put("speedlimit", speedLimit);
		jo.put("weather", weather);
		jo.put("co2", contTotal);
		JSONArray ja = new JSONArray();
		for(Vehicle v: vehicles) {
			ja.put(v._id);
		}
		jo.put("vehicles", ja);
		return jo;
	}
	
	public JSONObject save() {
		JSONObject jo = new JSONObject();
		jo.put("id", _id);
		jo.put("length", length);
		jo.put("speedlimit", speedLimit);
		jo.put("weather", weather);
		jo.put("contTotal", contTotal);
		jo.put("co2limit", contLimit);
		
		jo.put("src", srcJunc.getId());
		jo.put("dest", destJunc.getId());
		
		return jo;
	}
	
	protected void enter(Vehicle v) throws RoadMethodException {
		vehicles.add(v);
		vehiclesMap.put(v._id, v);
	}

	protected void exit(Vehicle v) {
		vehicles.remove(v);
	}

	public void setWeather(Weather w) throws RoadMethodException {
		if(w != null) {
			weather = w;
		} else {
			throw new RoadMethodException("The argument passed to setWeather cannot be null.");
		}
	}
	
	protected void addContamination(int c) throws RoadMethodException {
		if(c >= 0) {
			contTotal += c;
		} else {
			throw new RoadMethodException("The argument to addContamination must be positive.");
		}
	}
	
	abstract void reduceTotalContamination();
	abstract void updateSpeedLimit();
	abstract int calculateVehicleSpeed(Vehicle v);
		
	public Junction getDestJunc() {
		return destJunc;
	}


	public Junction getSrcJunc() {
		return srcJunc;
	}


	public int getLength() {
		return length;
	}
	
	public int getTotalCO2() {
		return contTotal;
	}
	
	public int getCO2Limit() {
		return contLimit;
	}


	public  Weather getWeather() {
		return weather;
	}


	public int getMaxSpeed() {
		return maxSpeed;
	}
	
	public int getSpeedLimit() {
		return speedLimit;
	}

}
