package restructo.robot.doc;

import java.io.File;

import restructo.robot.context.WorkspaceContext;

public class RobotDoc {
	
	private WorkspaceContext workspace;
	private File file;
	private String[] settings; //this is without resource
	private RobotDoc[] resources;
	private String[] body;
	
	public RobotDoc(File file) {
		this.file = file;
	}
	
	public static RobotDoc[] parseRobotDocWithResources(File file){
		//need implementation
		return null;
	}
	
	public String getName(){
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
	
	public WorkspaceContext getWorkspace() {
		return workspace;
	}

	public void setWorkspace(WorkspaceContext workspace) {
		this.workspace.removeDocument(this);
		//need implementation to remove all test case, keywords and variable to workspace
		this.workspace = workspace;
		//need implementation to add all test case, keywords and variable to new workspace
	}
	
	public Variable[] getVariables(){
		//need implementation
		return null;
	}
	
	public Keyword[] getKeywords(){
		//need implementation
		return null;
	}
	
	public TestCase[] getTestCases(){
		//need implementation
		return null;
	}
	
}
