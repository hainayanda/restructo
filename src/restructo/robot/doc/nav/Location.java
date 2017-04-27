package restructo.robot.doc.nav;

import restructo.robot.doc.Function;
import restructo.robot.doc.RobotDoc;

public class Location {

	private Function function;
	private Position position;

	public Location(Function function, Position position) {
		this.function = function;
		this.position = position;
	}
	
	public RobotDoc getDocument(){
		return this.function.getOrigin();
	}
	
	public Function getFunction() {
		return function;
	}

	public Position getPosition() {
		return position;
	}

}
