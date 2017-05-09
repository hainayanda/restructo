package restructo.robot.doc;

public class Keyword extends Function {
	
	private String[] args;
	private String ret;
	
	public Keyword(RobotDoc origin, String name, String[] body, String[] args, String ret) {
		super(origin, name, body);
		this.args = args;
		this.ret = ret;
	}

	public String[] getArgs() {
		return args;
	}
	
	public void setArgs(String[] args) {
		this.args = args;
	}

	public String getReturnValue() {
		return ret;
	}

	public void setReturnValue(String ret) {
		this.ret = ret;
	}
	
	
}
