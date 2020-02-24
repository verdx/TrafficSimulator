package simulator.model;

import java.util.Comparator;

public class EventComparator implements Comparator<Event>{

	@Override
	public int compare(Event arg0, Event arg1) {
		int resul;
		if(arg0._time == arg1._time) {
			resul = 0;
		} else if (arg0._time > arg1._time) {
			resul = 1;
		} else {
			resul = -1;
		}
		return resul;
	}

}
