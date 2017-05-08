package restructo.robot.doc;

import restructo.robot.doc.nav.Location;

public abstract class Caller<T> {
	
	private Location location;
	private T Origin;

	public abstract String getName();

	public Location getLocation() {
		return location;
	}

	protected void setLocation(Location location) {
		this.location = location;
	}

	public T getOrigin() {
		return Origin;
	}

	protected void setOrigin(T origin) {
		Origin = origin;
	}

}
