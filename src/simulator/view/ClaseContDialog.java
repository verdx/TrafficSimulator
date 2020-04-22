package simulator.view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
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
import javax.swing.border.EmptyBorder;

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

		//Set Location and Size
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int width = 800;
		int height = 150;
		int x = (dim.width-width)/2;
		int y = (dim.height-height)/2;
		this.setLocation(x, y);
		this.setPreferredSize(new Dimension(width, height));

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		this.setContentPane(mainPanel);

		//TOP PANEL
		JPanel topPanel = new JPanel(new GridLayout());
		JLabel explanationText = new JLabel("Schedule an event to change the CO2 class of a vehicle after a given number of simulation ticks from now.");
		explanationText.setPreferredSize(new Dimension(width, height/3));
		topPanel.add(explanationText);
		topPanel.setBorder(new EmptyBorder(10,10,10,10));

		//MIDDLE PANEL
		JPanel middlePanel = new JPanel();
		middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.X_AXIS));
		middlePanel.setPreferredSize(new Dimension(width, height/3));
		middlePanel.setBorder(new EmptyBorder(10,10,10,10));

		//Vehicle
		JLabel vehicleText = new JLabel("Vehicle: ");
		middlePanel.add(vehicleText);

		JComboBox<Vehicle> vehicleDropList = new JComboBox<Vehicle>();
		for(Vehicle v: vehicles) {
			vehicleDropList.addItem(v);
		}
		vehicleDropList.setMaximumSize(new Dimension(width, 30));
		middlePanel.add(vehicleDropList);
		
		//CO2 Class
		JLabel CO2ClassText = new JLabel("CO2 Class: ");
		middlePanel.add(CO2ClassText);

		SpinnerNumberModel CO2ClassSpinnerNM = new SpinnerNumberModel(0,0,10,1);
		JSpinner CO2ClassSpinner = new JSpinner(CO2ClassSpinnerNM);
		CO2ClassSpinner.setMaximumSize(new Dimension(width, 30));
		middlePanel.add(CO2ClassSpinner);

		//Ticks
		JLabel TicksText = new JLabel("Ticks: ");
		middlePanel.add(TicksText);

		SpinnerNumberModel ticksSpinnerNM = new SpinnerNumberModel(1,1,100,1);
		JSpinner ticksSpinner = new JSpinner(ticksSpinnerNM);
		ticksSpinner.setMaximumSize(new Dimension(width, 30));
		middlePanel.add(ticksSpinner);


		//LOWER PANEL
		JPanel lowerPanelContainer = new JPanel();
		lowerPanelContainer.setLayout(new GridLayout(1, 2, 10, 10));
		lowerPanelContainer.setMaximumSize(new Dimension(width/3, height/3));
		lowerPanelContainer.setBorder(new EmptyBorder(10,10,10,10));

		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		cancel.setPreferredSize(new Dimension(height/3-20, width/6-10));
		lowerPanelContainer.add(cancel);

		JButton ok = new JButton("Ok");
		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object vehicle = vehicleDropList.getSelectedItem();
				if(vehicle != null ) {
					String vehicleId = ((Vehicle) vehicle).getId();
					int Co2Class = CO2ClassSpinnerNM.getNumber().intValue();
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
		});
		ok.setPreferredSize(new Dimension(height/3-20, width/6-10));
		lowerPanelContainer.add(ok);
		
		JPanel lowerPanel = new JPanel();
		lowerPanel.setLayout(new BoxLayout(lowerPanel, BoxLayout.X_AXIS));
		lowerPanel.add(lowerPanelContainer);

		this.add(topPanel);
		this.add(middlePanel);
		this.add(lowerPanel);
		
		this.pack();
		this.setVisible(true);
	
	}
}
