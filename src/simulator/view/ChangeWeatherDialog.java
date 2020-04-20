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
import simulator.events.SetWeatherEvent;
import simulator.misc.Pair;
import simulator.model.Road;
import simulator.model.Weather;

public class ChangeWeatherDialog extends JDialog{

	private static final long serialVersionUID = 1L;

	public ChangeWeatherDialog(Controller _ctrl, List<Road> roads, int _time) {
		super();
		this.setTitle("Change Road Weather");

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		this.setContentPane(mainPanel);

		JLabel explanationText = new JLabel("Schedule an event to change the weather of a road after a given number of simulation ticks from now.");
		this.add(explanationText);

		JPanel middlePanel = new JPanel();
		middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.X_AXIS));

		JLabel roadText = new JLabel("Road: ");
		middlePanel.add(roadText);

		JComboBox<Road> roadDropList = new JComboBox<Road>();
		for(Road r: roads) {
			roadDropList.addItem(r);
		}
		middlePanel.add(roadDropList);

		JLabel weatherText = new JLabel("Weather: ");
		middlePanel.add(weatherText);

		//SpinnerListModel weatherSpinnerNM = new SpinnerListModel(Weather.values());
		//JSpinner weatherSpinner = new JSpinner(weatherSpinnerNM);
		//middlePanel.add(weatherSpinner);
		JComboBox<Weather> weatherDropList = new JComboBox<Weather>();
		for(Weather w: Weather.values()) {
			weatherDropList.addItem(w);
		}
		middlePanel.add(weatherDropList);

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
				Object road = roadDropList.getSelectedItem();
				Object weatherObj = weatherDropList.getSelectedItem();
				if (road == null ) { 
					JOptionPane.showMessageDialog(null, "No Road selected!");	
				} else if(weatherObj == null) {
					JOptionPane.showMessageDialog(null, "No Weather selected!");
				} else {
					String roadId = ((Road) road).getId();
					Weather weather = (Weather) weatherObj;
					int ticks = TicksSpinnerNM.getNumber().intValue();
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
		});
		lowerPanel.add(ok);

		this.add(lowerPanel);

		this.pack();
		this.setVisible(true);
	}
}
