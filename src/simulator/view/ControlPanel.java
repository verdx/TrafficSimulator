package simulator.view;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import simulator.control.Controller;
import simulator.events.Event;
import simulator.events.SetContClassEvent;
import simulator.events.SetWeatherEvent;
import simulator.misc.Pair;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.Weather;

public class ControlPanel extends JPanel implements TrafficSimObserver{
	
	private static final long serialVersionUID = 1L;
	private Controller _ctrl;
	private boolean _stopped;
	private JButton cargaFicheros;
	private JButton cambiarClaseCont;
	private JButton cambiarWeather;
	private JSpinner ticksSpinner;
	private JButton runButton;
	private JButton stopButton;
	private JButton exitButton;
	
	public ControlPanel(Controller ctrl) {
		super();
		_ctrl = ctrl;
		_stopped = true;
		
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		//Carga de ficheros
		cargaFicheros = new JButton();
		cargaFicheros.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						cargaFicherosPulsado();
					}
				});
			}	
		});
		cargaFicheros.setBounds(0, 0, 50, 50);
		cargaFicheros.setIcon(new ImageIcon("icons/open.png"));
		
		
		cargaFicheros.setVisible(true);
		
		//Cambio clase de contaminacion
		cambiarClaseCont = new JButton();
		cambiarClaseCont.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						cambiarClaseContPulsado();
					}
				});
			}
		});
		cambiarClaseCont.setIcon(new ImageIcon("icons/co2class.png"));
		cambiarClaseCont.setVisible(true);
		
		//Cambiar weather de una carretera
		cambiarWeather = new JButton();
		cambiarWeather.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						cambiarWeatherPulsado();
					}
				});
			}	
		});
		cambiarWeather.setVisible(true);
		
		//TicksSpinner
		SpinnerNumberModel ticksSpinnerNM = new SpinnerNumberModel(1, 1, 1000, 1);
		ticksSpinner = new JSpinner(ticksSpinnerNM);
		
		//RunButton
		runButton = new JButton();
		runButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setEnabled(false);
				run_sim(ticksSpinnerNM.getNumber().intValue());
			}
		});
		runButton.setVisible(true);
		
		//StopButton
		stopButton = new JButton();
		stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				stop();
			}
		});
		stopButton.setVisible(true);
		
		//ExitProgram
		exitButton = new JButton();
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int response = JOptionPane.showConfirmDialog(null, "Do you want to exit?", "Confirm",
				        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				    if (response == JOptionPane.YES_OPTION) {
				      System.exit(0);
				    }
			}
		});
		exitButton.setVisible(true);
		

		this.add(cargaFicheros);
		this.add(cambiarClaseCont);
		this.add(cambiarWeather);
		this.add(runButton);
		this.add(stopButton);
		this.add(ticksSpinner);
		
		
		
	}		

	private void enableToolBar(boolean bool) {
		cargaFicheros.setEnabled(bool);
		cambiarClaseCont.setEnabled(bool);
		cambiarWeather.setEnabled(bool);
		ticksSpinner.setEnabled(bool);
		runButton.setEnabled(bool);
	}
	
	private void run_sim(int n) {
		if (n > 0 && !_stopped) {
			try {
				_ctrl.run(1);
				} catch (Exception e​) {
					// ​TODO​ show error message
					_stopped = true;
					return;
					}
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						run_sim(n - 1);
						}
				});
			} else {
				enableToolBar(true);
				_stopped = true;
			}
		}
	
	private void stop() {
		_stopped = true;
		}
	
	private void cargaFicherosPulsado() {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON files", ".json");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(this);
		try {
			if(returnVal != JFileChooser.APPROVE_OPTION)
				throw new FileNotFoundException();
			_ctrl.reset();
			_ctrl.loadEvents(new FileInputStream(chooser.getSelectedFile()));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Error retrieving events from file: " + e.getMessage());
		}
	}
	
	private void cambiarClaseContPulsado() {
		JFrame frame = new JFrame("Change CO2 Class");
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		frame.setContentPane(mainPanel);
		
		JLabel explanationText = new JLabel("Schedule an event to change the CO2 class of a vehicle after a given number of simulation ticks from now.");
		frame.add(explanationText);
		
		JPanel middlePanel = new JPanel();
		middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.X_AXIS));
		
		JLabel vehicleText = new JLabel("Vehicle: ");
		middlePanel.add(vehicleText);
		
		SpinnerListModel vehicleSpinnerLM = new SpinnerListModel(_ctrl.getVehicles());
		JSpinner vehicleSpinner = new JSpinner(vehicleSpinnerLM);
		middlePanel.add(vehicleSpinner);
		
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
		
		frame.add(middlePanel);
		
		JPanel lowerPanel = new JPanel();
		lowerPanel.setLayout(new BoxLayout(lowerPanel, BoxLayout.X_AXIS));
		
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		lowerPanel.add(cancel);
		
		JButton ok = new JButton("Ok");
		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String vehicle = ((Vehicle) vehicleSpinnerLM.getValue()).getId();
				int Co2Class = CO2ClassSpinnerNM.getNumber().intValue();
				int ticks = TicksSpinnerNM.getNumber().intValue();
				List<Pair<String, Integer>> cs = new ArrayList<>();
				cs.add(new Pair<String, Integer>(vehicle, Co2Class));
				try {
					Event event = new SetContClassEvent(_ctrl.getTime() + ticks, cs);
					_ctrl.addEvent(event);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		lowerPanel.add(ok);
		
		frame.add(lowerPanel);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		
	}
	
	private void cambiarWeatherPulsado() {
		JFrame frame = new JFrame("Change Road Weather");
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		frame.setContentPane(mainPanel);
		
		JLabel explanationText = new JLabel("Schedule an event to change the weather of a road after a given number of simulation ticks from now.");
		frame.add(explanationText);
		
		JPanel middlePanel = new JPanel();
		middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.X_AXIS));
		
		JLabel roadText = new JLabel("Road: ");
		middlePanel.add(roadText);
		
		SpinnerListModel roadSpinnerLM = new SpinnerListModel(_ctrl.getRoads());
		JSpinner roadSpinner = new JSpinner(roadSpinnerLM);
		middlePanel.add(roadSpinner);
		
		JLabel weatherText = new JLabel("Weather: ");
		middlePanel.add(weatherText);
		
		SpinnerListModel weatherSpinnerNM = new SpinnerListModel(Weather.values());
		JSpinner weatherSpinner = new JSpinner(weatherSpinnerNM);
		middlePanel.add(weatherSpinner);
		
		JLabel TicksText = new JLabel("Ticks: ");
		middlePanel.add(TicksText);
		
		SpinnerNumberModel TicksSpinnerNM = new SpinnerNumberModel(1,1,100,1);
		JSpinner TicksSpinner = new JSpinner(TicksSpinnerNM);
		middlePanel.add(TicksSpinner);
		
		frame.add(middlePanel);
		
		JPanel lowerPanel = new JPanel();
		lowerPanel.setLayout(new BoxLayout(lowerPanel, BoxLayout.X_AXIS));
		
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		lowerPanel.add(cancel);
		
		JButton ok = new JButton("Ok");
		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String road = ((Road) roadSpinnerLM.getValue()).getId();
				Weather weather = (Weather) weatherSpinnerNM.getValue();
				int ticks = TicksSpinnerNM.getNumber().intValue();
				List<Pair<String, Weather>> ws = new ArrayList<>();
				ws.add(new Pair<String, Weather>(road, weather));
				try {
					Event event = new SetWeatherEvent(ticks, ws);
					_ctrl.addEvent(event);
				} catch (Exception e1) {
					e1.printStackTrace();
				} finally {
					frame.dispose();
				}
			}
		});
		lowerPanel.add(ok);
		
		frame.add(lowerPanel);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		
	}
	
	public void onAdvanceStart(RoadMap map​, List<Event> events, int time​) {}
	public void onAdvanceEnd(RoadMap map​, List<Event> events, int time​) {}
	public void onEventAdded(RoadMap map​, List<Event> events​, Event e, int time​) {}
	public void onReset(RoadMap map​, List<Event> events​, int time​) {
		enableToolBar(true);
	}
	public void onRegister(RoadMap map​, List<Event> events, int time​) {}
	public void onError(String err​) {
		_stopped = true;
		enableToolBar(true);
	}

}
