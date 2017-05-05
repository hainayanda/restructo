package restructo.robot.doc;

import restructo.robot.doc.nav.Position;

public abstract class Caller<T> {
	
	private Position position;
	private T Origin;

	public abstract String getName();

	public Position getPosition() {
		return position;
	}

	protected void setPosition(Position position) {
		this.position = position;
	}

	public T getOrigin() {
		return Origin;
	}

	protected void setOrigin(T origin) {
		Origin = origin;
	}

}
