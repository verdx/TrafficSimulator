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

import simulator.factories.Builder;
import simulator.factories.BuilderBasedFactory;
import simulator.factories.Factory;
import simulator.factories.MostCrowdedStrategyBuilder;
import simulator.factories.MoveAllStrategyBuilder;
import simulator.factories.MoveFirstStrategyBuilder;
import simulator.factories.RoundRobinStrategyBuilder;

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
		init();
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
		return Collections.unmodifiableList(new ArrayList<>(junctions));
		//return (new ArrayList<Junction>(junctions));
	}
	
	public List<Road> getRoads() {
		return Collections.unmodifiableList(new ArrayList<>(roads));
		//return (new ArrayList<Road>(roads));
	}
	
	public List<Vehicle> getVehicles() {
		return Collections.unmodifiableList(new ArrayList<>(vehicles));
		//return (new ArrayList<Vehicle>(vehicles));	
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
	
	public void init() {
		junctions = new LinkedList<Junction>();
		roads = new LinkedList<Road>();
		vehicles = new LinkedList<Vehicle>();
		junctionsMap = new HashMap<String, Junction>();
		roadsMap = new HashMap<String, Road>();
		vehiclesMap = new HashMap<String, Vehicle>();
		srcJuncRoadMap = new HashMap<Road, Junction>();
		destJuncRoadMap = new HashMap<Road, Junction>();
	}
	
	public void reset() {
		junctions.clear();
		roads.clear();
		vehicles.clear();
		junctionsMap.clear();
		roadsMap.clear();
		vehiclesMap.clear();
		srcJuncRoadMap.clear();
		destJuncRoadMap.clear();
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

	public void load(JSONObject roadmap) throws Exception{
		JSONArray vehiclesJSON = roadmap.getJSONArray("vehicles");
		JSONArray junctionsJSON = roadmap.getJSONArray("junctions");
		JSONArray roadsJSON = roadmap.getJSONArray("roads");


		//Factories para LightSwitchingStrategy
		List<Builder<LightSwitchingStrategy>> lsbs = new ArrayList<>();
		lsbs.add( new RoundRobinStrategyBuilder() );
		lsbs.add( new MostCrowdedStrategyBuilder() );
		Factory<LightSwitchingStrategy> lssFactory = new BuilderBasedFactory
				<>(lsbs);

		//Factories para DequeuingStrategy
		List<Builder<DequeuingStrategy>> dqbs = new ArrayList<>();
		dqbs.add( new MoveFirstStrategyBuilder() );
		dqbs.add( new MoveAllStrategyBuilder() );
		Factory<DequeuingStrategy> dqsFactory = new BuilderBasedFactory<>(dqbs);
		for(int i = 0; i < junctionsJSON.length(); i++) {
			Junction junction = parseJunction(junctionsJSON.getJSONObject(i), lssFactory, dqsFactory);
			this.addJunction(junction);
		}
		for(int i = 0; i < vehiclesJSON.length(); i++) {
			Vehicle vehicle = parseVehicle(vehiclesJSON.getJSONObject(i));
			this.addVehicle(vehicle);
		}
		for(int i = 0; i < roadsJSON.length(); i++) {
			Road road = parseRoad(roadsJSON.getJSONObject(i));
			this.addRoad(road);			
		}

	}

	private Junction parseJunction(JSONObject jo, Factory<LightSwitchingStrategy> lssFactory, Factory<DequeuingStrategy> dqsFactory) throws Exception {
		String id = jo.getString("id");
		int xCoor = jo.getJSONArray("coor").getInt(0);
		int yCoor = jo.getJSONArray("coor").getInt(1);
		LightSwitchingStrategy lss = lssFactory.createInstance(jo.getJSONObject("ls_strategy"));
		DequeuingStrategy dqs = dqsFactory.createInstance(jo.getJSONObject("dq_strategy"));
		return new Junction(id, lss, dqs, xCoor, yCoor);
		
	}


	private Vehicle parseVehicle(JSONObject jo) throws Exception {
		String id = jo.getString("id");
		int maxSpeed = jo.getInt("maxspeed");
		int contClass = jo.getInt("class");
		JSONArray itinerary_ja = jo.getJSONArray("itinerary");
		List<String> itinerary = new ArrayList<String>();
		for(int j = 0; j < itinerary_ja.length(); j++) {
			itinerary.add(itinerary_ja.getString(j));
		}
		List<Junction> itinerary_j = new LinkedList<Junction>();
		Junction j;
		for(String s: itinerary) {
			j = this.getJunction(s);
			if (j != null) {
				itinerary_j.add(j);
			} else {
				throw new Exception("Cannot find junction in itinerary.");
			}
		}
			
		return new Vehicle(id, maxSpeed, contClass, itinerary_j);
	}
	
	private Road parseRoad(JSONObject jo) throws Exception{
		String id = jo.getString("id");
		String src = jo.getString("src");
		String dest = jo.getString("dest");
		int length = jo.getInt("length");
		int co2limit = jo.getInt("co2limit");
		int maxSpeed = jo.getInt("maxspeed");
		String weather_str = jo.getString("weather");
		Weather weather = Weather.valueOf(weather_str.toUpperCase());
		String type = jo.getString("type");
		if(type.equals("inter_city_road")) {
			return new InterCityRoad(id, this.getJunction(src), this.getJunction(dest), maxSpeed,
					co2limit, length, weather);
		} else if(type.contentEquals("city_road")) {
			return new CityRoad(id, this.getJunction(src), this.getJunction(dest), maxSpeed,
					co2limit, length, weather);
		} else {
			throw new Exception("Problem parsing road.");
		}
	}
	
}
