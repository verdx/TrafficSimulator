package simulator.events;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.Junction;
import simulator.model.RoadMap;
import simulator.model.Vehicle;

public class NewVehicleEvent extends Event {

	String id;
	int maxSpeed;
	int contClass;
	List<String> itinerary;
	
	public NewVehicleEvent(int time, String id, int maxSpeed, int contClass, List<String> itinerary) {
		super(time);
		this.id = id;
		this.maxSpeed = maxSpeed;
		this.contClass = contClass;
		this.itinerary = itinerary;
	}

	@Override
	public void execute(RoadMap map) {
		try {
			List<Junction> itinerary_j = new LinkedList<Junction>();
			Junction j;
			for(String s: itinerary) {
				j = map.getJunction(s);
				if (j != null) {
					itinerary_j.add(j);
				} else {
					throw new Exception("Cannot find junction in itinerary.");
				}
			}
				
			Vehicle vehicle = new Vehicle(id, maxSpeed, contClass, itinerary_j);
			map.addVehicle(vehicle);
			vehicle.moveToNextRoad();
		} catch (Exception e) {
			System.out.println("Problem executing NewVehicleEvent: " + e.getMessage());
		}
	}
	
	@Override
	public String toString() {
		return "New Vehicle '" + id + "'"; 
	}
	
	@Override
	public JSONObject save() {
		JSONObject jo = new JSONObject();
		jo.put("type", "new_vehicle");
		
		JSONObject data = new JSONObject();
		data.put("time", _time);
		data.put("id", id);
		data.put("maxpeed", maxSpeed);
		data.put("class", contClass);
		
		JSONArray itineraryJSON = new JSONArray();
		for(String s: itinerary) {
			itineraryJSON.put(s);
		}
		data.put("itinerary", itineraryJSON);
		jo.put("data", data);
		return jo;
	}

}
