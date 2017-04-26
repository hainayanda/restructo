package restructo.robot.context;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import restructo.helper.Util;
import restructo.robot.doc.Keyword;
import restructo.robot.doc.RobotDoc;
import restructo.robot.doc.TestCase;
import restructo.robot.doc.Variable;

public class WorkspaceContext {
	private RobotDoc[] documents;
	private TestCase[] testCases;
	private Variable[] variables;
	private Keyword[] keywords;

	public WorkspaceContext() {
	}

	public static WorkspaceContext parseWorkspace(File folder) {
		RobotDoc[] docs = filesScanner(folder);
		TestCase[] test = testsScanner(docs);
		Variable[] vars = variablesScanner(docs);
		Keyword[] keys = keywordsScanner(docs);
		return null;
	}

	private static Keyword[] keywordsScanner(RobotDoc[] docs) {
		Keyword[] result = new Keyword[0];
		for(int i = 0; i < docs.length; i++){
			Keyword[] temp = keyScanner(docs[i]);
			result = (Keyword[]) Util.concat(result, temp);
		}
		return result;
	}

	private static Keyword[] keyScanner(RobotDoc doc) {
		List<Keyword> keywords = new LinkedList<>();
		String[] body = doc.getBody();
		boolean isInKeywordField = false;
		for (int i = 0; i < body.length; i++) {
			String line = body[i];
			if (!isInKeywordField) {
				if (line.matches("^\\*\\*\\*+\\sKeywords\\s\\*\\*\\*")) {
					isInKeywordField = true;
				}
			} else {
				if (line.matches("^\\*\\*\\*+\\s\\w+\\s?\\w+\\s\\*\\*\\*")) {
					isInKeywordField = false;
					break;
				} else {
					if (line.matches("^\\w+")) {
						String tempName = line;
						List<String> tempBody = new LinkedList<>();
						List<String> tempArgs = new LinkedList<>();
						String tempRet = null;
						for (; i < body.length; i++) {
							line = body[i];
							if (line.matches("^\\s{2,}\\S+")) {
								String temp = line.replaceAll("^\\s+", "").replaceAll("\\s+$", "");
								tempBody.add(temp);
							} else if (line.matches("\\[Arguments\\]")) {
								String[] temp = line.replaceAll("^\\s+", "").replaceAll("\\s+$", "").split("\\s{2,}");
								for (int j = 1; j < temp.length; j++) {
									tempArgs.add(temp[j]);
								}
							} else if (line.matches("\\[Return\\]")) {
								tempRet = line.replaceAll("^\\s+\\[Return\\]\\s+", "").replaceAll("\\s+$", "");
							} else if (line.matches("^\\w+")
									|| line.matches("^\\*\\*\\*+\\s\\w+\\s?\\w+\\s\\*\\*\\*")) {
								break;
							}
						}
						i--;
						Keyword key = new Keyword(doc, tempName, (String[]) tempBody.toArray(),
								(String[]) tempArgs.toArray(), tempRet);
						keywords.add(key);
					}
				}
			}
		}
		return (Keyword[]) keywords.toArray();
	}

	private static Variable[] variablesScanner(RobotDoc[] docs) {
		// TODO Auto-generated method stub
		return null;
	}

	private static TestCase[] testsScanner(RobotDoc[] docs) {
		// TODO Auto-generated method stub
		return null;
	}

	private static RobotDoc[] filesScanner(File folder) {
		List<RobotDoc> robots = new LinkedList<>();
		File[] listOfFiles = folder.listFiles();
		for (File file : listOfFiles) {
			if (file.isFile() && file.getName().matches("\\.(robot|txt)$")) {
				robots.add(RobotDoc.parseRobotDoc(file));
			} else if (file.isDirectory()) {
				RobotDoc[] temp = filesScanner(file);
				for (int i = 0; i < temp.length; i++) {
					robots.add(temp[i]);
				}
			}
		}
		return (RobotDoc[]) robots.toArray();
	}

	public RobotDoc[] getDocuments() {
		return documents;
	}

	public void setDocuments(RobotDoc[] documents) {
		this.documents = documents;
	}

	public TestCase[] getTestCases() {
		return testCases;
	}

	public void setTestCases(TestCase[] testCases) {
		this.testCases = testCases;
	}

	public Variable[] getVariables() {
		return variables;
	}

	public void setVariables(Variable[] variables) {
		this.variables = variables;
	}

	public Keyword[] getKeywords() {
		return keywords;
	}

	public void setKeywords(Keyword[] keywords) {
		this.keywords = keywords;
	}

}
