package restructo.robot.doc;

public class Component {
	
	private String name;
	private String[] body;
	private RobotDoc origin;
	
	public Component(String name, String[] body){
		this.name = name;
		this.body = body;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getBody() {
		return body;
	}

	public void setBody(String[] body) {
		this.body = body;
	}

	public RobotDoc getOrigin() {
		return origin;
	}

	public void setOrigin(RobotDoc origin) {
		this.origin = origin;
	}
	
	
}
