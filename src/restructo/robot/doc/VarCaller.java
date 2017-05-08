package restructo.robot.doc;

import restructo.robot.doc.nav.Location;

public class VarCaller extends Caller<Variable> {

	public VarCaller(Variable origin, Location location) {
		this.setOrigin(origin);
		this.setLocation(location);
	}

	@Override
	public String getName() {
		return this.getOrigin().getName();
	}

}
