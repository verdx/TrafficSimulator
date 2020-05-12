package simulator.view;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;

import simulator.control.Controller;
import simulator.events.Event;
import simulator.events.SetWeatherEvent;
import simulator.misc.Pair;
import simulator.model.Road;
import simulator.model.Weather;

public class ChangeWeatherDialog extends CustomDialog{

	private static final long serialVersionUID = 1L;

	public ChangeWeatherDialog(MainWindow mw, Controller _ctrl, List<Road> roads, int _time) {
		super(mw, _ctrl, roads, new ArrayList<>(Arrays.asList(Weather.values())), _time, "Road", "Weather", "Schedule an event to change the weather of a road after a given number of simulation ticks from now.");
	}

	@Override
	public void okActionPerformed(ActionEvent e) {
		Object road = list1DropList.getSelectedItem();
		Object weatherObj = list2DropList.getSelectedItem();
		if (road == null ) { 
			JOptionPane.showMessageDialog(null, "No Road selected!");	
		} else if(weatherObj == null) {
			JOptionPane.showMessageDialog(null, "No Weather selected!");
		} else {
			String roadId = ((Road) road).getId();
			Weather weather = (Weather) weatherObj;
			int ticks = ticksSpinnerNM.getNumber().intValue();
			List<Pair<String, Weather>> ws = new ArrayList<>();
			ws.add(new Pair<String, Weather>(roadId, weather));
			try {
				Event event = new SetWeatherEvent(_time + ticks, ws);
				_ctrl.addEvent(event);
			} catch (Exception e1) {
				e1.printStackTrace();
			} finally {
				dispose();
			}

		}
	}

}

