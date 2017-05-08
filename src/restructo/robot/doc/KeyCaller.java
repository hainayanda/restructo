package restructo.robot.doc;

import restructo.robot.doc.nav.Location;

public class KeyCaller extends Caller<Keyword> {

	public KeyCaller(Keyword origin, Location location) {
		this.setOrigin(origin);
		this.setLocation(location);
	}

	@Override
	public String getName() {
		return this.getOrigin().getName();
	}

}
