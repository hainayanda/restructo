package restructo.robot.doc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import restructo.robot.context.WorkspaceContext;

public class RobotDoc {

	private WorkspaceContext workspace;
	private File file;
	private String[] settings; // this is without resource
	private RobotDoc[] resources;
	private String[] body;

	public RobotDoc(File file, WorkspaceContext workspace) {
		this.file = file;
		this.workspace = workspace;
	}

	public static RobotDoc parseRobotDoc(File file, WorkspaceContext workspace) throws IOException {
		RobotDoc doc = new RobotDoc(file, workspace);
		doc.readFiles(file);
		doc.searchSettings(workspace);
		return doc;
	}

	private void searchSettings(WorkspaceContext workspace) throws IOException {
		boolean isInSettings = false;
		List<RobotDoc> resources = new LinkedList<>();
		List<String> settings = new LinkedList<>();
		for (int i = 0; i < this.body.length; i++) {
			String line = this.body[i];
			if (!isInSettings) {
				if (line.matches("^\\*\\*\\*+\\sSettings?\\s\\*\\*\\*")) {
					isInSettings = true;
				}
			} else {
				if (line.matches("^\\*\\*\\*+\\s.+\\s\\*\\*\\*")) {
					break;
				} else if (line.matches("^Resource\\s{2,}")) {
					String relativePath = line.replaceAll("\\s+$", "").split("\\s{2,}")[1];
					File resource = this.getFileFromRelativePath(relativePath);
					if (workspace.documentIsExist(resource)) {
						resources.add(workspace.getDocumentByFile(resource));
					} else {
						RobotDoc res = parseRobotDoc(resource, workspace);
						workspace.addDocument(res);
						resources.add(res);
					}
				} else if (!line.matches("^\\s*$")) {
					settings.add(line);
				}
			}
		}
	}

	private String getFullPathFromRelativePath(String filePath) {
		String thisFolderPath = this.file.getAbsolutePath().replaceAll("([^\\/\\\\])+$", "");
	    while (filePath.matches("^\\.\\.\\.?(\\/|\\\\)")) {
	        filePath = filePath.replaceAll("^\\.\\.\\.?(\\/|\\\\)", "");
	        thisFolderPath = thisFolderPath.replaceAll("([^\\/\\\\])+(\\/|\\\\)$", "");
	    }
	    return thisFolderPath + filePath;
	}
	
	private File getFileFromRelativePath(String filePath){
		String path = getFullPathFromRelativePath(filePath);
		return new File(path);
	}
	
	private void readFiles(File file) throws IOException {
		BufferedReader buffer = new BufferedReader(new FileReader(file));
		List<String> lines = new ArrayList<String>();
		String line = buffer.readLine();
		while (line != null) {
			lines.add(line);
			line = buffer.readLine();
		}
		buffer.close();
		this.body = (String[]) lines.toArray();
	}

	public String getName() {
		return this.file.getName().replaceAll("\\.\\S+$", "");
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

	public void addCompVariable(CompositeVariable compVariable) {
		compVariable.setOrigin(this);
	}

	public String[] getBody() {
		return body;
	}

	public WorkspaceContext getWorkspace() {
		return workspace;
	}

	public void setWorkspace(WorkspaceContext workspace) {
		this.workspace.removeDocument(this);
		this.workspace = workspace;
	}

	public Variable[] getVariables() {
		Variable[] vars = this.getWorkspace().getVariables();
		List<Variable> result = new LinkedList<>();
		for (int i = 0; i < vars.length; i++) {
			if (vars[i].getOrigin().equals(this)) {
				result.add(vars[i]);
			}
		}
		return (Variable[]) result.toArray();
	}

	public CompositeVariable[] getCompVariables() {
		CompositeVariable[] vars = this.getWorkspace().getCompVariables();
		List<CompositeVariable> result = new LinkedList<>();
		for (int i = 0; i < vars.length; i++) {
			if (vars[i].getOrigin().equals(this)) {
				result.add(vars[i]);
			}
		}
		return (CompositeVariable[]) result.toArray();
	}

	public Keyword[] getKeywords() {
		Keyword[] keys = this.getWorkspace().getKeywords();
		List<Keyword> result = new LinkedList<>();
		for (int i = 0; i < keys.length; i++) {
			if (keys[i].getOrigin().equals(this)) {
				result.add(keys[i]);
			}
		}
		return (Keyword[]) result.toArray();
	}

	public TestCase[] getTestCases() {
		TestCase[] tests = this.getWorkspace().getTestCases();
		List<TestCase> result = new LinkedList<>();
		for (int i = 0; i < tests.length; i++) {
			if (tests[i].getOrigin().equals(this)) {
				result.add(tests[i]);
			}
		}
		return (TestCase[]) result.toArray();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(body);
		result = prime * result + ((file == null) ? 0 : file.hashCode());
		result = prime * result + Arrays.hashCode(resources);
		result = prime * result + Arrays.hashCode(settings);
		result = prime * result + ((workspace == null) ? 0 : workspace.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RobotDoc other = (RobotDoc) obj;
		if (!Arrays.equals(body, other.body))
			return false;
		if (file == null) {
			if (other.file != null)
				return false;
		} else if (!file.equals(other.file))
			return false;
		if (!Arrays.equals(resources, other.resources))
			return false;
		if (!Arrays.equals(settings, other.settings))
			return false;
		if (workspace == null) {
			if (other.workspace != null)
				return false;
		} else if (!workspace.equals(other.workspace))
			return false;
		return true;
	}

}
