package restructo.robot.doc;

import restructo.robot.doc.nav.Position;

public class KeyCaller extends Caller<Keyword> {

	public KeyCaller(Keyword origin, Position position) {
		this.setOrigin(origin);
		this.setPosition(position);
	}

	@Override
	public String getName() {
		return this.getOrigin().getName();
	}

}
