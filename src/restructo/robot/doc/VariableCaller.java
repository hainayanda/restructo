package restructo.robot.doc;

import restructo.robot.doc.nav.Location;

public class VariableCaller extends Caller {
	
	public VariableCaller(Variable origin, Location callerLocation) {
		super(origin, callerLocation);
		
	}
	
	public String getName(){
		return this.getOrigin().getName();
	}
	
	public String getValue(){
		return ((Variable)this.getOrigin()).getValue();
	}

}
