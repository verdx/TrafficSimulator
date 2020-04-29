package simulator.view;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public class StatusBar extends JPanel{
	
	
	private static final long serialVersionUID = 1L;
	private JTextArea ticksText;
	private JTextArea eventsText;
	
	public StatusBar() {
		super();
		
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		ticksText = new JTextArea();
		ticksText.setSize(20, this.getHeight());
		ticksText.setText("0");
		
		eventsText = new JTextArea();
		eventsText.setText("Welcome to TrafficSimulator!");
		
		this.add(ticksText);
		this.add(new JSeparator(SwingConstants.VERTICAL));
		this.add(eventsText);
	}
	
	void actualizarTime(String time) {
		ticksText.setText(time);
	}
	
	void actualizarEvents(String events) {
		eventsText.setText(events);
	}
}
