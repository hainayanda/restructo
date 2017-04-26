package restructo.robot.doc;

import restructo.robot.doc.nav.Position;

public class KeyCaller implements Caller<Keyword> {
	
	private Keyword origin;
	private Position position;
	
	public KeyCaller(Keyword origin, Position position){
		this.origin = origin;
		this.position = position;
	}
	
	@Override
	public String getName() {
		return this.origin.getName();
	}

	@Override
	public Keyword getOrigin() {
		return this.origin;
	}

	@Override
	public Position getPosition() {
		return this.position;
	}

}
