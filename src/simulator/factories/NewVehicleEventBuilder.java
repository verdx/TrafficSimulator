package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewVehicleEvent;

public class NewVehicleEventBuilder extends Builder<Event>{

	NewVehicleEventBuilder() {
		super("new_vehicle");
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		Event ve = null;
		try {
			int time = data.getInt("time");
			String id = data.getString("id");
			int maxSpeed = data.getInt("maxspeed");
			int contClass = data.getInt("class");
			JSONArray itinerary_ja = data.getJSONArray("itinerary");
			List<String> itinerary = new ArrayList<String>();
			for(int i = 0; i<itinerary_ja.length(); i++) {
				itinerary.add(itinerary_ja.getString(i));
			}
			ve = new NewVehicleEvent(time, id, maxSpeed, contClass, itinerary);
		}catch (JSONException e) {
			System.out.println("Problem parsing JSONObject for NewVehicleEvent" + e.getMessage());
		}
		return ve;
	}

}
