package restructo.robot.doc;

import java.util.LinkedList;
import java.util.List;

import restructo.helper.Util;

public abstract class Function {
	private RobotDoc origin;
	private String name;
	private String[] body;
	private List<KeyCaller> keyCallers = new LinkedList<>();
	private List<VarCaller> varCallers = new LinkedList<>();
	private List<CompVarCaller> compVarCallers = new LinkedList<>();
	
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
		return keyCallers.toArray(new KeyCaller[keyCallers.size()]);
	}

	public void setKeyCallers(KeyCaller[] keyCallers) {
		this.keyCallers = Util.arrayToLinkedList(keyCallers);
	}

	public VarCaller[] getVarCallers() {
		return varCallers.toArray(new VarCaller[varCallers.size()]);
	}

	public void setVarCallers(VarCaller[] varCallers) {
		this.varCallers = Util.arrayToLinkedList(varCallers);
	}
	
	public void addKeyCaller(KeyCaller keyCaller){
		this.keyCallers.add(keyCaller);
	}
	
	public void addVarCaller(VarCaller caller){
		this.varCallers.add(caller);
	}

	public void addCompVarCaller(CompVarCaller caller) {
		this.compVarCallers.add(caller);
		
	}
}
