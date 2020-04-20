package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import simulator.control.Controller;
import simulator.events.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;


public class ControlPanel extends JPanel implements TrafficSimObserver{

	private static final long serialVersionUID = 1L;
	
	private Controller _ctrl;
	private RoadMap _map;
	private int _time;
	
	private boolean _stopped;
	
	private JToolBar toolBar;
	private JFileChooser jsonChooser;
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
		_ctrl.addObserver(this);
		_stopped = true;
		this.setLayout(new BorderLayout());
		toolBar = new JToolBar();
		jsonChooser = new JFileChooser();

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
		cargaFicheros.setIcon(new ImageIcon("resources/icons/open.png"));


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
		cambiarClaseCont.setIcon(new ImageIcon("resources/icons/co2class.png"));
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
		cambiarWeather.setIcon(new ImageIcon("resources/icons/weather.png"));

		//TicksSpinner
		JLabel ticksText = new JLabel("Ticks: ");
		SpinnerNumberModel ticksSpinnerNM = new SpinnerNumberModel(1, 1, 1000, 1);
		ticksSpinner = new JSpinner(ticksSpinnerNM);
		int w = ticksSpinner.getWidth();   
		int h = ticksSpinner.getHeight();
		Dimension d = new Dimension(w / 10, h/10);
		ticksSpinner.setPreferredSize(d);
		//NO DEJA CAMBIAR TAMAÑO

		//RunButton
		runButton = new JButton();
		runButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setEnabled(false);
				_stopped = false;
				run_sim(ticksSpinnerNM.getNumber().intValue());
			}
		});
		runButton.setIcon(new ImageIcon("resources/icons/run.png"));
		runButton.setVisible(true);

		//StopButton
		stopButton = new JButton();
		stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				stop();
			}
		});
		stopButton.setIcon(new ImageIcon("resources/icons/stop.png"));
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
		exitButton.setIcon(new ImageIcon("resources/icons/exit.png"));
		exitButton.setVisible(true);
		


		toolBar.add(cargaFicheros);
		toolBar.addSeparator();
		toolBar.add(cambiarClaseCont);
		toolBar.add(cambiarWeather);
		toolBar.addSeparator();
		toolBar.add(runButton);
		toolBar.add(stopButton);
		toolBar.addSeparator();
		toolBar.add(ticksText);
		toolBar.add(ticksSpinner);
		toolBar.add(Box.createHorizontalGlue());
		toolBar.add(exitButton);
		this.add(toolBar, BorderLayout.CENTER);




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
				enableToolBar(false);
				_ctrl.run(1);
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						run_sim(n - 1);
					}
				});
			} catch (Exception e​) {
				// ​TODO​ show error message
				_stopped = true;
				return;
			}
			
		} else {
			enableToolBar(true);
			_stopped = true;
		}
	}

	private void stop() {
		_stopped = true;
	}

	private void cargaFicherosPulsado() {
		
		FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON files", "json");
		jsonChooser.setFileFilter(filter);
		int returnVal = jsonChooser.showOpenDialog(this);
		try {
			if(returnVal != JFileChooser.APPROVE_OPTION)
				throw new FileNotFoundException();
			_ctrl.reset();
			_ctrl.loadEvents(new FileInputStream(jsonChooser.getSelectedFile()));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Error retrieving events from file: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void cambiarClaseContPulsado() {
		new ClaseContDialog(_ctrl, _map.getVehicles(), _time);
	}

	private void cambiarWeatherPulsado() {
		new ChangeWeatherDialog(_ctrl, _map.getRoads(), _time);

	}

	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		_map = map;
		_time = time;
	}
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		_map = map;
		_time = time;
	}
	public void onEventAdded(RoadMap map, List<Event> events​, Event e, int time) {
		_map = map;
		_time = time;
	}
	public void onReset(RoadMap map, List<Event> events​, int time) {
		enableToolBar(true);
		_map = map;
		_time = time;
	}
	public void onRegister(RoadMap map, List<Event> events, int time) {
		_map = map;
		_time = time;
	}
	public void onError(String err​) {
		_stopped = true;
		enableToolBar(true);
	}
}
