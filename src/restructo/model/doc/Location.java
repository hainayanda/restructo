package restructo.model.doc;

public class Location {
	private RobotDoc document;
	private Range range;

	public Location(RobotDoc document, Range range) {
		this.document = document;
		this.range = range;
	}
	
	public Location(RobotDoc document, Position position){
		this(document, new Range(position, position));
	}

	public RobotDoc getDocument() {
		return document;
	}

	public Range getRange() {
		return range;
	}

}
