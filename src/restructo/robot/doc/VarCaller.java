package restructo.robot.doc;

import restructo.robot.doc.nav.Position;

public class VarCaller extends Caller<Variable> {

	public VarCaller(Variable origin, Position position) {
		this.setOrigin(origin);
		this.setPosition(position);
	}

	@Override
	public String getName() {
		return this.getOrigin().getName();
	}

}
