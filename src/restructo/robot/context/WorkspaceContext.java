package restructo.robot.context;

import java.io.File;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import restructo.helper.Util;
import restructo.robot.doc.Function;
import restructo.robot.doc.KeyCaller;
import restructo.robot.doc.Keyword;
import restructo.robot.doc.PlainLocator;
import restructo.robot.doc.RobotDoc;
import restructo.robot.doc.TestCase;
import restructo.robot.doc.VarCaller;
import restructo.robot.doc.Variable;
import restructo.robot.doc.nav.Location;
import restructo.robot.doc.nav.Position;

public class WorkspaceContext {
	private List<RobotDoc> documents = new LinkedList<>();
	private List<TestCase> testCases = new LinkedList<>();
	private List<Variable> variables = new LinkedList<>();
	private List<Keyword> keywords = new LinkedList<>();
	private List<PlainLocator> locators = new LinkedList<>();

	public WorkspaceContext() {
	}

	public WorkspaceContext(RobotDoc[] documents, TestCase[] testCases, Variable[] variables, Keyword[] keywords) {
		Util.addArrayToList(documents, this.documents);
		Util.addArrayToList(testCases, this.testCases);
		Util.addArrayToList(variables, this.variables);
		Util.addArrayToList(keywords, this.keywords);
	}

	public static WorkspaceContext parseWorkspace(File folder) {
		RobotDoc[] docs = filesScanner(folder);
		TestCase[] test = testsScanner(docs);
		Variable[] vars = variablesScanner(docs);
		Keyword[] keys = keywordsScanner(docs);
		WorkspaceContext workspace = new WorkspaceContext(docs, test, vars, keys);
		scanAllElement(workspace);
		return workspace;
	}

	private static void scanAllElement(WorkspaceContext workspace) {
		Function[] keys = workspace.getKeywords();
		Function[] tests = workspace.getTestCases();
		Function[] allFuncs = (Function[]) Util.concat(keys, tests);
		scanFunctionsElements(allFuncs);
	}

	private static void scanFunctionsElements(Function[] functions) {
		for (int i = 0; i < functions.length; i++) {
			scanFunctionCaller(functions[i]);
			scanLocators(functions[i]);
		}
	}

	private static void scanLocators(Function function) {
		String[] body = function.getBody();
		WorkspaceContext workspace = function.getOrigin().getWorkspace();
		PlainLocator[] locators = workspace.getLocators();
		for (int i = 0; i < body.length; i++) {
			String[] line = body[i].replaceAll("^\\s+", "").split("\\s{2,}");
			for (int j = 0; j < line.length; j++) {
				String sentence = line[j];
				if (sentence.matches("^(sizzle|id(entifier)?|css|name|xpath|dom|(partial )?link|jquery|tag)\\s?=")) {
					String locator = sentence.replaceAll("\\s+=\\s+", "=");
					boolean isNew = true;
					for (int k = 0; k < locators.length; k++) {
						if (locators[k].getValue().equals(locator)) {
							isNew = false;
							locators[k].addLocator(new Location(function, new Position(i, j)));
						}
					}
					if (isNew) {
						workspace.locators.add(new PlainLocator(locator, new Location(function, new Position(i, j))));
					}
				}
			}
		}
	}

	private static void scanFunctionCaller(Function function) {
		RobotDoc origin = function.getOrigin();
		RobotDoc[] allResources = (RobotDoc[]) Util.addToArray(origin, origin.getResources());
		Keyword[] allKeys = extractAllKeywords(origin);
		Variable[] allVars = extractAllVariables(origin);
		String[] body = function.getBody();
		for (int i = 0; i < body.length; i++) {
			String[] sentences = body[i].replaceAll("^\\s+", "").split("\\s{2,}");
			for (int j = 0; j < sentences.length; j++) {
				String sentence = sentences[j];
				if (sentence.matches("^\\w+\\.\\S+")) {
					String[] splitted = sentence.split("\\.");
					for (int k = 0; k < allResources.length; k++) {
						if (splitted[0].equals(allResources[k].getName())) {
							Keyword[] keys = allResources[k].getKeywords();
							for (int l = 0; l < keys.length; l++) {
								if (keys[l].getName().equals(splitted[1])) {
									KeyCaller caller = new KeyCaller(keys[l], new Position(i, j));
									function.addKeyCaller(caller);
								}
							}
						}
					}
				} else if (sentence.matches("^\\$\\{[^${}]+\\}")) {
					String varName = sentence.replaceAll("(^\\$\\{|\\}$)", "");
					for (int k = 0; k < allVars.length; k++) {
						Variable var = allVars[k];
						if (var.getName().equals(varName)) {
							VarCaller caller = new VarCaller(var, new Position(i, j));
							function.addVarCaller(caller);
						}
					}
				} else {
					for (int k = 0; k < allKeys.length; k++) {
						Keyword key = allKeys[k];
						if (key.getName().equals(sentence)) {
							KeyCaller caller = new KeyCaller(key, new Position(i, j));
							function.addKeyCaller(caller);
						}
					}
				}
			}
		}
	}

	private static Variable[] extractAllVariables(RobotDoc document) {
		RobotDoc[] resources = document.getResources();
		RobotDoc[] allResources = (RobotDoc[]) Util.addToArray(document, resources);
		Variable[] allKeywords = new Variable[0];
		for (int i = 0; i < allResources.length; i++) {
			RobotDoc resource = allResources[i];
			Variable[] resKeywords = resource.getVariables();
			allKeywords = (Variable[]) Util.concat(allKeywords, resKeywords);
		}
		return allKeywords;
	}

	private static Keyword[] extractAllKeywords(RobotDoc document) {
		RobotDoc[] resources = document.getResources();
		RobotDoc[] allResources = (RobotDoc[]) Util.addToArray(document, resources);
		Keyword[] allKeywords = new Keyword[0];
		for (int i = 0; i < allResources.length; i++) {
			RobotDoc resource = allResources[i];
			Keyword[] resKeywords = resource.getKeywords();
			allKeywords = (Keyword[]) Util.concat(allKeywords, resKeywords);
		}
		return allKeywords;
	}

	private static Keyword[] keywordsScanner(RobotDoc[] docs) {
		Keyword[] result = new Keyword[0];
		for (int i = 0; i < docs.length; i++) {
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
								String temp = line.replaceAll("^\\s+", "  ").replaceAll("\\s+$", "");
								tempBody.add(temp);
							} else if (line.matches("\\[Arguments\\]")) {
								String[] temp = line.replaceAll("^\\s+", "  ").replaceAll("\\s+$", "").split("\\s{2,}");
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
		Variable[] result = new Variable[0];
		for (int i = 0; i < docs.length; i++) {
			Variable[] temp = varScanner(docs[i]);
			result = (Variable[]) Util.concat(result, temp);
		}
		return result;
	}

	private static Variable[] varScanner(RobotDoc doc) {
		List<Variable> variables = new LinkedList<>();
		String[] body = doc.getBody();
		boolean isInVariableField = false;
		for (int i = 0; i < body.length; i++) {
			String line = body[i];
			if (!isInVariableField) {
				if (line.matches("^\\*\\*\\*+\\sVariable\\s\\*\\*\\*")) {
					isInVariableField = true;
				}
			} else {
				if (line.matches("^\\*\\*\\*+\\s\\w+\\s?\\w+\\s\\*\\*\\*")) {
					isInVariableField = false;
					break;
				} else {
					if (line.matches("^\\$\\{[^${}]+\\}")) {
						String[] sentences = line.replaceAll("\\s+$", "").split("\\s{2,}");
						String varName = sentences[0].replaceAll("(\\$\\{|\\})", "");
						String value = sentences[1];
						variables.add(new Variable(doc, varName, value));
					}
				}
			}
		}
		return (Variable[]) variables.toArray();
	}

	private static TestCase[] testsScanner(RobotDoc[] docs) {
		TestCase[] result = new TestCase[0];
		for (int i = 0; i < docs.length; i++) {
			TestCase[] temp = testScanner(docs[i]);
			result = (TestCase[]) Util.concat(result, temp);
		}
		return result;
	}

	private static TestCase[] testScanner(RobotDoc doc) {
		List<TestCase> testCases = new LinkedList<>();
		String[] body = doc.getBody();
		boolean isInTestField = false;
		for (int i = 0; i < body.length; i++) {
			String line = body[i];
			if (!isInTestField) {
				if (line.matches("^\\*\\*\\*+\\sTest Case\\s\\*\\*\\*")) {
					isInTestField = true;
				}
			} else {
				if (line.matches("^\\*\\*\\*+\\s\\w+\\s?\\w+\\s\\*\\*\\*")) {
					isInTestField = false;
					break;
				} else {
					if (line.matches("^\\w+")) {
						String tempName = line;
						List<String> tempBody = new LinkedList<>();
						for (; i < body.length; i++) {
							line = body[i];
							if (line.matches("^\\s{2,}\\S+")) {
								String temp = line.replaceAll("^\\s+", "  ").replaceAll("\\s+$", "");
								tempBody.add(temp);
							} else if (line.matches("^\\w+")
									|| line.matches("^\\*\\*\\*+\\s\\w+\\s?\\w+\\s\\*\\*\\*")) {
								break;
							}
						}
						i--;
						TestCase key = new TestCase(doc, tempName, (String[]) tempBody.toArray());
						testCases.add(key);
					}
				}
			}
		}
		return (TestCase[]) testCases.toArray();
	}

	private static RobotDoc[] filesScanner(File folder) {
		Set<RobotDoc> robots = new HashSet<>();
		File[] listOfFiles = folder.listFiles();
		for (File file : listOfFiles) {
			RobotDoc[] temp = null;
			if (file.isFile() && file.getName().matches("\\.(robot|txt)$")) {
				temp = RobotDoc.parseRobotDocWithResources(file);
			} else if (file.isDirectory()) {
				temp = filesScanner(file);
			}
			if (temp != null) {
				for (int i = 0; i < temp.length; i++) {
					robots.add(temp[i]);
				}
			}
		}
		return (RobotDoc[]) robots.toArray();
	}

	public RobotDoc[] getDocuments() {
		return (RobotDoc[]) this.documents.toArray();
	}

	public void setDocuments(RobotDoc[] documents) {
		this.documents = Util.arrayToLinkedList(documents);
	}

	public TestCase[] getTestCases() {
		return (TestCase[]) testCases.toArray();
	}

	public void setTestCases(TestCase[] testCases) {
		this.testCases = Util.arrayToLinkedList(testCases);
	}

	public Variable[] getVariables() {
		return (Variable[]) variables.toArray();
	}

	public void setVariables(Variable[] variables) {
		this.variables = Util.arrayToLinkedList(variables);
	}

	public Keyword[] getKeywords() {
		return (Keyword[]) keywords.toArray();
	}

	public void setKeywords(Keyword[] keywords) {
		this.keywords = Util.arrayToLinkedList(keywords);
	}

	public void removeDocument(RobotDoc document) {
		this.documents.remove(document);
	}

	public PlainLocator[] getLocators() {
		return (PlainLocator[]) locators.toArray();
	}

	public void setLocators(PlainLocator[] locators) {
		this.locators = Util.arrayToLinkedList(locators);
	}

}
