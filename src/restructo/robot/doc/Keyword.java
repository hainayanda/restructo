package restructo.robot.doc;

public class Keyword extends Component {
	
	private KeywordCaller[] keyCallers;
	private VariableCaller[] variableCallers;
	private PlainLocator[] locators;
	
	public Keyword(String name, String[] body, KeywordCaller[] keyCallers, VariableCaller[] variableCallers, PlainLocator[] locators) {
		super(name, body);
		this.keyCallers = keyCallers;
		this.locators = locators;
		this.variableCallers = variableCallers;
	}

	public KeywordCaller[] getKeyCallers() {
		return keyCallers;
	}

	public void setKeyCallers(KeywordCaller[] keyCallers) {
		this.keyCallers = keyCallers;
	}

	public VariableCaller[] getVariableCallers() {
		return variableCallers;
	}

	public void setVariableCallers(VariableCaller[] variableCallers) {
		this.variableCallers = variableCallers;
	}

	public PlainLocator[] getLocators() {
		return locators;
	}

	public void setLocators(PlainLocator[] locators) {
		this.locators = locators;
	}
	
}
