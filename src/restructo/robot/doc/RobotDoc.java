package restructo.robot.doc;

import java.io.File;

public class RobotDoc {

	private File file;
	private String[] settings; //this is without resource
	private RobotDoc[] resources;
	private String[] body;
	
	public RobotDoc(File file) {
		this.file = file;
	}
	
	public static RobotDoc parseRobotDoc(File file){
		//need implementation
		return null;
	}
	
	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String[] getSettings() {
		return settings;
	}

	public void setSettings(String[] settings) {
		this.settings = settings;
	}

	public RobotDoc[] getResources() {
		return resources;
	}

	public void setResources(RobotDoc[] resources) {
		this.resources = resources;
	}

	public void addTestCase(TestCase testCase) {
		testCase.setOrigin(this);
	}

	public void addKeyword(Keyword keyword) {
		keyword.setOrigin(this);
	}

	public void addVariable(Variable variable) {
		variable.setOrigin(this);
	}

	public String[] getBody() {
		return body;
	}

}
