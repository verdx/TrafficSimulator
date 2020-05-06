package simulator.events;

import org.json.JSONObject;

import simulator.model.RoadMap;

public abstract class Event{

	protected int _time;

	Event(int time) {
		if (time < 1)
			throw new IllegalArgumentException("Time must be positive (" + time + ")");
		else
			_time = time;
	}

	public int getTime() {
		return _time;
	}

	public abstract void execute(RoadMap map);

	public abstract JSONObject save();
}
