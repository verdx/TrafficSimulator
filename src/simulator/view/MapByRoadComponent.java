package simulator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import simulator.control.Controller;
import simulator.events.Event;
import simulator.model.Junction;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;

public class MapByRoadComponent extends JComponent implements TrafficSimObserver{


	private static final long serialVersionUID = 1L;

	private static final int _JRADIUS = 10;
	private static final int _PADDING = 50;

	private static final Color _BG_COLOR = Color.WHITE;
	private static final Color _JUNCTION_COLOR = Color.BLUE;
	private static final Color _JUNCTION_LABEL_COLOR = new Color(200, 100, 0);
	private static final Color _GREEN_LIGHT_COLOR = Color.GREEN;
	private static final Color _RED_LIGHT_COLOR = Color.RED;
	private static final Color _ROAD_LABEL_COLOR = Color.BLACK;

	private RoadMap _map;

	private Image _car;

	MapByRoadComponent(Controller ctrl) {
		initGUI();
		ctrl.addObserver(this);
		this.setPreferredSize(new Dimension(300,200));
	}

	private void initGUI() {
		_car = loadImage("car.png");
	}

	@Override
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// clear with a background color
		g.setColor(_BG_COLOR);
		g.clearRect(0, 0, getWidth(), getHeight());

		if (_map == null || _map.getRoads().size() == 0) {
			g.setColor(Color.red);
			g.drawString("No map yet!", getWidth() / 2 - 50, getHeight() / 2);
		} else {
			updatePrefferedSize();
			drawMap(g);
		}
	}

	private void drawMap(Graphics g) {
		for (Road r : _map.getRoads()) {
			// the road goes from (x1,y1) to (x2,y2)
			int x1 = _PADDING;
			int x2 = getWidth()-100;
			int y = (_map.getRoads().indexOf(r) + 1) * 50;
			
			drawRoad(g, r , x1, x2, y);
			
			drawVehicles(g, r, x1, x2, y);
			
			drawWeatherCO2(g, r, x2, y);
		}
	}
	
	private void drawRoad(Graphics g, Road r, int x1, int x2, int y) {
		g.setColor(_ROAD_LABEL_COLOR);
		g.drawLine(x1, y, x2, y);
		
		// choose a color for the desetJunc circle depending on the traffic light of the road
		Color destColor = _RED_LIGHT_COLOR;
		int idx = r.getDestJunc().getGreenLightIndex();
		if (idx != -1 && r.equals(r.getDestJunc().getInRoads().get(idx))) {
			destColor = _GREEN_LIGHT_COLOR;
		}

		//Draw Junctions
		drawCenteredCircle(g, x1, y, _JRADIUS, _JUNCTION_COLOR);
		g.setColor(_JUNCTION_LABEL_COLOR);
		g.drawString(r.getSrcJunc().getId(), x1, y);
		
		
		drawCenteredCircle(g, x2, y, _JRADIUS, destColor);
		g.setColor(_JUNCTION_LABEL_COLOR);
		g.drawString(r.getDestJunc().getId(), x2, y);
		
		//Draw Identifier
		g.setColor(_ROAD_LABEL_COLOR);
		g.drawString(r.getId(), x1/2, y);
		
	}

	private void drawVehicles(Graphics g, Road r, int x1, int x2, int y) {
		//DrawVehicles
		for(Vehicle v: _map.getVehicles()) {
			if(v.getRoad().equals(r)) {
				int x = x1 + (int) ((x2 - x1) * ((double) v.getLocation()/ (double) r.getLength()));
				g.drawImage(_car, x, y, 12, 12, this);
				g.drawString(v.getId(), x, y);
			}
		}
	}
	
	private void drawWeatherCO2(Graphics g, Road r, int x2, int y) {
		
		//Draw Weather
		Image weather;
		switch(r.getWeather()) {
		case SUNNY:
			weather = loadImage("sun.png");
			break;
		case CLOUDY:
			weather = loadImage("cloud.png");
			break;
		case RAINY:
			weather = loadImage("rain.png");
			break;
		case WINDY:
			weather = loadImage("wind.png");
			break;
		case STORM:
			weather = loadImage("storm.png");
			break;
		default:
			weather = loadImage("sun.png");
			break;
		}
		g.drawImage(weather, x2 + 20, y - 16, 32, 32, this);
		
		//Draw CO2 Level
		int c = (int) Math.floor(Math.min(r.getTotalCO2()/ (1.0 + r.getCO2Limit()), 1.0) / 0.19);
		Image co2 = loadImage("cont_" + c + ".png");
		g.drawImage(co2, x2 + 72, y - 16, 32, 32 , this);
	}
	// this method is used to update the preffered and actual size of the component,
	// so when we draw outside the visible area the scrollbars show up
	private void updatePrefferedSize() {
		int maxW = 200;
		int maxH = 200;
		for (Junction j : _map.getJunctions()) {
			maxW = Math.max(maxW, j.getX());
			maxH = Math.max(maxH, j.getY());
		}
		maxW += 20;
		maxH += 20;
		setPreferredSize(new Dimension(maxW, maxH));
		setSize(new Dimension(maxW, maxH));
	}

	private void drawCenteredCircle(Graphics g, int x, int y, int r, Color c) {
		x = x - (r/2);
		y = y - (r/2);
		g.setColor(c);
		g.fillOval(x, y, r, r);
	}

	// loads an image from a file
	private Image loadImage(String img) {
		Image i = null;
		try {
			return ImageIO.read(new File("resources/icons/" + img));
		} catch (IOException e) {
		}
		return i;
	}

	public void update(RoadMap map) {
		_map = map;
		repaint();
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		update(map);
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onError(String err) {
	}

}
