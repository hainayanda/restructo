package restructo.robot.doc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import restructo.robot.context.*;

public class RobotDoc {
	
	private File document;
	private List<RobotDoc> resources = new ArrayList<>();
	private List<String> settings = new ArrayList<>();
	private List<TestCase> testCases = new ArrayList<>();
	private WorkspaceContext workspace;
	
	public RobotDoc(WorkspaceContext workspace, File document){
		this.workspace = workspace;
		this.document = document;
	}
	
	public void addKeyword(Keyword keyword){
		keyword.setOrigin(this);
	}
	
	public void addVariable(Variable variable){
		variable.setOrigin(this);
	}
	
	public File getDocument() {
		return document;
	}

	public void setDocument(File document) {
		this.document = document;
	}

	public RobotDoc[] getResources() {
		return (RobotDoc[]) resources.toArray();
	}

	public void setResources(List<RobotDoc> resources) {
		this.resources = resources;
	}

	public String[] getSettings() {
		return (String[]) settings.toArray();
	}

	public void setSettings(List<String> settings) {
		this.settings = settings;
	}

	public TestCase[] getTestCases() {
		return (TestCase[]) testCases.toArray();
	}

	public void setTestCases(List<TestCase> testCases) {
		this.testCases = testCases;
	}

	public WorkspaceContext getWorkspace() {
		return workspace;
	}

	public void setWorkspace(WorkspaceContext workspace) {
		this.workspace = workspace;
	}
	
	
}
