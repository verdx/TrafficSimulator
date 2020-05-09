package simulator.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

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
	private int simStatus;
	private short undos;
	private boolean redo;

	public Controller(TrafficSimulator sim, Factory<Event> eventsFactory) throws Exception {
		if(sim == null || eventsFactory == null) {
			throw new Exception("Problem creating new Controller: simulator or events factory is null.");
		} else {
			this.sim = sim;
			this.eventsFactory = eventsFactory;
			simStatus = 0;
			undos = 0;
			redo = false;
		}
		new File("resources/savedStates").mkdirs();
	}

	public void load(InputStream in) throws Exception {
		reset();
		JSONObject jo = new JSONObject(new JSONTokener(in));
		JSONArray events = jo.getJSONArray("events");
		loadEventsArray(events);
		sim.load(jo);
	}

	public void loadEvents(InputStream in) throws Exception {
		try {
			JSONObject jo = new JSONObject(new JSONTokener(in));
			JSONArray events = jo.getJSONArray("events");
			loadEventsArray(events);
		}catch(JSONException e) {
			throw new Exception("Problem parsing inputStream json: " + e.getMessage());
		}
	}

	private void loadEventsArray(JSONArray events) {
		for(int i = 0; i < events.length();i++) {
			sim.addEvent(eventsFactory.createInstance(events.getJSONObject(i)));
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
	
	public void totalReset() {
		simStatus = 0;
		undos  = 0;
		redo = false;
		reset();
	}

	public void addObserver(TrafficSimObserver o) {
		sim.addObserver(o);
	}

	public void removeObserver(TrafficSimObserver o) {
		sim.removeObserver(o);
	}

	public void addEvent(Event event) {
		sim.addEvent(event);
	}

	public void run(int n) throws Exception{
		redo = false;
		for(int i = 0; i < n; i++) {
			sim.advance();
		}
	}

	public List<Event> getEvents() {
		return sim.getEvents();
	}

	public JSONObject save() {
		return sim.save();
	}
	
	public void saveState() {
		saveStateImpl();
		simStatus++;
		if(undos > 0) undos--;
		redo = false;
	}
	
	private void saveStateImpl() {
		JSONObject jo = save();
		File file = new File("resources/savedStates/ss" + simStatus % 4 + ".json");
		try {
			if(!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file, false);
			fw.write(jo.toString(4));
			fw.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(new JFrame(), "Problem saving: " + e.getMessage(), "Dialog",
			        JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void undo() {
		try {
			if(undos >= 3) throw new Exception("Can't undo more than four times");
			if(simStatus < 1) throw new Exception("Not enough saved states");
			if (!redo) saveStateImpl();
			undos++;
			simStatus--;
			redo = true;
			File file = new File("resources/savedStates/ss" + simStatus  % 4 + ".json");
			load(new FileInputStream(file));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), "Dialog",
					JOptionPane.ERROR_MESSAGE);
		}
	}
	public boolean canRedo() {
		return redo;
	}
	
	public boolean canUndo() {
		return (simStatus > 0 && undos < 3);
	}
	
	public void redo() {
		try {
			if(!redo || undos <= 0) throw new Exception("Can't redo right now");
			undos--;
			simStatus++;
			redo = (undos >= 1);
			File file = new File("resources/savedStates/ss" + simStatus % 4 + ".json");
			load(new FileInputStream(file));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), "Dialog",
					JOptionPane.ERROR_MESSAGE);
		}
	}
	

	public void close() {
		File file;
		for(int i = 0; i < 4;i++) {
			file = new File("resources/savedStates/ss" + i + ".json");
			if(file.exists()) {
				file.delete();
			}
		}	
	}

}
