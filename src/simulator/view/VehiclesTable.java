package simulator.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.events.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;

public class VehiclesTable extends AbstractTableModel implements TrafficSimObserver{


	private static final long serialVersionUID = 1L;


	private List<Vehicle> _vehicles;
	private String[] _colNames = {"Id", "Location", "Itinerary", 
			"CO2 Class", "Max. Speed", "Speed", "Total CO2", "Distance"};


	public VehiclesTable(Controller ctrl) {
		ctrl.addObserver(this);
	}

	public void update() {
		fireTableDataChanged();	
	}

	public void setVehiclesList(List<Vehicle> vehicles) {
		_vehicles = vehicles;
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
		return _vehicles == null ? 0 : _vehicles.size();
	}

	@Override
	// método obligatorio
	// así es como se va a cargar la tabla desde el ArrayList
	// el índice del arrayList es el número de fila pq en este ejemplo
	// quiero enumerarlos.
	//
	// returns the value of a particular cell 
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object s = null;
		Vehicle v = _vehicles.get(rowIndex);
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

	public void onAdvanceStart(RoadMap map​, List<Event> events, int time​) {}
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time​) {
		setVehiclesList(map.getVehicles());
	}
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time​) {}
	public void onReset(RoadMap map, List<Event> events, int time​) {
		setVehiclesList(map.getVehicles());
	}
	public void onRegister(RoadMap map​, List<Event> events, int time​) {}
	public void onError(String error) {}
}

