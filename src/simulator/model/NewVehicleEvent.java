package simulator.model;

import java.util.LinkedList;
import java.util.List;

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
	void execute(RoadMap map) {
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

}
