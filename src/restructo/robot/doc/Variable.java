package restructo.robot.doc;

public class Variable extends Component {
	
	private String value;
	
	public Variable(String name, String[] body, String value) {
		super(name, body);
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
