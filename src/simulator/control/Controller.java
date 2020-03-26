package simulator.control;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.events.Event;
import simulator.factories.Factory;
import simulator.model.TrafficSimObserver;
import simulator.model.TrafficSimulator;

public class Controller{
	
	private TrafficSimulator sim;
	private Factory<Event> eventsFactory;
	
	public Controller(TrafficSimulator sim, Factory<Event> eventsFactory) throws Exception {
		if(sim == null || eventsFactory == null) {
			throw new Exception("Problem creating new Controller: simulator or events factory is null.");
		} else {
		this.sim = sim;
		this.eventsFactory = eventsFactory;
	
		}
	}

	public void loadEvents(InputStream in) throws Exception {
		try {
			JSONObject jo = new JSONObject(new JSONTokener(in));
			JSONArray events = jo.getJSONArray("events");
			for(int i = 0; i < events.length();i++) {
				sim.addEvent(eventsFactory.createInstance(events.getJSONObject(i)));
			}
		}catch(JSONException e) {
			throw new Exception("Problem parsing inputStream json: " + e.getMessage());
		}
	}
	
	public void run(int n, OutputStream out) throws Exception {
		JSONObject jo = new JSONObject();
		JSONArray states = new JSONArray();
		for(int i = 0; i < n; i++) {
			sim.advance();
			states.put(sim.report());
		}
		jo.put("states", states);
		PrintStream p = new PrintStream(out);
		p.print(jo.toString(4));
	}
	
	public void reset() {
		sim.reset();
	}
	
	public void addObserver(TrafficSimObserver o) {
		sim.addObserver(o);
	}
	
	public void removeObserver(TrafficSimObserver o) {
		sim.removeObserver(o);
	}
	
	public void addEvent(Event e) {
		sim.addEvent(e);
	}
}
