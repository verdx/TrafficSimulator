package simulator.factories;

import org.json.JSONException;
import org.json.JSONObject;

import simulator.model.LightSwitchingStrategy;
import simulator.model.MostCrowdedStrategy;

public class MostCrowdedStrategyBuilder extends Builder<LightSwitchingStrategy>{

	public MostCrowdedStrategyBuilder() {
		super("most_crowded_lss");
	}

	@Override
	protected LightSwitchingStrategy createTheInstance(JSONObject data) {
		LightSwitchingStrategy mcs;
		if(data == null) {
			mcs = new MostCrowdedStrategy(1);
		} else {
			try {
				mcs = new MostCrowdedStrategy(data.getInt("timeslot"));
			} catch (JSONException e) {
				System.out.println("Problem parsing LightSwitchingStrategy JSON: " + e.getMessage());
				mcs = new MostCrowdedStrategy(1);
			}
		}
		return mcs;
	}

}
