package restructo.robot.doc;

import restructo.helper.Util;

public abstract class Function {
	private RobotDoc origin;
	private String name;
	private String[] body;
	private KeyCaller[] keyCallers = new KeyCaller[0];
	private VarCaller[] varCallers = new VarCaller[0];
	
	public Function(RobotDoc origin, String name, String[] body){
		this.origin = origin;
		this.name = name;
		this.body = body;
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

	public String[] getBody() {
		return body;
	}

	public KeyCaller[] getKeyCallers() {
		return keyCallers;
	}

	public void setKeyCallers(KeyCaller[] keyCallers) {
		this.keyCallers = keyCallers;
	}

	public VarCaller[] getVarCallers() {
		return varCallers;
	}

	public void setVarCallers(VarCaller[] varCallers) {
		this.varCallers = varCallers;
	}
	
	public void addKeyCaller(KeyCaller keyCaller){
		this.keyCallers = (KeyCaller[]) Util.addToArray(keyCaller, this.keyCallers);
	}
	
	public void addVarCaller(VarCaller caller){
		this.varCallers = (VarCaller[]) Util.addToArray(caller, this.varCallers);
	}
}
