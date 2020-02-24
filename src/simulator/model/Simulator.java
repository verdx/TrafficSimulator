package simulator.model;

import java.util.Iterator;
import java.util.List;

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

		
		Event e;
		int eTime = 0;
		while(evItr.hasNext() && eTime <= this._time) {
			e = evItr.next();
			if (e._time == this._time) e.execute(roadMap);
			eTime = e._time;
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
		events = new SortedArrayList<Event>(new EventComparator());
	}
	
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		jo.put("time", _time);
		
		JSONObject state = roadMap.report();
		
		jo.put("state", state);
		return jo;
	}
}
