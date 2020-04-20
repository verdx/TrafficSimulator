package simulator.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import simulator.control.Controller;
import simulator.events.Event;
import simulator.events.SetContClassEvent;
import simulator.misc.Pair;
import simulator.model.Vehicle;

public class ClaseContDialog extends JDialog{

	private static final long serialVersionUID = 1L;

	public ClaseContDialog(Controller _ctrl, List<Vehicle> vehicles, int _time) {
		super();
		this.setTitle("Change CO2 Class");
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		this.setContentPane(mainPanel);

		JLabel explanationText = new JLabel("Schedule an event to change the CO2 class of a vehicle after a given number of simulation ticks from now.");
		this.add(explanationText);

		JPanel middlePanel = new JPanel();
		middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.X_AXIS));

		JLabel vehicleText = new JLabel("Vehicle: ");
		middlePanel.add(vehicleText);

		JComboBox<Vehicle> vehicleDropList = new JComboBox<Vehicle>();
		for(Vehicle v: vehicles) {
			vehicleDropList.addItem(v);
		}
		middlePanel.add(vehicleDropList);
		

		JLabel CO2ClassText = new JLabel("CO2 Class: ");
		middlePanel.add(CO2ClassText);

		SpinnerNumberModel CO2ClassSpinnerNM = new SpinnerNumberModel(0,0,10,1);
		JSpinner CO2ClassSpinner = new JSpinner(CO2ClassSpinnerNM);
		middlePanel.add(CO2ClassSpinner);

		JLabel TicksText = new JLabel("Ticks: ");
		middlePanel.add(TicksText);

		SpinnerNumberModel TicksSpinnerNM = new SpinnerNumberModel(1,1,100,1);
		JSpinner TicksSpinner = new JSpinner(TicksSpinnerNM);
		middlePanel.add(TicksSpinner);

		this.add(middlePanel);

		JPanel lowerPanel = new JPanel();
		lowerPanel.setLayout(new BoxLayout(lowerPanel, BoxLayout.X_AXIS));

		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		lowerPanel.add(cancel);

		JButton ok = new JButton("Ok");
		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object vehicle = vehicleDropList.getSelectedItem();
				if(vehicle != null ) {
					String vehicleId = ((Vehicle) vehicle).getId();
					int Co2Class = CO2ClassSpinnerNM.getNumber().intValue();
					int ticks = TicksSpinnerNM.getNumber().intValue();
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
					JOptionPane.showMessageDialog(null, "No JComboBox<Vehicle> vehicleDropList = new JComboBox<Vehicle>();\n" + 
							"		for(Vehicle v: vehicles) {\n" + 
							"			vehicleDropList.addItem(v);\n" + 
							"		}ehicle selected!");
				}
			}
		});
		lowerPanel.add(ok);

		this.add(lowerPanel);
		
		this.pack();
		this.setVisible(true);
	
	}
}
