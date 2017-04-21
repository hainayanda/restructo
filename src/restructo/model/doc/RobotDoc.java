package restructo.model.doc;

import java.io.File;

public class RobotDoc {
	private File document;
	private Keyword[] keywords;
	private Variable[] variables;
	private Setting settings;
	private TestCase[] testCase;

	private RobotDoc(){};
	
	public static RobotDoc parseRobotDoc(File document){
		return null;
	}
	
	public static RobotDoc createRobotDoc(Uri uri, Keyword[] keywords, Variable[] variables){
		return null;
	}

	public File getDocument() {
		return document;
	}

	public Keyword[] getKeywords() {
		return keywords;
	}

	public Variable[] getVariables() {
		return variables;
	}

	public Setting getSettings() {
		return settings;
	}

	public TestCase[] getTestCase() {
		return testCase;
	}
	

}
