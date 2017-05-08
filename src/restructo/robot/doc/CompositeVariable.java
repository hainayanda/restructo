package restructo.robot.doc;

public class CompositeVariable {

	private RobotDoc origin;
	private String name;
	private Variable[] members;
	
	public CompositeVariable(RobotDoc origin, String name, Variable[] members){
		this.origin = origin;
		this.name = name;
		this.members = members;
	}
	
	public RobotDoc getOrigin() {
		return origin;
	}

	public void setOrigin(RobotDoc origin) {
		this.origin = origin;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Variable[] getMembers() {
		return members;
	}

	public void setMembers(Variable[] members) {
		this.members = members;
	}
	
	public Variable getMember(String memberName){
		for(int i = 0; i < this.members.length; i++){
			if(this.members[i].getName().equals(memberName)){
				return this.members[i];
			}
		}
		return null;
	}

}
