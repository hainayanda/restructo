package restructo.model.doc;

public class Variable {
	private Location originLoc;
	private String names;
	private String value;

	public String getValue() {
		return value;
	}

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

	public Variable(String names, String value, Location originLoc) {
		super();
		this.originLoc = originLoc;
		this.value = value;
		this.names = names;
	}

}
