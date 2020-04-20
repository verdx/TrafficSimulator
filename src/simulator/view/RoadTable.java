package simulator.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.events.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class RoadTable extends AbstractTableModel implements TrafficSimObserver{

	private static final long serialVersionUID = 1L;


	private List<Road> _roads;
	private String[] _colNames = {"Id", "Length", "Weather", 
			"Max. Speed", "Speed Limit", "Total CO2", "CO2 Limit"};


	public RoadTable(Controller ctrl) {
		ctrl.addObserver(this);
	}

	public void update() {
		fireTableDataChanged();	
	}

	public void setRoadsList(List<Road> roads) {
		_roads = roads;
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
		return _roads == null ? 0 : _roads.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object s = null;
		Road r = _roads.get(rowIndex);
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

	public void onAdvanceStart(RoadMap map​, List<Event> events, int time​) {}
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time​) {
		setRoadsList(map.getRoads());
	}
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time​) {}
	public void onReset(RoadMap map, List<Event> events, int time​) {
		setRoadsList(map.getRoads());
	}
	public void onRegister(RoadMap map​, List<Event> events, int time​) {}
	public void onError(String error) {}

}
