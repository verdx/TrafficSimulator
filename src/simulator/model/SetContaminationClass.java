package simulator.model;

import java.util.List;

import simulator.misc.Pair;

public class SetContaminationClass extends Event {
	
	private List<Pair<String, Integer>> cs;

	public SetContaminationClass(int time, List<Pair<String, Integer>> cs) throws Exception {
		super(time);
		if (cs == null) {
			throw new Exception("Problem creating SetContaminationClass:The argument cs is null.");
		} else {
			this.cs = cs;
		}
	}

	@Override
	void execute(RoadMap map) {
		try {
		for(Pair<String, Integer> w: cs) {
			if(map.getVehicle(w.getFirst()) == null) {
				throw new Exception("Problem executing SetContaminationClass: Vehicle " + w.getFirst() + " does not exist");
			} else {
				map.getVehicle(w.getFirst()).setContaminationClass(w.getSecond());
			}
		}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

}
