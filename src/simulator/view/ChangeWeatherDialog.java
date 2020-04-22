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
import simulator.events.SetWeatherEvent;
import simulator.misc.Pair;
import simulator.model.Road;
import simulator.model.Weather;

public class ChangeWeatherDialog extends JDialog{

	private static final long serialVersionUID = 1L;

	public ChangeWeatherDialog(Controller _ctrl, List<Road> roads, int _time) {
		super();
		this.setTitle("Change Road Weather");

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
		JLabel explanationText = new JLabel("Schedule an event to change the weather of a road after a given number of simulation ticks from now.");
		explanationText.setPreferredSize(new Dimension(width, height/3));
		topPanel.add(explanationText);
		topPanel.setBorder(new EmptyBorder(10,10,10,10));

		
		//MIDDLE PANEL
		JPanel middlePanel = new JPanel();
		middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.X_AXIS));
		middlePanel.setPreferredSize(new Dimension(width, height/3));
		middlePanel.setBorder(new EmptyBorder(10,10,10,10));

		//Road
		JLabel roadText = new JLabel("Road: ");
		middlePanel.add(roadText);

		JComboBox<Road> roadDropList = new JComboBox<Road>();
		for(Road r: roads) {
			roadDropList.addItem(r);
		}
		roadDropList.setMaximumSize(new Dimension(width, 30));
		middlePanel.add(roadDropList);

		//Weather
		JLabel weatherText = new JLabel("Weather: ");
		middlePanel.add(weatherText);

		JComboBox<Weather> weatherDropList = new JComboBox<Weather>();
		for(Weather w: Weather.values()) {
			weatherDropList.addItem(w);
		}
		weatherDropList.setMaximumSize(new Dimension(width, 30));
		middlePanel.add(weatherDropList);

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
				Object road = roadDropList.getSelectedItem();
				Object weatherObj = weatherDropList.getSelectedItem();
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
