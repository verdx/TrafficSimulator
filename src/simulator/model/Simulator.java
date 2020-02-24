package simulator.model;

import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.SortedArrayList;

public class Simulator {

	private RoadMap roadMap;
	private List<Event> events;
	private int _time;

	public Simulator() {
		reset();
	}

	public void addEvent(Event e) {
		events.add(e);
	}

	public void advance() {
		//Avanzar tiempo
		_time++;

		//Ejecutar eventos
		Iterator<Event> evItr = events.iterator();
		int itTime = 0;
		while(evItr.hasNext() && itTime < this._time) {
			itTime = evItr.next()._time;
		}
		if(evItr.hasNext()) {
			Event e = evItr.next();
			while(e._time == this._time && evItr.hasNext()) {
				e.execute(roadMap);
				e = evItr.next();
			}
		}

		//Avanzar cruces
		for(Junction j: roadMap.getJunctions()) {
			j.advance(_time);
		}

		//Avanzar carreteras
		for(Road r: roadMap.getRoads()) {
			r.advance(_time);
		}
	}

	public void reset() {
		_time = 0;
		roadMap = new RoadMap();
		events = new SortedArrayList<Event>();
	}
	
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		jo.put("time", _time);
		
		JSONObject state = roadMap.report();
		
		jo.put("state", state);
		return jo;
	}
}
