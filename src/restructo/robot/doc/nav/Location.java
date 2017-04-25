package restructo.robot.doc.nav;

import restructo.robot.doc.Component;

public class Location {
	
	private int line;
	private Component component;
	
	public Location(Component component,int line){
		this.component = component;
		this.line = line;
	}

	public int getLine() {
		return line;
	}

	public Component getComponent() {
		return component;
	}

}
