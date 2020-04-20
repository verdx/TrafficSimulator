package simulator.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.events.Event;
import simulator.model.Junction;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class JunctionsTable extends AbstractTableModel implements TrafficSimObserver{

	private static final long serialVersionUID = 1L;


	private List<Junction> _junctions;
	private String[] _colNames = {"Id","Green", "Queues"};


	public JunctionsTable(Controller ctrl) {
		ctrl.addObserver(this);
	}

	public void update() {
		fireTableDataChanged();	
	}

	public void setJunctionsList(List<Junction> junctions) {
		_junctions = junctions;
		update();
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	@Override
	public String getColumnName(int col) {
		return _colNames[col];
	}

	@Override
	public int getColumnCount() {
		return _colNames.length;
	}

	@Override
	public int getRowCount() {
		return _junctions == null ? 0 : _junctions.size();
	}

	@Override
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object s = null;
		Junction j = _junctions.get(rowIndex);
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

	public void onAdvanceStart(RoadMap map, List<Event> events, int time​) {
		setJunctionsList(map.getJunctions());
	}
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time​) {
		setJunctionsList(map.getJunctions());
	}
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time​) {}
	public void onReset(RoadMap map, List<Event> events, int time​) {
		setJunctionsList(map.getJunctions());
	}
	public void onRegister(RoadMap map, List<Event> events, int time​) {
		setJunctionsList(map.getJunctions());
	}
	public void onError(String error) {}
}
