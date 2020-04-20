package simulator.view;

import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import simulator.control.Controller;
import simulator.events.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class StatusBar extends JPanel implements TrafficSimObserver{
	
	
	private static final long serialVersionUID = 1L;
	private Controller _ctrl;
	private JTextArea ticksText;
	private JTextArea eventsText;
	
	public StatusBar(Controller ctrl) {
		super();
		_ctrl = ctrl;
		_ctrl.addObserver(this);
		
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		ticksText = new JTextArea();
		ticksText.setSize(20, this.getHeight());
		
		eventsText = new JTextArea();
		
		this.add(ticksText);
		this.add(new JSeparator(SwingConstants.VERTICAL));
		this.add(eventsText);
	}
	
	public void onAdvanceStart(RoadMap map​, List<Event> events, int time) {
		ticksText.setText("Time: " + time);
		eventsText.setText("");
	}
	public void onAdvanceEnd(RoadMap map​, List<Event> events, int time​) {}
	public void onEventAdded(RoadMap map​, List<Event> events​, Event e, int time​) {
		eventsText.setText( "Event added: " + e.toString());
	}
	public void onReset(RoadMap map​, List<Event> events​, int time​) {
		ticksText.setText("");
		eventsText.setText("");
	}
	public void onRegister(RoadMap map​, List<Event> events, int time​) {}
	public void onError(String err​) {
		eventsText.setText(err​);
	}

}
