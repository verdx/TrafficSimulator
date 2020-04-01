package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class RoadMap {
	
	private List<Junction> junctions;
	private List<Road> roads;
	private List<Vehicle> vehicles;
	private Map<String, Junction> junctionsMap;
	private Map<String, Road> roadsMap;
	private Map<String, Vehicle> vehiclesMap;
	private Map<Road, Junction> srcJuncRoadMap;
	private Map<Road, Junction> destJuncRoadMap;
	
	
	
	RoadMap() {
		reset();
	}
	
	public void addJunction(Junction j) {
		if (!junctionsMap.containsKey(j._id)) {
			junctions.add(j);
			junctionsMap.put(j._id, j);
		}
	}
	
	public void addRoad(Road r) {
		if (!roadsMap.containsKey(r._id) && junctionsMap.containsKey(r.getDestJunc()._id)
				&& junctionsMap.containsKey(r.getSrcJunc()._id)) {
			roads.add(r);
			roadsMap.put(r._id, r);
			srcJuncRoadMap.put(r, r.getSrcJunc());
			destJuncRoadMap.put(r, r.getDestJunc());
		}
	}
	
	public void addVehicle(Vehicle v) {
		if(canAddVehicle(v)) {
			vehicles.add(v);
			vehiclesMap.put(v._id, v);
		}
	}
	
	private boolean canAddVehicle(Vehicle v) {
		boolean result = true;
		if (!vehiclesMap.containsKey(v._id)) {
			
			//Recorremos itinerary de v desde el segundo elemento
			//viendo que cada elemento y su anterior estan unidos
			Iterator<Junction> jIt = v.getItinerary().iterator();
			Junction j1;
			Junction j2 = jIt.hasNext() ? jIt.next() : null;
			while (jIt.hasNext()) {
				j1 = j2;
				j2 = jIt.next();
				if (!roadThatConnects(j1,j2)) {
					result = false;
				}
			}
		} else {
			result = false;
		}
		return result;
	}
	
	private boolean roadThatConnects(Junction j1, Junction j2) {
		boolean result = false;
		
		//Recorremos todas las carreteras hasta que una de
		//ellas conecta los dos cruces, si ninguna lo hace
		//devolvemos false
		Iterator<Road> rIt = roads.iterator();
		while (rIt.hasNext() && !result) {
			Road r = rIt.next();
			if(r.getSrcJunc()==j1 && r.getDestJunc()==j2) {
				result = true;
			}
		}
		return result;
	}
	
	public Junction getJunction(String id) {
		return junctionsMap.get(id);
	}
	
	public Road getRoad(String id) {
		return roadsMap.get(id);
	}
	
	public Vehicle getVehicle(String id) {
		return vehiclesMap.get(id);
	}
	
	public List<Junction> getJunctions() {
		//return Collections.unmodifiableList(new ArrayList<>(junctions));
		return (new ArrayList<Junction>(junctions));
	}
	
	public List<Road> getRoads() {
		//return Collections.unmodifiableList(new ArrayList<>(roads));
		return (new ArrayList<Road>(roads));
	}
	
	public List<Vehicle> getVehicles() {
		//return Collections.unmodifiableList(new ArrayList<>(vehicles));
		return (new ArrayList<Vehicle>(vehicles));	
		}
	
	//Alomejor no hace falta, borrar si se ve bien
	//el spinner de vehiculos en la ventana de cambiar clase de Co2
	public List<String> getVehiclesIds() {
		List<String> ids = new ArrayList<>();
		for(Vehicle v: vehicles) {
			ids.add(v._id);
		}
		return Collections.unmodifiableList(ids);
	}
	
	public void reset() {
		junctions = new LinkedList<Junction>();
		roads = new LinkedList<Road>();
		vehicles = new LinkedList<Vehicle>();
		junctionsMap = new HashMap<String, Junction>();
		roadsMap = new HashMap<String, Road>();
		vehiclesMap = new HashMap<String, Vehicle>();
		srcJuncRoadMap = new HashMap<Road, Junction>();
		destJuncRoadMap = new HashMap<Road, Junction>();
	}
	
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		
		
		JSONArray junctions_ja = new JSONArray();
		for(Junction j: junctions)
			junctions_ja.put(j.report());
		jo.put("junctions", junctions_ja);
		
		
		JSONArray roads_ja = new JSONArray();
		for(Road r: roads) 
			roads_ja.put(r.report());
		jo.put("roads", roads_ja);
		
		
		JSONArray vehicles_ja = new JSONArray();
		for(Vehicle v: vehicles)
			vehicles_ja.put(v.report());
		jo.put("vehicles", vehicles_ja);
		
		return jo;
	}
}
