package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.json.JSONObject;

import simulator.control.Controller;
import simulator.events.Event;
import simulator.model.Junction;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;

public class MainWindow extends JFrame implements TrafficSimObserver{

	private static final long serialVersionUID = 1L;
	private Controller _ctrl;
	private JFileChooser jsonChooser;
	private ControlPanel contPanel;
	private StatusBar statusBar;
	private boolean _stopped;

	public MainWindow(Controller ctrl) {
		super("Traffic Simulator");
		_ctrl = ctrl;
		initGui();
		_ctrl.addObserver(this);
		jsonChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON files", "json");
		jsonChooser.setFileFilter(filter);
		_stopped = true;
	}

	private void initGui() {
		
		 
		JPanel mainPanel = new JPanel(new BorderLayout());
		this.setContentPane(mainPanel);
		this.setLocation(200, 100);
		
		this.setJMenuBar(new TopMenuBar(_ctrl, this));
		contPanel = new ControlPanel(_ctrl, this);
		mainPanel.add(contPanel, BorderLayout.PAGE_START);
		statusBar = new StatusBar();
		mainPanel.add(statusBar, BorderLayout.PAGE_END);

		JPanel viewsPanel = new JPanel(new GridLayout(1,2));
		mainPanel.add(viewsPanel, BorderLayout.CENTER);

		JPanel tablesPanel = new JPanel();
		tablesPanel.setLayout(new BoxLayout(tablesPanel, BoxLayout.Y_AXIS));
		viewsPanel.add(tablesPanel);

		JPanel mapsPanel = new JPanel();
		mapsPanel.setLayout(new BoxLayout(mapsPanel, BoxLayout.Y_AXIS));
		viewsPanel.add(mapsPanel);

		//tables
		JPanel eventsView = createViewPanel(new JTable(createEventsTable()), "Events");
		eventsView.setPreferredSize(new Dimension(500, 200));
		tablesPanel.add(eventsView);
		
		JPanel vehiclesView = createViewPanel(new JTable(createVehiclesTable()), "Vehicles");
		vehiclesView.setPreferredSize(new Dimension(500, 200));
		tablesPanel.add(vehiclesView);
		
		JPanel roadsView = createViewPanel(new JTable(createRoadsTable()), "Roads");
		roadsView.setPreferredSize(new Dimension(500,200));
		tablesPanel.add(roadsView);
		
		JPanel junctionsView = createViewPanel(new JTable(createJunctionsTable()), "Junctions");
		junctionsView.setPreferredSize(new Dimension(500,200));
		tablesPanel.add(junctionsView);

		//maps
		JPanel mapView = createViewPanel(new MapComponent(_ctrl), "Map");
		mapView.setPreferredSize(new Dimension(500, 400));
		mapsPanel.add(mapView);
		
		JPanel mapByRoadView = createViewPanel(new MapByRoadComponent(_ctrl), "Map By Road");
		mapsPanel.add(mapByRoadView);

		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
		    public void windowClosing(WindowEvent e) {
		       close();
		    
			}
		});
		this.pack();
		this.setVisible(true);
	}
	
	private TableModel<Event> createEventsTable() {
		String[] colNames = {"Time", "Description" };
		TableModel<Event> eventsTable = new TableModel<Event>(_ctrl, colNames) {

			private static final long serialVersionUID = 1L;

			@Override
			public void setContentsList(RoadMap map, List<Event> events) {
				_contents = events;
				update();	
			}

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				Object s = null;
				switch (columnIndex) {
				case 0:
					s = _contents.get(rowIndex).getTime();
					break;
				case 1:
					s = _contents.get(rowIndex).toString();
					break;
				}
				return s;
			}
			
		};
		eventsTable.setContentsList(null, _ctrl.getEvents());
		return eventsTable;
	}
	
	private TableModel<Vehicle> createVehiclesTable() {
		String[] colNames  = {"Id", "Location", "Itinerary", 
				"CO2 Class", "Max. Speed", "Speed", "Total CO2", "Distance"};
		TableModel<Vehicle> vehiclesTable = new TableModel<Vehicle>(_ctrl, colNames) {

			private static final long serialVersionUID = 1L;

			@Override
			public void setContentsList(RoadMap map, List<Event> events) {
				_contents = map.getVehicles();
				update();	
			}

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				Object s = null;
				Vehicle v = _contents.get(rowIndex);
				switch (columnIndex) {
				case 0:
					s = v.getId();
					break;
				case 1:
					s = v.getRoad() + ":" + v.getLocation();
					break;
				case 2:
					s = v.getItinerary();
					break;
				case 3:
					s = v.getContClass();
					break;
				case 4:
					s = v.getMaxSpeed();
					break;
				case 5:
					s = v.getSpeed();
					break;
				case 6:
					s = v.getTotalCo2();
					break;
				case 7:
					s = v.getDistance();
					break;
				}
				return s;
			}
		};
		return vehiclesTable;
		
	}
	
	private TableModel<Junction> createJunctionsTable() {
		String[] colNames = {"Id","Green", "Queues"};
		TableModel<Junction> junctionsTable = new TableModel<Junction>(_ctrl, colNames) {

			private static final long serialVersionUID = 1L;

			@Override
			public void setContentsList(RoadMap map, List<Event> events) {
				_contents = map.getJunctions();
				update();	
			}

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				Object s = null;
				Junction j = _contents.get(rowIndex);
				switch (columnIndex) {
				case 0:
					s = j.getId();
					break;
				case 1:
					if(j.getGreenLightIndex() == -1) {
						s = "NONE";
					} else {
						s = j.getInRoads().get(j.getGreenLightIndex());	
					}
					break;
				case 2:
					s = j.getQueues();
					break;
				}
				return s;
			}
		};
		return junctionsTable;
	}
	
	private TableModel<Road> createRoadsTable() {
		String[] colNames = {"Id", "Length", "Weather", 
				"Max. Speed", "Speed Limit", "Total CO2", "CO2 Limit"};
		TableModel<Road> roadsTable = new TableModel<Road>(_ctrl, colNames) {

			private static final long serialVersionUID = 1L;

			@Override
			public void setContentsList(RoadMap map, List<Event> events) {
				_contents = map.getRoads();
				update();	
			}

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				Object s = null;
				Road r = _contents.get(rowIndex);
				switch (columnIndex) {
				case 0:
					s = r.getId();
					break;
				case 1:
					s = r.getLength();
					break;
				case 2:
					s = r.getWeather();
					break;
				case 3:
					s = r.getMaxSpeed();
					break;
				case 4:
					s = r.getSpeedLimit();
					break;
				case 5:
					s = r.getTotalCO2();
					break;
				case 6:
					s = r.getCO2Limit();
					break;
				}
				return s;
			}
			
		};
		return roadsTable;
	}
	
	
	

	private JPanel createViewPanel(JComponent c, String title) {
		JPanel p = new JPanel( new BorderLayout());
		p.setBorder(BorderFactory.createTitledBorder(title));
		p.add(new JScrollPane(c));
		return p;
	}
	
	void cargaFicherosPulsado() {
		
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

	void cambiarClaseContPulsado(List<Vehicle> vehicles, int time) {
		new ClaseContDialog(_ctrl, vehicles, time);
	}

	void cambiarWeatherPulsado(List<Road> roads, int time) {
		new ChangeWeatherDialog(_ctrl, roads, time);
	}
	
	void runSimGeneral() {
		_ctrl.saveState();
		_stopped = false;
		run_sim(contPanel.getTicks());
		
	}
	
	private void run_sim(int n) {
		if (n > 0 && !_stopped) {
			try {
				contPanel.enableToolBar(false);
				_ctrl.run(1);
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						run_sim(n - 1);
					}
				});
			} catch (Exception e) {
				System.err.print(e.getMessage());
				_stopped = true;
				return;
			}
			
		} else {
			contPanel.enableToolBar(true);
			_stopped = true;
		}
	}

	void stopSim() {
		_stopped = true;
	}
	
	void reset() {
		_ctrl.totalReset();
	}
	
	void save() {
		JSONObject jo = _ctrl.save();
		int retval = jsonChooser.showSaveDialog(this);
		if (retval == JFileChooser.APPROVE_OPTION) {
			File file = jsonChooser.getSelectedFile();
			if (file == null) {
				return;
			}
			if (!file.getName().toLowerCase().endsWith(".json")) {
				file = new File(file.getParentFile(), file.getName() + ".json");
			}
			try {
				@SuppressWarnings("resource")
				FileWriter fw = new FileWriter(file);
				fw.write(jo.toString(4));
				fw.flush();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(new JFrame(), "Problem saving: " + e.getMessage(), "Dialog",
				        JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	void load() {
		int returnVal = jsonChooser.showOpenDialog(this);
		try {
			if(returnVal != JFileChooser.APPROVE_OPTION)
				throw new FileNotFoundException();
			_ctrl.totalReset();
			_ctrl.load(new FileInputStream(jsonChooser.getSelectedFile()));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Error retrieving events from file: " + e.getMessage());
			e.printStackTrace();
		}
		
	}

	void close() {
		_ctrl.close();
		int response = JOptionPane.showConfirmDialog(null, "Do you want to exit?", "Confirm",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (response == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}
	
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		statusBar.actualizarTime("Time: " + time);
		statusBar.actualizarEvents("Running simulation");
		contPanel.enableRedo(_ctrl.canRedo()); 
	}
	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		contPanel.actualizar(map, time);
	}
	@Override
	public void onEventAdded(RoadMap map, List<Event> events​, Event e, int time) {
		contPanel.actualizar(map, time);
		statusBar.actualizarEvents("Event added: " + e.toString());
	}
	@Override
	public void onReset(RoadMap map, List<Event> events​, int time) {
		contPanel.enableToolBar(true);
		contPanel.actualizar(map, time);
		statusBar.actualizarTime("");
		statusBar.actualizarEvents("Simulator resetted");
		contPanel.enableRedo(_ctrl.canRedo());
		contPanel.enableUndo(_ctrl.canUndo());
	}
	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		contPanel.actualizar(map, time);
	}
	@Override
	public void onError(String err) {
		stopSim();
		contPanel.enableToolBar(true);
		statusBar.actualizarEvents(err);
	}

	@Override
	public void onSave() {
		statusBar.actualizarEvents("Juego guardado");
		
	}

	@Override
	public void onLoad(RoadMap map, List<Event> event, int time) {
		statusBar.actualizarEvents("Se ha cargado un juego");
		statusBar.actualizarTime("" + time);
	}

	public void undo() {
		_ctrl.undo();
	}

	public void redo() {
		_ctrl.redo();
	}

}
