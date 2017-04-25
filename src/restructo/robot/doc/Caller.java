package restructo.robot.doc;

import restructo.robot.doc.nav.Location;

public class Caller {
	
	private Component origin;
	private Location callerLocation;
	
	public Caller(Component origin, Location callerLocation){
		this.origin = origin;
		this.callerLocation = callerLocation;
	}

	public Component getOrigin() {
		return origin;
	}

	public void setOrigin(Component origin) {
		this.origin = origin;
	}

	public Location getCallerLocation() {
		return callerLocation;
	}

	public void setCallerLocation(Location callerLocation) {
		this.callerLocation = callerLocation;
	}
	
	
}
