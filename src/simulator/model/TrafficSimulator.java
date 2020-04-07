package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;

import extra.jtable.EventEx;
import simulator.events.Event;
import simulator.events.EventComparator;
import simulator.misc.SortedArrayList;

public class TrafficSimulator implements Observable<TrafficSimObserver>{

	private RoadMap roadMap;
	private List<Event> events;
	private int _time;
	private List<TrafficSimObserver> observers;

	public TrafficSimulator() {
		init();
	}

	public void addEvent(Event e) {
		events.add(e);
		for(TrafficSimObserver o: observers) {
			o.onEventAdded(roadMap, events, e, _time);
		}
	}

	public void advance() throws Exception {
		//Avanzar tiempo
		_time++;
		
		for(TrafficSimObserver o: observers) {
			o.onAdvanceStart(roadMap, events, _time);
		}

		//Ejecutar eventos
		Iterator<Event> evItr = events.iterator();

		Event e;
		int eTime = 0;
		while(evItr.hasNext() && eTime <= this._time) {
			e = evItr.next();
			if (e.getTime() == this._time) { 
				e.execute(roadMap);
				evItr.remove();
			}
			eTime = e.getTime();
		}
		
		try {
			//Avanzar cruces
			for(Junction j: roadMap.getJunctions()) {
				j.advance(_time);
			}

			//Avanzar carreteras
			for(Road r: roadMap.getRoads()) {
				r.advance(_time);
			}
		} catch(Exception exc) {
			for(TrafficSimObserver o: observers) {
				o.onError(exc.getMessage());
				throw new Exception(exc);
			}
			
		}
		
		for(TrafficSimObserver o: observers) {
			o.onAdvanceEnd(roadMap, events, _time);
		}
	}

	private void init() {
		_time = 0;
		roadMap = new RoadMap();
		events = new SortedArrayList<Event>(new EventComparator());
		observers = new ArrayList<TrafficSimObserver>();
	}
	
	public void reset() {
		_time = 0;
		roadMap.reset();
		events.clear();
		for(TrafficSimObserver o: observers) {
			if(o != null)
				o.onReset(roadMap, events, _time);
		}
	}
	
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		jo.put("time", _time);
		
		JSONObject state = roadMap.report();
		
		jo.put("state", state);
		return jo;
	}
	
	@Override
	public void addObserver(TrafficSimObserver o) {
		observers.add(o);
		for(TrafficSimObserver ob: observers) {
			ob.onRegister(roadMap, events, _time);
		}
	}
	@Override
	public void removeObserver(TrafficSimObserver o) {
		observers.remove(o);
	}

	public List<Vehicle> getVehicles() {
		return roadMap.getVehicles();
	}

	public int getTime() {
		return _time;
	}

	public List<Road> getRoads() {
		return roadMap.getRoads();
	}

	public List<Event> getEvents() {
		return Collections.unmodifiableList(new ArrayList<Event>(events));
	}

	public List<Junction> getJunctions() {
		return roadMap.getJunctions();
	}

}
