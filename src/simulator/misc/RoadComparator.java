package simulator.misc;

import java.util.Comparator;

import simulator.model.Road;

public class RoadComparator implements Comparator<Road>{

	@Override
	public int compare(Road arg0, Road arg1) {
		return arg0.getId().compareTo(arg1.getId());
	}

}
