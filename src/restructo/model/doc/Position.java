package restructo.model.doc;

public class Position {
	private int line;
	private int character;

	public Position(int line, int character) {
		this.line = line;
		this.character = character;
	}

	public int getLine() {
		return this.line;
	}
	
	public int getCharacter(){
		return this.character;
	}

}
