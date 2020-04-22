package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

import simulator.control.Controller;
import simulator.model.Road;
import simulator.model.Vehicle;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private Controller _ctrl;
	private JFileChooser jsonChooser;
	private ControlPanel contPanel;
	private boolean _stopped;

	public MainWindow(Controller ctrl) {
		super("Traffic Simulator");
		_ctrl = ctrl;
		jsonChooser = new JFileChooser();
		_stopped = true;
		initGui();
	}

	private void initGui() {
		
		 
		JPanel mainPanel = new JPanel(new BorderLayout());
		this.setContentPane(mainPanel);
		this.setJMenuBar(new TopMenuBar(_ctrl, this));
		contPanel = new ControlPanel(_ctrl, this);
		mainPanel.add(contPanel, BorderLayout.PAGE_START);
		mainPanel.add(new StatusBar(_ctrl), BorderLayout.PAGE_END);

		JPanel viewsPanel = new JPanel(new GridLayout(1,2));
		mainPanel.add(viewsPanel, BorderLayout.CENTER);

		JPanel tablesPanel = new JPanel();
		tablesPanel.setLayout(new BoxLayout(tablesPanel, BoxLayout.Y_AXIS));
		viewsPanel.add(tablesPanel);

		JPanel mapsPanel = new JPanel();
		mapsPanel.setLayout(new BoxLayout(mapsPanel, BoxLayout.Y_AXIS));
		viewsPanel.add(mapsPanel);

		//tables
		JPanel eventsView = createViewPanel(new JTable(new EventsTable(_ctrl)), "Events");
		eventsView.setPreferredSize(new Dimension(500, 200));
		tablesPanel.add(eventsView);
		
		JPanel vehiclesView = createViewPanel(new JTable(new VehiclesTable(_ctrl)), "Vehicles");
		vehiclesView.setPreferredSize(new Dimension(500, 200));
		tablesPanel.add(vehiclesView);
		
		JPanel roadsView = createViewPanel(new JTable(new RoadTable(_ctrl)), "Roads");
		roadsView.setPreferredSize(new Dimension(500,200));
		tablesPanel.add(roadsView);
		
		JPanel junctionsView = createViewPanel(new JTable(new JunctionsTable(_ctrl)), "Junctions");
		junctionsView.setPreferredSize(new Dimension(500,200));
		tablesPanel.add(junctionsView);

		//maps
		JPanel mapView = createViewPanel(new MapComponent(_ctrl), "Map");
		mapView.setPreferredSize(new Dimension(500, 400));
		mapsPanel.add(mapView);
		
		JPanel mapByRoadView = createViewPanel(new MapByRoadComponent(_ctrl), "Map By Road");
		mapsPanel.add(mapByRoadView);

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}

	private JPanel createViewPanel(JComponent c, String title) {
		JPanel p = new JPanel( new BorderLayout());
		p.setBorder(BorderFactory.createTitledBorder(title));
		p.add(new JScrollPane(c));
		return p;
	}
	
	void cargaFicherosPulsado() {
		
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

	void cambiarClaseContPulsado(List<Vehicle> vehicles, int time) {
		new ClaseContDialog(_ctrl, vehicles, time);
	}

	void cambiarWeatherPulsado(List<Road> roads, int time) {
		new ChangeWeatherDialog(_ctrl, roads, time);
	}
	
	void runSimGeneral() {
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

}
