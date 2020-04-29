package simulator.view;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import simulator.control.Controller;
import simulator.events.Event;
import simulator.events.SetContClassEvent;
import simulator.misc.Pair;
import simulator.model.Vehicle;

public class ClaseContDialog extends CustomDialog{

	private static final long serialVersionUID = 1L;

	public ClaseContDialog(Controller _ctrl, List<Vehicle> vehicles, int _time) {
		super(_ctrl, vehicles, new ClaseContList(), _time, "Vehicle", "CO2 Class", "Schedule an event to change the CO2 class of a vehicle after a given number of simulation ticks from now.");
		
	}

	@Override
	public void okActionPerformed(ActionEvent e) {
		Object vehicle = list1DropList.getSelectedItem();
		if(vehicle != null ) {
			String vehicleId = ((Vehicle) vehicle).getId();
			int Co2Class = (int) list2DropList.getSelectedItem();
			int ticks = ticksSpinnerNM.getNumber().intValue();
			List<Pair<String, Integer>> cs = new ArrayList<>();
			cs.add(new Pair<String, Integer>(vehicleId, Co2Class));
			try {
				Event event = new SetContClassEvent(_time + ticks, cs);
				_ctrl.addEvent(event);
			} catch (Exception e1) {
				e1.printStackTrace();
			} finally {
				dispose();
			}
		} else {
			JOptionPane.showMessageDialog(null, "No Vehicle selected!");
		}
		
	}
}
