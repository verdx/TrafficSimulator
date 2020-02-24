package simulator.model;

public abstract class NewRoadEvent extends Event{
	
	protected String id;
	protected String destJunc, srcJunc;
	protected int length;
	protected int maxSpeed;
	protected int speedLimit;
	protected int contLimit;
	protected Weather weather;
	protected int contTotal;
	
	NewRoadEvent(int time, String id, String srcJunc, String destJunc, int maxSpeed, int contLimit, 
			int length, Weather weather) {
		super(time);
		this.id = id;
		this.destJunc = destJunc;
		this.srcJunc = srcJunc;
		this.length = length;
		this.maxSpeed = maxSpeed;
		this.contLimit = contLimit;
		this.weather = weather;
	}
}
