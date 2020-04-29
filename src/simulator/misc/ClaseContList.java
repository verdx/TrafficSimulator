package simulator.misc;

import java.util.ArrayList;

public class ClaseContList extends ArrayList<Integer> {

	private static final long serialVersionUID = 1L;

	public ClaseContList() {
		super();
		for(int i = 0; i < 10; i++) {
			this.add(i);
		}
	}

}
