package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetContaminationClass;

public class SetContClassEventBuilder extends Builder<Event>{

	public SetContClassEventBuilder() {
		super("set_cont_class");
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		Event swe = null;
		try {
			int time = data.getInt("time");
			JSONArray info_list = data.getJSONArray("info");
			List<Pair<String, Integer>> cs = new ArrayList<Pair<String, Integer>>();
			for(int i = 0; i<info_list.length();i++) {
				String vehicle = info_list.getJSONObject(i).getString("vehicle");
				int contClass = info_list.getJSONObject(i).getInt("class");
				cs.add(new Pair<String, Integer>(vehicle, contClass));
			}
			
			swe = new SetContaminationClass(time, cs);
		} catch (Exception e) {
			System.out.println("Problem parsing JSONObject for SetContClassEvent" + e.getMessage());
		}
		return swe;
	}

}
