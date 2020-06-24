package simulator.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.events.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class CO2TableModel extends AbstractTableModel implements TrafficSimObserver{

	private String[] _colNames = {"Tick", "Roads"};
	private List<Map<String, Integer>> tableMap;
	private int limit = 0;

	private static final long serialVersionUID = 1L;
	
	public CO2TableModel(Controller _ctrl) {
		_ctrl.addObserver(this);
		tableMap = new ArrayList<Map<String, Integer>>();
	}
	
	public void update(int limit) {
		this.limit = limit;
		fireTableDataChanged();
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
		return tableMap.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

		Object s = null;
		switch (columnIndex) {
		case 0:
			s = rowIndex;
			break;
		case 1:
			List<String> list = new ArrayList<String>();
			for(String str: tableMap.get(rowIndex).keySet()) {
				if(tableMap.get(rowIndex).get(str) > limit) {
					list.add(str);
				}
			}
			s = list;
			break;
		}
		return s;
	}
	
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
	}
	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		Map<String, Integer> co2Map = new HashMap<String, Integer>();
		for(Road r: map.getRoads()) {
			co2Map.put(r.getId(), r.getTotalCO2());
		}
		tableMap.add(co2Map);
	}
	@Override
	public void onEventAdded(RoadMap map, List<Event> events​, Event e, int time) {
	}
	@Override
	public void onReset(RoadMap map, List<Event> events​, int time) {
		tableMap.clear();
	}
	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
	}
	@Override
	public void onError(String err​) {
	}
	
}

