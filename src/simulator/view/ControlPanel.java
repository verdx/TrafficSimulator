package simulator.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import simulator.control.Controller;
import simulator.events.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class ControlPanel extends JPanel implements TrafficSimObserver{
	
	private Controller _ctrl;
	
	public ControlPanel(Controller ctrl) {
		super();
		_ctrl = ctrl;
		
		JButton cargaFicheros = new JButton();
		this.add(cargaFicheros);
		cargaFicheros.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				cargaFicherosPulsado();
			}	
		});
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
	
	public void onAdvanceStart(RoadMap map​, List<Event> events, int time​) {}
	public void onAdvanceEnd(RoadMap map​, List<Event> events, int time​) {}
	public void onEventAdded(RoadMap map​, List<Event> events​, Event e, int time​) {}
	public void onReset(RoadMap map​, List<Event> events​, int time​) {}
	public void onRegister(RoadMap map​, List<Event> events, int time​) {}
	public void onError(String err​) {}

}
