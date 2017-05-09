package restructo.robot.doc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import restructo.helper.Util;
import restructo.robot.context.WorkspaceContext;

public class RobotDoc {

	private WorkspaceContext workspace;
	private File file;
	private String[] settings = new String[0]; // this is without resource
	private RobotDoc[] resources = new RobotDoc[0];
	private String[] body = new String[0];

	public RobotDoc(File file, WorkspaceContext workspace) {
		this.file = file;
		this.workspace = workspace;
	}

	public static RobotDoc parseRobotDoc(File file, WorkspaceContext workspace) throws IOException {
		RobotDoc doc = new RobotDoc(file, workspace);
		doc.readFiles(file);
		doc.searchSettings(workspace);
		workspace.addDocument(doc);
		return doc;
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

	public String[] getBody() {
		return body;
	}

	public WorkspaceContext getWorkspace() {
		return workspace;
	}

	public void setWorkspace(WorkspaceContext workspace) {
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
		return result.toArray(new Variable[result.size()]);
	}

	public Variable[] getAllIncludedVariables() {
		Variable[] thisVar = this.getVariables();
		List<Variable> included = new LinkedList<>();
		for (int i = 0; i < this.resources.length; i++) {
			Variable[] include = this.resources[i].getVariables();
			for (int j = 0; j < include.length; j++) {
				included.add(include[j]);
			}
		}
		return Util.concat(thisVar, included.toArray(new Variable[included.size()]),
				new Variable[thisVar.length + included.size()]);
	}

	public CompositeVariable[] getCompVariables() {
		CompositeVariable[] vars = this.getWorkspace().getCompVariables();
		List<CompositeVariable> result = new LinkedList<>();
		for (int i = 0; i < vars.length; i++) {
			if (vars[i].getOrigin().equals(this)) {
				result.add(vars[i]);
			}
		}
		return result.toArray(new CompositeVariable[result.size()]);
	}

	public CompositeVariable[] getAllIncludedCompVariables() {
		CompositeVariable[] thisVar = this.getCompVariables();
		List<CompositeVariable> included = new LinkedList<>();
		for (int i = 0; i < this.resources.length; i++) {
			CompositeVariable[] include = this.resources[i].getCompVariables();
			for (int j = 0; j < include.length; j++) {
				included.add(include[j]);
			}
		}
		return Util.concat(thisVar, included.toArray(new CompositeVariable[included.size()]),
				new CompositeVariable[included.size() + thisVar.length]);
	}

	public Keyword[] getKeywords() {
		Keyword[] keys = this.getWorkspace().getKeywords();
		List<Keyword> result = new LinkedList<>();
		for (int i = 0; i < keys.length; i++) {
			if (keys[i].getOrigin().equals(this)) {
				result.add(keys[i]);
			}
		}
		return result.toArray(new Keyword[result.size()]);
	}

	public TestCase[] getTestCases() {
		TestCase[] tests = this.getWorkspace().getTestCases();
		List<TestCase> result = new LinkedList<>();
		for (int i = 0; i < tests.length; i++) {
			if (tests[i].getOrigin().equals(this)) {
				result.add(tests[i]);
			}
		}
		return result.toArray(new TestCase[result.size()]);
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
				} else if (line.matches("^Resource\\s{2,}.+")) {
					String relativePath = line.replaceAll("\\s+$", "").split("\\s{2,}")[1];
					File resource = this.getFileFromRelativePath(relativePath);
					if (workspace.documentIsExist(resource)) {
						resources.add(workspace.getDocumentByFile(resource));
					} else {
						RobotDoc res = parseRobotDoc(resource, workspace);
						resources.add(res);
					}
				} else if (!line.matches("^\\s*$")) {
					settings.add(line);
				}
			}
		}
		if(resources.size() > 0){
			this.setResources(resources.toArray(new RobotDoc[resources.size()]));
		}
		if(settings.size() > 0){
			this.setSettings(settings.toArray(new String[settings.size()]));
		}
	}

	private String getFullPathFromRelativePath(String filePath) {
		String thisFolderPath = this.file.getAbsolutePath().replaceAll("([^\\/\\\\])+$", "");
		while (filePath.matches("^\\.\\.\\.?(\\/|\\\\).+")) {
			filePath = filePath.replaceAll("^\\.\\.\\.?(\\/|\\\\)", "");
			thisFolderPath = thisFolderPath.replaceAll("([^\\/\\\\])+(\\/|\\\\)$", "");
		}
		return thisFolderPath + filePath;
	}

	private File getFileFromRelativePath(String filePath) {
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
		this.body = lines.toArray(new String[lines.size()]);
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
		if (other.getFile().equals(this.getFile())){
			return true;
		}
		return false;
	}

}
