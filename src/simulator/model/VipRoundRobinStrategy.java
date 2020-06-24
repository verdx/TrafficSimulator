package simulator.model;

import java.util.List;

public class VipRoundRobinStrategy implements LightSwitchingStrategy {

	private int timeSlot;
	private String viptag;

	public VipRoundRobinStrategy(String viptag, int timeSlot) {
		this.timeSlot = timeSlot;
		this.viptag = viptag;
	}

	@Override
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime,
			int currTime) {


		if(roads.size() > 0) {
			boolean encontradovip = false;
			int i;
			int limite;
			if(currGreen  == -1) {
				i = 0;
				limite = 0;
			} else {
				i = currGreen;
				limite = currGreen;
			}


			//La primera vuelta esta fuera porque si no la comparacion
			//i != limite no deja entrar al bucle
			int j = 0;
			while(j < qs.get(i).size() && !encontradovip) {
				if(qs.get(i).get(j).getId().endsWith(viptag)) {
					encontradovip = true;
				} else {
					j++;
				}
			}
			if(!encontradovip)	{
				i = (i + 1) % roads.size();
				while(i != limite  && !encontradovip) {
					j = 0;
					while(j < qs.get(i).size() && !encontradovip) {
						if(qs.get(i).get(j).getId().endsWith(viptag)) {
							encontradovip = true;
						} else {
							j++;
						}
					}
					if(!encontradovip)	
						i = (i + 1) % roads.size();
				}
			}
			if(encontradovip)
				return i;

		}


		if (roads.size() == 0) {
			return -1;
		} else if (currGreen == -1) {
			return 0;
		} else if (currTime-lastSwitchingTime < timeSlot) {
			return currGreen;
		} else {
			return ((currGreen + 1)%roads.size());
		}
	}
}

