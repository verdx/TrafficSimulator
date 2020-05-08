package simulator.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

import simulator.control.Controller;
import simulator.events.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class TopMenuBar extends JMenuBar implements TrafficSimObserver{
	
	private static final long serialVersionUID = 1L;
	
	private Controller _ctrl;
	private MainWindow _mw;
	private RoadMap _map;
	private int _time;
	
	public TopMenuBar(Controller ctrl, MainWindow mw) {
		super();
		_ctrl = ctrl;
		_mw = mw;
		_ctrl.addObserver(this);
		
		initGUI();

	}		
	
	private void initGUI() {
		createMenuBar();
	}
	
	private void createMenuBar() {
		
		//FILE Category
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		
		//Save as 
		JMenuItem saveMenuItem = new JMenuItem("Save as...");
		saveMenuItem.setMnemonic(KeyEvent.VK_S);
		saveMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						_mw.save();
					}
				});
			}
		});

		//Save as 
		JMenuItem loadMenuItem = new JMenuItem("Load File");
		loadMenuItem.setMnemonic(KeyEvent.VK_S);
		loadMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						_mw.load();
					}
				});
			}
		});

		//ADD Category
		JMenu addMenu = new JMenu("Add");
		addMenu.setMnemonic(KeyEvent.VK_A);

		//Add file
		JMenuItem fileMenuItem = new JMenuItem("Events File");
		fileMenuItem.setMnemonic(KeyEvent.VK_F);
		fileMenuItem.setToolTipText("Add a json file");
		fileMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						_mw.cargaFicherosPulsado();
					}
				});
			}
        });
        
      //Add change CO2 Event
        JMenuItem CO2ClassMenuItem = new JMenuItem("Change CO2 Class Event");
        CO2ClassMenuItem.setMnemonic(KeyEvent.VK_W);
        CO2ClassMenuItem.setToolTipText("Add new Change CO2 Class Event");
        CO2ClassMenuItem.addActionListener(new ActionListener() {
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
        
        //Add change weather event
        JMenuItem weatherMenuItem = new JMenuItem("Change Weather Event");
        weatherMenuItem.setMnemonic(KeyEvent.VK_W);
        weatherMenuItem.setToolTipText("Add new Change Weather Event");
        weatherMenuItem.addActionListener(new ActionListener() {
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
        
        //Simulator
        JMenu simMenu = new JMenu("Simulator");
        
        
        //Run
        JMenuItem runMenu = new JMenuItem("Run");
        runMenu.setMnemonic(KeyEvent.VK_ENTER);
        runMenu.setToolTipText("Run the simulation");
        runMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							_mw.runSimGeneral();
						}
					});
			}
        });
        
        //Stop
        JMenuItem stopMenu = new JMenuItem("Stop");
        stopMenu.setMnemonic(KeyEvent.VK_K);
        stopMenu.setToolTipText("Stop the simulation");
        stopMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						_mw.stopSim();
					}
				});
			}
        });
        
        
        

        
        fileMenu.add(saveMenuItem);
        fileMenu.add(loadMenuItem);
        addMenu.add(fileMenuItem);
        addMenu.add(CO2ClassMenuItem);
        addMenu.add(weatherMenuItem);
        simMenu.add(runMenu);
        simMenu.add(stopMenu);
        this.add(fileMenu);
        this.add(addMenu);
        this.add(simMenu);

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
	}

	@Override
	public void onSave() {}

	@Override
	public void onLoad(RoadMap map, List<Event> event, int time) {}

}
