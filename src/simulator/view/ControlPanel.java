package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import simulator.control.Controller;
import simulator.events.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;


public class ControlPanel extends JPanel implements TrafficSimObserver{

	private static final long serialVersionUID = 1L;
	
	private Controller _ctrl;
	private RoadMap _map;
	private int _time;
	
	private JToolBar toolBar;
	private JButton cargaFicheros;
	private JButton cambiarClaseCont;
	private JButton cambiarWeather;
	private SpinnerNumberModel ticksSpinnerNM;
	private JSpinner ticksSpinner;
	private JButton runButton;
	private JButton stopButton;
	private JButton exitButton;
	private MainWindow _mw;
	
	

	public ControlPanel(Controller ctrl, MainWindow mw) {
		super();
		_ctrl = ctrl;
		_mw = mw;
		_ctrl.addObserver(this);
		
		initGUI();

	}		
	
	private void initGUI() {
		this.setLayout(new BorderLayout());
		toolBar = new JToolBar();

		//Carga de ficheros
		cargaFicheros = new JButton();
		cargaFicheros.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						_mw.cargaFicherosPulsado();
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
						_mw.cambiarClaseContPulsado(_map.getVehicles(), _time);
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
						_mw.cambiarWeatherPulsado(_map.getRoads(), _time);
					}
				});
			}	
		});
		cambiarWeather.setVisible(true);
		cambiarWeather.setIcon(new ImageIcon("resources/icons/weather.png"));

		//TicksSpinner
		JLabel ticksText = new JLabel("Ticks: ");
		ticksSpinnerNM = new SpinnerNumberModel(1, 1, 1000, 1);
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
				_mw.runSimGeneral();
			}
		});
		runButton.setIcon(new ImageIcon("resources/icons/run.png"));
		runButton.setVisible(true);

		//StopButton
		stopButton = new JButton();
		stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_mw.stop();
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

	void enableToolBar(boolean bool) {
		cargaFicheros.setEnabled(bool);
		cambiarClaseCont.setEnabled(bool);
		cambiarWeather.setEnabled(bool);
		ticksSpinner.setEnabled(bool);
		runButton.setEnabled(bool);
	}

	int getTicks() {
		return ticksSpinnerNM.getNumber().intValue();
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		_map = map;
		_time = time;
	}
	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		_map = map;
		_time = time;
	}
	@Override
	public void onEventAdded(RoadMap map, List<Event> events​, Event e, int time) {
		_map = map;
		_time = time;
	}
	@Override
	public void onReset(RoadMap map, List<Event> events​, int time) {
		enableToolBar(true);
		_map = map;
		_time = time;
	}
	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		_map = map;
		_time = time;
	}
	@Override
	public void onError(String err​) {
		_mw.stop();
		enableToolBar(true);
	}
}
