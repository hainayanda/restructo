package restructo.robot.doc;

import restructo.robot.doc.nav.Position;

public class VarCaller implements Caller<Variable> {
	
	private Variable origin;
	private Position position;
	
	public VarCaller(Variable origin, Position position){
		this.origin = origin;
		this.position = position;
	}
	
	@Override
	public String getName() {
		return this.origin.getName();
	}

	@Override
	public Variable getOrigin() {
		return this.origin;
	}

	@Override
	public Position getPosition() {
		return this.position;
	}

}
