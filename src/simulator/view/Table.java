package simulator.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.events.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public abstract class Table<T> extends AbstractTableModel implements TrafficSimObserver{

	private static final long serialVersionUID = 1L;
	
	protected List<T> _contents;
	private String[] _colNames;
	
	public Table(Controller ctrl, String[] colNames) {
		ctrl.addObserver(this);
		_colNames = colNames;
	}
	
	public Table() {};

	public void update() {
		fireTableDataChanged();	
	}

	public abstract void setContentsList(RoadMap map, List<Event> events);

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
		return _contents == null ? 0 : _contents.size();
	}

	@Override
	public abstract Object getValueAt(int rowIndex, int columnIndex);
	
	@Override
	public void onAdvanceStart(RoadMap map​, List<Event> events, int time​) {}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time​) {
		setContentsList(map, events);
	}
	
	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time​) {
		setContentsList(map, events);
	}
	
	@Override
	public void onReset(RoadMap map, List<Event> events, int time​) {
		setContentsList(map, events);
	}
	
	@Override
	public void onRegister(RoadMap map​, List<Event> events, int time​) {}

	@Override
	public void onError(String error) {}
}
