package restructo.robot.doc;

public class Variable {

	private RobotDoc origin;
	private String name;
	private String value;
	
	public Variable(RobotDoc origin, String name, String value){
		this.origin = origin;
		this.name = name;
		this.value = value;
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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
}
