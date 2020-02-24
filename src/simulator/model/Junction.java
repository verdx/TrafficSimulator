package simulator.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class Junction extends SimulatedObject {

	private List<Road> in_roads;
	private Map<Junction, Road> outRoadByJunction;
	private Map<Road, List<Vehicle>> queueByRoad;
	private List<List<Vehicle>> queues;
	private int currGreen;
	private int lastSwitchingTime;
	private LightSwitchingStrategy lsStrategy;
	private DequeuingStrategy dqStrategy;
	private int x;
	private int y;
	
	
	Junction(String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dqStrategy, int xCoor, int yCoor) throws JunctionCreationException {
		super(id);
		if (lsStrategy != null && dqStrategy != null) {
			this.lsStrategy = lsStrategy;
			this.dqStrategy = dqStrategy;
		} else {
			throw new JunctionCreationException("light-switching and dequeuing strategies can't be null.");
		}	
		if(xCoor >= 0 && yCoor >= 0) {
			this.x = xCoor;
			this.y = yCoor;
		} else {
			throw new JunctionCreationException("xCoor and yCoor can't be null.");
		}
		currGreen = -1;
		lastSwitchingTime = 0;
		in_roads = new LinkedList<Road>();
		outRoadByJunction = new HashMap<Junction, Road>();
		queueByRoad = new HashMap<Road, List<Vehicle>>();
		queues = new LinkedList<List<Vehicle>>();
	}

	@Override
	void advance(int time) {
		List<Vehicle> toDequeue = dqStrategy.dequeue(queues.get(currGreen));
		for( Vehicle v: toDequeue) {
			try {
				v.moveToNextRoad();
			} catch (Exception e) {
				System.out.println("Problem advancing junction: " + this._id + ": " + e.getMessage());
			}
			queues.get(currGreen).remove(v);
		}
		int index = lsStrategy.chooseNextGreen(in_roads, queues, currGreen, lastSwitchingTime, time);
		if(index != currGreen) {
			currGreen = index;
			lastSwitchingTime = time;
		}
	}

	@Override
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		jo.append("id", this._id);
		jo.append("green", in_roads.get(currGreen)._id);
		JSONArray ja = new JSONArray();
		for (Road r: in_roads) {
			JSONObject jo1 = new JSONObject();
			jo1.put("road", r._id);
			JSONArray ja1 = new JSONArray();
			for(Vehicle v: queueByRoad.get(r)) {
				ja1.put(v);
			}
			jo1.put("vehicles", ja1);
		}
		jo.put("queues", ja);
		return jo;
	}

	 void enter(Vehicle vehicle) {
		queueByRoad.get(vehicle.getRoad()).add(vehicle);
	}

	 Road roadTo(Junction junction) {
		return outRoadByJunction.get(junction);
	}

	 void addOutGoingRoad(Road road) throws JunctionMethodException {
		if(outRoadByJunction.containsKey(road.getDestJunc()) ) {
			throw new JunctionMethodException("A road to the junction already exists.");
		} else if (road.getSrcJunc() != this){
			throw new JunctionMethodException("The road is not really an outgoing road."); 
		} else {
			outRoadByJunction.put(road.getDestJunc(), road);
		}
	}
	
	 void addIncomingRoad(Road road) throws JunctionMethodException {
		if (road.getDestJunc() == this) {
			in_roads.add(road);
			List<Vehicle> temp = new LinkedList<Vehicle>();
			queues.add(temp);
			queueByRoad.put(road, temp);
		} else {
			throw new JunctionMethodException("Trying to add incoming road incorrectly");
		}
	}

}
