package simulator.events;

import simulator.model.DequeuingStrategy;
import simulator.model.Junction;
import simulator.model.JunctionCreationException;
import simulator.model.LightSwitchingStrategy;
import simulator.model.RoadMap;

public class NewJunctionEvent extends Event {
	
	private String id;
	private LightSwitchingStrategy lsStrategy;
	private DequeuingStrategy dqStrategy;
	private int xCoor, yCoor;

	public NewJunctionEvent(int time, String id, LightSwitchingStrategy lsStrategy, 
			DequeuingStrategy dqStrategy, int xCoor, int yCoor){
		super(time);
		this.id = id;
		this.lsStrategy = lsStrategy;
		this.dqStrategy = dqStrategy;
		this.xCoor = xCoor;
		this.yCoor = yCoor;

	}

	@Override
	public void execute(RoadMap map) {
		Junction junction;
		try {
			junction = new Junction(id, lsStrategy, dqStrategy, xCoor, yCoor);
			map.addJunction(junction);
		} catch (JunctionCreationException e) {
			System.out.println("Problem executing NewJunctionEvent" + e.getMessage());
		}

	}
	
	@Override
	public String toString() {
		return "New Junction '" + id + "'"; 
	}

}