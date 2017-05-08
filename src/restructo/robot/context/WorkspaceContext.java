package restructo.robot.context;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import restructo.helper.Util;
import restructo.robot.doc.CompVarCaller;
import restructo.robot.doc.CompositeVariable;
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

	private Set<RobotDoc> documents = new HashSet<>();
	private List<TestCase> testCases = new LinkedList<>();
	private List<Variable> variables = new LinkedList<>();
	private List<CompositeVariable> compVariables = new LinkedList<>();
	private List<Keyword> keywords = new LinkedList<>();
	private List<PlainLocator> locators = new LinkedList<>();

	public WorkspaceContext() {
	}

	public static WorkspaceContext parseWorkspace(File folder) throws IOException {
		WorkspaceContext workspace = new WorkspaceContext();
		filesScanner(folder, workspace);
		testsScanner(workspace);
		variablesScanner(workspace);
		keywordsScanner(workspace);
		scanAllElement(workspace);
		return workspace;
	}

	private static void scanAllElement(WorkspaceContext workspace) {
		Function[] keys = workspace.getKeywords();
		Function[] tests = workspace.getTestCases();
		Function[] allFuncs = Util.concat(keys, tests, new Function[keys.length + tests.length]);
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
				if (sentence.matches("^(sizzle|id(entifier)?|css|name|xpath|dom|(partial )?link|jquery|tag)\\s?=.+")) {
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
		RobotDoc[] allResources = Util.addToArray(origin, origin.getResources(),
				new RobotDoc[origin.getResources().length + 1]);
		Keyword[] allKeys = extractAllKeywords(origin);
		Variable[] allVars = extractAllVariables(origin);
		CompositeVariable[] allCompVars = extractAllCompVars(origin);
		String[] body = function.getBody();
		for (int i = 0; i < body.length; i++) {
			String[] sentences = body[i].replaceAll("^\\s+", "").split("\\s{2,}");
			for (int j = 0; j < sentences.length; j++) {
				String sentence = sentences[j];
				if (sentence.matches("^\\w+\\.\\S+.*")) {
					String[] splitted = sentence.split("\\.");
					for (int k = 0; k < allResources.length; k++) {
						if (splitted[0].equals(allResources[k].getName())) {
							Keyword[] keys = allResources[k].getKeywords();
							for (int l = 0; l < keys.length; l++) {
								if (keys[l].getName().equals(splitted[1])) {
									KeyCaller caller = new KeyCaller(keys[l],
											new Location(function, new Position(i, j)));
									function.addKeyCaller(caller);
								}
							}
						}
					}
				} else if (sentence.matches("^\\$\\{[^${}]+\\}")) {
					String varName = sentence.replaceAll("(^\\$\\{|\\}$)", "");
					if (varName.matches("[^${}.]+\\.[^${}.]+")) {
						String[] vars = varName.replaceAll("\\s\\.\\s", ".").split("\\.");
						varName = vars[0];
						String nestedName = vars[1];
						for (int k = 0; k < allCompVars.length; k++) {
							CompositeVariable comp = allCompVars[k];
							if (comp.getName().equals(varName)) {
								Variable nested = comp.getMember(nestedName);
								if (nestedName != null) {
									CompVarCaller caller = new CompVarCaller(comp, nested,
											new Location(function, new Position(i, j)));
									function.addCompVarCaller(caller);
								}
							}
						}

					} else {
						for (int k = 0; k < allVars.length; k++) {
							Variable var = allVars[k];
							if (var.getName().equals(varName)) {
								VarCaller caller = new VarCaller(var, new Location(function, new Position(i, j)));
								function.addVarCaller(caller);
							}
						}
					}
				} else {
					for (int k = 0; k < allKeys.length; k++) {
						Keyword key = allKeys[k];
						if (key.getName().equals(sentence)) {
							KeyCaller caller = new KeyCaller(key, new Location(function, new Position(i, j)));
							function.addKeyCaller(caller);
						}
					}
				}
			}
		}
	}

	private static CompositeVariable[] extractAllCompVars(RobotDoc document) {
		RobotDoc[] resources = document.getResources();
		RobotDoc[] allResources = Util.addToArray(document, resources, new RobotDoc[resources.length + 1]);
		CompositeVariable[] allVar = new CompositeVariable[0];
		for (int i = 0; i < allResources.length; i++) {
			RobotDoc resource = allResources[i];
			CompositeVariable[] resKeywords = resource.getCompVariables();
			allVar = Util.concat(allVar, resKeywords, new CompositeVariable[allVar.length + resKeywords.length]);
		}
		return allVar;
	}

	private static Variable[] extractAllVariables(RobotDoc document) {
		RobotDoc[] resources = document.getResources();
		RobotDoc[] allResources = Util.addToArray(document, resources, new RobotDoc[resources.length + 1]);
		Variable[] allKeywords = new Variable[0];
		for (int i = 0; i < allResources.length; i++) {
			RobotDoc resource = allResources[i];
			Variable[] resKeywords = resource.getVariables();
			allKeywords = Util.concat(allKeywords, resKeywords, new Variable[allKeywords.length + resKeywords.length]);
		}
		return allKeywords;
	}

	private static Keyword[] extractAllKeywords(RobotDoc document) {
		RobotDoc[] resources = document.getResources();
		RobotDoc[] allResources = Util.addToArray(document, resources, new RobotDoc[resources.length + 1]);
		Keyword[] allKeywords = new Keyword[0];
		for (int i = 0; i < allResources.length; i++) {
			RobotDoc resource = allResources[i];
			Keyword[] resKeywords = resource.getKeywords();
			allKeywords = Util.concat(allKeywords, resKeywords, new Keyword[allKeywords.length + resKeywords.length]);
		}
		return allKeywords;
	}

	private static void keywordsScanner(WorkspaceContext workspace) {
		Keyword[] result = new Keyword[0];
		RobotDoc[] docs = workspace.getDocuments();
		for (int i = 0; i < docs.length; i++) {
			Keyword[] temp = keyScanner(docs[i]);
			result = Util.concat(result, temp, new Keyword[result.length + temp.length]);
		}
		workspace.setKeywords(result);
	}

	private static Keyword[] keyScanner(RobotDoc doc) {
		List<Keyword> keywords = new LinkedList<>();
		String[] body = doc.getBody();
		boolean isInKeywordField = false;
		for (int i = 0; i < body.length; i++) {
			String line = body[i];
			if (!isInKeywordField) {
				if (line.matches("^\\*\\*\\*+\\sKeywords\\s\\*\\*\\*\\s*")) {
					isInKeywordField = true;
				}
			} else {
				if (line.matches("^\\*\\*\\*+\\s\\w+\\s?\\w+\\s\\*\\*\\*\\s*")) {
					isInKeywordField = false;
					break;
				} else {
					if (line.matches("^\\w+.*")) {
						String tempName = line;
						List<String> tempBody = new LinkedList<>();
						List<String> tempArgs = new LinkedList<>();
						String tempRet = null;
						for (i++; i < body.length; i++) {
							line = body[i];
							if (line.matches("^\\s{2,}\\S+.*")) {
								String temp = line.replaceAll("^\\s+", "  ").replaceAll("\\s+$", "");
								tempBody.add(temp);
							} else if (line.matches("\\[Arguments\\]\\s*")) {
								String[] temp = line.replaceAll("^\\s+", "  ").replaceAll("\\s+$", "").split("\\s{2,}");
								for (int j = 1; j < temp.length; j++) {
									tempArgs.add(temp[j]);
								}
							} else if (line.matches("\\[Return\\]\\s*")) {
								tempRet = line.replaceAll("^\\s+\\[Return\\]\\s+", "").replaceAll("\\s+$", "");
							} else if (line.matches("^\\w+.*")
									|| line.matches("^\\*\\*\\*+\\s\\w+\\s?\\w+\\s\\*\\*\\*\\s*")) {
								i--;
								break;
							}
						}
						Keyword key = new Keyword(doc, tempName, tempBody.toArray(new String[tempBody.size()]),
								tempArgs.toArray(new String[tempArgs.size()]), tempRet);
						keywords.add(key);
					}
				}
			}
		}
		return keywords.toArray(new Keyword[keywords.size()]);
	}

	private static void variablesScanner(WorkspaceContext workspace) {
		RobotDoc[] docs = workspace.getDocuments();
		Variable[] result = new Variable[0];
		CompositeVariable[] compResult = new CompositeVariable[0];
		for (int i = 0; i < docs.length; i++) {
			Variable[] temp = varScanner(docs[i]);
			result = Util.concat(result, temp, new Variable[result.length + temp.length]);
			CompositeVariable[] compTemp = compVarScanner(docs[i]);
			compResult = Util.concat(compResult, compTemp, new CompositeVariable[compResult.length + compTemp.length]);
		}
		workspace.setCompVariables(compResult);
		workspace.setVariables(result);
	}

	private static CompositeVariable[] compVarScanner(RobotDoc doc) {
		List<CompositeVariable> compVar = new LinkedList<>();
		String[] body = doc.getBody();
		boolean isInVariableField = false;
		for (int i = 0; i < body.length; i++) {
			String line = body[i];
			if (!isInVariableField) {
				if (line.matches("^\\*\\*\\*+\\sVariable\\s\\*\\*\\*\\s*")) {
					isInVariableField = true;
				}
			} else {
				if (line.matches("^\\*\\*\\*+\\s\\w+\\s?\\w+\\s\\*\\*\\*\\s*")) {
					isInVariableField = false;
					break;
				} else {
					if (line.matches("^&\\{[^{}]+\\}\\s{2,}\\S+.*")) {
						List<Variable> nesteds = new LinkedList<>();
						String[] sentences = line.replaceAll("\\s+$", "").split("\\s{2,}");
						String varName = sentences[0].replaceAll("(&\\{|\\})", "");
						String[] nest = sentences[1].replaceAll("\\s=\\s", "=").split("=");
						Variable var = new Variable(doc, nest[0], nest[1]);
						nesteds.add(var);
						for (; i < body.length; i++) {
							if (line.matches("^\\*\\*\\*+\\s\\w+\\s?\\w+\\s\\*\\*\\*\\s*")) {
								i--;
								break;
							} else if (line.matches("(\\S+\\s?)+=\\s?(\\S+\\s?)+")) {
								sentences = line.replaceAll("\\s+$", "").split("\\s{2,}");
								nest = sentences[1].replaceAll("\\s=\\s", "=").split("=");
								var = new Variable(doc, nest[0], nest[1]);
								nesteds.add(var);
							} else if (line.matches("^&\\{[^{}]+\\}\\s{2,}\\S+.*")
									|| line.matches("^\\$\\{[^${}]+\\}.*")) {
								i--;
								break;
							}
						}
						compVar.add(new CompositeVariable(doc, varName, nesteds.toArray(new Variable[nesteds.size()])));
					}
				}
			}
		}
		return compVar.toArray(new CompositeVariable[compVar.size()]);
	}

	private static Variable[] varScanner(RobotDoc doc) {
		List<Variable> variables = new LinkedList<>();
		String[] body = doc.getBody();
		boolean isInVariableField = false;
		for (int i = 0; i < body.length; i++) {
			String line = body[i];
			if (!isInVariableField) {
				if (line.matches("^\\*\\*\\*+\\sVariable\\s\\*\\*\\*\\s*")) {
					isInVariableField = true;
				}
			} else {
				if (line.matches("^\\*\\*\\*+\\s\\w+\\s?\\w+\\s\\*\\*\\*\\s*")) {
					isInVariableField = false;
					break;
				} else {
					if (line.matches("^\\$\\{[^${}]+\\}\\s{2,}.+")) {
						String[] sentences = line.replaceAll("\\s+$", "").split("\\s{2,}");
						String varName = sentences[0].replaceAll("(\\$\\{|\\})", "");
						String value = "";
						if (sentences.length > 1) {
							value = sentences[1];
						}
						variables.add(new Variable(doc, varName, value));
					}
				}
			}
		}
		return variables.toArray(new Variable[variables.size()]);
	}

	private static void testsScanner(WorkspaceContext workspace) {
		RobotDoc[] docs = workspace.getDocuments();
		TestCase[] result = new TestCase[0];
		for (int i = 0; i < docs.length; i++) {
			TestCase[] temp = testScanner(docs[i]);
			if (temp.length > 0) {
				result = Util.concat(result, temp, new TestCase[result.length + temp.length]);
			}
		}
		workspace.setTestCases(result);
	}

	private static TestCase[] testScanner(RobotDoc doc) {
		List<TestCase> testCases = new LinkedList<>();
		String[] body = doc.getBody();
		boolean isInTestField = false;
		for (int i = 0; i < body.length; i++) {
			String line = body[i];
			if (!isInTestField) {
				if (line.matches("^\\*\\*\\*+\\sTest Case\\s\\*\\*\\*\\s*")) {
					isInTestField = true;
				}
			} else {
				if (line.matches("^\\*\\*\\*+\\s\\w+\\s?\\w+\\s\\*\\*\\*\\s*")) {
					isInTestField = false;
					break;
				} else {
					if (line.matches("^\\w+.+")) {
						String tempName = line;
						List<String> tempBody = new LinkedList<>();
						for (i++; i < body.length; i++) {
							line = body[i];
							if (line.matches("^\\s{2,}\\S+.+")) {
								String temp = line.replaceAll("^\\s+", "  ").replaceAll("\\s+$", "");
								tempBody.add(temp);
							} else if (line.matches("^\\w+.+")
									|| line.matches("^\\*\\*\\*+\\s\\w+\\s?\\w+\\s\\*\\*\\*\\s*")) {
								i--;
								break;
							}
						}
						TestCase key = new TestCase(doc, tempName, tempBody.toArray(new String[tempBody.size()]));
						testCases.add(key);
					}
				}
			}
		}
		return testCases.toArray(new TestCase[testCases.size()]);
	}

	private static void filesScanner(File folder, WorkspaceContext workspace) throws IOException {
		File[] listOfFiles = folder.listFiles();
		for (File file : listOfFiles) {
			if (file.isFile() && file.getName().matches(".+\\.(txt|robot)$")) {
				RobotDoc.parseRobotDoc(file, workspace);
			} else if (file.isDirectory()) {
				filesScanner(file, workspace);
			}
		}
	}

	public void addDocument(RobotDoc robotDoc) {
		this.documents.add(robotDoc);

	}

	public RobotDoc[] getDocuments() {
		return this.documents.toArray(new RobotDoc[this.documents.size()]);
	}

	public void setDocuments(RobotDoc[] documents) {
		this.documents = Util.arrayToHashSet(documents);
	}

	public TestCase[] getTestCases() {
		return testCases.toArray(new TestCase[testCases.size()]);
	}

	public void setTestCases(TestCase[] testCases) {
		this.testCases = Util.arrayToLinkedList(testCases);
	}

	public Variable[] getVariables() {
		return variables.toArray(new Variable[variables.size()]);
	}

	public void setVariables(Variable[] variables) {
		this.variables = Util.arrayToLinkedList(variables);
	}

	public Keyword[] getKeywords() {
		return keywords.toArray(new Keyword[keywords.size()]);
	}

	public void setKeywords(Keyword[] keywords) {
		this.keywords = Util.arrayToLinkedList(keywords);
	}

	private void removeVariables(RobotDoc document) {
		Variable[] vars = this.getVariables();
		for (int i = 0; i < vars.length; i++) {
			if (vars[i].getOrigin().equals(document)) {
				this.variables.remove(vars[i]);
			}
		}
	}

	private void removeCompVariables(RobotDoc document) {
		CompositeVariable[] compVars = this.getCompVariables();
		for (int i = 0; i < compVars.length; i++) {
			if (compVars[i].getOrigin().equals(document)) {
				this.compVariables.remove(compVars[i]);
			}
		}
	}

	private void removeKeywords(RobotDoc document) {
		Keyword[] keys = this.getKeywords();
		for (int i = 0; i < keys.length; i++) {
			if (keys[i].getOrigin().equals(document)) {
				this.keywords.remove(keys[i]);
			}
		}
	}

	private void removeTestCases(RobotDoc document) {
		TestCase[] test = this.getTestCases();
		for (int i = 0; i < test.length; i++) {
			if (test[i].getOrigin().equals(document)) {
				this.testCases.remove(test[i]);
			}
		}
	}

	public void removeDocument(RobotDoc document) {
		this.documents.remove(document);
		this.removeVariables(document);
		this.removeCompVariables(document);
		this.removeKeywords(document);
		this.removeTestCases(document);
	}

	public PlainLocator[] getLocators() {
		return locators.toArray(new PlainLocator[locators.size()]);
	}

	public void setLocators(PlainLocator[] locators) {
		this.locators = Util.arrayToLinkedList(locators);
	}

	public CompositeVariable[] getCompVariables() {
		return compVariables.toArray(new CompositeVariable[compVariables.size()]);
	}

	public void setCompVariables(CompositeVariable[] compVariables) {
		this.compVariables = Util.arrayToLinkedList(compVariables);
	}

	public boolean documentIsExist(File file) {
		if (this.getDocuments() != null) {
			RobotDoc[] docs = this.getDocuments();
			for (int i = 0; i < docs.length; i++) {
				if (docs[i].getFile().equals(file)) {
					return true;
				}
			}
		}
		return false;
	}

	public RobotDoc getDocumentByFile(File file) {
		if (this.getDocuments() != null) {
			RobotDoc[] docs = this.getDocuments();
			for (int i = 0; i < docs.length; i++) {
				if (docs[i].getFile().equals(file)) {
					return docs[i];
				}
			}
		}
		return null;
	}

}
