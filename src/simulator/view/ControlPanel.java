package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import simulator.control.Controller;
import simulator.model.RoadMap;


public class ControlPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	
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
	private JButton resetButton;
	private JButton undoButton;
	private JButton redoButton;
	private MainWindow _mw;
	
	

	public ControlPanel(Controller ctrl, MainWindow mw) {
		super();
		_mw = mw;
		
		initGUI();

	}		
	
	private void initGUI() {
		this.setLayout(new BorderLayout());
		toolBar = new JToolBar();
		toolBar.setBorder(new EmptyBorder(5, 5, 5, 5));

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
		ticksSpinner.setMaximumSize(new Dimension(100, 20));

		//RunButton
		runButton = new JButton();
		runButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						_mw.runSimGeneral();
					}
				});
			}
		});
		runButton.setIcon(new ImageIcon("resources/icons/run.png"));
		runButton.setVisible(true);

		//StopButton
		stopButton = new JButton();
		stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						_mw.stopSim();
					}
				});
			}
		});
		stopButton.setIcon(new ImageIcon("resources/icons/stop.png"));
		stopButton.setVisible(true);
		
		//UndoButton
		undoButton = new JButton();
		undoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						_mw.undo();
					}
				});
			}
		});
		undoButton.setIcon(new ImageIcon("resources/icons/undo.png"));
		undoButton.setVisible(true);

		//UndoButton
		redoButton = new JButton();
		redoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						_mw.redo();
					}
				});
			}
		});
		redoButton.setIcon(new ImageIcon("resources/icons/redo.png"));
		redoButton.setVisible(true);
		redoButton.setEnabled(false);

		//ResetButton
		resetButton = new JButton();
		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						_mw.reset();
					}
				});
			}
		});
		resetButton.setIcon(new ImageIcon("resources/icons/reset.png"));
		resetButton.setVisible(true);

		//ExitProgram
		exitButton = new JButton();
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				_mw.close();
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
		toolBar.add(undoButton);
		toolBar.add(redoButton);
		toolBar.addSeparator();
		toolBar.add(ticksText);
		toolBar.add(ticksSpinner);
		toolBar.addSeparator();
		toolBar.add(resetButton);
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
		resetButton.setEnabled(bool);
		undoButton.setEnabled(bool);
	}

	int getTicks() {
		return ticksSpinnerNM.getNumber().intValue();
	}
	
	void actualizar(RoadMap map, int time) {
		_map = map;
		_time = time;
	}

	public void enableRedo(boolean bool) {
		redoButton.setEnabled(bool);
	}

	
}
