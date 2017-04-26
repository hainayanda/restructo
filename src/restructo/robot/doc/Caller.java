package restructo.robot.doc;

import restructo.robot.doc.nav.Position;

public interface Caller<T> {
	
	public String getName();
	public T getOrigin();
	public Position getPosition();
	
}
