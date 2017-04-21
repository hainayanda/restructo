package restructo.model.doc;

public class Range {
	private Position start;
	private Position end;

	public Range(Position start, Position end) {
		this.start = start;
		this.end = end;
	}

	public Position getStart() {
		return start;
	}

	public Position getEnd() {
		return end;
	}

}
