package restructo.robot.doc;

import restructo.robot.doc.nav.Location;

public class CompVarCaller extends Caller<CompositeVariable> {
	
	public Variable nestedOrigin;
	
	public CompVarCaller(CompositeVariable compositeVar, Variable nestedVar, Location location){
		this.setOrigin(compositeVar);
		this.nestedOrigin = nestedVar;
		this.setLocation(location);
	}
	
	@Override
	public String getName() {
		return this.getOrigin().getName() + "." + this.nestedOrigin.getName();
	}
	
	public Variable getVariable(){
		return nestedOrigin;
	}
	
}
