package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.exceptions.RoadMethodException;
import simulator.exceptions.VehicleCreationException;
import simulator.exceptions.VehicleMethodException;

public class Vehicle extends SimulatedObject{
	
	private List<Junction> itinerary;
	private int maxSpeed;
	private int speed;
	private VehicleStatus status;
	private Road road;
	private int location;
	private int contClass;
	private int contTotal;
	private int distTotal;
	private int nextJunction;
	
	

	public Vehicle(String id, int maxSpeed, int contClass,
			List<Junction> itinerary) throws VehicleCreationException {
			super(id);
			if(maxSpeed < 0) {
				throw new VehicleCreationException("Speed must be positive.");
			} else if(contClass < 0 || contClass > 10) {
				throw new VehicleCreationException("Contamination Class must be between 0 and 10");	
			} else if(itinerary.size() < 2) {
				throw new VehicleCreationException("Itinerary has to be of length 2.");
			} else {
				this.maxSpeed = maxSpeed;
				this.contClass = contClass;
				this.itinerary = Collections.unmodifiableList(new ArrayList<>(itinerary));
				speed = 0;
				location = 0;
				contTotal = 0;
				distTotal = 0;
				status = VehicleStatus.PENDING;
				nextJunction = 1;
			}
	}
	
	public Vehicle(String id, int maxSpeed, int contClass,
			List<Junction> itinerary, int speed, int location, int contTotal, int distTotal, 
			VehicleStatus status, int nextJunction, Road road) throws VehicleCreationException {
			super(id);
			if(maxSpeed < 0) {
				throw new VehicleCreationException("Speed must be positive.");
			} else if(contClass < 0 || contClass > 10) {
				throw new VehicleCreationException("Contamination Class must be between 0 and 10");	
			} else if(itinerary.size() < 2) {
				throw new VehicleCreationException("Itinerary has to be of length 2.");
			} else {
				this.maxSpeed = maxSpeed;
				this.contClass = contClass;
				this.itinerary = Collections.unmodifiableList(new ArrayList<>(itinerary));
				this.speed = speed;
				this.location = location;
				this.contTotal = contTotal;
				this.distTotal = distTotal;
				this.status = status;
				this.nextJunction = nextJunction;
				this.road = road;
			}
			
	}



	@Override
	void advance(int time) {
		if(status == VehicleStatus.TRAVELING) {
			int distRec;
			boolean fin = false;
			if(location + speed >= road.getLength()) {
				distRec = road.getLength() - location;
				location = road.getLength();
				fin = true;
			} else {
				distRec = speed;
				location += speed;
			}
			
			distTotal += distRec;
			int cont = contClass * distRec;
			contTotal += cont;
			try {
				road.addContamination(cont);
			} catch (RoadMethodException e) {
				System.out.println("Problem adding contamination to road." + e.toString());
			}
			if (fin) {
				this.status = VehicleStatus.WAITING;
				speed = 0;
				road.getDestJunc().enter(this);
			}
		}

	}

	@Override
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		// we put some keys with simple values into 'jo1'
		jo.put("id", _id);
		jo.put("speed", speed);
		jo.put("distance", distTotal);
		jo.put("co2", contTotal);
		jo.put("class", contClass);
		jo.put("status", status);
		if(status != VehicleStatus.PENDING && status != VehicleStatus.ARRIVED) {
			jo.put("road", road._id);
			jo.put("location", location);
		}
		return jo;
	}
	
	public JSONObject save() {
		JSONObject jo = new JSONObject();
		// we put some keys with simple values into 'jo1'
		jo.put("id", _id);
		jo.put("maxspeed", maxSpeed);
		jo.put("speed", speed);
		jo.put("distTotal", distTotal);
		jo.put("contTotal", contTotal);
		jo.put("contClass", contClass);
		jo.put("status", status.toString());
		jo.put("nextJunction", nextJunction);
		
		if(status != VehicleStatus.PENDING && status != VehicleStatus.ARRIVED) {
			jo.put("road", road._id);
			jo.put("location", location);
		}
		
		JSONArray itineraryJSON = new JSONArray();
		for(Junction j: itinerary) {
			itineraryJSON.put(j.getId());
		}
		
		jo.put("itinerary", itineraryJSON);
		return jo;
	}
	
	void setSpeed(int s) throws VehicleMethodException {
		if(status == VehicleStatus.TRAVELING) {
			if (s < 0) {
				throw new VehicleMethodException("Setting speed, argument must be positive");
			} else if (s > maxSpeed) {
				this.speed = maxSpeed;
			} else {
				this.speed = s;
			}
		}
	}
	
	public void setContaminationClass(int c) throws VehicleMethodException{
		if (c < 0 || c > 10) {
			throw new VehicleMethodException("Setting contamination class, argument must be between 0 and 10.");
		} else {
			this.contClass = c;
		}
	}
	
	public void moveToNextRoad() throws Exception {
		Road nextRoad;
		if(nextJunction < itinerary.size()) {
			if(status == VehicleStatus.PENDING) {
				nextRoad = itinerary.get(0).roadTo(itinerary.get(nextJunction));
			} else if (status == VehicleStatus.WAITING){
				road.exit(this);
				nextRoad = road.getDestJunc().roadTo(itinerary.get(nextJunction));
			} else {
				throw new VehicleMethodException("Cannot move to next road if status isnt't pending or waiting");
			}
			road = nextRoad;
			location = 0;
			road.enter(this);
			status = VehicleStatus.TRAVELING;
			nextJunction++;
		} else {
			status = VehicleStatus.ARRIVED;
			road.exit(this);
		}
	}


	public int getLocation() {
		return location;
	}


	public int getSpeed() {
		return speed;
	}


	public int getContClass() {
		return contClass;
	}
	
	public Road getRoad() {
		return road;
	}


	public List<Junction> getItinerary() {
		return itinerary;
	}
	
	public VehicleStatus getStatus() {
		return status;
	}


	public int getMaxSpeed() {
		return maxSpeed;
	}


	public int getTotalCo2() {
		return contTotal;
	}


	public int getDistance() {
		return distTotal;
	}

	public Junction getCurrentJunction() throws Exception {
		if(status == VehicleStatus.WAITING) {
			return itinerary.get(nextJunction - 1);
		} else { 
			throw new Exception("This vehicle is not in a junction right now");
		}
	}
}
