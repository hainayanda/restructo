package restructo.model.doc;

public class Keyword {
	private Location originLoc;
	private String names;
	private String[] arguments;
	private String returnVal;
	private String[] body;

	public Location getOriginLoc() {
		return originLoc;
	}

	public void setOriginLoc(Location originLoc) {
		this.originLoc = originLoc;
	}

	public String getNames() {
		return names;
	}

	public void setNames(String names) {
		this.names = names;
	}

	public String[] getBody() {
		return this.body;
	}

	public String[] getArguments() {
		return arguments;
	}

	public String getReturnVal() {
		return returnVal;
	}

	public Keyword(String names, String[] arguments, String returnVal, String[] body, Location originLoc) {
		super();
		this.originLoc = originLoc;
		this.body = body;
		this.names = names;
		this.arguments = arguments;
		this.returnVal = returnVal;
	}

}
