package simulator.factories;

import org.json.JSONException;
import org.json.JSONObject;

import simulator.model.LightSwitchingStrategy;
import simulator.model.VipRoundRobinStrategy;

public class VipRoundRobinStrategyBuilder extends Builder<LightSwitchingStrategy>{
	public VipRoundRobinStrategyBuilder() {
		super("vip_lss");
	}

	@Override
	protected LightSwitchingStrategy createTheInstance(JSONObject data) {
		LightSwitchingStrategy rrs;
		if(data == null) {
			rrs = new VipRoundRobinStrategy("vip", 1);
		} else {
			int timeslot;
			String viptag;
			try {
				timeslot = data.getInt("timeslot");
			} catch (JSONException e) {
				System.out.println("Problem parsing VipRoundRobinStrategy json: " + e.getMessage());
				timeslot = 0;
			}
			try {
				viptag = data.getString("viptag");
			} catch (JSONException e) {
				System.out.println("Problem parsing VipRoundRobinStrategy json: " + e.getMessage());
				viptag = "vip";
			}
			rrs = new VipRoundRobinStrategy(viptag, timeslot);
			
		}
		return rrs;
	}
}
