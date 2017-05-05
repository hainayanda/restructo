package restructo.robot.doc;

import restructo.robot.doc.nav.Position;

public class CompVarCaller extends Caller<CompositeVariable> {
	
	public Variable nestedOrigin;
	
	public CompVarCaller(CompositeVariable compositeVar, Variable nestedVar, Position position){
		this.setOrigin(compositeVar);
		this.nestedOrigin = nestedVar;
		this.setPosition(position);
	}
	
	@Override
	public String getName() {
		return this.getOrigin().getName() + "." + this.nestedOrigin.getName();
	}
	
	public Variable getVariable(){
		return nestedOrigin;
	}
	
}
