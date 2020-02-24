package simulator.control;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.Event;
import simulator.model.Simulator;

public class Controller {
	
	private Simulator sim;
	private Factory<Event> eventsFactory;
	
	public Controller(Simulator sim, Factory<Event> eventsFactory) throws Exception {
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
	
	public void run(int n, OutputStream out) {
		JSONObject jo = new JSONObject();
		JSONArray states = new JSONArray();
		for(int i = 0; i < n; i++) {
			sim.advance();
			states.put(sim.report());
		}
		jo.put("states", states);
		PrintStream p = new PrintStream(out);
		p.print(jo.toString(3));
	}
	
	public void reset() {
		sim.reset();
	}
}
