package restructo.robot.context;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import restructo.robot.doc.Keyword;
import restructo.robot.doc.RobotDoc;
import restructo.robot.doc.Variable;

public class WorkspaceContext {
	private List<Keyword> keywords = new ArrayList<>();
	private List<Variable> variables = new ArrayList<>();
	private List<RobotDoc> documents = new ArrayList<>();
	private List<File> files = new ArrayList<>();
	
	
	private WorkspaceContext(){}
	
	public static WorkspaceContext parseWorkspace(File path){
		WorkspaceContext workspace = new WorkspaceContext();
		scanWorkspace(path, workspace);
		return workspace;
	}
	
	private static void scanWorkspace(File path, WorkspaceContext workspace){
		File[] listOfFiles = path.listFiles();
		int length = listOfFiles.length;
		for(int i = 0; i < length; i++){
			File file = listOfFiles[i];
			if(file.isDirectory()){
				scanWorkspace(file, workspace);
			}
			else if(file.getName().matches("\\.(robot|txt)$")){
				workspace.files.add(file);
			}
		}
	}
	
	public Keyword[] getKeywords(RobotDoc document){
		ArrayList<Keyword> keys = new ArrayList<>();
		for(int i = 0; i < this.keywords.size(); i++){
			if(this.keywords.get(i).getOrigin().equals(document)){
				keys.add(this.keywords.get(i));
			}
		}
		return (Keyword[]) keys.toArray();
	}
	
	public Variable[] getVariables(RobotDoc document){
		ArrayList<Variable> vars = new ArrayList<>();
		for(int i = 0; i < this.variables.size(); i++){
			if(this.variables.get(i).getOrigin().equals(document)){
				vars.add(this.variables.get(i));
			}
		}
		return (Variable[]) vars.toArray();
	}

	public List<RobotDoc> getDocuments() {
		return documents;
	}

	public void setDocuments(List<RobotDoc> documents) {
		this.documents = documents;
	}

	public List<Keyword> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<Keyword> keywords) {
		this.keywords = keywords;
	}

	public List<Variable> getVariables() {
		return variables;
	}

	public void setVariables(List<Variable> variables) {
		this.variables = variables;
	}
	
	
}
