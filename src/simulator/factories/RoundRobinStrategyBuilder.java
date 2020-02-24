package simulator.factories;

import org.json.JSONException;
import org.json.JSONObject;

import simulator.model.LightSwitchingStrategy;
import simulator.model.RoundRobinStrategy;

public class RoundRobinStrategyBuilder extends Builder<LightSwitchingStrategy>{

	public RoundRobinStrategyBuilder() {
		super("round_robin_lss");
	}

	@Override
	protected LightSwitchingStrategy createTheInstance(JSONObject data) {
		LightSwitchingStrategy rrs;
		if(data == null) {
			rrs = new RoundRobinStrategy(1);
		} else {
			try {
				rrs = new RoundRobinStrategy(data.getInt("timeslot"));
			} catch (JSONException e) {
				System.out.println("Problem parsing RoundRobinStrategy json: " + e.getMessage());
				rrs = new RoundRobinStrategy(1);
			}
		}
		return rrs;
	}

}
